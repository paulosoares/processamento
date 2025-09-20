package br.gov.stf.estf.assinatura.relatorio.dataaccess.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.assinatura.relatorio.RelatorioAcordaoPublicado;
import br.gov.stf.estf.assinatura.relatorio.RelatorioAutosEmprestadosAdvogados;
import br.gov.stf.estf.assinatura.relatorio.RelatorioAutosEmprestadosOrgaosExternos;
import br.gov.stf.estf.assinatura.relatorio.RelatorioGuiaDeslocamentoPeticao;
import br.gov.stf.estf.assinatura.relatorio.RelatorioGuiaDeslocamentoProcesso;
import br.gov.stf.estf.assinatura.relatorio.RelatorioProcessoInteresse;
import br.gov.stf.estf.assinatura.relatorio.dataaccess.ProcessamentoRelatorioDao;
import br.gov.stf.estf.assinatura.relatorio.dataaccess.RelatorioAutosEmprestadosAdvogadosMapper;
import br.gov.stf.estf.assinatura.relatorio.dataaccess.RelatorioAutosEmprestadosOrgaosExternosMapper;
import br.gov.stf.estf.assinatura.relatorio.dataaccess.RelatorioGuiaDeslocamentoPeticaoMapper;
import br.gov.stf.estf.assinatura.relatorio.dataaccess.RelatorioGuiaDeslocamentoProcessoMapper;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.model.entity.BaseEntity;

