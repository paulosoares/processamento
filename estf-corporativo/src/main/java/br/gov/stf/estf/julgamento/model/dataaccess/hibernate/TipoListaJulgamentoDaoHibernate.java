package br.gov.stf.estf.julgamento.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.julgamento.TipoListaJulgamento;
import br.gov.stf.estf.julgamento.model.dataaccess.TipoListaJulgamentoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TipoListaJulgamentoDaoHibernate extends GenericHibernateDao<TipoListaJulgamento, Long> implements TipoListaJulgamentoDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3331057537592313131L;

	public TipoListaJulgamentoDaoHibernate() {
		super(TipoListaJulgamento.class);
	}
	
	@Override
	public List<TipoListaJulgamento> recuperarTipoListaJulgamentoAtivas()	throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(TipoListaJulgamento.class);
			c.add(Restrictions.eq("ativo", Boolean.TRUE));
			return c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
}
