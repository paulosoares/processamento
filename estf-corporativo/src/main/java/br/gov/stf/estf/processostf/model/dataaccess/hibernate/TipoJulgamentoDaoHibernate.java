/**
 * 
 */
package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.TipoJulgamento;
import br.gov.stf.estf.processostf.model.dataaccess.TipoJulgamentoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Paulo.Estevao
 * @since 25.11.2010
 */
@Repository
public class TipoJulgamentoDaoHibernate extends GenericHibernateDao<TipoJulgamento, String> implements TipoJulgamentoDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TipoJulgamentoDaoHibernate() {
		super(TipoJulgamento.class);
	}

	@Override
	public TipoJulgamento recuperarTipoJulgamento(Long seqTipoRecurso, Long sequenciaCadeia) throws DaoException {
		Session session = retrieveSession();
		TipoJulgamento resultado = null;

		try {
			Criteria c = session.createCriteria(TipoJulgamento.class);

			if (seqTipoRecurso != null)
				c.add(Restrictions.eq("tipoRecurso.id", seqTipoRecurso));

			if (sequenciaCadeia != null)
				c.add(Restrictions.eq("sequenciaCadeia", sequenciaCadeia));
			
			c.add(Restrictions.not(Restrictions.like("id", "%-%")));
			
			resultado = (TipoJulgamento) c.uniqueResult();

		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return resultado;
	}
	
}
