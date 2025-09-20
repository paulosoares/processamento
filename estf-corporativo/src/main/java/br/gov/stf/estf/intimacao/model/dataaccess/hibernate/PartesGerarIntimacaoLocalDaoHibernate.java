package br.gov.stf.estf.intimacao.model.dataaccess.hibernate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.configuracao.model.dataaccess.hibernate.Util;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.ModeloComunicacaoEnum;
import br.gov.stf.estf.intimacao.model.dataaccess.ModeloComunicacaoLocalDao;
import br.gov.stf.estf.intimacao.model.dataaccess.PartesGerarIntimacaoLocalDao;
import br.gov.stf.estf.intimacao.model.dataaccess.TipoMeioIntimacaoEnum;
import br.gov.stf.estf.intimacao.visao.dto.ParteIntimacaoDto;
import br.gov.stf.estf.intimacao.visao.dto.ParteProcessoIntimacaoDto;
import br.gov.stf.estf.intimacao.visao.dto.PecaIntimacaoDto;
import br.gov.stf.estf.intimacao.visao.dto.ProcessoIntimacaoDto;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.model.entity.BaseEntity;

@Repository
public class PartesGerarIntimacaoLocalDaoHibernate extends GenericHibernateDao implements PartesGerarIntimacaoLocalDao {
    
    @Autowired
    private ModeloComunicacaoLocalDao modeloComunicacaoLocalDao;
    
    @SuppressWarnings("unchecked")
    public PartesGerarIntimacaoLocalDaoHibernate() {
        super(BaseEntity.class);
    }
    
    @Override
    public List<ParteIntimacaoDto> listarPartes(Date dataPublicacaoDj,
            TipoMeioIntimacaoEnum tipoMeioIntimacaoProcesso,
            Boolean intimacaoRealizada,
            Boolean representanteComPrerrogIntPess,
            TipoMeioIntimacaoEnum tipoMeioIntimacaoRepresentanteParte) throws DaoException {
        Session session = retrieveSession();
        SQLQuery query = session.createSQLQuery(getQueryListaPartes(dataPublicacaoDj,
                tipoMeioIntimacaoProcesso,
                intimacaoRealizada,
                representanteComPrerrogIntPess,
                tipoMeioIntimacaoRepresentanteParte));
        query.setTimestamp("dataPublicacaoInicio", Util.inicioDia(dataPublicacaoDj));
        query.setTimestamp("dataPublicacaoFim", Util.fimDia(dataPublicacaoDj));
        List<Long> codigosModeloComunicacao = new ArrayList<Long>();
        ModeloComunicacao modeloComunicacao = modeloComunicacaoLocalDao.buscar(ModeloComunicacaoEnum.MANDADO);
        codigosModeloComunicacao.add(modeloComunicacao.getId());
        modeloComunicacao = modeloComunicacaoLocalDao.buscar(ModeloComunicacaoEnum.CARTA);
        codigosModeloComunicacao.add(modeloComunicacao.getId());
        modeloComunicacao = modeloComunicacaoLocalDao.buscar(ModeloComunicacaoEnum.INTIMACAO_DESPACHO_DECISAO_ACORDAO);
        codigosModeloComunicacao.add(modeloComunicacao.getId());
        query.setParameterList("codigosModeloComunicacao", codigosModeloComunicacao);
        if (tipoMeioIntimacaoProcesso != null) {
            query.setString("tipoMeioIntimacaoProcesso", tipoMeioIntimacaoProcesso.getId());
        }
        if (intimacaoRealizada != null) {
            query.setString("intimacaoRealizada", intimacaoRealizada ? "S" : "N");
        }
        if (representanteComPrerrogIntPess != null) {
            query.setString("representanteComPrerrogIntPess", representanteComPrerrogIntPess ? "S" : "N");
        }
        if (tipoMeioIntimacaoRepresentanteParte != null) {
            query.setString("tipoMeioIntimacaoReprParte", tipoMeioIntimacaoRepresentanteParte.getId());
        }
        query.setResultTransformer(new ParteIntimacaoDtoResultTransformer());
        
        return query.list();
    }
    
    @Override
    public List<String> obterUFParte(long seqPessoa) throws DaoException {
        Session session = retrieveSession();
        SQLQuery query = session.createSQLQuery(getQueryUFParte());
        query.setLong("seqPessoa", seqPessoa);
        query.setResultTransformer(new UFPessoaResultTransformer());
        
        return query.list();
    }
    
    @Override
    public List<Setor> obterSetor(String sigClasse, long numProcesso) throws DaoException {
        Session session = retrieveSession();
        SQLQuery query = session.createSQLQuery(getQuerySetor());
        query.setString("sigClasse", sigClasse);
        query.setLong("numProcesso", numProcesso);
        query.setResultTransformer(new SetorResultTransformer());
        
        return query.list();
    }
    
    @Override
    public String obterClasseProcesso(String siglaClasseProcesso) throws DaoException {
        
        Session session = retrieveSession();
        SQLQuery query = session.createSQLQuery(getQueryClasseProcesso());
        query.setString("siglaClasseProcesso", siglaClasseProcesso);
        query.setResultTransformer(new ClasseProcessoResultTransformer());
        
        return query.list().get(0).toString();
    }
    
    private String getQueryListaPartes(Date dataPublicacaoDj,
            TipoMeioIntimacaoEnum tipoMeioIntimacaoProcesso,
            Boolean intimacaoRealizada,
            Boolean representanteComPrerrogIntPess,
            TipoMeioIntimacaoEnum tipoMeioIntimacaoRepresentanteParte) {
        StringBuilder query = new StringBuilder();
//        query.append("	SELECT * FROM (	");
        query.append(getQueryListaPartesGeral(dataPublicacaoDj, tipoMeioIntimacaoProcesso, intimacaoRealizada, representanteComPrerrogIntPess, tipoMeioIntimacaoRepresentanteParte));
        query.append(" MINUS ");
        query.append(getQueryListaPartesPoloAtivoAtosOrdinatorios(dataPublicacaoDj, tipoMeioIntimacaoProcesso, intimacaoRealizada, representanteComPrerrogIntPess, tipoMeioIntimacaoRepresentanteParte));
//        query.append("	)  WHERE SEQ_PESSOA = 2538935 ");

/*
        File file = new File("c:\\temp\\teste.sql");
        BufferedWriter writer = null;
        try {
        	writer = new BufferedWriter(new FileWriter(file));
            writer.write(query.toString());
        } catch (Exception e) {
			e.printStackTrace();
		} finally {
            if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        }
*/        
        return query.toString();
        
    }
    
