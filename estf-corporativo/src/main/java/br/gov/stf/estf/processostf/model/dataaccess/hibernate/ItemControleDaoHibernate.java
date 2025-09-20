package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.SessionImplementor;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.documento.ArquivoProcessoEletronico;
import br.gov.stf.estf.entidade.processostf.ItemControle;
import br.gov.stf.estf.entidade.processostf.TipoSituacaoControle;
import br.gov.stf.estf.processostf.model.dataaccess.ItemControleDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ItemControleDaoHibernate extends GenericHibernateDao<ItemControle, Long> implements ItemControleDao {

	private static final long serialVersionUID = 5783649864875271053L;

	public ItemControleDaoHibernate() {
		super(ItemControle.class);
	}

	/**
	 * GABINETE_SEJ: o usuário visualiza todos os controles independente de setor e final de número.
	 * MANTER_GRUPOS: o usuário visualiza todos os controles que estão associados ao seu usuário e 
	 * 	todos os controles vinculados ao seu setor (judiciario.mapeamento_classe_setor) independente do final de número.
	 * USUÁRIO COMUM: visualiza os controles que estão associados ao seu usuário e os controles do seu 
	 * 	setor (judiciario.mapeamento_classe_setor) e que ele possui o final cadastrado.  
	 */
	@SuppressWarnings("unchecked")
	public List<ItemControle> buscaListaDeItemUsuario(List<String> listaFiltroUsuarioItensControle,
			TipoSituacaoControle tipoSC, Long idSetor, boolean permissaoGabineteSEJ,
			String sigUsuario, boolean permissaoManterGrupo) throws DaoException {

		List<ItemControle> listaItensControle = null;

		StringBuffer sql = new StringBuffer();

		try {
			sql.append("SELECT IC.* FROM judiciario.item_controle IC ");

			if (!permissaoGabineteSEJ) {

				sql.append(" , JUDICIARIO.OBJETO_INCIDENTE OB ");
				
			    sql.append(",(SELECT pr.sig_classe_proces,");
			    sql.append("           pr.seq_objeto_incidente, pr.num_processo,");
			    sql.append("           NVL (ip.seq_tipo_preferencia, -1) AS seq_tipo_preferencia");
			    sql.append("      FROM judiciario.processo pr, judiciario.incidente_preferencia ip");
			    sql.append("     WHERE pr.seq_objeto_incidente = ip.seq_objeto_incidente(+)");
			    sql.append("           AND ip.seq_tipo_preferencia(+) = 2) pr,");
			    sql.append("   judiciario.mapeamento_classe_setor mp");
							
				sql.append(" WHERE IC.SEQ_OBJETO_INCIDENTE = OB.SEQ_OBJETO_INCIDENTE ");
				sql.append(" AND OB.SEQ_OBJETO_INCIDENTE_PRINCIPAL = PR.SEQ_OBJETO_INCIDENTE AND IC.SEQ_TIPO_CONTROLE = 1 ");
				
				// busca somente os processos (itens) caso o setor do usuário conste na tabela
				// JUDICIARIO.MAPEAMENTO_CLASSE_SETOR
				if (idSetor != null && idSetor > 0) {
					sql.append("   AND mp.sig_classe = pr.sig_classe_proces");
					sql.append("   AND mp.cod_setor = :idSetor");
					sql.append("   AND NVL (mp.seq_tipo_preferencia, -1) = pr.seq_tipo_preferencia");										
				}

						
				if (!permissaoManterGrupo && listaFiltroUsuarioItensControle != null && listaFiltroUsuarioItensControle.size() > 0) {
					sql.append(" AND (");

					int quantidadeFiltros = listaFiltroUsuarioItensControle.size();

					for (String filtro : listaFiltroUsuarioItensControle) {
						sql.append(filtro);

						if (quantidadeFiltros > 1) {
							sql.append(" OR ");
							quantidadeFiltros--;
						}

					}					
					sql.append(" )");				
				}
				sql.append(" AND IC.SEQ_TIPO_SITUACAO_CONTROLE <> :idTipo ");
			} else {
				sql.append(" WHERE IC.SEQ_TIPO_SITUACAO_CONTROLE <> :idTipo AND IC.SEQ_TIPO_CONTROLE = 1 ");
			}

			SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString());

			if (!permissaoGabineteSEJ) {
				if (idSetor != null && idSetor > 0L) {
					sqlQuery.setLong("idSetor", idSetor);
				}

				sqlQuery.setLong("idTipo", tipoSC.getId());
	
			} else {
				sqlQuery.setLong("idTipo", tipoSC.getId());
			}

			sqlQuery.addEntity(ItemControle.class);
			listaItensControle = sqlQuery.list();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return listaItensControle;
	}

	public String pesquisaUltimoDeslocamentoProcesso(Long idObjetoIncidente) throws DaoException {

		String nomeSetorDeslocaProcesso = "";
		StringBuffer sql = new StringBuffer();

		try {
			sql.append(" SELECT d.descricao ");
			sql.append(" FROM STF.DESLOCA_PROCESSOS dp, ");
			sql.append(" 	(SELECT s.cod_setor codigo, s.dsc_setor descricao ");
			sql.append("		FROM STF.SETORES s ");
			sql.append("	UNION ALL ");
			sql.append("	SELECT o.cod_origem, o.dsc_origem ");
			sql.append("		FROM stf.origens o) d ");
			sql.append("	WHERE dp.cod_orgao_destino = d.codigo  ");
			sql.append("	AND dp.flg_ultimo_deslocamento = 'S' ");
			sql.append("	AND dp.SEQ_OBJETO_INCIDENTE = :idObIncidente ");
			

			SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString());

			sqlQuery.setLong("idObIncidente", idObjetoIncidente);

			nomeSetorDeslocaProcesso = (String) sqlQuery.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return nomeSetorDeslocaProcesso;

	}

	public ArquivoProcessoEletronico existePecaProcessoEletronicoJuntada(Long idPecaProcEletronico) throws DaoException {

		ArquivoProcessoEletronico arquivoProcessoEletronico = new ArquivoProcessoEletronico();
		Session session = retrieveSession();

		try {

			Criteria criteria = session.createCriteria(ArquivoProcessoEletronico.class, "ape");

			criteria.add(Restrictions.eq("ape.pecaProcessoEletronico.id", idPecaProcEletronico));

			arquivoProcessoEletronico = (ArquivoProcessoEletronico) criteria.uniqueResult();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
		return arquivoProcessoEletronico;

	}

	public void chamaPackageItemControle() throws DaoException, SQLException {
		Session session = retrieveSession();
		SessionImplementor callableSession = (SessionImplementor) session;
		CallableStatement storedProcedureStmt = callableSession.getBatcher().prepareCallableStatement(
				"{call JUDICIARIO.PKG_INSERE_ITEM_CONTROLE.prc_cumprimento_despacho_e_dec()}");
		storedProcedureStmt.execute();
		callableSession.getBatcher().closeStatement(storedProcedureStmt);
		session.flush();
	}
	
	public boolean isItemControleRepublicacao(Long itemControleId) throws DaoException {
		StringBuffer sql = new StringBuffer();

		try {
			
			sql.append(" select count(1) ");
			sql.append(" from stf.materias m,  ");
			sql.append(" 	 jurisprudencia.item_controle_publicacao_dj it,  ");
			sql.append(" 	 judiciario.item_controle ic, ");
			sql.append(" 	 stf.data_publicacoes dp,  ");
			sql.append(" 	 stf.processo_publicados pp, ");
			sql.append("     stf.capitulos c ");
			sql.append(" where  ");
			sql.append("     it.seq_item_controle = ic.seq_item_controle ");
			sql.append("     and dp.seq_data_publicacoes = it.seq_data_publicacoes ");
			sql.append("     and m.seq_data_publicacoes = it.seq_data_publicacoes ");
			sql.append("     and m.cod_capitulo = pp.cod_capitulo ");
			sql.append("     and m.cod_materia = pp.cod_materia ");
			sql.append("     and m.ano_materia = pp.ano_materia ");
			sql.append("     and m.num_materia = pp.num_materia  ");   
			sql.append("     and c.cod_capitulo = m.cod_capitulo ");
			sql.append("     and c.cod_materia = m.cod_materia ");
			sql.append("     and c.cod_conteudo = m.cod_conteudo ");
			sql.append("     and ic.seq_objeto_incidente = pp.seq_objeto_incidente ");
			sql.append("     and m.cod_conteudo = 50 ");
			sql.append("     and upper(c.dsc_materia) like '%REPUBLICA%' ");
			sql.append("     and dp.dat_publicacao_dj is not null ");
			sql.append("     and it.seq_item_controle = :itemControle ");
			
			SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString());

			sqlQuery.setLong("itemControle", itemControleId);
			
			Number number = (Number) sqlQuery.uniqueResult();

			return number != null && number.intValue() > 0;

		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