@Repository
public class ProcessamentoRelatorioDaoLocal extends GenericHibernateDao implements ProcessamentoRelatorioDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public ProcessamentoRelatorioDaoLocal() {
		super(BaseEntity.class);
	}

	// --- autos emprestados --- 
	
	@Override
	// método para recuperação de autos emprestados a advogados com todos os parâmetros
	public List<RelatorioAutosEmprestadosAdvogados> consultarRelatorioAutosEmprestadosAdvogados(String siglaClasseProcesso,
			String dataInicial, String dataFinal) throws DaoException {
		boolean bFiltrarClasse = false;
		boolean bFiltrarPeriodo = false;
		if ((siglaClasseProcesso == null) || (siglaClasseProcesso.isEmpty())) {
			bFiltrarClasse = false;
		} else {
			bFiltrarClasse = true;
		}
		if ((dataInicial == null || dataFinal == null)) {
			bFiltrarPeriodo = false;
		} else {
			bFiltrarPeriodo = true;
		}
		Session session = retrieveSession();
		SQLQuery query = session.createSQLQuery(getQueryRelatorioAutosEmprestadosAdvogados(bFiltrarPeriodo, bFiltrarClasse));
		if (bFiltrarClasse) {
			query.setString("siglaClasseProcesso", siglaClasseProcesso);
		}
		if (bFiltrarPeriodo) {
			query.setString("dataInicial", dataInicial);
			query.setString("dataFinal", dataFinal);
		}
		query.setResultTransformer(new AutosEmprestadosAdvogadosResultsetTransformer());
		return query.list();
	}
	
	
	@Override
	// método para recuperação de autos emprestados a setores externos com todos os parâmetros
	public List<RelatorioAutosEmprestadosOrgaosExternos> consultarRelatorioAutosEmprestadosOrgaosExternos(String siglaClasseProcesso,
			String dataInicial, String dataFinal, Long codigoOrgaoExterno) throws DaoException {
		boolean bFiltrarClasse = false;
		boolean bFiltrarPeriodo = false;
		boolean bFiltrarOrgao = false;
		if ((siglaClasseProcesso == null) || (siglaClasseProcesso.isEmpty())) {
			bFiltrarClasse = false;
		} else {
			bFiltrarClasse = true;
		}
		if ((dataInicial == null || dataFinal == null)) {
			bFiltrarPeriodo = false;
		} else {
			bFiltrarPeriodo = true;
		}
		if (codigoOrgaoExterno == null) {
			bFiltrarOrgao = false;
		} else {
			bFiltrarOrgao = true;
		}
		Session session = retrieveSession();
		SQLQuery query = session.createSQLQuery(getQueryRelatorioAutosEmprestadosOrgaosExternos(bFiltrarOrgao, bFiltrarPeriodo, bFiltrarClasse));
		if (bFiltrarClasse) {
			query.setString("siglaClasseProcesso", siglaClasseProcesso);
		}
		if (bFiltrarPeriodo) {
			query.setString("dataInicial", dataInicial);
			query.setString("dataFinal", dataFinal);
		}
		if (bFiltrarOrgao) {
			query.setLong("codigoOrgaoExterno", codigoOrgaoExterno);
		}
		query.setResultTransformer(new AutosEmprestadosOrgaosExternosResultsetTransformer());
		return query.list();
	}
	
	//---- guias ----
	
	@Override
	public List<RelatorioGuiaDeslocamentoProcesso> consultarRelatorioVariasGuiasDeslocamentoProcesso(List<Guia> guias) throws DaoException {
		Session session = retrieveSession();
		SQLQuery query = session.createSQLQuery(getQueryRelatorioVariasGuiasDeslocamentoProcesso(guias));
		query.setResultTransformer(new GuiaDeslocamentoProcessoResultsetTransformer());
		return query.list();
	}
	@Override
	public List<RelatorioGuiaDeslocamentoProcesso> consultarRelatorioGuiaDeslocamentoProcesso(Long numeroDaGuia,
			Short anoDaGuia, Long codigoOrgaoOrigem) throws DaoException {
		Session session = retrieveSession();
		SQLQuery query = session.createSQLQuery(getQueryRelatorioGuiaDeslocamentoProcesso());
		query.setShort("anoDaGuia", anoDaGuia);
		query.setLong("numeroDaGuia", numeroDaGuia);
		query.setLong("codigoOrgaoOrigem", codigoOrgaoOrigem);
		query.setResultTransformer(new GuiaDeslocamentoProcessoResultsetTransformer());
		return query.list();
	}
	
	@Override
	public List<RelatorioGuiaDeslocamentoProcesso> consultarRelatorioGuiaRetiradaAutosProcesso(Long numeroDaGuia,
			Short anoDaGuia, Long codigoOrgaoOrigem) throws DaoException {
		Session session = retrieveSession();
		SQLQuery query = session.createSQLQuery(getQueryRelatorioGuiaRetiradaAutosProcesso());
		query.setShort("anoDaGuia", anoDaGuia);
		query.setLong("numeroDaGuia", numeroDaGuia);
		query.setLong("codigoOrgaoOrigem", codigoOrgaoOrigem);
		query.setResultTransformer(new GuiaDeslocamentoProcessoResultsetTransformer());
		return query.list();
	}
	
	@Override
	public List<RelatorioGuiaDeslocamentoProcesso> consultarRelatorioGuiaAntigaRetiradaAutosProcesso(Long numeroDaGuia,
			Short anoDaGuia, Long codigoOrgaoOrigem) throws DaoException {
		Session session = retrieveSession();
		SQLQuery query = session.createSQLQuery(getQueryRelatorioGuiaAntigaRetiradaAutosProcesso());
		query.setShort("anoDaGuia", anoDaGuia);
		query.setLong("numeroDaGuia", numeroDaGuia);
		query.setLong("codigoOrgaoOrigem", codigoOrgaoOrigem);
		query.setResultTransformer(new GuiaDeslocamentoProcessoResultsetTransformer());
		return query.list();
	}
	
	@Override
	public List<RelatorioGuiaDeslocamentoProcesso> consultarRelatorioGuiaDevolucaoAutosProcesso(List<Guia> guias) throws DaoException {
		Session session = retrieveSession();
		SQLQuery query = session.createSQLQuery(getQueryRelatorioGuiaDevolucaoAutosProcesso(guias));
//		query.setShort("anoDaGuia", anoDaGuia);
//		query.setLong("numeroDaGuia", numeroDaGuia);
//		query.setLong("codigoOrgaoOrigem", codigoOrgaoOrigem);
		query.setResultTransformer(new GuiaDeslocamentoProcessoResultsetTransformer());
		return query.list();
	}
	
	@Override
	public List<RelatorioGuiaDeslocamentoProcesso> consultarRelatorioGuiaAntigaDevolucaoAutosProcesso(List<Guia> guias) throws DaoException {
		Session session = retrieveSession();
		SQLQuery query = session.createSQLQuery(getQueryRelatorioGuiaAntigaDevolucaoAutosProcesso(guias));
//		query.setShort("anoDaGuia", anoDaGuia);
//		query.setLong("numeroDaGuia", numeroDaGuia);
//		query.setLong("codigoOrgaoOrigem", codigoOrgaoOrigem);
		query.setResultTransformer(new GuiaDeslocamentoProcessoResultsetTransformer());
		return query.list();
	}

	/* (non-Javadoc)
	 * @see br.gov.stf.estf.assinatura.relatorio.dataaccess.ProcessamentoRelatorioDao#consultarRelatorioGuiaDeslocamentoPeticao(java.lang.Long, java.lang.Short, java.lang.Long)
	 */
	@Override
	public List<RelatorioGuiaDeslocamentoPeticao> consultarRelatorioGuiaDeslocamentoPeticao(List<Guia> guias) throws DaoException {
		Session session = retrieveSession();
		SQLQuery query = session.createSQLQuery(getQueryRelatorioGuiaDeslocamentoPeticao(guias));
//		query.setShort("anoDaGuia", anoDaGuia);
//		query.setLong("numeroDaGuia", numeroDaGuia);
//		query.setLong("codigoOrgaoOrigem", codigoOrgaoOrigem);
		query.setResultTransformer(new GuiaDeslocamentoPeticaoResultsetTransformer());
		return query.list();
		
	}
	
	// Acórdãos publicados e atualmente recebido na seção de publicação de acórdãos
	@Override
	public List<RelatorioAcordaoPublicado> consultarRelatorioAcordaoPublicado(String dataPublicacao, String codigoSetorPubAcordao, String deslocadoParaAcordao) throws DaoException {
		Session session = retrieveSession();
		SQLQuery query = session.createSQLQuery(getQueryRelatorioAcordaosPublicados(codigoSetorPubAcordao, deslocadoParaAcordao));
		query.setString("dataPublicacao", dataPublicacao);
		query.setResultTransformer(new AcordaosPublicadosResultsetTransformer());
		return query.list();
	}
	
	// Processos de interesse com andamentos dos últimos sete dias (movimentada)
	@Override
	public List<RelatorioProcessoInteresse> consultarRelatorioProcessoInteresse(Long seqJurisdicionado) throws DaoException {
		Session session = retrieveSession();
		SQLQuery query = session.createSQLQuery(getQueryRelatorioProcessoInteresse());
		query.setLong("seqJurisdicionado", seqJurisdicionado);
		query.setResultTransformer(new ProcessoInteresseResultsetTransformer());
		return query.list();
	}


	private String getQueryRelatorioGuiaDeslocamentoProcesso() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT g.num_guia as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.NUMERO_GUIA);
		query.append(", g.ano_guia as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.ANO_GUIA);
		query.append(", g.cod_orgao_origem as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.CODIGO_ORGAO_ORIGEM);

		query.append(", CASE g.tip_orgao_origem");
		query.append("    WHEN '1' THEN advorigem.NOM_JURISDICIONADO");
		query.append("    WHEN '2' THEN sorigem.dsc_setor");
		query.append("	  WHEN '3' THEN orgorigem.dsc_origem");
		query.append("	 END as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.DESCRICAO_ORGAO_ORIGEM);

		query.append(", g.cod_orgao_destino as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.CODIGO_ORGAO_DESTINO);
		
		query.append(", CASE g.tip_orgao_destino");
		query.append("    WHEN '1' THEN advdestino.NOM_JURISDICIONADO");
		query.append("    WHEN '2' THEN sdestino.dsc_setor");
		query.append("	  WHEN '3' THEN orgdestino.dsc_origem");
		query.append("	 END as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.DESCRICAO_ORGAO_DESTINO);

		
		// CAMPO ENDEREÇO (LOGRADOURO)
		query.append(",  CASE g.tip_orgao_destino");
		query.append("    WHEN '3' THEN decode(ENDERECO.DSC_LOGRADOURO, null, ' ', ENDERECO.DSC_LOGRADOURO)"); 
		query.append("    ELSE NULL "); 
		query.append("   END as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.ENDERECO_ORGAO_DESTINO);
	       
	    // CAMPO NÚMERO DA LOCALIZAÇÃO
		query.append(",  CASE g.tip_orgao_destino");
		query.append("    WHEN '3' THEN decode(ENDERECO.NUM_LOCALIZACAO, null, ' ', 'N° ' || ENDERECO.NUM_LOCALIZACAO)");
		query.append("    ELSE NULL "); 
		query.append("   END as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.NUM_LOCALIZACAO);
	       
	    // CAMPO BAIRRO
		query.append(",  CASE g.tip_orgao_destino");
		query.append("	  WHEN '3' THEN decode(ENDERECO.NOM_BAIRRO, null, ' ', ENDERECO.NOM_BAIRRO)");
		query.append("    ELSE NULL "); 
		query.append("	END as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.BAIRRO);
	       
	    // CAMPO COMPLEMENTO        
		query.append(",	CASE g.tip_orgao_destino");
		query.append("	  WHEN '3' THEN decode(ENDERECO.DSC_COMPLEMENTO,null, ' ', ENDERECO.DSC_COMPLEMENTO)");
		query.append("    ELSE NULL "); 
		query.append("	END as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.COMPLEMENTO);

	    // CAMPO CIDADE (MUNICIPIO)        
		query.append(",	CASE g.tip_orgao_destino");
		query.append("    WHEN '3' THEN decode(ENDERECO.NOM_MUNICIPIO,null, ' ', ENDERECO.NOM_MUNICIPIO)");
		query.append("    ELSE NULL "); 
		query.append("  END as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.MUNICIPIO_ORGAO_DESTINO);

	    // CAMPO UF
		query.append(",	CASE g.tip_orgao_destino");
		query.append("	  WHEN '3' THEN decode(ENDERECO.SIG_UF,null, ' ', ENDERECO.SIG_UF)");
		query.append("    ELSE NULL "); 
		query.append("	END as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.UF_ORGAO_DESTINO);

	     // CAMPO CEP        
		query.append(",	CASE g.tip_orgao_destino");
		query.append("	  WHEN '3' THEN decode(ENDERECO.COD_CEP,null, ' ', ENDERECO.COD_CEP)");
		query.append("    ELSE NULL "); 
		query.append("	END as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.CEP_ORGAO_DESTINO);
		
		query.append(", pr.sig_classe_proces as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.SIGLA_CLASSE_PROCESSO);
		query.append(", pr.num_processo as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.NUMERO_PROCESSO);
		
		query.append(", decode(pr.qtd_volumes,null,0,pr.qtd_volumes) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.QUANTIDADE_VOLUMES);
		query.append(", decode(pr.qtd_apensos,null,0,pr.qtd_apensos) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.QUANTIDADE_APENSOS);
		query.append(", decode(pr.qtd_juntada_linha,null,0,pr.qtd_juntada_linha) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.QUANTIDADE_JUNTADA_LINHA);
		query.append("		, (select count(*) from stf.processo_dependencia pd");
		query.append("                where pd.sig_classe_vinculador = pr.sig_classe_proces");
		query.append("                 and pd.num_processo_vinculador = pr.num_processo");
		query.append("                 and pd.DAT_FIM_DEPENDENCIA is null");
		query.append("                 and pd.cod_tipo_dependencia_processo in (5,9)) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.QUANTIDADE_VINCULO);

		query.append(", decode(g.dsc_observacao,null,'',g.dsc_observacao) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.OBSERVACAO);
		
		query.append(", g.dat_remessa as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.DATA_REMESSA);
		query.append(", dp.dat_recebimento as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.DATA_RECEBIMENTO);
		// RECUPERA 1 SE FOR UM VINCULADO OU 0 SE NÃO FOR
		query.append("    , (select count(*) from STF.PROCESSO_DEPENDENCIA dep ");
		query.append("		       where PR.NUM_PROCESSO = DEP.NUM_PROCESSO ");
	    query.append("       and PR.SIG_CLASSE_PROCES = DEP.SIG_CLASSE_PROCES ");
	    query.append("		       and DEP.COD_TIPO_DEPENDENCIA_PROCESSO in (5,9) ");
	    query.append("		       and DEP.DAT_FIM_DEPENDENCIA is null) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.VINCULADO);
		// RECUPERA O TIPO MEIO DO PROCESSO
		query.append(", PR.tip_meio_processo as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.TIPO_MEIO);
		// RECUPERA O DESTINATARIO REFERENTE AO ENDEREÇO
		query.append(", destinatario.dsc_destinatario as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.DESTINATARIO);
		query.append(", destinatario.seq_destinatario_origem as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.CODIGO_DESTINATARIO);
				
		query.append(" FROM stf.guias g,");
		query.append(" stf.desloca_processos dp,");
		query.append(" judiciario.processo pr,");
		query.append(" stf.setores sorigem,");
		query.append(" stf.setores sdestino,");
		
		query.append(" judiciario.jurisdicionado advorigem,");
		query.append(" judiciario.jurisdicionado advdestino,");
		
		query.append(" judiciario.origem orgorigem,");
		query.append(" judiciario.origem orgdestino,");
		query.append(" judiciario.endereco_destinatario endereco, ");
		query.append(" judiciario.destinatario_origem destinatario ");
		
		query.append(" WHERE g.num_guia = dp.num_guia");
		query.append(" AND g.ano_guia = dp.ano_guia");
		query.append(" AND g.cod_orgao_origem = dp.cod_orgao_origem");
		query.append(" AND dp.num_processo = pr.num_processo");
		query.append(" AND dp.sig_classe_proces = pr.sig_classe_proces");

		query.append(" AND g.cod_orgao_origem = sorigem.cod_setor (+)");
		query.append(" AND g.cod_orgao_destino = sdestino.cod_setor (+)");
		
		query.append(" AND g.cod_orgao_origem = advorigem.seq_jurisdicionado (+)");
		query.append(" AND g.cod_orgao_destino = advdestino.seq_jurisdicionado (+)");
		
		query.append(" AND g.cod_orgao_origem = orgorigem.cod_origem (+)");
		query.append(" AND g.cod_orgao_destino = orgdestino.cod_origem (+)");
		
		query.append(" AND g.seq_endereco_destinatario = endereco.seq_endereco_destinatario (+)");
		query.append(" AND endereco.seq_destinatario_origem = destinatario.seq_destinatario_origem (+)");
		
		query.append(" AND g.ano_guia = :anoDaGuia");
		query.append(" AND g.num_guia = :numeroDaGuia");
		query.append(" AND g.cod_orgao_origem = :codigoOrgaoOrigem");
		query.append(" ORDER BY DP.NUM_SEQUENCIA");
		return query.toString();
	}

	private String getQueryRelatorioVariasGuiasDeslocamentoProcesso(List<Guia> guias) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT g.num_guia as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.NUMERO_GUIA);
		query.append(", g.ano_guia as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.ANO_GUIA);
		query.append(", g.cod_orgao_origem as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.CODIGO_ORGAO_ORIGEM);

		query.append(", CASE g.tip_orgao_origem");
		query.append("    WHEN '1' THEN advorigem.NOM_JURISDICIONADO");
		query.append("    WHEN '2' THEN sorigem.dsc_setor");
		query.append("	  WHEN '3' THEN orgorigem.dsc_origem");
		query.append("	 END as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.DESCRICAO_ORGAO_ORIGEM);

		query.append(", g.cod_orgao_destino as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.CODIGO_ORGAO_DESTINO);
		
		query.append(", CASE g.tip_orgao_destino");
		query.append("    WHEN '1' THEN advdestino.NOM_JURISDICIONADO");
		query.append("    WHEN '2' THEN sdestino.dsc_setor");
		query.append("	  WHEN '3' THEN orgdestino.dsc_origem");
		query.append("	 END as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.DESCRICAO_ORGAO_DESTINO);

		
		// CAMPO ENDEREÇO (LOGRADOURO)
		query.append(",  CASE g.tip_orgao_destino");
		query.append("    WHEN '3' THEN decode(ENDERECO.DSC_LOGRADOURO, null, ' ', ENDERECO.DSC_LOGRADOURO)"); 
		query.append("    ELSE NULL "); 
		query.append("   END as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.ENDERECO_ORGAO_DESTINO);
	       
	    // CAMPO NÚMERO DA LOCALIZAÇÃO
		query.append(",  CASE g.tip_orgao_destino");
		query.append("    WHEN '3' THEN decode(ENDERECO.NUM_LOCALIZACAO, null, ' ', 'N° ' || ENDERECO.NUM_LOCALIZACAO)");
		query.append("    ELSE NULL "); 
		query.append("   END as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.NUM_LOCALIZACAO);
	       
	    // CAMPO BAIRRO
		query.append(",  CASE g.tip_orgao_destino");
		query.append("	  WHEN '3' THEN decode(ENDERECO.NOM_BAIRRO, null, ' ', ENDERECO.NOM_BAIRRO)");
		query.append("    ELSE NULL "); 
		query.append("	END as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.BAIRRO);
	       
	    // CAMPO COMPLEMENTO        
		query.append(",	CASE g.tip_orgao_destino");
		query.append("	  WHEN '3' THEN decode(ENDERECO.DSC_COMPLEMENTO,null, ' ', ENDERECO.DSC_COMPLEMENTO)");
		query.append("    ELSE NULL "); 
		query.append("	END as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.COMPLEMENTO);

	    // CAMPO CIDADE (MUNICIPIO)        
		query.append(",	CASE g.tip_orgao_destino");
		query.append("    WHEN '3' THEN decode(ENDERECO.NOM_MUNICIPIO,null, ' ', ENDERECO.NOM_MUNICIPIO)");
		query.append("    ELSE NULL "); 
		query.append("  END as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.MUNICIPIO_ORGAO_DESTINO);

	    // CAMPO UF
		query.append(",	CASE g.tip_orgao_destino");
		query.append("	  WHEN '3' THEN decode(ENDERECO.SIG_UF,null, ' ', ENDERECO.SIG_UF)");
		query.append("    ELSE NULL "); 
		query.append("	END as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.UF_ORGAO_DESTINO);

	     // CAMPO CEP        
		query.append(",	CASE g.tip_orgao_destino");
		query.append("	  WHEN '3' THEN decode(ENDERECO.COD_CEP,null, ' ', ENDERECO.COD_CEP)");
		query.append("    ELSE NULL "); 
		query.append("	END as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.CEP_ORGAO_DESTINO);
		
		query.append(", pr.sig_classe_proces as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.SIGLA_CLASSE_PROCESSO);
		query.append(", pr.num_processo as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.NUMERO_PROCESSO);
		
		query.append(", decode(pr.qtd_volumes,null,0,pr.qtd_volumes) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.QUANTIDADE_VOLUMES);
		query.append(", decode(pr.qtd_apensos,null,0,pr.qtd_apensos) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.QUANTIDADE_APENSOS);
		query.append(", decode(pr.qtd_juntada_linha,null,0,pr.qtd_juntada_linha) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.QUANTIDADE_JUNTADA_LINHA);
		query.append("		, (select count(*) from stf.processo_dependencia pd");
		query.append("                where pd.sig_classe_vinculador = pr.sig_classe_proces");
		query.append("                 and pd.num_processo_vinculador = pr.num_processo");
		query.append("                 and pd.DAT_FIM_DEPENDENCIA is null");
		query.append("                 and pd.cod_tipo_dependencia_processo in (5,9)) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.QUANTIDADE_VINCULO);

		query.append(", decode(g.dsc_observacao,null,'',g.dsc_observacao) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.OBSERVACAO);
		
		query.append(", g.dat_remessa as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.DATA_REMESSA);
		query.append(", dp.dat_recebimento as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.DATA_RECEBIMENTO);
		// RECUPERA 1 SE FOR UM VINCULADO OU 0 SE NÃO FOR
		query.append("    , (select count(*) from STF.PROCESSO_DEPENDENCIA dep ");
		query.append("		       where PR.NUM_PROCESSO = DEP.NUM_PROCESSO ");
	    query.append("       and PR.SIG_CLASSE_PROCES = DEP.SIG_CLASSE_PROCES ");
	    query.append("		       and DEP.COD_TIPO_DEPENDENCIA_PROCESSO in (5,9) ");
	    query.append("		       and DEP.DAT_FIM_DEPENDENCIA is null) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.VINCULADO);
		// RECUPERA O TIPO MEIO DO PROCESSO
		query.append(", PR.tip_meio_processo as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.TIPO_MEIO);
		// RECUPERA O DESTINATARIO REFERENTE AO ENDEREÇO
		query.append(", destinatario.dsc_destinatario as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.DESTINATARIO);
		query.append(", destinatario.seq_destinatario_origem as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.CODIGO_DESTINATARIO);
				
		query.append(" FROM stf.guias g,");
		query.append(" stf.desloca_processos dp,");
		query.append(" judiciario.processo pr,");
		query.append(" stf.setores sorigem,");
		query.append(" stf.setores sdestino,");
		
		query.append(" judiciario.jurisdicionado advorigem,");
		query.append(" judiciario.jurisdicionado advdestino,");
		
		query.append(" judiciario.origem orgorigem,");
		query.append(" judiciario.origem orgdestino,");
		query.append(" judiciario.endereco_destinatario endereco, ");
		query.append(" judiciario.destinatario_origem destinatario ");
		
		query.append(" WHERE g.num_guia = dp.num_guia");
		query.append(" AND g.ano_guia = dp.ano_guia");
		query.append(" AND g.cod_orgao_origem = dp.cod_orgao_origem");
		query.append(" AND dp.num_processo = pr.num_processo");
		query.append(" AND dp.sig_classe_proces = pr.sig_classe_proces");

		query.append(" AND g.cod_orgao_origem = sorigem.cod_setor (+)");
		query.append(" AND g.cod_orgao_destino = sdestino.cod_setor (+)");
		
		query.append(" AND g.cod_orgao_origem = advorigem.seq_jurisdicionado (+)");
		query.append(" AND g.cod_orgao_destino = advdestino.seq_jurisdicionado (+)");
		
		query.append(" AND g.cod_orgao_origem = orgorigem.cod_origem (+)");
		query.append(" AND g.cod_orgao_destino = orgdestino.cod_origem (+)");
		
		query.append(" AND g.seq_endereco_destinatario = endereco.seq_endereco_destinatario (+)");
		query.append(" AND endereco.seq_destinatario_origem = destinatario.seq_destinatario_origem (+)");
		
		query.append(getINGuia(guias));
		query.append(" ORDER BY g.num_guia, g.ano_guia, g.cod_orgao_origem, DP.NUM_SEQUENCIA");
		return query.toString();
	}
	
	// retorna cláusula IN do where no select de guias: filtro para múltiplas guias (num_guia, ano_guia e cod_orgao_origem)
	private String getINGuia(List<Guia> guias) {
		
		/** Gera o código para a cláusula IN do select quando se quer várias guias no mesmo relatório.
		 * Exemplo:
		 * and (g.num_guia, g.ano_guia, g.cod_orgao_origem) IN
           (SELECT '1762', '2010', '600000621' FROM DUAL
            UNION
            SELECT '9555', '2010', '600000643' FROM DUAL
            UNION
            SELECT '1102269', '2013', '7' FROM DUAL)

		 */
		String whereGuia = " AND (g.num_guia, g.ano_guia, g.cod_orgao_origem) IN (";
		StringBuilder ins = new StringBuilder("");
		for (Guia guia: guias){
			if (ins.length()>0) {
				ins.append(" UNION ");
			}
			ins.append( " SELECT '" + guia.getId().getNumeroGuia().toString() + "','" + guia.getId().getAnoGuia().toString() + "','" +
					guia.getId().getCodigoOrgaoOrigem().toString() + "' FROM DUAL ");
		}
	    whereGuia = whereGuia + ins + ")";
		return whereGuia;
	}
	
	// query para relatório de guias de retirada de autos
	private String getQueryRelatorioGuiaRetiradaAutosProcesso() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT G.NUM_GUIA AS ");
        query.append(RelatorioGuiaDeslocamentoProcessoMapper.NUMERO_GUIA);
		query.append(", G.ANO_GUIA AS "); 
        query.append(RelatorioGuiaDeslocamentoProcessoMapper.ANO_GUIA);
		query.append(", G.COD_ORGAO_ORIGEM AS "); 
        query.append(RelatorioGuiaDeslocamentoProcessoMapper.CODIGO_ORGAO_ORIGEM);
		query.append(", SETOR_ORIGEM.DSC_SETOR AS ");
        query.append(RelatorioGuiaDeslocamentoProcessoMapper.DESCRICAO_ORGAO_ORIGEM);
		query.append(", G.COD_ORGAO_DESTINO AS ");
        query.append(RelatorioGuiaDeslocamentoProcessoMapper.CODIGO_ORGAO_DESTINO);
		//query.append(", ADV_DESTINO.NOM_JURISDICIONADO AS ");
		query.append(", DECODE(JURIS_GRUPO.SEQ_JURISDICIONADO, NULL, ADV_DESTINO.NOM_JURISDICIONADO, JURIS_GRUPO.NOM_JURISDICIONADO || ' (' || ADV_DESTINO.NOM_JURISDICIONADO || ')') AS ");
        query.append(RelatorioGuiaDeslocamentoProcessoMapper.DESCRICAO_ORGAO_DESTINO);
		
        query.append(", NULL AS ");
        query.append(RelatorioGuiaDeslocamentoProcessoMapper.ENDERECO_ORGAO_DESTINO);
		query.append(", NULL AS ");
        query.append(RelatorioGuiaDeslocamentoProcessoMapper.NUM_LOCALIZACAO);
		query.append(", NULL AS ");
        query.append(RelatorioGuiaDeslocamentoProcessoMapper.BAIRRO);
		query.append(", NULL AS ");
        query.append(RelatorioGuiaDeslocamentoProcessoMapper.COMPLEMENTO);
		query.append(", NULL AS ");
        query.append(RelatorioGuiaDeslocamentoProcessoMapper.MUNICIPIO_ORGAO_DESTINO);
		query.append(", NULL AS ");
        query.append(RelatorioGuiaDeslocamentoProcessoMapper.UF_ORGAO_DESTINO);
		query.append(", NULL AS ");
        query.append(RelatorioGuiaDeslocamentoProcessoMapper.CEP_ORGAO_DESTINO);
		query.append(", PR.SIG_CLASSE_PROCES AS ");
        query.append(RelatorioGuiaDeslocamentoProcessoMapper.SIGLA_CLASSE_PROCESSO);
		query.append(", PR.NUM_PROCESSO AS ");
        query.append(RelatorioGuiaDeslocamentoProcessoMapper.NUMERO_PROCESSO);
		query.append(", decode(pr.qtd_volumes,null,0,pr.qtd_volumes) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.QUANTIDADE_VOLUMES);
		query.append(", decode(pr.qtd_apensos,null,0,pr.qtd_apensos) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.QUANTIDADE_APENSOS);
		query.append(", decode(pr.qtd_juntada_linha,null,0,pr.qtd_juntada_linha) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.QUANTIDADE_JUNTADA_LINHA);
		query.append("		, (select count(*) from stf.processo_dependencia pd");
		query.append("                where pd.sig_classe_vinculador = pr.sig_classe_proces");
		query.append("                 and pd.num_processo_vinculador = pr.num_processo");
		query.append("                 and pd.DAT_FIM_DEPENDENCIA is null");
		query.append("                 and pd.cod_tipo_dependencia_processo in (5,9)) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.QUANTIDADE_VINCULO);
		
		query.append(", decode(g.dsc_observacao,null,'',g.dsc_observacao) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.OBSERVACAO);
		
		query.append(", g.dat_remessa as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.DATA_REMESSA);
		query.append(", dp.dat_recebimento as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.DATA_RECEBIMENTO);
		// RECUPERA 1 SE FOR UM VINCULADO OU 0 SE NÃO FOR
		query.append("    , (select count(*) from STF.PROCESSO_DEPENDENCIA dep ");
		query.append("		       where PR.NUM_PROCESSO = DEP.NUM_PROCESSO ");
	    query.append("       and PR.SIG_CLASSE_PROCES = DEP.SIG_CLASSE_PROCES ");
	    query.append("		       and DEP.COD_TIPO_DEPENDENCIA_PROCESSO in (5,9) ");
	    query.append("		       and DEP.DAT_FIM_DEPENDENCIA is null) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.VINCULADO);
		// RECUPERA O TIPO MEIO DO PROCESSO
		query.append(", PR.tip_meio_processo as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.TIPO_MEIO);
		// O DESTINATARIO REFERENTE AO ENDEREÇO NESTE CASO É NULL
		query.append(", NULL as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.DESTINATARIO);
	       
		query.append("	FROM STF.GUIAS G, ");
		query.append(" STF.DESLOCA_PROCESSOS DP,");
		query.append(" JUDICIARIO.PROCESSO PR,");
		query.append(" JUDICIARIO.EMPRESTIMO_AUTOS_PROCESSO EMPRESTIMO_RETIRADA,");
		query.append(" JUDICIARIO.JURISDICIONADO ADV_DESTINO,");
		query.append(" STF.SETORES SETOR_ORIGEM,");
		query.append(" JUDICIARIO.PAPEL_JURISDICIONADO PAPEL_GRUPO,");
		query.append(" JUDICIARIO.JURISDICIONADO JURIS_GRUPO,");
		query.append(" JUDICIARIO.ASSOCIACAO_JURISDICIONADO ASSOCIACAO");
		query.append("	WHERE G.NUM_GUIA = DP.NUM_GUIA");
		query.append("	  AND G.ANO_GUIA = DP.ANO_GUIA");
		query.append("	  AND G.COD_ORGAO_ORIGEM = DP.COD_ORGAO_ORIGEM");
		query.append("	  AND DP.SEQ_OBJETO_INCIDENTE = PR.SEQ_OBJETO_INCIDENTE");
	  //-- ORIGEM DEVE SER UM SETOR DO STF
		query.append("	  AND DP.COD_ORGAO_ORIGEM = SETOR_ORIGEM.COD_SETOR (+)");
	  //-- DESTINO DEVE SER UM ADVOGADO
		query.append("	  AND DP.COD_ORGAO_DESTINO = ADV_DESTINO.SEQ_JURISDICIONADO (+)");
	  
		query.append("	  AND DP.SEQ_DESLOCA_PROCESSOS = EMPRESTIMO_RETIRADA.SEQ_DESLOCAMENTO_RETIRADA (+)");

		query.append("	  AND EMPRESTIMO_RETIRADA.SEQ_ASSOCIACAO_JURISDICIONADO = ASSOCIACAO.SEQ_ASSOCIACAO_JURISDICIONADO (+)");
		query.append("	  AND ASSOCIACAO.SEQ_JURISDICIONADO_GRUPO = PAPEL_GRUPO.SEQ_PAPEL_JURISDICIONADO (+)");
		query.append("	  AND PAPEL_GRUPO.SEQ_JURISDICIONADO = JURIS_GRUPO.SEQ_JURISDICIONADO (+)");
	  
		query.append("	  AND G.ANO_GUIA = :anoDaGuia");
		query.append("	  AND G.NUM_GUIA = :numeroDaGuia");
		query.append("	  AND G.COD_ORGAO_ORIGEM = :codigoOrgaoOrigem");
		query.append("	ORDER BY DP.NUM_SEQUENCIA");
		return query.toString();
	}

	// query para relatório de guias de devolução de autos
	private String getQueryRelatorioGuiaDevolucaoAutosProcesso(List<Guia> guias) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT G.NUM_GUIA AS " + RelatorioGuiaDeslocamentoProcessoMapper.NUMERO_GUIA);
		query.append(", G.ANO_GUIA AS " + RelatorioGuiaDeslocamentoProcessoMapper.ANO_GUIA);
		query.append(", G.COD_ORGAO_ORIGEM AS " + RelatorioGuiaDeslocamentoProcessoMapper.CODIGO_ORGAO_ORIGEM);
		query.append(", DECODE(JURIS_GRUPO.SEQ_JURISDICIONADO, NULL, ADV_ORIGEM.NOM_JURISDICIONADO, JURIS_GRUPO.NOM_JURISDICIONADO || ' (' || ADV_ORIGEM.NOM_JURISDICIONADO || ')') AS ");
        query.append(RelatorioGuiaDeslocamentoProcessoMapper.DESCRICAO_ORGAO_ORIGEM);
		query.append(", G.COD_ORGAO_DESTINO AS " + RelatorioGuiaDeslocamentoProcessoMapper.CODIGO_ORGAO_DESTINO);
		query.append(", SETOR_DESTINO.DSC_SETOR AS " + RelatorioGuiaDeslocamentoProcessoMapper.DESCRICAO_ORGAO_DESTINO);
		
        query.append(", NULL AS " + RelatorioGuiaDeslocamentoProcessoMapper.ENDERECO_ORGAO_DESTINO);
		query.append(", NULL AS " + RelatorioGuiaDeslocamentoProcessoMapper.NUM_LOCALIZACAO);
		query.append(", NULL AS " + RelatorioGuiaDeslocamentoProcessoMapper.BAIRRO);
		query.append(", NULL AS " + RelatorioGuiaDeslocamentoProcessoMapper.COMPLEMENTO);
		query.append(", NULL AS " + RelatorioGuiaDeslocamentoProcessoMapper.MUNICIPIO_ORGAO_DESTINO);
		query.append(", NULL AS " + RelatorioGuiaDeslocamentoProcessoMapper.UF_ORGAO_DESTINO);
		query.append(", NULL AS " + RelatorioGuiaDeslocamentoProcessoMapper.CEP_ORGAO_DESTINO);
		
        query.append(", PR.SIG_CLASSE_PROCES AS " + RelatorioGuiaDeslocamentoProcessoMapper.SIGLA_CLASSE_PROCESSO);
		query.append(", PR.NUM_PROCESSO AS " + RelatorioGuiaDeslocamentoProcessoMapper.NUMERO_PROCESSO);
		query.append(", decode(pr.qtd_volumes,null,0,pr.qtd_volumes) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.QUANTIDADE_VOLUMES);
		query.append(", decode(pr.qtd_apensos,null,0,pr.qtd_apensos) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.QUANTIDADE_APENSOS);
		query.append(", decode(pr.qtd_juntada_linha,null,0,pr.qtd_juntada_linha) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.QUANTIDADE_JUNTADA_LINHA);
		query.append("		, (select count(*) from stf.processo_dependencia pd");
		query.append("                where pd.sig_classe_vinculador = pr.sig_classe_proces");
		query.append("                 and pd.num_processo_vinculador = pr.num_processo");
		query.append("                 and pd.DAT_FIM_DEPENDENCIA is null");
		query.append("                 and pd.cod_tipo_dependencia_processo in (5,9)) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.QUANTIDADE_VINCULO);

		query.append(", decode(g.dsc_observacao,null,'',g.dsc_observacao) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.OBSERVACAO);

		query.append(", g.dat_remessa as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.DATA_REMESSA);
		query.append(", dp.dat_recebimento as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.DATA_RECEBIMENTO);
		// RECUPERA 1 SE FOR UM VINCULADO OU 0 SE NÃO FOR
		query.append("    , (select count(*) from STF.PROCESSO_DEPENDENCIA dep ");
		query.append("		       where PR.NUM_PROCESSO = DEP.NUM_PROCESSO ");
	    query.append("       and PR.SIG_CLASSE_PROCES = DEP.SIG_CLASSE_PROCES ");
	    query.append("		       and DEP.COD_TIPO_DEPENDENCIA_PROCESSO in (5,9) ");
	    query.append("		       and DEP.DAT_FIM_DEPENDENCIA is null) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.VINCULADO);
		// RECUPERA O TIPO MEIO DO PROCESSO
		query.append(", PR.tip_meio_processo as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.TIPO_MEIO);
		// O DESTINATARIO REFERENTE AO ENDEREÇO NESTE CASO É NULL
		query.append(", NULL as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.DESTINATARIO);
		
		query.append("	FROM STF.GUIAS G, ");
		query.append(" STF.DESLOCA_PROCESSOS DP,");
		query.append(" JUDICIARIO.PROCESSO PR,");
		query.append(" JUDICIARIO.EMPRESTIMO_AUTOS_PROCESSO EMPRESTIMO_DEVOLUCAO,");
		query.append(" JUDICIARIO.JURISDICIONADO ADV_ORIGEM,");
		query.append(" STF.SETORES SETOR_DESTINO,");
		query.append(" JUDICIARIO.PAPEL_JURISDICIONADO PAPEL_GRUPO,");
		query.append(" JUDICIARIO.JURISDICIONADO JURIS_GRUPO,");
		query.append(" JUDICIARIO.ASSOCIACAO_JURISDICIONADO ASSOCIACAO");
		query.append("	WHERE G.NUM_GUIA = DP.NUM_GUIA");
		query.append("	  AND G.ANO_GUIA = DP.ANO_GUIA");
		query.append("	  AND G.COD_ORGAO_ORIGEM = DP.COD_ORGAO_ORIGEM");
		query.append("	  AND DP.SEQ_OBJETO_INCIDENTE = PR.SEQ_OBJETO_INCIDENTE");
	  //-- ORIGEM DEVE SER UM JURISDICIONADO (ADVOGADO)
		query.append("	  AND DP.COD_ORGAO_ORIGEM = ADV_ORIGEM.SEQ_JURISDICIONADO");
	  //-- DESTINO DEVE SER UM SETOR DO STF
		query.append("	  AND DP.COD_ORGAO_DESTINO = SETOR_DESTINO.COD_SETOR");
		
		query.append("	  AND DP.SEQ_DESLOCA_PROCESSOS = EMPRESTIMO_DEVOLUCAO.SEQ_DESLOCAMENTO_DEVOLUCAO (+)");

		query.append("	  AND EMPRESTIMO_DEVOLUCAO.SEQ_ASSOCIACAO_JURISDICIONADO = ASSOCIACAO.SEQ_ASSOCIACAO_JURISDICIONADO (+)");
		query.append("	  AND ASSOCIACAO.SEQ_JURISDICIONADO_GRUPO = PAPEL_GRUPO.SEQ_PAPEL_JURISDICIONADO (+)");
		query.append("	  AND PAPEL_GRUPO.SEQ_JURISDICIONADO = JURIS_GRUPO.SEQ_JURISDICIONADO (+)");

		query.append(getINGuia(guias));
		query.append(" ORDER BY g.num_guia, g.ano_guia, g.cod_orgao_origem, DP.NUM_SEQUENCIA");
	  
		return query.toString();
	}
	
	// query para relatório de guia antiga para retirada de autos (carga realizada no MAP)
	private String getQueryRelatorioGuiaAntigaRetiradaAutosProcesso() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT G.NUM_GUIA AS ");
        query.append(RelatorioGuiaDeslocamentoProcessoMapper.NUMERO_GUIA);
		query.append(", G.ANO_GUIA AS "); 
        query.append(RelatorioGuiaDeslocamentoProcessoMapper.ANO_GUIA);
		query.append(", G.COD_ORGAO_ORIGEM AS "); 
        query.append(RelatorioGuiaDeslocamentoProcessoMapper.CODIGO_ORGAO_ORIGEM);
		query.append(", SETOR_ORIGEM.DSC_SETOR AS ");
        query.append(RelatorioGuiaDeslocamentoProcessoMapper.DESCRICAO_ORGAO_ORIGEM);
		query.append(", G.COD_ORGAO_DESTINO AS ");
        query.append(RelatorioGuiaDeslocamentoProcessoMapper.CODIGO_ORGAO_DESTINO);
		query.append(", ADV_DESTINO.NOM_ADV AS ");
        query.append(RelatorioGuiaDeslocamentoProcessoMapper.DESCRICAO_ORGAO_DESTINO);
		
        query.append(", NULL AS " + RelatorioGuiaDeslocamentoProcessoMapper.ENDERECO_ORGAO_DESTINO);
		query.append(", NULL AS " + RelatorioGuiaDeslocamentoProcessoMapper.NUM_LOCALIZACAO);
		query.append(", NULL AS " + RelatorioGuiaDeslocamentoProcessoMapper.BAIRRO);
		query.append(", NULL AS " + RelatorioGuiaDeslocamentoProcessoMapper.COMPLEMENTO);
		query.append(", NULL AS " + RelatorioGuiaDeslocamentoProcessoMapper.MUNICIPIO_ORGAO_DESTINO);
		query.append(", NULL AS " + RelatorioGuiaDeslocamentoProcessoMapper.UF_ORGAO_DESTINO);
		query.append(", NULL AS " + RelatorioGuiaDeslocamentoProcessoMapper.CEP_ORGAO_DESTINO);
		query.append(", PR.SIG_CLASSE_PROCES AS " + RelatorioGuiaDeslocamentoProcessoMapper.SIGLA_CLASSE_PROCESSO);
		query.append(", PR.NUM_PROCESSO AS ");
        query.append(RelatorioGuiaDeslocamentoProcessoMapper.NUMERO_PROCESSO);
		query.append(", decode(pr.qtd_volumes,null,0,pr.qtd_volumes) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.QUANTIDADE_VOLUMES);
		query.append(", decode(pr.qtd_apensos,null,0,pr.qtd_apensos) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.QUANTIDADE_APENSOS);
		query.append(", decode(pr.qtd_juntada_linha,null,0,pr.qtd_juntada_linha) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.QUANTIDADE_JUNTADA_LINHA);
		query.append("		, (select count(*) from stf.processo_dependencia pd");
		query.append("                where pd.sig_classe_vinculador = pr.sig_classe_proces");
		query.append("                 and pd.num_processo_vinculador = pr.num_processo");
		query.append("                 and pd.DAT_FIM_DEPENDENCIA is null");
		query.append("                 and pd.cod_tipo_dependencia_processo in (5,9)) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.QUANTIDADE_VINCULO);
		
		query.append(", decode(g.dsc_observacao,null,'',g.dsc_observacao) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.OBSERVACAO);
		
		query.append(", g.dat_remessa as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.DATA_REMESSA);
		query.append(", dp.dat_recebimento as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.DATA_RECEBIMENTO);
		// RECUPERA 1 SE FOR UM VINCULADO OU 0 SE NÃO FOR
		query.append("    , (select count(*) from STF.PROCESSO_DEPENDENCIA dep ");
		query.append("		       where PR.NUM_PROCESSO = DEP.NUM_PROCESSO ");
	    query.append("       and PR.SIG_CLASSE_PROCES = DEP.SIG_CLASSE_PROCES ");
	    query.append("		       and DEP.COD_TIPO_DEPENDENCIA_PROCESSO in (5,9) ");
	    query.append("		       and DEP.DAT_FIM_DEPENDENCIA is null) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.VINCULADO);
		// RECUPERA O TIPO MEIO DO PROCESSO
		query.append(", PR.tip_meio_processo as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.TIPO_MEIO);
		// O DESTINATARIO REFERENTE AO ENDEREÇO NESTE CASO É NULL
		query.append(", NULL as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.DESTINATARIO);
	       
		query.append("	FROM STF.GUIAS G, ");
		query.append(" STF.DESLOCA_PROCESSOS DP,");
		query.append(" JUDICIARIO.PROCESSO PR,");
		query.append(" STF.ADVOGADOS ADV_DESTINO,");
		query.append(" STF.SETORES SETOR_ORIGEM");
		query.append("	WHERE G.NUM_GUIA = DP.NUM_GUIA");
		query.append("	  AND G.ANO_GUIA = DP.ANO_GUIA");
		query.append("	  AND G.COD_ORGAO_ORIGEM = DP.COD_ORGAO_ORIGEM");
		query.append("	  AND DP.SEQ_OBJETO_INCIDENTE = PR.SEQ_OBJETO_INCIDENTE");
   	  	// ORIGEM DEVE SER UM SETOR DO STF
		query.append("	  AND DP.COD_ORGAO_ORIGEM = SETOR_ORIGEM.COD_SETOR (+)");
		// DESTINO DEVE SER UM ADVOGADO
		query.append("	  AND DP.COD_ORGAO_DESTINO = ADV_DESTINO.COD_ADV (+)");
	  
		query.append("	  AND G.ANO_GUIA = :anoDaGuia");
		query.append("	  AND G.NUM_GUIA = :numeroDaGuia");
		query.append("	  AND G.COD_ORGAO_ORIGEM = :codigoOrgaoOrigem");
		query.append("	ORDER BY DP.NUM_SEQUENCIA");
		return query.toString();
	}
	
	// query para relatório de guias antigas de devolução de autos (carga efetuada pelo MAP)
	// relacionamento com STF.ADVOGADOS
	private String getQueryRelatorioGuiaAntigaDevolucaoAutosProcesso(List<Guia> guias) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT G.NUM_GUIA AS ");
        query.append(RelatorioGuiaDeslocamentoProcessoMapper.NUMERO_GUIA);
		query.append(", G.ANO_GUIA AS "); 
        query.append(RelatorioGuiaDeslocamentoProcessoMapper.ANO_GUIA);
		query.append(", G.COD_ORGAO_ORIGEM AS "); 
        query.append(RelatorioGuiaDeslocamentoProcessoMapper.CODIGO_ORGAO_ORIGEM);
		query.append(", ADV_ORIGEM.NOM_ADV AS ");
        query.append(RelatorioGuiaDeslocamentoProcessoMapper.DESCRICAO_ORGAO_ORIGEM);
		query.append(", G.COD_ORGAO_DESTINO AS ");
        query.append(RelatorioGuiaDeslocamentoProcessoMapper.CODIGO_ORGAO_DESTINO);
		query.append(", SETOR_DESTINO.DSC_SETOR AS ");
        query.append(RelatorioGuiaDeslocamentoProcessoMapper.DESCRICAO_ORGAO_DESTINO);
		
        query.append(", NULL AS "+RelatorioGuiaDeslocamentoProcessoMapper.ENDERECO_ORGAO_DESTINO);
		query.append(", NULL AS "+RelatorioGuiaDeslocamentoProcessoMapper.NUM_LOCALIZACAO);
		query.append(", NULL AS "+RelatorioGuiaDeslocamentoProcessoMapper.BAIRRO);
		query.append(", NULL AS "+RelatorioGuiaDeslocamentoProcessoMapper.COMPLEMENTO);
		query.append(", NULL AS "+RelatorioGuiaDeslocamentoProcessoMapper.MUNICIPIO_ORGAO_DESTINO);
		query.append(", NULL AS "+RelatorioGuiaDeslocamentoProcessoMapper.UF_ORGAO_DESTINO);
		query.append(", NULL AS "+RelatorioGuiaDeslocamentoProcessoMapper.CEP_ORGAO_DESTINO);
		
        query.append(", PR.SIG_CLASSE_PROCES AS ");
        query.append(RelatorioGuiaDeslocamentoProcessoMapper.SIGLA_CLASSE_PROCESSO);
		query.append(", PR.NUM_PROCESSO AS ");
        query.append(RelatorioGuiaDeslocamentoProcessoMapper.NUMERO_PROCESSO);
		query.append(", decode(pr.qtd_volumes,null,0,pr.qtd_volumes) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.QUANTIDADE_VOLUMES);
		query.append(", decode(pr.qtd_apensos,null,0,pr.qtd_apensos) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.QUANTIDADE_APENSOS);
		query.append(", decode(pr.qtd_juntada_linha,null,0,pr.qtd_juntada_linha) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.QUANTIDADE_JUNTADA_LINHA);
		query.append("		, (select count(*) from stf.processo_dependencia pd");
		query.append("                where pd.sig_classe_vinculador = pr.sig_classe_proces");
		query.append("                 and pd.num_processo_vinculador = pr.num_processo");
		query.append("                 and pd.DAT_FIM_DEPENDENCIA is null");
		query.append("                 and pd.cod_tipo_dependencia_processo in (5,9)) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.QUANTIDADE_VINCULO);

		query.append(", decode(g.dsc_observacao,null,'',g.dsc_observacao) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.OBSERVACAO);

		query.append(", g.dat_remessa as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.DATA_REMESSA);
		query.append(", dp.dat_recebimento as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.DATA_RECEBIMENTO);
		// RECUPERA 1 SE FOR UM VINCULADO OU 0 SE NÃO FOR
		query.append("    , (select count(*) from STF.PROCESSO_DEPENDENCIA dep ");
		query.append("		       where PR.NUM_PROCESSO = DEP.NUM_PROCESSO ");
	    query.append("       and PR.SIG_CLASSE_PROCES = DEP.SIG_CLASSE_PROCES ");
	    query.append("		       and DEP.COD_TIPO_DEPENDENCIA_PROCESSO in (5,9) ");
	    query.append("		       and DEP.DAT_FIM_DEPENDENCIA is null) as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.VINCULADO);
		// RECUPERA O TIPO MEIO DO PROCESSO
		query.append(", PR.tip_meio_processo as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.TIPO_MEIO);
		// O DESTINATARIO REFERENTE AO ENDEREÇO NESTE CASO É NULL
		query.append(", NULL as ");
		query.append(RelatorioGuiaDeslocamentoProcessoMapper.DESTINATARIO);
		
		query.append("	FROM STF.GUIAS G, ");
		query.append(" STF.DESLOCA_PROCESSOS DP,");
		query.append(" JUDICIARIO.PROCESSO PR,");
		query.append(" STF.ADVOGADOS ADV_ORIGEM,");
		query.append(" STF.SETORES SETOR_DESTINO");

		query.append("	WHERE G.NUM_GUIA = DP.NUM_GUIA");
		query.append("	  AND G.ANO_GUIA = DP.ANO_GUIA");
		query.append("	  AND G.COD_ORGAO_ORIGEM = DP.COD_ORGAO_ORIGEM");
		query.append("	  AND DP.SEQ_OBJETO_INCIDENTE = PR.SEQ_OBJETO_INCIDENTE");
	    // ORIGEM DEVE SER UM JURISDICIONADO (ADVOGADO)
		query.append("	  AND DP.COD_ORGAO_ORIGEM = ADV_ORIGEM.COD_ADV");
	    // DESTINO DEVE SER UM SETOR DO STF
		query.append("	  AND DP.COD_ORGAO_DESTINO = SETOR_DESTINO.COD_SETOR");
		
		query.append(getINGuia(guias));
		query.append(" ORDER BY g.num_guia, g.ano_guia, g.cod_orgao_origem, DP.NUM_SEQUENCIA");

		return query.toString();
	}
	
	
	// query para relatórios de guia no deslocamento de petições
	private String getQueryRelatorioGuiaDeslocamentoPeticao(List<Guia> guias) {
		StringBuilder query = new StringBuilder();
		
		query.append("SELECT g.num_guia as ");
		query.append(RelatorioGuiaDeslocamentoPeticaoMapper.NUMERO_GUIA);
		query.append(", g.ano_guia as ");
		query.append(RelatorioGuiaDeslocamentoPeticaoMapper.ANO_GUIA);
		query.append(", g.cod_orgao_origem as ");
		query.append(RelatorioGuiaDeslocamentoPeticaoMapper.CODIGO_ORGAO_ORIGEM);
		query.append(", CASE g.tip_orgao_origem");
		query.append("    WHEN '1' THEN advorigem.nom_adv");
		query.append("    WHEN '2' THEN sorigem.dsc_setor");
		query.append("	  WHEN '3' THEN orgorigem.dsc_origem");
		query.append("	 END as ");
		query.append(RelatorioGuiaDeslocamentoPeticaoMapper.DESCRICAO_ORGAO_ORIGEM);
		query.append(", g.cod_orgao_destino as ");
		query.append(RelatorioGuiaDeslocamentoPeticaoMapper.CODIGO_ORGAO_DESTINO);
		query.append(", CASE g.tip_orgao_destino");
		query.append("    WHEN '1' THEN advdestino.nom_adv");
		query.append("    WHEN '2' THEN sdestino.dsc_setor");
		query.append("	  WHEN '3' THEN orgdestino.dsc_origem");
		query.append("	 END as ");
		query.append(RelatorioGuiaDeslocamentoPeticaoMapper.DESCRICAO_ORGAO_DESTINO);

		query.append(", CASE g.tip_orgao_destino");
		query.append("    WHEN '1' THEN DECODE(advdestino.end_adv_lograd, NULL, 'Endereço não informado', advdestino.end_adv_lograd)");
		query.append("    WHEN '2' THEN NULL");
		query.append("    WHEN '3' THEN NULL");
		query.append("	 END as ");
		query.append(RelatorioGuiaDeslocamentoPeticaoMapper.ENDERECO_ORGAO_DESTINO);
		
		query.append(", CASE g.tip_orgao_destino");
		query.append("    WHEN '1' THEN DECODE(advdestino.end_adv_cidade, NULL, ' ', advdestino.end_adv_cidade)");
		query.append("    WHEN '2' THEN NULL");
		query.append("    WHEN '3' THEN NULL");
		query.append("	 END as ");
		query.append(RelatorioGuiaDeslocamentoPeticaoMapper.MUNICIPIO_ORGAO_DESTINO);

		query.append(", CASE g.tip_orgao_destino");
		query.append("    WHEN '1' THEN DECODE(advdestino.uf_adv, NULL, ' ', advdestino.uf_adv)");
		query.append("    WHEN '2' THEN NULL");
		query.append("    WHEN '3' THEN NULL");
		query.append("	 END as ");
		query.append(RelatorioGuiaDeslocamentoPeticaoMapper.UF_ORGAO_DESTINO);

		query.append(", CASE g.tip_orgao_destino");
		query.append("    WHEN '1' THEN DECODE(advdestino.cep_adv, NULL, ' ',advdestino.cep_adv)");
		query.append("    WHEN '2' THEN NULL");
		query.append("    WHEN '3' THEN NULL");
		query.append("	 END as ");
		query.append(RelatorioGuiaDeslocamentoPeticaoMapper.CEP_ORGAO_DESTINO);

		query.append(", pr.sig_classe_proces as " + RelatorioGuiaDeslocamentoPeticaoMapper.SIGLA_CLASSE_PROCESSO);
		query.append(", pr.num_processo as " + RelatorioGuiaDeslocamentoPeticaoMapper.NUMERO_PROCESSO);
		query.append(", pet.num_peticao as " + RelatorioGuiaDeslocamentoPeticaoMapper.NUMERO_PETICAO);
		query.append(", pet.ano_peticao as " + RelatorioGuiaDeslocamentoPeticaoMapper.ANO_PETICAO);
		query.append(", g.dsc_observacao as " + RelatorioGuiaDeslocamentoPeticaoMapper.OBSERVACAO);
		query.append(", g.dat_remessa as " + RelatorioGuiaDeslocamentoPeticaoMapper.DATA_REMESSA);
		query.append(", dp.dat_recebimento as " + RelatorioGuiaDeslocamentoPeticaoMapper.DATA_RECEBIMENTO);
		query.append(", PR.TIP_MEIO_PROCESSO as " + RelatorioGuiaDeslocamentoPeticaoMapper.TIPO_MEIO);
		
		query.append(" FROM stf.desloca_peticaos dp,");
		query.append(" stf.guias g, ");
		query.append(" judiciario.peticao pet,");
		query.append(" judiciario.processo pr,");
		query.append(" stf.setores sorigem,");
		query.append(" stf.setores sdestino,");
		query.append(" stf.advogados advorigem,");
		query.append(" stf.advogados advdestino,");
		query.append(" judiciario.origem orgorigem,");
		query.append(" judiciario.origem orgdestino ");
		
		query.append(" WHERE g.num_guia = dp.num_guia");
		query.append(" AND g.ano_guia = dp.ano_guia");
		query.append(" AND g.cod_orgao_origem = dp.cod_orgao_origem");
		query.append(" AND DP.SEQ_OBJETO_INCIDENTE = PET.SEQ_OBJETO_INCIDENTE");

		query.append(" AND PET.SEQ_OBJETO_INCIDENTE_VINC = PR.SEQ_OBJETO_INCIDENTE (+)");
		
		query.append(" AND dp.cod_orgao_origem = sorigem.cod_setor (+)");
		query.append(" AND dp.cod_orgao_destino = sdestino.cod_setor (+)");
		query.append(" AND dp.cod_orgao_origem = advorigem.cod_adv (+)");
		query.append(" AND dp.cod_orgao_destino = advdestino.cod_adv (+)");
		query.append(" AND dp.cod_orgao_origem = orgorigem.cod_origem (+)");
		query.append(" AND dp.cod_orgao_destino = orgdestino.cod_origem (+)");
		
		query.append(getINGuia(guias));
		query.append(" ORDER BY g.num_guia, g.ano_guia, g.cod_orgao_origem, DP.NUM_SEQUENCIA");

		//query.append(" AND g.ano_guia = :anoDaGuia");
		//query.append(" AND g.num_guia = :numeroDaGuia");
		//query.append(" AND g.cod_orgao_origem = :codigoOrgaoOrigem");
		
		
		return query.toString();
	}

	private String getQueryRelatorioAutosEmprestadosAdvogados(boolean filtroPeriodo, boolean filtroClasse) {
		StringBuilder query = new StringBuilder();

		query.append("SELECT a.nom_adv as ");
		query.append(RelatorioAutosEmprestadosAdvogadosMapper.NOME_ADVOGADO);
		query.append(",   a.cod_adv as ");
		query.append(RelatorioAutosEmprestadosAdvogadosMapper.CODIGO_ADVOGADO);
		query.append(",   dp.sig_classe_proces as ");
		query.append(RelatorioAutosEmprestadosAdvogadosMapper.SIGLA_CLASSE_PROCESSO);
		query.append(",   dp.num_processo as ");
		query.append(RelatorioAutosEmprestadosAdvogadosMapper.NUMERO_PROCESSO);
		query.append(",   g.dat_remessa as ");
		query.append(RelatorioAutosEmprestadosAdvogadosMapper.DATA_REMESSA);
		query.append(",   m.nom_ministro as ");
		query.append(RelatorioAutosEmprestadosAdvogadosMapper.RELATOR);
		query.append(",   g.tip_orgao_destino as ");
		query.append(RelatorioAutosEmprestadosAdvogadosMapper.TIPO_ORGAO_DESTINO);
		query.append("  FROM stf.desloca_processos dp");
		query.append("   JOIN stf.advogados a");
		query.append("       ON a.cod_adv = dp.cod_orgao_destino");
		query.append("   JOIN stf.guias g");
		query.append("       ON     g.num_guia = dp.num_guia");
		query.append("          AND g.ano_guia = dp.ano_guia");
		query.append("          AND g.cod_orgao_origem = dp.cod_orgao_origem");
		query.append("          AND g.cod_orgao_destino = dp.cod_orgao_destino");
		query.append("   JOIN stf.sit_min_processos smp");
		query.append("       ON smp.sig_classe_proces = dp.sig_classe_proces");
		query.append("          AND smp.num_processo = dp.num_processo");
		query.append("   JOIN stf.ministros m");
		query.append("       ON m.cod_ministro = smp.cod_ministro");
		query.append("  WHERE 1 = 1 ");
		if (filtroPeriodo) {
			query.append("   AND TRUNC (dp.dat_recebimento) >= TO_DATE (:dataInicial, 'dd/mm/yyyy')");
			query.append("   AND TRUNC (dp.dat_recebimento) <= TO_DATE (:dataFinal, 'dd/mm/yyyy')");
		}
		query.append("   AND dp.flg_ultimo_deslocamento = 'S'");
		query.append("   AND smp.flg_relator_atual = 'S'");
		query.append("   AND g.tip_orgao_destino = '1'");
		if (filtroClasse) {
			query.append("  AND dp.sig_classe_proces = :siglaClasseProcesso");
		}
		query.append("  ORDER BY a.nom_adv,");
		query.append("     a.cod_adv,");
		query.append("     g.dat_remessa,");
		query.append("     dp.num_processo,");
		query.append("     m.nom_ministro");
		return query.toString();
	}

	private String getQueryRelatorioAutosEmprestadosOrgaosExternos(boolean filtroOrgao, boolean filtroPeriodo, boolean filtroClasse) {
		StringBuilder query = new StringBuilder();

		query.append("SELECT o.dsc_origem as ");
		query.append(RelatorioAutosEmprestadosOrgaosExternosMapper.DESCRICAO_ORGAO_EXTERNO);
		query.append(", dp.sig_classe_proces as ");
		query.append(RelatorioAutosEmprestadosOrgaosExternosMapper.SIGLA_CLASSE_PROCESSO);
		query.append(", dp.num_processo as ");
		query.append(RelatorioAutosEmprestadosOrgaosExternosMapper.NUMERO_PROCESSO);
		query.append(", g.dat_remessa as ");
		query.append(RelatorioAutosEmprestadosOrgaosExternosMapper.DATA_REMESSA);
		query.append(", m.nom_ministro as ");
		query.append(RelatorioAutosEmprestadosOrgaosExternosMapper.RELATOR);
		query.append(", g.tip_orgao_destino as ");
		query.append(RelatorioAutosEmprestadosOrgaosExternosMapper.TIPO_ORGAO_DESTINO);
		query.append(", dp.cod_orgao_destino as ");
		query.append(RelatorioAutosEmprestadosOrgaosExternosMapper.NUMERO_ORGAO_EXTERNO);
		query.append("	  FROM stf.desloca_processos dp");
		query.append("         JOIN stf.origens o");
		query.append("	           ON o.cod_origem = dp.cod_orgao_destino");
		query.append("	       JOIN stf.guias g");
		query.append("	           ON     g.num_guia = dp.num_guia");
		query.append("	              AND g.ano_guia = dp.ano_guia");
		query.append("	              AND g.cod_orgao_origem = dp.cod_orgao_origem");
		query.append("	              AND g.cod_orgao_destino = dp.cod_orgao_destino");
		query.append("	       JOIN stf.sit_min_processos smp");
		query.append("	           ON smp.sig_classe_proces = dp.sig_classe_proces");
		query.append("	              AND smp.num_processo = dp.num_processo");
		query.append("	       JOIN stf.ministros m");
		query.append("	           ON m.cod_ministro = smp.cod_ministro");
		query.append("	 WHERE     1 = 1 ");
		if (filtroPeriodo) {
			query.append("	       AND TRUNC (dp.dat_recebimento) >= TO_DATE (:dataInicial, 'dd/mm/yyyy')");
			query.append("	       AND TRUNC (dp.dat_recebimento) <= TO_DATE (:dataFinal, 'dd/mm/yyyy')");
		}
		query.append("	       AND dp.flg_ultimo_deslocamento = 'S'");
		query.append("	       AND smp.flg_relator_atual = 'S'");
		query.append("	       AND g.tip_orgao_destino = '3'");
		if (filtroOrgao) {
			query.append("	       AND o.cod_origem = :codigoOrgaoExterno");
		}
		if (filtroClasse) {
			query.append("	       AND dp.sig_classe_proces = :siglaClasseProcesso");
		}
		query.append("	ORDER BY o.dsc_origem,");
		query.append("	         g.dat_remessa,");
		query.append("	         dp.num_processo,");
		query.append("	         m.nom_ministro");
		return query.toString();
	}
	
	private String getQueryRelatorioAcordaosPublicados(String codigoSetorPubAcordao, String deslocadoParaAcordao) {
		StringBuilder query = new StringBuilder();

		
		query.append("SELECT 	d.sig_classe_proces,	d.num_processo, PI.DAT_PUBLICACAO,"); 
		query.append("			(SELECT rp.dsc_cadeia_incidente");
		query.append("				FROM judiciario.recurso_processo rp");
		query.append("				WHERE rp.seq_objeto_incidente = pi.seq_objeto_incidente) as Recurso,");
		query.append("			decode(p.tip_meio_processo,'F','Físico','E','Eletrônico')");
		query.append("		FROM stf.desloca_processos d,");
		query.append("           stf.processos_img pi,");
		query.append("   		 judiciario.objeto_incidente oi,");
		query.append("   		 judiciario.processo p");
		query.append("		WHERE pi.seq_objeto_incidente = oi.seq_objeto_incidente");
		query.append("			AND d.seq_objeto_incidente = oi.seq_objeto_incidente_principal");
		query.append("			AND d.seq_objeto_incidente = p.seq_objeto_incidente");
		
		if(deslocadoParaAcordao.equals("S")) {
			query.append("			AND d.cod_orgao_destino = " + codigoSetorPubAcordao);
		}else {
			query.append("			AND d.cod_orgao_destino <> " + codigoSetorPubAcordao);
			
		}
		query.append("			AND d.flg_ultimo_deslocamento = 'S'");
		query.append("			AND pi.dat_publicacao = TO_DATE (:dataPublicacao, 'dd/mm/yyyy')");
		query.append("			AND D.SEQ_OBJETO_INCIDENTE NOT IN ( ");
		query.append("			      SELECT X.SEQ_OBJETO_INCIDENTE ");
		query.append("			         FROM STF.DESLOCA_PROCESSOS X WHERE X.SEQ_OBJETO_INCIDENTE = D.SEQ_OBJETO_INCIDENTE");
		query.append("			       AND X.DAT_RECEBIMENTO IS NULL)");
		query.append("		ORDER BY d.dat_recebimento");
		
		return query.toString();
	}
	
	private String getQueryRelatorioProcessoInteresse() {
		StringBuilder query = new StringBuilder();
		query.append("		SELECT A.DSC_ANDAMENTO,");
		query.append("	       ap.DSC_OBSER_AND, ");
		query.append("	       ap.DAT_ANDAMENTO, ");
		query.append("	       P.SIG_CLASSE_PROCES,"); 
		query.append("	       P.NUM_PROCESSO, ");
		query.append("	       I.DSC_IDENTIFICACAO, ");
		query.append("	       J.SEQ_JURISDICIONADO, ");
		query.append("	       J.NOM_JURISDICIONADO");
	       
		query.append("	FROM STF.ANDAMENTO_PROCESSOS ap,");
		query.append("	     STF.ANDAMENTOS a, ");
		query.append("	     JUDICIARIO.PROCESSO_INTERESSE pi,"); 
		query.append("	     JUDICIARIO.PROCESSO p,");
		query.append("	     JUDICIARIO.JURISDICIONADO j,");
		query.append("	     JUDICIARIO.IDENTIFICACAO_PESSOA i");
		query.append("	WHERE ap.COD_ANDAMENTO = A.COD_ANDAMENTO");
		query.append("	  and AP.SEQ_OBJETO_INCIDENTE = PI.SEQ_OBJETO_INCIDENTE");
		query.append("	  and PI.SEQ_OBJETO_INCIDENTE = P.SEQ_OBJETO_INCIDENTE");
		query.append("	  and PI.SEQ_JURISDICIONADO = :seqJurisdicionado");
		query.append("	  AND PI.SEQ_JURISDICIONADO = J.SEQ_JURISDICIONADO");
		query.append("	  and I.SEQ_JURISDICIONADO = J.SEQ_JURISDICIONADO (+)" );
		query.append("	  and I.SEQ_TIPO_IDENTIFICACAO (+) = 2");
		query.append("	  and ap.DAT_ANDAMENTO between (sysdate - 7) and sysdate");
		query.append("	ORDER BY pi.SEQ_OBJETO_INCIDENTE, ap.NUM_SEQUENCIA DESC");
		return query.toString();
	}
	
	private class GuiaDeslocamentoProcessoResultsetTransformer extends GuiaDeslocamentoResultTransformer {

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
			RelatorioGuiaDeslocamentoProcesso dado = new RelatorioGuiaDeslocamentoProcesso();
			dado.setNumeroDaGuia(getLong(rowData[0]));
			dado.setAnoDaGuia(getShort(rowData[1]));
			dado.setCodigoOrgaoOrigem(getLong(rowData[2]));
			dado.setDescricaoOrgaoOrigem(getString(rowData[3]));
			dado.setCodigoOrgaoDestino(getLong(rowData[4]));
			dado.setDescricaoOrgaoDestino(getString(rowData[5]));
			dado.setEnderecoOrgaoDestino(getString(rowData[6]));
			dado.setNumLocalizacao(getString(rowData[7]));
			dado.setBairro(getString(rowData[8]));
			dado.setComplemento(getString(rowData[9]));
			dado.setMunicipioOrgaoDestino(getString(rowData[10]));
			dado.setUfOrgaoDestino(getString(rowData[11]));
			dado.setCepOrgaoDestino(getString(rowData[12]));
			dado.setSiglaClasseProcesso(getString(rowData[13]));
			dado.setNumeroProcesso(getLong(rowData[14]));
			dado.setQuantidadeVolumes(getInteger(rowData[15]));
			dado.setQuantidadeApensos(getInteger(rowData[16]));
			dado.setQuantidadeJuntadaLinha(getInteger(rowData[17]));
			dado.setQuantidadeVinculo(getInteger(rowData[18]));
			dado.setObservacao(getString(rowData[19]));
			dado.setDataRemessa(getDate((Date) rowData[20]));
			dado.setDataRecebimento(getDate((Date) rowData[21]));
			dado.setVinculado(getInteger(rowData[22]));
			dado.setTipoMeio(getString((String) rowData[23]));
			dado.setDestinatario(getString((String) rowData[24]));
			if (rowData.length == 26) {
				dado.setCodigoDestinatario(getLong(rowData[25]));
			}
			return dado;
		}

	}

	private class GuiaDeslocamentoPeticaoResultsetTransformer extends GuiaDeslocamentoResultTransformer {

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
			RelatorioGuiaDeslocamentoPeticao dado = new RelatorioGuiaDeslocamentoPeticao();
			dado.setNumeroDaGuia(getLong(rowData[0]));
			dado.setAnoDaGuia(getShort(rowData[1]));
			dado.setCodigoOrgaoOrigem(getLong(rowData[2]));
			dado.setDescricaoOrgaoOrigem(getString(rowData[3]));
			dado.setCodigoOrgaoDestino(getLong(rowData[4]));
			dado.setDescricaoOrgaoDestino(getString(rowData[5]));
			dado.setEnderecoOrgaoDestino(getString(rowData[6]));
			dado.setMunicipioOrgaoDestino(getString(rowData[7]));
			dado.setUfOrgaoDestino(getString(rowData[8]));
			dado.setCepOrgaoDestino(getString(rowData[9]));
			dado.setSiglaClasseProcesso(getString(rowData[10]));
			dado.setNumeroProcesso(getLong(rowData[11]));
			dado.setNumeroPeticao(getLong(rowData[12]));
			dado.setAnoPeticao(getShort(rowData[13]));
			dado.setObservacao(getString(rowData[14]));
			dado.setDataRemessa(getDate((Date) rowData[15]));
			dado.setDataRecebimento(getDate((Date) rowData[16]));
			dado.setTipoMeio(getString((String) rowData[17]));
			return dado;
		}

	}
	
	private class AutosEmprestadosAdvogadosResultsetTransformer extends AutosEmprestadosResultTransformer {

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
			RelatorioAutosEmprestadosAdvogados dado = new RelatorioAutosEmprestadosAdvogados();
			dado.setNomeAdvogado(getString(rowData[0]));
			dado.setCodigoAdvogado(getLong(rowData[1]));
			dado.setSiglaClasseProcesso(getString(rowData[2]));
			dado.setNumeroProcesso(getLong(rowData[3]));
			dado.setDataRemessa(getDate(rowData[4]));
			dado.setRelator(getString(rowData[5]));
			dado.setTipoOrgaoDestino(getInteger(rowData[5]));
			dado.setDescricaoDestino(getString(rowData[0]));
			return dado;
		}

	}
	
	private class AutosEmprestadosOrgaosExternosResultsetTransformer extends AutosEmprestadosResultTransformer {

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
			RelatorioAutosEmprestadosOrgaosExternos dado = new RelatorioAutosEmprestadosOrgaosExternos();
			dado.setDescricaoOrgaoExterno(getString(rowData[0]));
			dado.setSiglaClasseProcesso(getString(rowData[1]));
			dado.setNumeroProcesso(getLong(rowData[2]));
			dado.setDataRemessa(getDate(rowData[3]));
			dado.setRelator(getString(rowData[4]));
			dado.setTipoOrgaoDestino(getInteger(rowData[5]));
			dado.setDescricaoDestino(getString(rowData[0]));
			dado.setNumeroOrgaoDestino(getLong(rowData[6]));
			return dado;
		}

	}
	
	private class AcordaosPublicadosResultsetTransformer extends AutosEmprestadosResultTransformer {

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
			RelatorioAcordaoPublicado dado = new RelatorioAcordaoPublicado();
			dado.setSiglaProcessual(getString(rowData[0]));
			dado.setNumeroProcessual(getLong(rowData[1]));
			dado.setDataPublicacao(getDate(rowData[2]));
			dado.setRecurso(getString(rowData[3]));
			dado.setTipoMeio(getString(rowData[4]));
			return dado;
		}

	}
	
	private class ProcessoInteresseResultsetTransformer extends AutosEmprestadosResultTransformer {

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
			RelatorioProcessoInteresse dado = new RelatorioProcessoInteresse();
			dado.setDescricaoAndamento(getString(rowData[0]));
			dado.setObservacaoAndamento(getString(rowData[1]));
			dado.setDataAndamento(getDate(rowData[2]));
			dado.setIdentificacaoProcesso( getString(rowData[3]) + " " + getString(rowData[4]) );
			dado.setOab(getString(rowData[5]));
			dado.setIdAdvogado(getLong(rowData[6]));
			dado.setNomeAdvogado(getString(rowData[7]));
			
			return dado;
		}

	}
}