	private String getQueryListaPartes(TipoMeioIntimacaoEnum tipoMeioComunicacaoEnum) {
		StringBuilder query = new StringBuilder();

		// Decisões e despachos
		query.append("SELECT pe.nom_pessoa, ");
		query.append("       NVL(dp.num_dj, dp.num_edicao_dje) num_dj, ");
		query.append("       pe.seq_pessoa,  ");
		query.append("       pe.tip_meio_intimacao, ");
		query.append("       u.flg_usuario_externo, ");
		query.append("       u.sig_usuario, ");
		query.append("       pp.sig_classe_proces, ");
		query.append("       pp.num_processo,  ");
		query.append("       p.tip_meio_processo, ");
		query.append("       pp.seq_objeto_incidente, ");
		query.append("       pp.cod_materia,  ");
		query.append("       pp.num_materia,    ");
		query.append("       pc.seq_peca_processual, ");
		query.append("       pc.seq_documento, ");
		query.append("       dp.dat_divulgacao_dje, ");
		query.append("       tpp.dsc_tipo_peca_processual, ");
		query.append("       NVL((SELECT DISTINCT 'M' ");
		query.append("             FROM corporativo.pessoa_endereco pee ");
		query.append("            WHERE pee.seq_pessoa = pe.seq_pessoa ");
		query.append("              AND pee.sig_uf = 'DF'), 'C') tipo_correspondencia, ");
		query.append("       c.dsc_classe, ");
		query.append("  	   NVL((SELECT s.cod_setor ||'-'|| s.dsc_setor ");
		query.append("              FROM judiciario.incidente_preferencia ip,  ");
		query.append("                   judiciario.mapeamento_classe_setor mcs, stf.setores s ");
		query.append("             WHERE ip.seq_objeto_incidente = p.seq_objeto_incidente ");
		query.append("               AND mcs.sig_classe = p.sig_classe_proces ");
		query.append("               AND mcs.seq_tipo_preferencia = ip.seq_tipo_preferencia ");
		query.append("               AND mcs.cod_setor = s.cod_setor), ");
		query.append("           (SELECT s.cod_setor ||'-'|| s.dsc_setor ");
		query.append("              FROM judiciario.mapeamento_classe_setor mcs, stf.setores s ");
		query.append("             WHERE mcs.cod_setor = s.cod_setor ");
		query.append("               AND mcs.sig_classe = p.sig_classe_proces ");
		query.append("               AND mcs.seq_tipo_preferencia IS NULL)) setor	   ");
		query.append("  FROM stf.materias m, stf.data_publicacoes dp, stf.processo_publicados pp, judiciario.objeto_incidente oi, ");
		query.append("       judiciario.processo p, judiciario.recurso_processo rp, judiciario.incidente_julgamento ij, ");
		query.append("	   judiciario.parte_processual ptp, corporativo.pessoa pe, corporativo.usuario u, stf.textos t, ");
		query.append("	   stf.documento_texto dt, judiciario.peca_processual pc, judiciario.tipo_peca_processual tpp, ");
		query.append("	   judiciario.classe c ");
		query.append(" WHERE m.seq_data_publicacoes = dp.seq_data_publicacoes ");
		query.append("   AND pp.cod_capitulo = m.cod_capitulo  ");
		query.append("   AND pp.cod_materia = m.cod_materia  ");
		query.append("   AND pp.ano_materia = m.ano_materia   ");
		query.append("   AND pp.num_materia = m.num_materia ");
		query.append("   AND oi.seq_objeto_incidente = pp.seq_objeto_incidente ");
		query.append("   AND p.seq_objeto_incidente = oi.seq_objeto_incidente_principal ");
		query.append("   AND rp.seq_objeto_incidente (+) = oi.seq_objeto_incidente ");
		query.append("   AND ij.seq_objeto_incidente (+) = oi.seq_objeto_incidente ");
		query.append("   AND ptp.seq_objeto_incidente = p.seq_objeto_incidente ");
		query.append("   AND pe.seq_pessoa = ptp.seq_pessoa ");
		query.append("   AND u.seq_pessoa (+) = pe.seq_pessoa ");
		query.append("   AND pp.seq_objeto_incidente = t.seq_objeto_incidente ");
		query.append("   AND pp.seq_arquivo_eletronico_texto = t.seq_arquivo_eletronico ");
		query.append("   AND c.sig_classe = pp.sig_classe_proces ");
		query.append("   AND t.seq_textos (+) = dt.seq_textos ");
		query.append("   AND dt.seq_documento (+) = pc.seq_documento ");
		query.append("   AND pc.seq_tipo_situacao_peca <> 1 ");
		query.append("   AND pc.cod_tipo_peca_processual (+) = tpp.cod_tipo_peca_processual ");
		query.append("   AND m.cod_capitulo = 6 ");
		query.append("   AND dt.seq_tipo_situacao_documento IN (3, 7) ");
		query.append("   AND m.cod_conteudo = 50 ");
		query.append("   AND ptp.cod_categoria IN (202,203,257,300) ");
		query.append("   AND pp.sig_classe_proces NOT IN ('ADI','ADO','ADC','ADPF','AP','Inq') ");
		query.append("   AND TRUNC(dp.dat_divulgacao_dje) = TO_DATE(:dataPublicacao, 'DD/MM/YYYY') ");
		query.append("UNION ");
		// Acórdãos e Repercussão Geral
		query.append("SELECT pe.nom_pessoa, ");
		query.append("       NVL(dp.num_dj, dp.num_edicao_dje) num_dj,  ");
		query.append("       pe.seq_pessoa,  ");
		query.append("       pe.tip_meio_intimacao, ");
		query.append("       u.flg_usuario_externo, ");
		query.append("       u.sig_usuario, ");
		query.append("       pp.sig_classe_proces, ");
		query.append("       pp.num_processo,  ");
		query.append("       p.tip_meio_processo, ");
		query.append("       pp.seq_objeto_incidente, ");
		query.append("       pp.cod_materia,  ");
		query.append("       pp.num_materia,    ");
		query.append("       pc.seq_peca_processual, ");
		query.append("       pc.seq_documento, ");
		query.append("       dp.dat_divulgacao_dje, ");
		query.append("       tpp.dsc_tipo_peca_processual, ");
		query.append("       NVL((SELECT DISTINCT 'M' ");
		query.append("             FROM corporativo.pessoa_endereco pee ");
		query.append("            WHERE pee.seq_pessoa = pe.seq_pessoa ");
		query.append("              AND pee.sig_uf = 'DF'), 'C') tipo_correspondencia, ");
		query.append("       c.dsc_classe, ");
		query.append("  	   NVL((SELECT s.cod_setor ||'-'|| s.dsc_setor ");
		query.append("              FROM judiciario.incidente_preferencia ip,  ");
		query.append("                   judiciario.mapeamento_classe_setor mcs, stf.setores s ");
		query.append("             WHERE ip.seq_objeto_incidente = p.seq_objeto_incidente ");
		query.append("               AND mcs.sig_classe = p.sig_classe_proces ");
		query.append("               AND mcs.seq_tipo_preferencia = ip.seq_tipo_preferencia ");
		query.append("               AND mcs.cod_setor = s.cod_setor), ");
		query.append("           (SELECT s.cod_setor ||'-'|| s.dsc_setor ");
		query.append("              FROM judiciario.mapeamento_classe_setor mcs, stf.setores s ");
		query.append("             WHERE mcs.cod_setor = s.cod_setor ");
		query.append("               AND mcs.sig_classe = p.sig_classe_proces ");
		query.append("               AND mcs.seq_tipo_preferencia IS NULL)) setor	   ");
		query.append("  FROM stf.materias m, stf.data_publicacoes dp, stf.processo_publicados pp, judiciario.objeto_incidente oi, ");
		query.append("       judiciario.processo p, judiciario.recurso_processo rp, judiciario.incidente_julgamento ij, ");
		query.append("	   judiciario.parte_processual ptp, corporativo.pessoa pe, corporativo.usuario u, ");
		query.append("	   judiciario.peca_processual pc, judiciario.tipo_peca_processual  tpp, judiciario.classe c ");
		query.append(" WHERE m.seq_data_publicacoes = dp.seq_data_publicacoes  ");
		query.append("   AND pp.cod_capitulo = m.cod_capitulo  ");
		query.append("   AND pp.cod_materia = m.cod_materia  ");
		query.append("   AND pp.ano_materia = m.ano_materia   ");
		query.append("   AND pp.num_materia = m.num_materia ");
		query.append("   AND oi.seq_objeto_incidente = pp.seq_objeto_incidente ");
		query.append("   AND p.seq_objeto_incidente = oi.seq_objeto_incidente_principal ");
		query.append("   AND rp.seq_objeto_incidente (+) = oi.seq_objeto_incidente ");
		query.append("   AND ij.seq_objeto_incidente (+) = oi.seq_objeto_incidente ");
		query.append("   AND ptp.seq_objeto_incidente = p.seq_objeto_incidente ");
		query.append("   AND pe.seq_pessoa = ptp.seq_pessoa ");
		query.append("   AND u.seq_pessoa (+) = pe.seq_pessoa  ");
		query.append("   AND pc.seq_objeto_incidente (+) = pp.seq_objeto_incidente ");
		query.append("   AND c.sig_classe = pp.sig_classe_proces ");
		query.append("   AND pc.seq_tipo_situacao_peca <> 1 ");
		query.append("   AND pc.cod_tipo_peca_processual = 1221 ");
		query.append("   AND pc.cod_tipo_peca_processual = tpp.cod_tipo_peca_processual ");
		query.append("   AND (m.cod_capitulo = 5 ");
		query.append("    OR (m.cod_capitulo = 2 AND m.cod_materia IN (7, 11)))  ");
		query.append("   AND m.cod_conteudo = 50 ");
		query.append("   AND ptp.cod_categoria IN (202,203,257,300) ");
		query.append("   AND pp.sig_classe_proces NOT IN ('ADI','ADO','ADC','ADPF','AP','Inq') ");
		query.append("   AND TRUNC(dp.dat_divulgacao_dje) = TO_DATE(:dataPublicacao, 'DD/MM/YYYY') ");

		return query.toString();
	}	
    
    
    private String getQueryListaPartesGeral(Date dataPublicacaoDj,
            TipoMeioIntimacaoEnum tipoMeioIntimacaoProcesso,
            Boolean intimacaoRealizada,
            Boolean representanteComPrerrogIntPess,
            TipoMeioIntimacaoEnum tipoMeioIntimacaoRepresentanteParte) {
        StringBuilder query = new StringBuilder();
        query.append(gerarTabelasTemporarias());
        query.append(gerarSelectPartesProcessosDj(dataPublicacaoDj));
        if (tipoMeioIntimacaoProcesso != null
                || intimacaoRealizada != null
                || representanteComPrerrogIntPess != null
                || tipoMeioIntimacaoRepresentanteParte != null) {
            query.append(" WHERE ");
            String and = "";
            if (tipoMeioIntimacaoProcesso != null) {
                query.append(and);
                and = " AND ";
                query.append("dados.tip_meio_processo = :tipoMeioIntimacaoProcesso ");
            }
            if (intimacaoRealizada != null) {
                query.append(and);
                and = " AND ";
                query.append("dados.flg_comunicacao = :intimacaoRealizada ");
            }
            if (representanteComPrerrogIntPess != null) {
                query.append(and);
                and = " AND ";
                query.append("dados.flg_intimacao_pessoal = :representanteComPrerrogIntPess ");
            }
            if (tipoMeioIntimacaoRepresentanteParte != null) {
                query.append(and);
                and = " AND ";
                query.append("dados.tip_meio_intimacao = :tipoMeioIntimacaoReprParte ");
            }
        }
        return query.toString();
    }    
    
