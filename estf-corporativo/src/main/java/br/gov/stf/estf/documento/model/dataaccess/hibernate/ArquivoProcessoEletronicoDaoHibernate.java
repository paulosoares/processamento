package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.ArquivoProcessoEletronicoDao;
import br.gov.stf.estf.documento.model.util.ArquivoProcessoEletronicoSearchData;
import br.gov.stf.estf.entidade.documento.ArquivoProcessoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.documento.TipoSituacaoPeca;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ArquivoProcessoEletronicoDaoHibernate extends
		GenericHibernateDao<ArquivoProcessoEletronico, Long> implements
		ArquivoProcessoEletronicoDao {

	private static final long serialVersionUID = 1L;

	public ArquivoProcessoEletronicoDaoHibernate() {
		super(ArquivoProcessoEletronico.class);
	}

	public ArquivoProcessoEletronicoDaoHibernate(
			Class<ArquivoProcessoEletronico> clazz) {
		super(clazz);
	}

	@Deprecated
	@Override
	public List<ArquivoProcessoEletronico> pesquisarArquivoProcessoEletronico(
			String siglaClasseProcessual, Long numeroProcessual,
			Long numeroProtocolo, Short anoProtocolo, Long idDocumentoEletronico)
			throws DaoException {

		return pesquisarArquivoProcessoEletronico(construirSearchData(
				siglaClasseProcessual, numeroProcessual, numeroProtocolo,
				anoProtocolo, idDocumentoEletronico));
	}

	private ArquivoProcessoEletronicoSearchData construirSearchData(
			String siglaClasseProcessual, Long numeroProcessual,
			Long numeroProtocolo, Short anoProtocolo, Long idDocumentoEletronico) {
		ArquivoProcessoEletronicoSearchData searchData = new ArquivoProcessoEletronicoSearchData();
		searchData.siglaClasseProcessual = siglaClasseProcessual;
		searchData.numeroProcessual = numeroProcessual;
		searchData.numeroProtocolo = numeroProtocolo;
		searchData.anoProtocolo = anoProtocolo;
		searchData.idDocumentoEletronico = idDocumentoEletronico;
		return searchData;
	}

	@Override
	public List<ArquivoProcessoEletronico> pesquisarArquivoProcessoEletronico(
			ArquivoProcessoEletronicoSearchData searchData) throws DaoException {
		List<ArquivoProcessoEletronico> result = null;

		if (searchData.siglaClasseProcessual != null && searchData.siglaClasseProcessual.trim().length() == 0) {
			searchData.siglaClasseProcessual = null;
		}
		if (searchData.numeroProcessual != null && searchData.numeroProcessual.intValue() == 0) {
			searchData.numeroProcessual = null;
		}
		if (searchData.numeroProtocolo != null && searchData.numeroProtocolo.intValue() == 0) {
			searchData.numeroProtocolo = null;
		}

		if (searchData.anoProtocolo != null && searchData.anoProtocolo.intValue() == 0) {
			searchData.anoProtocolo = null;
		}

		if (searchData.idDocumentoEletronico != null && searchData.idDocumentoEletronico.intValue() == 0) {
			searchData.idDocumentoEletronico = null;
		}

		try {
			Session session = retrieveSession();

			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT ape FROM ArquivoProcessoEletronico ape "
					+ " JOIN FETCH ape.pecaProcessoEletronico "
					+ " JOIN FETCH ape.pecaProcessoEletronico.tipoPecaProcesso "
					+ " JOIN FETCH ape.pecaProcessoEletronico.objetoIncidenteProcesso "
					+ " JOIN FETCH ape.documentoEletronicoView  ");

			if (searchData.numeroProtocolo != null || searchData.anoProtocolo != null) {
				hql.append(" , Protocolo pt ");
			}

			if (searchData.siglaClasseProcessual != null || searchData.numeroProcessual != null || CollectionUtils.isNotEmpty(searchData.listaIdObjetoIncidente)) {
				hql.append(" , Processo pc ");
			}

			hql.append(" WHERE 1=1 ");

			if (searchData.numeroProtocolo != null || searchData.anoProtocolo != null) {
				hql.append(" AND ape.pecaProcessoEletronico.objetoIncidente = pt ");

				if (searchData.numeroProtocolo != null) {
					hql.append(" AND pt.numeroProtocolo = :numeroProtocolo ");
				}

				if (searchData.anoProtocolo != null) {
					hql.append(" AND pt.anoProtocolo = :anoProtocolo ");
				}
			}

			if (searchData.siglaClasseProcessual != null || searchData.numeroProcessual != null || CollectionUtils.isNotEmpty(searchData.listaIdObjetoIncidente)) {
				hql.append(" AND ape.pecaProcessoEletronico.objetoIncidenteProcesso = pc ");

				if (searchData.siglaClasseProcessual != null) {
					hql.append(" AND UPPER(pc.siglaClasseProcessual) = :siglaClasseProcessual ");
				}
				
				if (searchData.numeroProcessual != null) {
					hql.append(" AND pc.numeroProcessual = :numeroProcessual ");
				}
				
				if(CollectionUtils.isNotEmpty(searchData.listaIdObjetoIncidente)){
					hql.append(" AND pc.id IN ( :listaIdObjetoIncidente ) ");
				}				
			}

			if (CollectionUtils.isNotEmpty(searchData.situacoesDocumentoEletronicoIncluidas)) {
				hql.append(" AND ape.documentoEletronicoView.descricaoStatusDocumento IN ( :situacoesDocumentoEletronicoIncluidas ) ");
			}

			if (CollectionUtils.isNotEmpty(searchData.situacoesDocumentoEletronicoExcluidas)) {
				hql.append(" AND ape.documentoEletronicoView.descricaoStatusDocumento NOT IN ( :situacoesDocumentoEletronicoExcluidas ) ");
			}			
			
			hql.append(" ORDER BY ape.pecaProcessoEletronico.numeroOrdemPeca ASC, ");
			hql.append(" ape.pecaProcessoEletronico.dataInclusao ASC, ");
			hql.append(" ape.pecaProcessoEletronico.tipoPecaProcesso.descricao ASC ");

			Query q = session.createQuery(hql.toString());

			if (searchData.numeroProcessual == null && searchData.numeroProtocolo == null) {
				q.setMaxResults(300);
			}

			if (searchData.numeroProtocolo != null) {
				q.setLong("numeroProtocolo", searchData.numeroProtocolo);
			}
			if (searchData.anoProtocolo != null) {
				q.setShort("anoProtocolo", searchData.anoProtocolo);
			}

			if (searchData.siglaClasseProcessual != null) {
				q.setString("siglaClasseProcessual", searchData.siglaClasseProcessual.toUpperCase());
			}
			if (searchData.numeroProcessual != null) {
				q.setLong("numeroProcessual", searchData.numeroProcessual);
			}

			if (CollectionUtils.isNotEmpty(searchData.situacoesDocumentoEletronicoIncluidas)) {
				q.setParameterList("situacoesDocumentoEletronicoIncluidas", searchData.situacoesDocumentoEletronicoIncluidas);
			}

			if (CollectionUtils.isNotEmpty(searchData.situacoesDocumentoEletronicoExcluidas)) {
				q.setParameterList("situacoesDocumentoEletronicoExcluidas", searchData.situacoesDocumentoEletronicoExcluidas);
			}
			
			if (CollectionUtils.isNotEmpty(searchData.listaIdObjetoIncidente)){
				q.setParameterList("listaIdObjetoIncidente", searchData.listaIdObjetoIncidente);
			}
			
			result = q.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return result;
	}

	public ArquivoProcessoEletronico recuperarDocumentoPeca(ObjetoIncidente objetoIncidente, String siglaTipoPecaProcesso) throws DaoException {
		ArquivoProcessoEletronico peca = null;
		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT ape FROM ArquivoProcessoEletronico ape ");
			hql.append(" WHERE ape.pecaProcessoEletronico.objetoIncidente.id = :objetoIncidente ");
			hql.append(" AND ape.pecaProcessoEletronico.tipoPecaProcesso.sigla = :sigla ");
			hql.append(" AND ape.pecaProcessoEletronico.tipoSituacaoPeca <> :tipoSituacaoPeca ");
			hql.append(" AND ape.pecaProcessoEletronico.id = (SELECT max(ape.pecaProcessoEletronico.id) ");
			hql.append(" FROM ArquivoProcessoEletronico ape WHERE ape.pecaProcessoEletronico.objetoIncidente.id = :objetoIncidente ");
			hql.append( "AND ape.pecaProcessoEletronico.tipoPecaProcesso.sigla = :sigla ");
			hql.append(" AND ape.pecaProcessoEletronico.tipoSituacaoPeca <> :tipoSituacaoPeca )");

			Query q = session.createQuery(hql.toString());
			q.setLong("objetoIncidente", objetoIncidente.getId());
			q.setString("sigla", siglaTipoPecaProcesso);
			q.setParameter("tipoSituacaoPeca",
					TipoSituacaoPeca.EXCLUIDA.getCodigo());

			peca = (ArquivoProcessoEletronico) q.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return peca;
	}

	public ArquivoProcessoEletronico recuperarDocumentoPeca(String siglaClasse,
			Long numeroProcesso, Long codigoRecurso, String tipoJulgamento,
			String siglaTipoPecaProcesso) throws DaoException {
		ArquivoProcessoEletronico peca = null;
		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT ape FROM ArquivoProcessoEletronico ape ");
			hql.append(" WHERE ape.pecaProcessoEletronico.processo.id.numeroProcessual = :numeroProcesso ");
			hql.append(" AND ape.pecaProcessoEletronico.processo.id.siglaClasseProcessual = :siglaClasse ");
			hql.append(" AND ape.pecaProcessoEletronico.tipoRecurso.id = :codigoRecurso ");
			hql.append(" AND ape.pecaProcessoEletronico.tipoJulgamento.id = :tipoJulgamento ");
			hql.append(" AND ape.pecaProcessoEletronico.tipoPecaProcesso.sigla = :siglaTipoPecaProcesso ");
			hql.append(" AND ape.pecaProcessoEletronico.tipoSituacaoPeca :tipoSituacaoPeca ");

			Query q = session.createQuery(hql.toString());
			q.setLong("numeroProcesso", numeroProcesso);
			q.setString("siglaClasse", siglaClasse);
			q.setLong("codigoRecurso", codigoRecurso);
			q.setString("tipoJulgamento", tipoJulgamento);
			q.setString("siglaTipoPecaProcesso", siglaTipoPecaProcesso);
			q.setParameter("tipoSituacaoPeca", TipoSituacaoPeca.EXCLUIDA);

			peca = (ArquivoProcessoEletronico) q.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return peca;
	}

	public ArquivoProcessoEletronico recuperarArquivoDoDocumentoTexto(
			DocumentoTexto documentoTexto) {
		Criteria criteria = getSession().createCriteria(
				ArquivoProcessoEletronico.class, "ape");
		Criteria criteriaJoin = criteria.createCriteria(
				"documentoEletronicoView", "de");
		criteriaJoin.add(Restrictions.eq("id", documentoTexto
				.getDocumentoEletronicoView().getId()));
		return (ArquivoProcessoEletronico) criteria.uniqueResult();
	}

	public List<ArquivoProcessoEletronico> pesquisarPecas(
			ObjetoIncidente objetoIncidente, String... siglaTipoPeca)
			throws DaoException {

		List<ArquivoProcessoEletronico> pecas = null;
		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT ape FROM ArquivoProcessoEletronico ape ");
			hql.append(" WHERE ape.pecaProcessoEletronico.objetoIncidente.id = :objetoIncidente ");
			if (siglaTipoPeca != null && siglaTipoPeca.length > 0) {
				hql.append(" AND ape.pecaProcessoEletronico.tipoPecaProcesso.sigla IN :siglas ");
			}
			hql.append(" AND ape.pecaProcessoEletronico.tipoSituacaoPeca <> :tipoSituacaoPeca ");

			Query q = session.createQuery(hql.toString());
			q.setLong("objetoIncidente", objetoIncidente.getId());
			if (siglaTipoPeca != null && siglaTipoPeca.length > 0) {
				q.setParameterList("siglas", siglaTipoPeca);
			}
			q.setParameter("tipoSituacaoPeca",
					TipoSituacaoPeca.EXCLUIDA.getCodigo());

			pecas = q.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}

		return pecas;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ArquivoProcessoEletronico> pesquisarPecasPeloIdObjetoIncidente(
			Long idObjetoIncidentePrincipal) throws DaoException {
		List<ArquivoProcessoEletronico> pecas = null;

		List<Long> listaTipoSituacacaoPeca = new ArrayList<Long>();
		listaTipoSituacacaoPeca.add(TipoSituacaoPeca.EXCLUIDA.getCodigo());
		listaTipoSituacacaoPeca.add(TipoSituacaoPeca.PENDENTE.getCodigo());

		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT ape FROM ArquivoProcessoEletronico ape ");
			hql.append(" WHERE ape.pecaProcessoEletronico.objetoIncidente.principal = :idobjetoIncidente ");
			hql.append(" AND ape.pecaProcessoEletronico.tipoSituacaoPeca NOT IN (:tipoSituacaoPeca) ");

			Query q = session.createQuery(hql.toString());

			q.setLong("idobjetoIncidente", idObjetoIncidentePrincipal);
			q.setParameterList("tipoSituacaoPeca", listaTipoSituacacaoPeca);

			pecas = q.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}

		return pecas;
	}

	public List<ArquivoProcessoEletronico> pesquisarPecasPeloIdObjetoIncidente(
			List<ObjetoIncidente<?>> listaIdsObjetoIncidente)
			throws DaoException {

		List<ArquivoProcessoEletronico> pecas = null;
		List<Long> listaTipoSituacacaoPeca = new ArrayList<Long>();
		listaTipoSituacacaoPeca.add(TipoSituacaoPeca.EXCLUIDA.getCodigo());
		listaTipoSituacacaoPeca.add(TipoSituacaoPeca.PENDENTE.getCodigo());
		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT ape FROM ArquivoProcessoEletronico ape ");
			hql.append(" WHERE ape.pecaProcessoEletronico.objetoIncidente IN (:idobjetoIncidente) ");
			hql.append(" AND ape.pecaProcessoEletronico.tipoSituacaoPeca NOT IN (:tipoSituacaoPeca) ");

			Query q = session.createQuery(hql.toString());

			q.setParameterList("idobjetoIncidente", listaIdsObjetoIncidente);
			q.setParameterList("tipoSituacaoPeca", listaTipoSituacacaoPeca);

			pecas = q.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}

		return pecas;
	}

	public List<Long> pesquisarPecasSetor(Long codSetor,
			List<String> tiposAcesso, Boolean gabineteSEJ, String siglaClasse,
			Long numeroProcesso) throws DaoException {
		List<Long> idsDocumentosSetor = null;

		List<Long> listaTipoSituacacaoPeca = new ArrayList<Long>();
		listaTipoSituacacaoPeca.add(TipoSituacaoPeca.EXCLUIDA.getCodigo());
		listaTipoSituacacaoPeca.add(TipoSituacaoPeca.PENDENTE.getCodigo());

		try {
			Session session = retrieveSession();
			String sql = "SELECT ape.seq_arq_processo_eletronico "
					+ "FROM doc.vw_documento documento, judiciario.processo processo,"
					+ "stf.arquivo_processo_eletronico ape, "
					+ "stf.peca_processo_eletronico ppe "
					+ "WHERE ape.seq_documento = documento.seq_documento "
					+ "AND ape.seq_peca_proc_eletronico = ppe.seq_peca_proc_eletronico "
					+ "AND ppe.seq_tipo_peca NOT IN (:tiposSituacaoPeca) "
					+ "AND documento.tip_acesso IN (:tiposAcesso) "
					+ "AND processo.seq_objeto_incidente = ppe.seq_objeto_incidente";

			if (siglaClasse != null && siglaClasse.trim().length() > 0) {
				sql = sql
						+ " AND upper(processo.sig_classe_proces) = :siglaClasse";
			}

			if (numeroProcesso != null) {
				sql = sql + " AND processo.num_processo = :numeroProcesso";
			}

			if (!gabineteSEJ) {
				sql = sql + " AND ppe.seq_objeto_incidente IN ( "
						+ "SELECT p.seq_objeto_incidente "
						+ "FROM judiciario.processo p "
						+ "WHERE p.sig_classe_proces IN ( "
						+ "SELECT m.sig_classe "
						+ "FROM judiciario.mapeamento_classe_setor m "
						+ "WHERE m.cod_setor = :codSetor) " + "AND ( " +
						/*
						 * ------------------------------------------------------
						 * ------------------------------
						 */
						/*
						 * Se houver apenas 1 setor na tabela de mapeamento,
						 * então é o setor em questão. ------
						 */
						/*
						 * ------------------------------------------------------
						 * ------------------------------
						 */
						"(SELECT COUNT (*) "
						+ "FROM judiciario.mapeamento_classe_setor m2 "
						+ "WHERE p.sig_classe_proces = m2.sig_classe) = 1 "
						+ "OR ( " +
						/*
						 * ------------------------------------------------------
						 * ------------------------------
						 */
						/*
						 * Se a preferência para o setor for NULL e a
						 * preferência do processo não estiver na lista.
						 */
						/*
						 * ------------------------------------------------------
						 * ------------------------------
						 */
						"(SELECT COUNT(*) "
						+ "FROM judiciario.mapeamento_classe_setor m3 "
						+ "WHERE m3.cod_setor = :codSetor "
						+ "AND m3.sig_classe = p.sig_classe_proces AND m3.seq_tipo_preferencia IS NULL) = 1 "
						+ "AND 2 NOT IN ( " + "SELECT seq_tipo_preferencia "
						+ "FROM judiciario.incidente_preferencia ip "
						+ "WHERE ip.seq_objeto_incidente = "
						+ "p.seq_objeto_incidente) " + ") " + "OR ( " +
						/*
						 * ------------------------------------------------------
						 * ------------------------------
						 */
						/*
						 * A preferência do setor é a preferência do processo.
						 * --------------------------------
						 */
						/*
						 * ------------------------------------------------------
						 * ------------------------------
						 */
						"(SELECT COUNT(*) "
						+ "FROM judiciario.mapeamento_classe_setor m4 INNER JOIN judiciario.incidente_preferencia ip2 "
						+ "ON ip2.seq_tipo_preferencia = m4.seq_tipo_preferencia " 
						+ "WHERE m4.cod_setor = :codSetor "
						+ "AND m4.sig_classe = p.sig_classe_proces "
						+ "AND ip2.seq_objeto_incidente = p.seq_objeto_incidente) = 1 " + "))) ";
			}

			// Quantidade de peças ao pesquisar. IMPLICA NO DESEMPENHO DO
			// SISTEMA.

			sql = sql + " AND ROWNUM < 201 ORDER BY processo.sig_classe_proces";

			Query q = session.createSQLQuery(sql);
			q.setParameterList("tiposSituacaoPeca", listaTipoSituacacaoPeca);
			q.setParameterList("tiposAcesso", tiposAcesso);

			if (siglaClasse != null && siglaClasse.trim().length() > 0) {
				q.setParameter("siglaClasse", siglaClasse);
			}

			if (numeroProcesso != null) {
				q.setParameter("numeroProcesso", numeroProcesso);
			}

			if (!gabineteSEJ) {
				q.setParameter("codSetor", codSetor);
			}

			idsDocumentosSetor = q.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return idsDocumentosSetor;
	}

	public List<Long> pesquisarPecasSetor(Long codSetor,
			List<String> tiposAcesso) throws DaoException {
		List<Long> idsDocumentosSetor = null;

		List<Long> listaTipoSituacacaoPeca = new ArrayList<Long>();
		listaTipoSituacacaoPeca.add(TipoSituacaoPeca.EXCLUIDA.getCodigo());
		listaTipoSituacacaoPeca.add(TipoSituacaoPeca.PENDENTE.getCodigo());

		try {
			Session session = retrieveSession();
			String sql = "SELECT ape.seq_arq_processo_eletronico "
					+ "FROM doc.vw_documento documento, "
					+ "stf.arquivo_processo_eletronico ape, "
					+ "stf.peca_processo_eletronico ppe "
					+ "WHERE ape.seq_documento = documento.seq_documento "
					+ "AND ape.seq_peca_proc_eletronico = ppe.seq_peca_proc_eletronico "
					+ "AND ppe.seq_tipo_peca NOT IN (:tiposSituacaoPeca) "
					+ "AND documento.tip_acesso IN (:tiposAcesso) "
					+ "AND ppe.seq_objeto_incidente IN ( "
					+ "SELECT p.seq_objeto_incidente "
					+ "FROM judiciario.processo p "
					+ "WHERE p.sig_classe_proces IN ( "
					+ "SELECT m.sig_classe "
					+ "FROM judiciario.mapeamento_classe_setor m "
					+ "WHERE m.cod_setor = :codSetor) " + "AND ( " +
					/*
					 * ----------------------------------------------------------
					 * --------------------------
					 */
					/*
					 * Se houver apenas 1 setor na tabela de mapeamento, então é
					 * o setor em questão. ------
					 */
					/*
					 * ----------------------------------------------------------
					 * --------------------------
					 */
					"(SELECT COUNT (*) "
					+ "FROM judiciario.mapeamento_classe_setor m2 "
					+ "WHERE p.sig_classe_proces = m2.sig_classe) = 1 "
					+ "OR ( " +
					/*
					 * ----------------------------------------------------------
					 * --------------------------
					 */
					/*
					 * Se a preferência para o setor for NULL e a preferência do
					 * processo não estiver na lista.
					 */
					/*
					 * ----------------------------------------------------------
					 * --------------------------
					 */
					"(SELECT seq_tipo_preferencia "
					+ "FROM judiciario.mapeamento_classe_setor m3 "
					+ "WHERE m3.cod_setor = :codSetor "
					+ "AND m3.sig_classe = p.sig_classe_proces) IS NULL "
					+ "AND 2 NOT IN ( " + "SELECT seq_tipo_preferencia "
					+ "FROM judiciario.incidente_preferencia ip "
					+ "WHERE ip.seq_objeto_incidente = "
					+ "p.seq_objeto_incidente) " + ") " + "OR ( " +
					/*
					 * ----------------------------------------------------------
					 * --------------------------
					 */
					/*
					 * A preferência do setor é a preferência do processo.
					 * --------------------------------
					 */
					/*
					 * ----------------------------------------------------------
					 * --------------------------
					 */
					"(SELECT seq_tipo_preferencia "
					+ "FROM judiciario.mapeamento_classe_setor m4 "
					+ "WHERE m4.cod_setor = :codSetor "
					+ "AND m4.sig_classe = p.sig_classe_proces) IN ( "
					+ "SELECT seq_tipo_preferencia "
					+ "FROM judiciario.incidente_preferencia ip2 "
					+ "WHERE ip2.seq_objeto_incidente = "
					+ "p.seq_objeto_incidente) " + ") " + ")) AND ROWNUM < 201";

			Query q = session.createSQLQuery(sql);
			q.setParameterList("tiposSituacaoPeca", listaTipoSituacacaoPeca);
			q.setParameterList("tiposAcesso", tiposAcesso);
			q.setParameter("codSetor", codSetor);

			idsDocumentosSetor = q.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return idsDocumentosSetor;
	}

	public List<ArquivoProcessoEletronico> pesquisar(List<Long> idsArquivos)
			throws DaoException {
		List<ArquivoProcessoEletronico> arquivos = null;
		try {
			Session session = retrieveSession();
			// String hql =
			// "FROM ArquivoProcessoEletronico arquivo WHERE arquivo.id IN ( :idsArquivos ) ORDER BY arquivo.id DESC";
			String hql = "SELECT arquivo from ArquivoProcessoEletronico arquivo where arquivo.id IN (:idsArquivos) ORDER BY arquivo.id desc ";
			Query q = session.createQuery(hql);
			q.setParameterList("idsArquivos", idsArquivos);
			arquivos = q.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}

		return arquivos;
	}

	@Override
	public int countPecasProcesso(Processo processo) throws DaoException {

		Integer numeroPecas = null;
		String sql = "SELECT COUNT(*) "
				+ "FROM judiciario.processo a, "
				+ "stf.peca_processo_eletronico p, "
				+ "stf.arquivo_processo_eletronico ape, "
				+ "doc.vw_documento d "
				+ "WHERE a.num_processo = :numeroProcesso "
				+ "AND a.sig_classe_proces = :sigla "
				+ "AND a.seq_objeto_incidente = p.seq_objeto_incidente "
				+ "AND p.seq_tipo_situacao_peca = :tipoSituacao "
				+ "AND p.seq_peca_proc_eletronico = ape.seq_peca_proc_eletronico "
				+ "AND ape.seq_documento = d.seq_documento ";

		try {
			Session session = retrieveSession();
			Query q = session.createSQLQuery(sql);
			q.setParameter("numeroProcesso", processo.getNumeroProcessual());
			q.setParameter("sigla", processo.getSiglaClasseProcessual());
			q.setParameter("tipoSituacao", TipoSituacaoPeca.JUNTADA.getCodigo());
			numeroPecas = ((BigDecimal) q.list().get(0)).intValue();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return numeroPecas;
	}

	@Override
	public List<PecaProcessoEletronico> pesquisarPecasPelosDocumentos(
			List<Long> listaSeqDocumento) throws DaoException {

		List<PecaProcessoEletronico> pecas = null;
		try {
			Session session = retrieveSession();

			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT ape.pecaProcessoEletronico FROM ArquivoProcessoEletronico ape ");
			hql.append(" WHERE (1=1) ");
			if (listaSeqDocumento != null && listaSeqDocumento.size() > 0) {
				hql.append(" AND ape.documentoEletronicoView.id IN (:seqsDocumento) ");
			}

			Query q = session.createQuery(hql.toString());
			q.setParameterList("seqsDocumento", listaSeqDocumento);

			pecas = q.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return pecas;
	}

	@Override
	public List<ArquivoProcessoEletronico> recuperarDocumentosPeca(Long id) throws DaoException {
		List<ArquivoProcessoEletronico> arquivosProcessoEletronico = null;
		try {
			Session session = retrieveSession();

			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT ape FROM ArquivoProcessoEletronico ape WHERE ape.pecaProcessoEletronico.id = :idPecaProcesso");
			
			Query q = session.createQuery(hql.toString());
			q.setParameter("idPecaProcesso", id);

			arquivosProcessoEletronico = q.list();
			session.flush();
		} catch (Exception e) {
			throw new DaoException(e);
		}

		return arquivosProcessoEletronico;
	}


}
