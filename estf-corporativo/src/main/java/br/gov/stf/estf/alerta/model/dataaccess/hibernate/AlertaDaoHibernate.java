package br.gov.stf.estf.alerta.model.dataaccess.hibernate;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.alerta.model.dataaccess.AlertaDao;
import br.gov.stf.estf.alerta.model.util.AlertaSearchData;
import br.gov.stf.estf.entidade.alerta.Alerta;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.model.util.TipoOrdem;
import br.gov.stf.estf.model.util.TipoOrdemProcesso;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.util.DateTimeHelper;
import br.gov.stf.framework.util.FWConfig;
import br.gov.stf.framework.util.SearchData;

@Repository
public class AlertaDaoHibernate extends GenericHibernateDao<Alerta, Long> implements AlertaDao {

	private static final long serialVersionUID = 1L;

	public AlertaDaoHibernate() {
		super(Alerta.class);
	}

	protected Query createAlertaQuery(AlertaSearchData searchData, Boolean count) throws DaoException {

		Session session = retrieveSession();
		StringBuffer hql = new StringBuffer();

		hql.append(" SELECT ");

		if (count != null && !count)
			hql.append(" a ");
		else
			hql.append(" COUNT(a.id) ");

		hql.append(" FROM Alerta a " + "  JOIN a.objetoIncidente oi, UsuarioCorporativo u ");

		if (searchData.andamento != null)
			hql.append(" JOIN a.tipoAndamento ad ");

		if (searchData.idSetor != null)
			hql.append(" JOIN a.setor s ");

		hql.append(" WHERE u.sigla = a.usuario.id ");

		if ((AlertaSearchData.maiorQueZero(searchData.anoProtocolo)) || (AlertaSearchData.maiorQueZero(searchData.numeroProtocolo))
				|| (AlertaSearchData.stringNotEmpty(searchData.siglaClasseProcessual)) || (AlertaSearchData.maiorQueZero(searchData.numeroProcessual)))
			hql.append(" AND ( EXISTS ");

		if (AlertaSearchData.stringNotEmpty(searchData.siglaClasseProcessual) || (AlertaSearchData.maiorQueZero(searchData.numeroProcessual))) {
			hql.append(" ( SELECT p.id FROM Processo p WHERE p.id = oi.principal.id ");

			if (AlertaSearchData.stringNotEmpty(searchData.siglaClasseProcessual))
				hql.append(" AND p.siglaClasseProcessual = :siglaClasseProcessual ");

			if (AlertaSearchData.maiorQueZero(searchData.numeroProcessual))
				hql.append(" AND p.numeroProcessual = :numeroProcessual ");

			hql.append(" ) ");

			hql.append(" OR EXISTS ( SELECT rp.id FROM RecursoProcesso rp, Processo rpp WHERE rp.id = oi.id AND rp.principal = rpp ");

			if (AlertaSearchData.stringNotEmpty(searchData.siglaClasseProcessual))
				hql.append(" AND rpp.siglaClasseProcessual = :siglaClasseProcessual ");

			if (AlertaSearchData.maiorQueZero(searchData.numeroProcessual))
				hql.append(" AND rpp.numeroProcessual = :numeroProcessual ");

			hql.append(" ) ");

		} else {
			if ((AlertaSearchData.maiorQueZero(searchData.anoProtocolo)) || (AlertaSearchData.maiorQueZero(searchData.numeroProtocolo))) {
				hql.append(" ( SELECT pr.id FROM Protocolo pr WHERE pr.id = oi.id ");

				if (AlertaSearchData.maiorQueZero(searchData.anoProtocolo))
					hql.append(" AND pr.anoProtocolo = :anoProtocolo ");

				if (AlertaSearchData.maiorQueZero(searchData.numeroProtocolo))
					hql.append(" AND pr.numeroProtocolo = :numeroProtocolo ");

				hql.append(" ) ");

				hql.append(" OR EXISTS ( SELECT pr2.id FROM Protocolo pr2 WHERE pr2.id = oi.anterior.id ");

				if (AlertaSearchData.maiorQueZero(searchData.anoProtocolo))
					hql.append(" AND pr2.anoProtocolo = :anoProtocolo ");

				if (AlertaSearchData.maiorQueZero(searchData.numeroProtocolo))
					hql.append(" AND pr2.numeroProtocolo = :numeroProtocolo ");

				hql.append(" ) ");

				hql.append(" OR EXISTS ( SELECT prrc.id FROM Protocolo prrc, Processo prpc " + "                 WHERE prpc.id = oi.pai.id AND prrc.id = prpc.anterior.id ");

				if (AlertaSearchData.maiorQueZero(searchData.anoProtocolo))
					hql.append(" AND prrc.anoProtocolo = :anoProtocolo ");

				if (AlertaSearchData.maiorQueZero(searchData.numeroProtocolo))
					hql.append(" AND prrc.numeroProtocolo = :numeroProtocolo ");

				hql.append(" ) ");

			}
		}

		if ((AlertaSearchData.maiorQueZero(searchData.anoProtocolo)) || (AlertaSearchData.maiorQueZero(searchData.numeroProtocolo))
				|| (SearchData.stringNotEmpty(searchData.siglaClasseProcessual)) || (AlertaSearchData.maiorQueZero(searchData.numeroProcessual)))
			hql.append(" ) ");

		if (searchData.andamento != null && AlertaSearchData.maiorQueZero(searchData.andamento.getId()))
			hql.append(" AND ad.id = :codAndamento ");

		if (searchData.usuario != null)
			hql.append(" AND u.sigla = :siglaUsuario ");

		if (searchData.dataNotificado != null)
			hql.append(" AND a.dataNotificado between to_date(:dataNotificadoInicial, 'DD/MM/YYYY HH24:MI:SS') and to_date(:dataNotificadoFinal , 'DD/MM/YYYY HH24:MI:SS') ");
		else
			hql.append(" AND a.dataNotificado IS NULL ");

		if (searchData.dataAndamento != null)
			hql.append(" AND a.dataAndamento  between to_date(:dataAndamentoInicial, 'DD/MM/YYYY HH24:MI:SS') and to_date(:dataAndamentoFinal , 'DD/MM/YYYY HH24:MI:SS') ");

		if (searchData.idSetor != null)
			hql.append(" AND s.id = :idSetor ");

		Query query = session.createQuery(hql.toString());

		if (searchData.andamento != null && AlertaSearchData.maiorQueZero(searchData.andamento.getId())) {
			query.setLong("codAndamento", searchData.andamento.getId());
		}

		if (searchData.usuario != null) {
			query.setString("siglaUsuario", searchData.usuario.getId());
		}

		if (searchData.dataNotificado != null) {
			query.setString("dataNotificadoInicial", DateTimeHelper.getDataString(searchData.dataNotificado) + " 00:00:00");
			query.setString("dataNotificadoFinal", DateTimeHelper.getDataString(searchData.dataNotificado) + " 23:59:59");
		}

		if (searchData.dataAndamento != null) {
			query.setString("dataAndamentoInicial", DateTimeHelper.getDataString(searchData.dataAndamento) + " 00:00:00");
			query.setString("dataAndamentoFinal", DateTimeHelper.getDataString(searchData.dataAndamento) + " 23:59:59");
		}

		if (SearchData.stringNotEmpty(searchData.siglaClasseProcessual) || (AlertaSearchData.maiorQueZero(searchData.numeroProcessual))) {
			if (SearchData.stringNotEmpty(searchData.siglaClasseProcessual)) {
				query.setString("siglaClasseProcessual", searchData.siglaClasseProcessual);
			}

			if (AlertaSearchData.maiorQueZero(searchData.numeroProcessual)) {
				query.setLong("numeroProcessual", searchData.numeroProcessual);
			}
		} else if ((AlertaSearchData.maiorQueZero(searchData.anoProtocolo)) || (AlertaSearchData.maiorQueZero(searchData.numeroProtocolo))) {

			if (AlertaSearchData.maiorQueZero(searchData.anoProtocolo)) {
				query.setShort("anoProtocolo", searchData.anoProtocolo);
			}

			if (AlertaSearchData.maiorQueZero(searchData.numeroProtocolo)) {
				query.setLong("numeroProtocolo", searchData.numeroProtocolo);
			}
		}

		if (searchData.idSetor != null) {
			query.setLong("idSetor", searchData.idSetor);
		}

		return query;
	}