    private String getQueryListaPartesPoloAtivoAtosOrdinatorios(Date dataPublicacaoDj,
            TipoMeioIntimacaoEnum tipoMeioIntimacaoProcesso,
            Boolean intimacaoRealizada,
            Boolean representanteComPrerrogIntPess,
            TipoMeioIntimacaoEnum tipoMeioIntimacaoRepresentanteParte) {
        StringBuilder query = new StringBuilder();
        query.append(gerarSelectPartesProcessosDjAtosOrdinatorios(dataPublicacaoDj));
        if (tipoMeioIntimacaoProcesso != null
                || intimacaoRealizada != null
                || representanteComPrerrogIntPess != null
                || tipoMeioIntimacaoRepresentanteParte != null) {
            query.append(" WHERE ");
            String and = "";
            if (tipoMeioIntimacaoProcesso != null) {
                query.append(and);
                and = " AND ";
                query.append("dados.tip_meio_processo = :tipoMeioIntimacaoProcesso ");
            }
            if (intimacaoRealizada != null) {
                query.append(and);
                and = " AND ";
                query.append("dados.flg_comunicacao = :intimacaoRealizada ");
            }
            if (representanteComPrerrogIntPess != null) {
                query.append(and);
                and = " AND ";
                query.append("dados.flg_intimacao_pessoal = :representanteComPrerrogIntPess ");
            }
            if (tipoMeioIntimacaoRepresentanteParte != null) {
                query.append(and);
                and = " AND ";
                query.append("dados.tip_meio_intimacao = :tipoMeioIntimacaoReprParte ");
            }
        }
        return query.toString();
    }     
    
    private String gerarTabelasTemporarias() {
        StringBuilder query = new StringBuilder();
        query.append(" WITH ");
        query.append(" dj ");
        query.append(" AS ( ");
        query.append(gerarQueryDj());
        query.append(" ), \n");
        query.append(" pe ");
        query.append(" AS ( ");
        query.append(gerarQueryPartes());
        query.append(" ) ");
        return query.toString();
    }
    
