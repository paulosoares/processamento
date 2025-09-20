package br.gov.stf.estf.julgamento.model.dataaccess.hibernate;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.julgamento.TipoVoto;
import br.gov.stf.estf.julgamento.model.dataaccess.TipoVotoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TipoVotoDaoHibernate extends GenericHibernateDao<TipoVoto, String> implements TipoVotoDao {

	private static final long serialVersionUID = 1L;

	public TipoVotoDaoHibernate() {
		super(TipoVoto.class);
	}
	
	@Override
	public TipoVoto recuperarTipoVoto(String descricao) throws DaoException {
		try {
			Session session = retrieveSession();
			
			StringBuffer hql = new StringBuffer("select tp FROM TipoVoto tp ");
			hql.append(" WHERE tp.descricao = :descricao ");
			
			Query q = session.createQuery(hql.toString());
			q.setParameter("descricao", descricao);
			
			return (TipoVoto) q.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
}
