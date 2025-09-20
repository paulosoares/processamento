package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.TipoComunicacaoDao;
import br.gov.stf.estf.entidade.documento.TipoComunicacao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TipoComunicacaoDaoHibernate extends GenericHibernateDao<TipoComunicacao, Long> implements TipoComunicacaoDao {

	private static final long serialVersionUID = 4976520521300254046L;

	public TipoComunicacaoDaoHibernate() {
		super(TipoComunicacao.class);
	}

	public TipoComunicacao pesquisarTipoModelo(Long idTipoModelo) throws DaoException {
		try {
			Session session = retrieveSession();

			StringBuilder hql = new StringBuilder("select tc from TipoComunicacao tc");
			hql.append(" WHERE 1=1 ");

			if (idTipoModelo != null && idTipoModelo != 0L) {
				hql.append(" AND tc.id = " + idTipoModelo);
			} 
			
			hql.append(" ORDER BY tc.descricao");

			Query q = session.createQuery(hql.toString());
			return (TipoComunicacao) q.uniqueResult();

		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TipoComunicacao> pesquisarListaTiposModelos(String nomeTipoModelo) throws DaoException {
		List<TipoComunicacao> list = null;

		try {
			Session session = retrieveSession();

			StringBuilder hql = new StringBuilder("SELECT tc FROM TipoComunicacao tc");
			hql.append(" WHERE (1=1)  ");

			if (StringUtils.isNotBlank(nomeTipoModelo)) {
				hql.append(" AND UPPER(tc.descricao) = " + "'" + nomeTipoModelo.toUpperCase() + "'");
			}

			hql.append(" ORDER BY tc.descricao ASC");

			Query q = session.createQuery(hql.toString());

			list = (List<TipoComunicacao>) q.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}

		return list;
	}

	@Override
	public TipoComunicacao pesquisarUnicoTipoModelo(String nomeTipoModelo, Long codigoTipoPermissao) throws DaoException {
		TipoComunicacao tipoComunicacao = null;

		try {
			Session session = retrieveSession();

			StringBuilder hql = new StringBuilder("SELECT tc FROM TipoComunicacao tc");
			hql.append(" WHERE (1=1) ");
			hql.append(" AND UPPER(tc.descricao) = :nomeTipoModelo ");
			hql.append(" AND tc.tipoPermissao = :tipoPermissao ");
			hql.append(" ORDER BY tc.descricao ASC");

			Query q = session.createQuery(hql.toString());

			q.setString("nomeTipoModelo", nomeTipoModelo);
			q.setLong("tipoPermissao", codigoTipoPermissao);

			tipoComunicacao = (TipoComunicacao) q.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}

		return tipoComunicacao;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TipoComunicacao> pesquisarListaTiposModelos(String nomeTipoModelo, Setor setor) throws DaoException {
		List<TipoComunicacao> list = null;

		try {
			Session session = retrieveSession();

			StringBuilder hql = new StringBuilder("SELECT tc FROM TipoComunicacao tc");

			hql.append(" WHERE (1=1)  ");

			if (nomeTipoModelo != null && nomeTipoModelo.trim().length() > 0) {
				hql.append(" AND UPPER(tc.descricao) LIKE ('%" + nomeTipoModelo.toUpperCase() + "%')");
			}

			if (setor != null) {
				hql.append(" AND (tc.tipoPermissao.setor IS NULL ");
				hql.append(" OR tc.tipoPermissao.setor = :setor ) ");
			}

			hql.append(" ORDER BY tc.descricao ASC");

			Query q = session.createQuery(hql.toString());

			if (setor != null) {
				q.setLong("setor", setor.getId());
			}

			list = (List<TipoComunicacao>) q.list();
		} catch (Exception exception) {
			throw new DaoException(exception);
		}

		return list;
	}

	@Override
	public Long pesquisaNumeracaoUnicaModelo(Long idTipoComunicaco) throws DaoException {
		Long numeracaoUnica = null;
	
		try {
			Session session = retrieveSession();

			StringBuilder hql = new StringBuilder("SELECT tc.numeroComunicacaoAnterior FROM TipoComunicacao tc");
			hql.append(" WHERE (1=1) ");
			hql.append(" AND tc.id = :idTipoComunicacao ");

			Query q = session.createQuery(hql.toString());

			q.setLong("idTipoComunicacao", idTipoComunicaco);

			numeracaoUnica = (Long) q.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}

		return numeracaoUnica;
	}
}