    private String gerarQueryDj() {
        StringBuilder query = new StringBuilder();
        query.append(gerarQueryDjDespacho());
        query.append(" UNION ALL \n");
        query.append(gerarQueryDjAcordao());
        query.append(" UNION ALL \n");
        query.append(gerarQueryDjAtosOrdinatorios());
        return query.toString();
    }
    
    private String gerarQueryDjDespacho() {
        String sql = "SELECT 'Despacho' dsc_titulo_materia,\n"
                + "                    pp.seq_objeto_incidente,\n"
                + "                    NVL (dp.num_dj, dp.num_edicao_dje) num_dj,\n"
                + "                    dp.dat_divulgacao_dje,\n"
                + "                    dp.seq_data_publicacoes,\n"
                + "                    pc.seq_peca_processual,\n"
                + "                    pc.seq_documento,\n"
                + "                    pp.num_processo,\n"
                + "                    pp.sig_classe_proces,\n"
                + "                    cod_tipo_peca_processual,\n"
                + "                    pp.cod_materia,\n"
                + "                    pp.num_materia,\n"
                + "                    p.tip_meio_processo,\n"
                + "                    oi.seq_objeto_incidente_principal\n"
                + "               FROM stf.data_publicacoes dp,\n"
                + "                    stf.materias m,\n"
                + "                    stf.processo_publicados pp,\n"
                + "                    judiciario.peca_processual pc,\n"
                + "                    stf.textos t,\n"
                + "                    stf.documento_texto dt,\n"
                + "                    judiciario.objeto_incidente oi,\n"
                + "                    judiciario.processo p\n"
                + "              WHERE m.seq_data_publicacoes = dp.seq_data_publicacoes\n"
                + "                    AND pp.cod_capitulo = m.cod_capitulo\n"
                + "                    AND pp.cod_materia = m.cod_materia\n"
                + "                    AND pp.ano_materia = m.ano_materia\n"
                + "                    AND pp.num_materia = m.num_materia\n"
                + "                    AND pp.seq_arquivo_eletronico_texto = t.seq_arquivo_eletronico\n"
                + "                    AND pp.seq_objeto_incidente = oi.seq_objeto_incidente\n"
                + "                    AND oi.seq_objeto_incidente_principal = p.seq_objeto_incidente\n"
                + "                    AND t.seq_textos(+) = dt.seq_textos\n"
                + "                    AND pc.seq_objeto_incidente(+) = pp.seq_objeto_incidente\n"
                + "                    AND pc.seq_tipo_situacao_peca <> 1\n"
                + "                    AND m.cod_capitulo = 6\n"
                + "                    AND m.cod_conteudo = 50\n"
                + "                    AND dt.seq_tipo_situacao_documento IN (3, 7)\n"
                + "                    AND dt.seq_documento(+) = pc.seq_documento\n"
                + "                    AND pp.sig_classe_proces NOT IN ('ADI', 'ADO', 'ADC', 'ADPF', 'AP', 'Inq', 'Ext', 'PPE')";
        return sql;
    }
    
    private String gerarQueryDjAcordao() {
        String sql = "SELECT 'Acórdão' dsc_titulo_materia,\n"
                + "                    pp.seq_objeto_incidente,\n"
                + "                    NVL (dp.num_dj, dp.num_edicao_dje) num_dj,\n"
                + "                    dp.dat_divulgacao_dje,\n"
                + "                    dp.seq_data_publicacoes,\n"
                + "                    pc.seq_peca_processual,\n"
                + "                    pc.seq_documento,\n"
                + "                    pp.num_processo,\n"
                + "                    pp.sig_classe_proces,\n"
                + "                    pc.cod_tipo_peca_processual,\n"
                + "                    pp.cod_materia,\n"
                + "                    pp.num_materia,\n"
                + "                    p.tip_meio_processo,\n"
                + "                    oi.seq_objeto_incidente_principal\n"
                + "               FROM stf.data_publicacoes dp,\n"
                + "                    stf.materias m,\n"
                + "                    stf.processo_publicados pp,\n"
                + "                    judiciario.peca_processual pc,\n"
                + "                    judiciario.objeto_incidente oi,\n"
                + "                    judiciario.processo p\n"
                + "              WHERE m.seq_data_publicacoes = dp.seq_data_publicacoes\n"
                + "                    AND pp.cod_capitulo = m.cod_capitulo\n"
                + "                    AND pp.cod_materia = m.cod_materia\n"
                + "                    AND pp.ano_materia = m.ano_materia\n"
                + "                    AND pp.num_materia = m.num_materia\n"
                + "                    AND pp.seq_objeto_incidente = oi.seq_objeto_incidente\n"
                + "                    AND oi.seq_objeto_incidente_principal = p.seq_objeto_incidente\n"
                + "                    AND m.cod_conteudo = 50\n"
                + "                    AND pc.cod_tipo_peca_processual = 1221\n"
                + "                    AND pc.seq_objeto_incidente(+) = pp.seq_objeto_incidente\n"
                + "                    AND pc.seq_tipo_situacao_peca <> 1\n"
                + "                    AND (m.cod_capitulo = 5 OR (m.cod_capitulo = 2 AND m.cod_materia IN (7, 11))) "
                + "					   AND pp.sig_classe_proces NOT IN ('ADI', 'ADO', 'ADC', 'ADPF', 'AP', 'Inq', 'Ext', 'PPE')";
        return sql;
    }
    