	@Deprecated
	@Override
	public List<Alerta> pesquisarAlerta(Andamento andamento, Usuario usuario, Date dataNotificado, Date dataAndamento, Short anoProtocolo, Long numeroProtocolo,
			String siglaClasseProcessual, Long numeroProcessual, Long idSetor, Boolean limitarPesquisa, Boolean oderByProcesso, Boolean oderByProtocolo,
			Boolean oderByDataAndamento, TipoOrdem tipoOrdem) throws DaoException {

		AlertaSearchData searchData = criarAlertaSearchData(andamento, usuario, dataNotificado, dataAndamento, anoProtocolo, numeroProtocolo, siglaClasseProcessual,
				numeroProcessual, idSetor, limitarPesquisa, oderByProcesso, oderByProtocolo, oderByDataAndamento, tipoOrdem);

		return pesquisarAlerta(searchData);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Alerta> pesquisarAlerta(AlertaSearchData searchData) throws DaoException {
		List<Alerta> listaAlerta = null;
		Query query = null;

		try {
			query = createAlertaQuery(searchData, Boolean.FALSE);

			if (AlertaSearchData.isTrue(searchData.limitarPesquisa)) {
				if (getLimiteQuantidadeResultados() > 0)
					query.setMaxResults(getLimiteQuantidadeResultados());
			}

			listaAlerta = query.list();

			if (searchData.tipoOrdemProcesso == TipoOrdemProcesso.PROCESSO) {
				if (searchData.tipoOrdem == TipoOrdem.CRESCENTE) {
					ordenarAlertaPorClasseNumeroCrescente(listaAlerta);
				} else {
					ordenarAlertaPorClasseNumeroDecrescente(listaAlerta);
				}
			} else if (searchData.tipoOrdemProcesso == TipoOrdemProcesso.DT_ANDAMENTO) {
				if (searchData.tipoOrdem == TipoOrdem.CRESCENTE) {
					ordenarAlertaPorClasseNumeroDtAndamentoCrescente(listaAlerta);
				} else {
					ordenarAlertaPorClasseNumeroDtAndamentoDecrescente(listaAlerta);
				}
			} else {
				ordenarAlertaPorClasseNumeroDtAndamentoCrescente(listaAlerta);
			}

		} catch (HibernateException exception) {
			throw new DaoException("HibernateException", convertHibernateAccessException(exception));
		} catch (RuntimeException exception) {
			throw new DaoException("RuntimeException", exception);
		}
		return listaAlerta;
	}

	@Deprecated
	@Override
	public Integer recuperarQuantidadeAlerta(Andamento tipoAndamento, Usuario usuario, Date dataNotificado, Date dataAndamento, Short anoProtocolo, Long numeroProtocolo,
			String siglaClasseProcessual, Long numeroProcessual, Long idSetor) throws DaoException {

		AlertaSearchData searchData = criarAlertaSearchData(tipoAndamento, usuario, dataNotificado, dataAndamento, anoProtocolo, numeroProtocolo, siglaClasseProcessual,
				numeroProcessual, idSetor, Boolean.FALSE, null, null, null, null);
		return recuperarQuantidadeAlerta(searchData);
	}

	@Override
	public Integer recuperarQuantidadeAlerta(AlertaSearchData searchData) throws DaoException {
		Query query = null;
		Integer qtd = null;

		try {
			query = createAlertaQuery(searchData, Boolean.TRUE);
			qtd = Integer.parseInt(query.uniqueResult().toString());
		} catch (HibernateException exception) {
			throw new DaoException("HibernateException", convertHibernateAccessException(exception));
		} catch (RuntimeException exception) {
			throw new DaoException("RuntimeException", exception);
		}

		return qtd;
	}

	private int getLimiteQuantidadeResultados() {
		return FWConfig.getInstance().getMaxQueryResult();
	}

	private AlertaSearchData criarAlertaSearchData(Andamento andamento, Usuario usuario, Date dataNotificado, Date dataAndamento, Short anoProtocolo, Long numeroProtocolo,
			String siglaClasseProcessual, Long numeroProcessual, Long idSetor, Boolean limitarPesquisa, Boolean oderByProcesso, Boolean oderByProtocolo,
			Boolean oderByDataAndamento, TipoOrdem tipoOrdem) {
		
		TipoOrdemProcesso tipoOrdemProcesso = null;
		if (AlertaSearchData.isTrue(oderByProcesso)) {
			tipoOrdemProcesso = TipoOrdemProcesso.PROCESSO;
		} else if (AlertaSearchData.isTrue(oderByDataAndamento)) {
			tipoOrdemProcesso = TipoOrdemProcesso.DT_ANDAMENTO;
		} else if (AlertaSearchData.isTrue(oderByProtocolo)) {
			tipoOrdemProcesso = TipoOrdemProcesso.PROTOCOLO;
		}
		
		AlertaSearchData searchData = new AlertaSearchData(andamento, usuario, dataNotificado, dataAndamento, anoProtocolo, numeroProtocolo, siglaClasseProcessual,
				numeroProcessual, idSetor, tipoOrdemProcesso);
		
		searchData.limitarPesquisa = limitarPesquisa;
		searchData.tipoOrdem = tipoOrdem;
		return searchData;
	}

	/**
	 * Método criado para corrigir o erro do tipo de confidencialidade nas depedências (cadeia do objeto incidente ou na objeto_incidente_anterior). Este erro
	 * ocorre quando o sistema peticionamento cria ou altera um incidente julgamento e não busca o tipo de confidencialidade do objeto incidente pai. Com este
	 * método o sistema egab fará apenas update na tabela altera_usuario, não sendo necessário fazer update na tabela
	 */
	public Boolean gravaAlertaJDBC(Alerta alerta) throws DaoException {
		Boolean alterado = Boolean.FALSE;
		try {
			Connection con = retrieveSession().connection();

			StringBuffer sql = new StringBuffer();
			sql.append(" UPDATE egab.alerta_usuario set dat_notificacao= ?");
			sql.append(" WHERE seq_alerta_usuario = ?");

			PreparedStatement ps = retrieveSession().connection().prepareStatement(sql.toString());

			ps.setDate(1, new java.sql.Date(alerta.getDataNotificado().getTime()));
			ps.setLong(2, alerta.getId());

			ps.executeUpdate();
			ps.close();
			alterado = Boolean.TRUE;

		} catch (SQLException exception) {
			throw new DaoException("SQLException", exception);
		} catch (HibernateException exception) {
			throw new DaoException("HibernateException", convertHibernateAccessException(exception));
		} catch (RuntimeException exception) {
			throw new DaoException("RuntimeException", exception);
		}

		return alterado;
	}
	
	// Novo método criado por muller.mendes
	
	public Boolean gravaTodosAlertaJDBC(Usuario usuario) throws DaoException {
		Boolean alterado = Boolean.FALSE;
		try {
			Connection con = retrieveSession().connection();

			StringBuffer sql = new StringBuffer();
			sql.append(" UPDATE egab.alerta_usuario set dat_notificacao = SYSDATE ");
			sql.append(" WHERE  dat_notificacao  is null and sig_usuario = ?");

			PreparedStatement ps = retrieveSession().connection().prepareStatement(sql.toString());

			ps.setString(1, usuario.getId());

			ps.executeUpdate();
			ps.close();
			alterado = Boolean.TRUE;

		} catch (SQLException exception) {
			throw new DaoException("SQLException", exception);
		} catch (HibernateException exception) {
			throw new DaoException("HibernateException", convertHibernateAccessException(exception));
		} catch (RuntimeException exception) {
			throw new DaoException("RuntimeException", exception);
		}

		return alterado;
	}

	public Boolean persistirAlerta(Alerta alerta) throws DaoException {
		Session session = retrieveSession();
		Boolean alterado = Boolean.FALSE;
		try {
			session.persist(alerta);
			session.flush();

			alterado = Boolean.TRUE;

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return alterado;

	}

	// CLASSES E MÉTODOS UTILITÁRIOS DE ORDENAÇÃO

	public List<Alerta> ordenarAlertaPorClasseNumeroCrescente(List<Alerta> listaAlerta) {
		Collections.sort(listaAlerta, new ClasseComparatorCrescente());
		Collections.sort(listaAlerta, new ClasseNumeroComparatorCrescente());
		return listaAlerta;
	}

	public List<Alerta> ordenarAlertaPorClasseNumeroDecrescente(List<Alerta> listaAlerta) {
		Collections.sort(listaAlerta, new ClasseComparatorDecrescente());
		Collections.sort(listaAlerta, new ClasseNumeroComparatorDecrescente());
		return listaAlerta;
	}

	public List<Alerta> ordenarAlertaPorClasseNumeroDtAndamentoCrescente(List<Alerta> listaAlerta) {
		Collections.sort(listaAlerta, new DtAndamentoComparatorCrescente());
		// Collections.sort(listaAlerta, new ClasseNumeroDtAndamentoComparatorCrescente());
		// Collections.sort(listaAlerta, new ClasseComparatorCrescente());
		// Collections.sort(listaAlerta, new ClasseNumeroComparatorCrescente());
		return listaAlerta;
	}

	public List<Alerta> ordenarAlertaPorClasseNumeroDtAndamentoDecrescente(List<Alerta> listaAlerta) {
		Collections.sort(listaAlerta, new DtAndamentoComparatorDecrescente());
		// Collections.sort(listaAlerta, new ClasseNumeroDtAndamentoComparatorDecrescente());
		// Collections.sort(listaAlerta, new ClasseComparatorDecrescente());
		// Collections.sort(listaAlerta, new ClasseNumeroComparatorDecrescente());
		return listaAlerta;
	}

	private static class ClasseComparatorCrescente implements Comparator {

		public int compare(Object obj1, Object obj2) {

			if (!(obj1 instanceof Alerta) || !(obj2 instanceof Alerta))
				return 0;

			Alerta doc1 = (Alerta) obj1;
			Alerta doc2 = (Alerta) obj2;

			if (doc1 == null || doc2 == null)
				return 0;

			if ((!doc1.getPossuiIndentificacaoProcessual() && !doc2.getPossuiIndentificacaoProcessual())
					&& (!doc1.getPossuiIndentificacaoProcessualProcessoProtocolado() && !doc2.getPossuiIndentificacaoProcessualProcessoProtocolado()))
				return 0;

			if (doc1.getPossuiIndentificacaoProcessual() && doc2.getPossuiIndentificacaoProcessual())
				return doc1.getProcesso().getClasseProcessual().getId().compareTo(doc2.getProcesso().getClasseProcessual().getId());
			else if (doc1.getPossuiIndentificacaoProcessualProcessoProtocolado() && doc2.getPossuiIndentificacaoProcessualProcessoProtocolado()) {
				return doc1.getProtocolo().getSiglaClasseProcessual().compareTo(doc2.getProtocolo().getSiglaClasseProcessual());
			} else
				return 0;
		}
	}

	private static class ClasseComparatorDecrescente implements Comparator {

		public int compare(Object obj1, Object obj2) {

			if (!(obj1 instanceof Alerta) || !(obj2 instanceof Alerta))
				return 0;

			Alerta doc1 = (Alerta) obj1;
			Alerta doc2 = (Alerta) obj2;

			if (doc1 == null || doc2 == null)
				return 0;

			if ((!doc1.getPossuiIndentificacaoProcessual() && !doc2.getPossuiIndentificacaoProcessual())
					&& (!doc1.getPossuiIndentificacaoProcessualProcessoProtocolado() && !doc2.getPossuiIndentificacaoProcessualProcessoProtocolado()))
				return 0;

			if (doc1.getPossuiIndentificacaoProcessual() && doc2.getPossuiIndentificacaoProcessual())
				return doc2.getProcesso().getClasseProcessual().getId().compareTo(doc1.getProcesso().getClasseProcessual().getId());
			else if (doc1.getPossuiIndentificacaoProcessualProcessoProtocolado() && doc2.getPossuiIndentificacaoProcessualProcessoProtocolado()) {
				return doc2.getProtocolo().getSiglaClasseProcessual().compareTo(doc1.getProtocolo().getSiglaClasseProcessual());
			} else
				return 0;
		}
	}

	private static class ClasseNumeroComparatorCrescente implements Comparator {

		public int compare(Object obj1, Object obj2) {

			if (!(obj1 instanceof Alerta) || !(obj2 instanceof Alerta))
				return 0;

			Alerta doc1 = (Alerta) obj1;
			Alerta doc2 = (Alerta) obj2;

			if (doc1 == null || doc2 == null)
				return 0;

			if ((!doc1.getPossuiIndentificacaoProcessual() && !doc2.getPossuiIndentificacaoProcessual())
					&& (!doc1.getPossuiIndentificacaoProcessualProcessoProtocolado() && !doc2.getPossuiIndentificacaoProcessualProcessoProtocolado()))
				return 0;

			if (doc1.getPossuiIndentificacaoProcessual() && doc2.getPossuiIndentificacaoProcessual()) {
				if (doc1.getProcesso().getClasseProcessual().getId().compareTo(doc2.getProcesso().getClasseProcessual().getId()) == 0) {
					return doc1.getProcesso().getNumeroProcessual().compareTo(doc2.getProcesso().getNumeroProcessual());
				} else {
					return doc1.getProcesso().getClasseProcessual().getId().compareTo(doc2.getProcesso().getClasseProcessual().getId());
				}
			} else if (doc1.getPossuiIndentificacaoProcessualProcessoProtocolado() && doc2.getPossuiIndentificacaoProcessualProcessoProtocolado()) {
				if (doc1.getProtocolo().getSiglaClasseProcessual().compareTo(doc2.getProtocolo().getSiglaClasseProcessual()) == 0) {
					return doc1.getProtocolo().getNumeroProcessual().compareTo(doc2.getProtocolo().getNumeroProcessual());
				} else {
					return doc1.getProtocolo().getSiglaClasseProcessual().compareTo(doc2.getProtocolo().getSiglaClasseProcessual());
				}
			} else {
				return 0;
			}
		}
	}

	private static class ClasseNumeroComparatorDecrescente implements Comparator {

		public int compare(Object obj1, Object obj2) {

			if (!(obj1 instanceof Alerta) || !(obj2 instanceof Alerta))
				return 0;

			Alerta doc1 = (Alerta) obj1;
			Alerta doc2 = (Alerta) obj2;

			if (doc1 == null || doc2 == null)
				return 0;

			if ((!doc1.getPossuiIndentificacaoProcessual() && !doc2.getPossuiIndentificacaoProcessual())
					&& (!doc1.getPossuiIndentificacaoProcessualProcessoProtocolado() && !doc2.getPossuiIndentificacaoProcessualProcessoProtocolado()))
				return 0;

			if (doc1.getPossuiIndentificacaoProcessual() && doc2.getPossuiIndentificacaoProcessual()) {
				if (doc1.getProcesso().getClasseProcessual().getId().compareTo(doc2.getProcesso().getClasseProcessual().getId()) == 0) {
					return doc2.getProcesso().getNumeroProcessual().compareTo(doc1.getProcesso().getNumeroProcessual());
				} else {
					return doc2.getProcesso().getClasseProcessual().getId().compareTo(doc1.getProcesso().getClasseProcessual().getId());
				}
			} else if (doc1.getPossuiIndentificacaoProcessualProcessoProtocolado() && doc2.getPossuiIndentificacaoProcessualProcessoProtocolado()) {
				if (doc1.getProtocolo().getSiglaClasseProcessual().compareTo(doc2.getProtocolo().getSiglaClasseProcessual()) == 0) {
					return doc2.getProtocolo().getNumeroProcessual().compareTo(doc1.getProtocolo().getNumeroProcessual());
				} else {
					return doc2.getProtocolo().getSiglaClasseProcessual().compareTo(doc1.getProtocolo().getSiglaClasseProcessual());
				}
			} else {
				return 0;
			}
		}
	}

	public static class DtAndamentoComparatorCrescente implements Comparator, Serializable {

		public int compare(Object obj1, Object obj2) {

			if (!(obj1 instanceof Alerta) || !(obj2 instanceof Alerta))
				return 0;

			Alerta doc1 = (Alerta) obj1;
			Alerta doc2 = (Alerta) obj2;

			if (doc1.getDataAndamento() == null || doc2.getDataAndamento() == null)
				return 0;

			return doc1.getDataAndamento().compareTo(doc2.getDataAndamento());
		}

	}

	public static class DtAndamentoComparatorDecrescente implements Comparator, Serializable {

		public int compare(Object obj1, Object obj2) {

			if (!(obj1 instanceof Alerta) || !(obj2 instanceof Alerta))
				return 0;

			Alerta doc1 = (Alerta) obj1;
			Alerta doc2 = (Alerta) obj2;

			if (doc1.getDataAndamento() == null || doc2.getDataAndamento() == null)
				return 0;

			return doc2.getDataAndamento().compareTo(doc1.getDataAndamento());
		}

	}

}