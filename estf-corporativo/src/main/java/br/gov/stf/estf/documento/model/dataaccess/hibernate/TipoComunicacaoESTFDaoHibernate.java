package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.TipoComunicacaoESTFDao;
import br.gov.stf.estf.entidade.documento.TipoComunicacaoESTF;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TipoComunicacaoESTFDaoHibernate extends
		GenericHibernateDao<TipoComunicacaoESTF, Integer> implements
		TipoComunicacaoESTFDao {

	private static final long serialVersionUID = 8499809028399160326L;

	public TipoComunicacaoESTFDaoHibernate() {
		super(TipoComunicacaoESTF.class);

	}

	@Override
	public List<TipoComunicacaoESTF> pesquisarTodos() throws DaoException {
		List<TipoComunicacaoESTF> lista = new ArrayList<TipoComunicacaoESTF>();
		try {
			Session session = retrieveSession();

			StringBuilder hql = new StringBuilder();
			hql.append("SELECT tce FROM TipoComunicacaoESTF tce ");
			hql.append("WHERE 1=1 ");

			Query q = session.createQuery(hql.toString());

			lista = q.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return lista;
	}

	@Override
	public List<TipoComunicacaoESTF> pesquisarPorTipo(Integer[] tipos)
			throws DaoException {
		List<TipoComunicacaoESTF> lista = new ArrayList<TipoComunicacaoESTF>();
		try {
			Session session = retrieveSession();

			StringBuilder hql = new StringBuilder();
			hql.append("SELECT tce FROM TipoComunicacaoESTF tce ");
			hql.append("WHERE tce.id IN ( :tipos ) ");

			Query q = session.createQuery(hql.toString());

			q.setParameterList("tipos", tipos);
			
			lista = q.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return lista;
	}

}