    private String gerarQueryDjAtosOrdinatorios() {
        String sql = "SELECT 'Atos ordinatórios' dsc_titulo_materia,\n"
                + "                    pp.seq_objeto_incidente,\n"
                + "                    NVL (dp.num_dj, dp.num_edicao_dje) num_dj,\n"
                + "                    dp.dat_divulgacao_dje,\n"
                + "                    dp.seq_data_publicacoes,\n"
                + "                    pc.seq_peca_processual,\n"
                + "                    pc.seq_documento,\n"
                + "                    pp.num_processo,\n"
                + "                    pp.sig_classe_proces,\n"
                + "                    pc.cod_tipo_peca_processual,\n"
                + "                    pp.cod_materia,\n"
                + "                    pp.num_materia,\n"
                + "                    p.tip_meio_processo,\n"
                + "                    oi.seq_objeto_incidente_principal\n"
                + "               FROM stf.materias m,\n"
                + "                    stf.data_publicacoes dp,\n"
                + "                    stf.processo_publicados pp,\n"
                + "                    judiciario.peca_processual pc,\n"
                + "                    judiciario.objeto_incidente oi,\n"
                + "                    judiciario.processo p\n"
                + "             WHERE  m.seq_data_publicacoes = dp.seq_data_publicacoes\n"
                + "                    AND pp.cod_capitulo = m.cod_capitulo\n"
                + "                    AND pp.cod_materia = m.cod_materia\n"
                + "                    AND pp.ano_materia = m.ano_materia\n"
                + "                    AND pp.num_materia = m.num_materia\n"
                + "                    AND pp.seq_objeto_incidente = oi.seq_objeto_incidente\n"
                + "                    AND oi.seq_objeto_incidente_principal = p.seq_objeto_incidente\n"
                + "                    AND pc.seq_objeto_incidente(+) = pp.seq_objeto_incidente\n"
                + "                    AND pc.seq_tipo_situacao_peca <> 1\n"
                + "                    AND pc.cod_tipo_peca_processual IN (1326, 1510)\n"
                + "                    AND m.cod_capitulo = 6\n"
                + "                    AND m.cod_materia = 14\n"
                + "                    AND m.cod_conteudo = 50\n"
                + "                    AND pp.sig_classe_proces NOT IN ('ADI', 'ADO', 'ADC', 'ADPF', 'AP', 'Inq', 'Ext', 'PPE')";
        return sql;
    }
    
    private String gerarQueryPartes() {
        String sql = "SELECT ptp.seq_pessoa,\n"
                + "                    nom_pessoa,\n"
                + "                    tip_meio_intimacao,\n"
                + "                    pe.flg_intimacao_pessoal,\n"
                + "                    seq_objeto_incidente,\n"
                + "                    seq_parte_processual,\n"
                + "                    u.flg_usuario_externo,\n"
                + "                    u.sig_usuario\n"
                + "               FROM judiciario.parte_processual ptp,\n"
                + "                    corporativo.pessoa pe,\n"
                + "                    corporativo.usuario u\n"
                + "              WHERE pe.seq_pessoa = ptp.seq_pessoa\n"
                + "                    AND u.seq_pessoa(+) = pe.seq_pessoa\n"
//                + "                    AND ptp.seq_pessoa = 2420254 \n"
                + "                    AND ptp.cod_categoria IN (202, 203, 257, 300) ";        
        return sql;
    }
    
    private String gerarSelectPartesProcessosDj(Date dataPublicacaoDj) {
        String sql = "SELECT *\n"
                + "FROM   (\n"
                + "SELECT DISTINCT 	dsc_titulo_materia,\n"
                + "       nom_pessoa,\n"
                + "       num_dj,\n"
                + "       seq_pessoa,\n"
                + "       tip_meio_intimacao,\n"
                + "       flg_intimacao_pessoal,\n"
                + "       flg_usuario_externo,\n"
                + "       sig_usuario,\n"
                + "       dj.sig_classe_proces,\n"
                + "       dj.num_processo,\n"
                + "       dj.tip_meio_processo,\n"
                + "       dj.seq_objeto_incidente,\n"
                + "       dj.cod_materia,\n"
                + "       dj.num_materia,\n"
                + "       dj.seq_peca_processual,\n"
                + "       dj.seq_documento,\n"
                + "       NULL seq_documento_acordao,\n"
                + "       dj.dat_divulgacao_dje,\n"
                + "       tpp.cod_tipo_peca_processual,\n"
                + "       DECODE (tpp.dsc_tipo_peca_processual,\n"
                + "               'Inteiro teor do acórdão', 'Acórdão',\n"
                + "               tpp.dsc_tipo_peca_processual)\n"
                + "           dsc_tipo_peca_processual,\n"
                + "       NVL ( (SELECT DISTINCT 'M'\n"
                + "                FROM corporativo.pessoa_endereco pee\n"
                + "               WHERE pee.seq_pessoa = pe.seq_pessoa AND pee.sig_uf = 'DF'),\n"
                + "            'C')\n"
                + "           tipo_correspondencia,\n"
                + "       c.dsc_classe,\n"
                + "       NVL (\n"
                + "           (SELECT s.cod_setor || '-' || s.dsc_setor\n"
                + "              FROM judiciario.incidente_preferencia ip,\n"
                + "                   judiciario.mapeamento_classe_setor mcs,\n"
                + "                   stf.setores s\n"
                + "             WHERE ip.seq_objeto_incidente = dj.seq_objeto_incidente_principal\n"
                + "                   AND mcs.sig_classe = dj.sig_classe_proces\n"
                + "                   AND mcs.seq_tipo_preferencia = ip.seq_tipo_preferencia\n"
                + "                   AND mcs.cod_setor = s.cod_setor),\n"
                + "           (SELECT s.cod_setor || '-' || s.dsc_setor\n"
                + "              FROM judiciario.mapeamento_classe_setor mcs, stf.setores s\n"
                + "             WHERE     mcs.cod_setor = s.cod_setor\n"
                + "                   AND mcs.sig_classe = dj.sig_classe_proces\n"
                + "                   AND mcs.seq_tipo_preferencia IS NULL))\n"
                + "           setor,\n"
                + "       (SELECT DECODE (COUNT (com.seq_comunicacao), 0, 'N', 'S')\n"
                + "          FROM judiciario.comunicacao com,\n"
                + "               judiciario.comunicacao_objeto_incidente coi\n"
//                + "               ,judiciario.peca_proc_elet_comunicacao ppec\n"
                + "         WHERE com.seq_pessoa_destinataria = pe.seq_pessoa\n"
                + "               AND com.seq_comunicacao = coi.seq_comunicacao\n"
                + "               AND coi.seq_objeto_incidente = dj.seq_objeto_incidente\n"
//                + "               AND ppec.seq_comunicacao = com.seq_comunicacao\n"
//                + "               AND ppec.seq_peca_proc_eletronico = dj.seq_peca_processual\n"
                + "               AND com.seq_pessoa_destinataria IS NOT NULL\n"
//                + "               AND dat_comunicacao_enviada BETWEEN :dataPublicacaoInicio AND :dataPublicacaoFim\n"
				+ "               AND ( TO_CHAR(com.DAT_INCLUSAO,'DD/MM/YYYY') = TO_CHAR(SYSDATE,'DD/MM/YYYY') or TO_CHAR(com.DAT_COMUNICACAO_ENVIADA,'DD/MM/YYYY') = TO_CHAR(SYSDATE,'DD/MM/YYYY') or (com.DAT_INCLUSAO BETWEEN :dataPublicacaoInicio AND :dataPublicacaoFim) or (com.DAT_COMUNICACAO_ENVIADA BETWEEN :dataPublicacaoInicio AND :dataPublicacaoFim) ) \n"
                + "               AND seq_modelo_comunicacao IN (:codigosModeloComunicacao)\n"
                + "       )\n"
                + "           flg_comunicacao\n"
                + "  FROM dj,\n"
                + "       pe,\n"
                + "       judiciario.classe c,\n"
                + "       judiciario.tipo_peca_processual tpp\n"
                + "WHERE  dj.seq_objeto_incidente = pe.seq_objeto_incidente\n"
                + "       AND dj.sig_classe_proces = c.sig_classe\n"
                + "       AND tpp.cod_tipo_peca_processual = dj.cod_tipo_peca_processual ";
        if (dataPublicacaoDj != null) {
            sql += "AND dj.dat_divulgacao_dje BETWEEN :dataPublicacaoInicio AND :dataPublicacaoFim";
        }
        sql += ") dados";
        return sql;
    }
    
    private String gerarSelectPartesProcessosDjAtosOrdinatorios(Date dataPublicacaoDj) {
        String sql = "SELECT *\n"
                + "FROM   (\n"
                + "SELECT DISTINCT 	dsc_titulo_materia,\n"
                + "       nom_pessoa,\n"
                + "       num_dj,\n"
                + "       seq_pessoa,\n"
                + "       tip_meio_intimacao,\n"
                + "       flg_intimacao_pessoal,\n"
                + "       flg_usuario_externo,\n"
                + "       sig_usuario,\n"
                + "       dj.sig_classe_proces,\n"
                + "       dj.num_processo,\n"
                + "       dj.tip_meio_processo,\n"
                + "       dj.seq_objeto_incidente,\n"
                + "       dj.cod_materia,\n"
                + "       dj.num_materia,\n"
                + "       dj.seq_peca_processual,\n"
                + "       dj.seq_documento,\n"
                + "       NULL seq_documento_acordao,\n"
                + "       dj.dat_divulgacao_dje,\n"
                + "       tpp.cod_tipo_peca_processual,\n"
                + "       DECODE (tpp.dsc_tipo_peca_processual,\n"
                + "               'Inteiro teor do acórdão', 'Acórdão',\n"
                + "               tpp.dsc_tipo_peca_processual)\n"
                + "           dsc_tipo_peca_processual,\n"
                + "       NVL ( (SELECT DISTINCT 'M'\n"
                + "                FROM corporativo.pessoa_endereco pee\n"
                + "               WHERE pee.seq_pessoa = pe.seq_pessoa AND pee.sig_uf = 'DF'),\n"
                + "            'C')\n"
                + "           tipo_correspondencia,\n"
                + "       c.dsc_classe,\n"
                + "       NVL (\n"
                + "           (SELECT s.cod_setor || '-' || s.dsc_setor\n"
                + "              FROM judiciario.incidente_preferencia ip,\n"
                + "                   judiciario.mapeamento_classe_setor mcs,\n"
                + "                   stf.setores s\n"
                + "             WHERE ip.seq_objeto_incidente = dj.seq_objeto_incidente_principal\n"
                + "                   AND mcs.sig_classe = dj.sig_classe_proces\n"
                + "                   AND mcs.seq_tipo_preferencia = ip.seq_tipo_preferencia\n"
                + "                   AND mcs.cod_setor = s.cod_setor),\n"
                + "           (SELECT s.cod_setor || '-' || s.dsc_setor\n"
                + "              FROM judiciario.mapeamento_classe_setor mcs, stf.setores s\n"
                + "             WHERE     mcs.cod_setor = s.cod_setor\n"
                + "                   AND mcs.sig_classe = dj.sig_classe_proces\n"
                + "                   AND mcs.seq_tipo_preferencia IS NULL))\n"
                + "           setor,\n"
                + "       (SELECT DECODE (COUNT (com.seq_comunicacao), 0, 'N', 'S')\n"
                + "          FROM judiciario.comunicacao com,\n"
                + "               judiciario.comunicacao_objeto_incidente coi\n"
//                + "               ,judiciario.peca_proc_elet_comunicacao ppec\n"
                + "         WHERE com.seq_pessoa_destinataria = pe.seq_pessoa\n"
                + "               AND com.seq_comunicacao = coi.seq_comunicacao\n"
                + "               AND coi.seq_objeto_incidente = dj.seq_objeto_incidente\n"
//                + "               AND ppec.seq_comunicacao = com.seq_comunicacao\n"
//                + "               AND ppec.seq_peca_proc_eletronico = dj.seq_peca_processual\n"
                + "               AND com.seq_pessoa_destinataria IS NOT NULL\n"
//                + "               AND dat_comunicacao_enviada BETWEEN :dataPublicacaoInicio AND :dataPublicacaoFim\n"
				+ "               AND ( TO_CHAR(com.DAT_INCLUSAO,'DD/MM/YYYY') = TO_CHAR(SYSDATE,'DD/MM/YYYY') or TO_CHAR(com.DAT_COMUNICACAO_ENVIADA,'DD/MM/YYYY') = TO_CHAR(SYSDATE,'DD/MM/YYYY') ) \n"
                + "               AND seq_modelo_comunicacao IN (:codigosModeloComunicacao)\n"
                + "       )\n"
                + "           flg_comunicacao\n"
                + "  FROM ( " + gerarQueryDjAtosOrdinatorios() + " ) dj,\n"
                + " ( " + gerarQueryPartes()
                + "  AND EXISTS ( "
                + "        SELECT 1 FROM judiciario.parte_processual pp "
                + "        WHERE pp.seq_objeto_incidente = ptp.seq_objeto_incidente "
                + "        AND pp.seq_pessoa = pe.seq_pessoa "
                + "        AND nvl(pp.num_ordem,0) < (SELECT min(num_ordem) " 
                + "                               FROM judiciario.parte_processual "
                + "                              WHERE seq_objeto_incidente = ptp.seq_objeto_incidente "
                + "                               AND tip_polo = 'PA')))  "
                + "       pe,\n"
                + "       judiciario.classe c,\n"
                + "       judiciario.tipo_peca_processual tpp\n"
                + "WHERE  dj.seq_objeto_incidente = pe.seq_objeto_incidente\n"
                + "       AND dj.sig_classe_proces = c.sig_classe\n"
                + "       AND tpp.cod_tipo_peca_processual = dj.cod_tipo_peca_processual ";
        if (dataPublicacaoDj != null) {
            sql += "AND dj.dat_divulgacao_dje BETWEEN :dataPublicacaoInicio AND :dataPublicacaoFim";
        }
        sql += ") dados";
        return sql;
    }    
    
    private String getQueryClasseProcesso() {
        StringBuilder query = new StringBuilder();
        
        query.append("SELECT ");
        
        query.append(" C.DSC_CLASSE ");
        
        query.append(" FROM  JUDICIARIO.CLASSE C ");
        
        query.append(" WHERE C.SIG_CLASSE = :siglaClasseProcesso ");
        
        return query.toString();
    }
    
    private String getQueryUFParte() {
        StringBuilder query = new StringBuilder();
        
        query.append("SELECT ");
        
        query.append(" PE.SIG_UF ");
        
        query.append(" FROM  CORPORATIVO.PESSOA_ENDERECO PE ");
        
        query.append(" WHERE PE.SEQ_PESSOA = :seqPessoa ");
        
        query.append(" AND PE.FLG_ATIVO = 'S' ");
        
        query.append(" ORDER BY PE.DAT_ALTERACAO DESC ");
        
        return query.toString();
    }
    
    private String getQuerySetor() {
        StringBuilder query = new StringBuilder();
        
        query.append(" SELECT ");
        query.append(" NVL((SELECT s.cod_setor ||'-'|| s.dsc_setor ");
        query.append(" FROM judiciario.processo p, judiciario.incidente_preferencia ip,  ");
        query.append("  judiciario.mapeamento_classe_setor mcs, stf.setores s ");
        query.append("   WHERE p.seq_objeto_incidente = ip.seq_objeto_incidente ");
        query.append("  AND mcs.sig_classe = p.sig_classe_proces ");
        query.append("  AND mcs.seq_tipo_preferencia = ip.seq_tipo_preferencia ");
        query.append(" AND mcs.cod_setor = s.cod_setor ");
        query.append(" AND p.sig_classe_proces = :sigClasse ");
        query.append(" AND p.num_processo = :numProcesso), ");
        query.append("  (SELECT s.cod_setor ||'-'|| s.dsc_setor ");
        query.append(" FROM judiciario.mapeamento_classe_setor mcs, stf.setores s ");
        query.append("  WHERE mcs.cod_setor = s.cod_setor ");
        query.append(" AND mcs.sig_classe = :sigClasse ");
        query.append("  AND mcs.seq_tipo_preferencia IS NULL)) ");
        query.append(" FROM dual ");
        
        return query.toString();
    }
    
    private class ParteIntimacaoDtoResultTransformer extends ParteProcessoResultTransformerAbstract {
        
        private Map<Long, ParteIntimacaoDto> mapaSeqPessoaParteIntimacaoDto = new HashMap<Long, ParteIntimacaoDto>();
        private Map<Long, Map<Long, ProcessoIntimacaoDto>> mapaSeqPessoaSeqObjetoIncidenteParteIntimacaoDto = new HashMap<Long, Map<Long, ProcessoIntimacaoDto>>();

        /**
         *
         */
        private static final long serialVersionUID = 1L;
        
        @Override
        public List transformList(List arg0) {
        	List listaPartes = new ArrayList();
        	listaPartes.addAll(mapaSeqPessoaParteIntimacaoDto.values());
            return listaPartes;
        }
        
        @Override
        public Object transformTuple(Object[] rowData, String[] aliases) {
            String nomeParte = getString(rowData[1]);
            String isUsuarioExterno = getString(rowData[6]);
            Long numeroDJ = getLong(rowData[2]);
            Long seqPessoa = getLong(rowData[3]);
            String tipoMeioIntimacao = getString(rowData[4]);
            String siglaClasseProcesso = getString(rowData[8]);
            Long numeroProcesso = getLong(rowData[9]);
            String tipoMeioProcesso = getString(rowData[10]);
            Long seqObjetoIncidente = getLong(rowData[11]);
            Long codigoMateria = getLong(rowData[12]);
            Long numeroMateria = getLong(rowData[13]);
            Long seqPecaProcessoEletronico = getLong(rowData[14]);
            Long seqDocumento = getLong(rowData[15]);
            Long seqDocumentoAcordao = null;
            if (rowData[16] != null) {
                seqDocumentoAcordao = getLong(rowData[16]);
            }
            Date dataDivulgacao = getDate(rowData[17]);
            Long idTipoPecaProcessual = getLong(rowData[18]);
            String descricaoTipoPecaProcessual = getString(rowData[19]);
            ModeloComunicacaoEnum modeloComunicacaoEnum = getString(rowData[20]).equals("M") ? ModeloComunicacaoEnum.MANDADO : ModeloComunicacaoEnum.CARTA;
            String descricaoClasseProcessual = getString(rowData[21]);
            String codigoDescricaoSetor = getString(rowData[22]);
            Long codigoSetor = null;
            String descricaoSetor = null;
            if (codigoDescricaoSetor != null && !codigoDescricaoSetor.trim().isEmpty()) {
                String[] arrayCodigoDescricaoSetor = codigoDescricaoSetor.split("-");
                String codigoSetorAux = arrayCodigoDescricaoSetor[0];
                descricaoSetor = arrayCodigoDescricaoSetor[1];
                codigoSetor = getLong(codigoSetorAux);
            }
            String descricaoMeioIntimacao;
            if (tipoMeioIntimacao.equalsIgnoreCase("E")
                    && (isUsuarioExterno != null
                    && isUsuarioExterno.equalsIgnoreCase("S"))) {
                descricaoMeioIntimacao = "Eletrônica";
            } else {
                descricaoMeioIntimacao = "Física";
            }
            Boolean intimado = null;
            if (rowData[23] != null) {
                intimado = rowData[23].equals("S");
            }
            
            ParteIntimacaoDto parteIntimacaoDto = mapaSeqPessoaParteIntimacaoDto.get(seqPessoa);
            if (parteIntimacaoDto == null) {
                parteIntimacaoDto = new ParteIntimacaoDto();
                parteIntimacaoDto.setSeqPessoa(seqPessoa);
                parteIntimacaoDto.setNomeParte(nomeParte);
                parteIntimacaoDto.setDescricaoMeioIntimacao(descricaoMeioIntimacao);
                parteIntimacaoDto.setTipoMeioIntimacao(tipoMeioIntimacao);
                parteIntimacaoDto.setModeloComunicacaoEnum(modeloComunicacaoEnum);
                parteIntimacaoDto.setNumeroDJ(numeroDJ);
                parteIntimacaoDto.setDataDivulgacao(dataDivulgacao);
                parteIntimacaoDto.setIsUsuarioExterno(isUsuarioExterno);
                parteIntimacaoDto.setProcessos(new ArrayList<ProcessoIntimacaoDto>());
                mapaSeqPessoaParteIntimacaoDto.put(seqPessoa, parteIntimacaoDto);
            }
            
            Map<Long, ProcessoIntimacaoDto> mapaSeqObjetoIncidenteParteIntimacaoDto = mapaSeqPessoaSeqObjetoIncidenteParteIntimacaoDto.get(seqPessoa);
            if (mapaSeqObjetoIncidenteParteIntimacaoDto == null) {
                mapaSeqObjetoIncidenteParteIntimacaoDto = new HashMap<Long, ProcessoIntimacaoDto>();
                mapaSeqPessoaSeqObjetoIncidenteParteIntimacaoDto.put(seqPessoa, mapaSeqObjetoIncidenteParteIntimacaoDto);
            }
            ProcessoIntimacaoDto processoIntimacaoDto = mapaSeqObjetoIncidenteParteIntimacaoDto.get(seqObjetoIncidente);
            if (processoIntimacaoDto == null) {
                processoIntimacaoDto = new ProcessoIntimacaoDto();
                parteIntimacaoDto.getProcessos().add(processoIntimacaoDto);
                processoIntimacaoDto.setSeqObjetoIncidente(seqObjetoIncidente);
                processoIntimacaoDto.setSiglaClasseProcesso(siglaClasseProcesso);
                processoIntimacaoDto.setNumeroProcesso(numeroProcesso);
                processoIntimacaoDto.setTipoMeioProcesso(tipoMeioProcesso);
                processoIntimacaoDto.setDescricaoClasseProcessual(descricaoClasseProcessual);
                processoIntimacaoDto.setCodigoSetor(codigoSetor);
                processoIntimacaoDto.setDescricaoSetor(descricaoSetor);
                processoIntimacaoDto.setPecas(new ArrayList<PecaIntimacaoDto>());
                mapaSeqObjetoIncidenteParteIntimacaoDto.put(seqObjetoIncidente, processoIntimacaoDto);
            }
            PecaIntimacaoDto pecaIntimacaoDto = new PecaIntimacaoDto();
            pecaIntimacaoDto.setSeqPecaProcessoEletronico(seqPecaProcessoEletronico);
            pecaIntimacaoDto.setCodigoMateria(codigoMateria);
            pecaIntimacaoDto.setNumeroMateria(numeroMateria);
            pecaIntimacaoDto.setSeqDocumento(seqDocumento);
            pecaIntimacaoDto.setSeqDocumentoAcordao(seqDocumentoAcordao);
            pecaIntimacaoDto.setIdTipoPecaProcessual(idTipoPecaProcessual);
            pecaIntimacaoDto.setDescricaoTipoPecaProcessual(descricaoTipoPecaProcessual);
            pecaIntimacaoDto.setIntimado(intimado);
            processoIntimacaoDto.getPecas().add(pecaIntimacaoDto);
            
            return parteIntimacaoDto;
        }
    }
    
	@Override
	public List<ParteProcessoIntimacaoDto> listarPartes(Date dataPublicacao,
			TipoMeioIntimacaoEnum tipoMeioComunicacaoEnum) throws DaoException {
		Session session = retrieveSession();
		SQLQuery query = session.createSQLQuery(getQueryListaPartes(tipoMeioComunicacaoEnum));
		query.setString("dataPublicacao",new SimpleDateFormat("dd/MM/yyyy").format(dataPublicacao));
		query.setResultTransformer(new ParteProcessoResultTransformer());
		return query.list();
	}
	
	
	private class ClasseProcessoResultTransformer extends ParteProcessoResultTransformerAbstract {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public List transformList(List arg0) {
			return arg0;
		}

		@Override
		public Object transformTuple(Object[] rowData, String[] aliases) {
			return getString(rowData[0]);
		}
	}

	private class UFPessoaResultTransformer extends ParteProcessoResultTransformerAbstract {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public List transformList(List arg0) {
			return arg0;
		}

		@Override
		public Object transformTuple(Object[] rowData, String[] aliases) {
			return getString(rowData[0]);
		}
	}

	private class SetorResultTransformer extends ParteProcessoResultTransformerAbstract {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public List transformList(List arg0) {
			return arg0;
		}

		@Override
		public Object transformTuple(Object[] rowData, String[] aliases) {

			Setor setor = new Setor();

			String codigoDescricaoSetor = getString(rowData[20]);
			if (codigoDescricaoSetor != null
					&& !codigoDescricaoSetor.trim().isEmpty()) {
				String[] arrayCodigoDescricaoSetor = codigoDescricaoSetor
						.split("-");
				String codigoSetor = arrayCodigoDescricaoSetor[0];
				String descricaoSetor = arrayCodigoDescricaoSetor[1];
				setor.setId(getLong(codigoSetor));
				setor.setNome(descricaoSetor);
			}

			return setor;
		}
	}

	private class ParteProcessoResultTransformer extends ParteProcessoResultTransformerAbstract {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public List transformList(List arg0) {
			return arg0;
		}

		@Override
		public Object transformTuple(Object[] rowData, String[] aliases) {

			ParteProcessoIntimacaoDto parteProcessoDto = new ParteProcessoIntimacaoDto();

			parteProcessoDto.setNomeParte(getString(rowData[0]));
			parteProcessoDto.setNumeroDJ(getLong(rowData[1]));
			parteProcessoDto.setSeqPessoa(getLong(rowData[2]));
			parteProcessoDto.setTipoMeioIntimacao(getString(rowData[3]));
			parteProcessoDto.setUsuarioExterno(getString(rowData[4]));
			parteProcessoDto.setSigUsuario(getString(rowData[5]));
			parteProcessoDto.setSiglaClasseProcesso(getString(rowData[6]));
			parteProcessoDto.setNumeroProcesso(getLong(rowData[7]));
			parteProcessoDto.setTipoMeioProcesso(getString(rowData[8]));
			parteProcessoDto.setSeqObjetoIncidente(getLong(rowData[9]));
			parteProcessoDto.setCodigoMateria(getLong(rowData[10]));
			parteProcessoDto.setNumeroMateria(getLong(rowData[11]));
			parteProcessoDto.setSeqPecaProcessoEletronico(getLong(rowData[12]));
			parteProcessoDto.setSeqDocumento(getLong(rowData[13]));
			parteProcessoDto.setDataDivulgacao(getDate(rowData[14]));
			parteProcessoDto.setDescricaoTipoPecaProcessual(getString(rowData[15]));
			parteProcessoDto.setModeloComunicacaoEnum(getString(rowData[16]).equals("M")?ModeloComunicacaoEnum.MANDADO:ModeloComunicacaoEnum.CARTA);
			parteProcessoDto.setDescricaoClasseProcessual(getString(rowData[17]));

			String tupla = getString(rowData[18]);

			parteProcessoDto.setCodigoSetor(Long.parseLong(tupla.split("-")[0]));
			parteProcessoDto.setDescricaoSetor(tupla.split("-")[1]);

			if (parteProcessoDto.getTipoMeioIntimacao().equalsIgnoreCase("E") && (parteProcessoDto.getUsuarioExterno() != null && parteProcessoDto.getUsuarioExterno().equalsIgnoreCase("S"))) {
				parteProcessoDto.setDescricaoMeioIntimacao("Eletrônica");
			} else {
				parteProcessoDto.setDescricaoMeioIntimacao("Física");
			}

			return parteProcessoDto;
		}
	}
	
}
