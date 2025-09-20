package br.gov.stf.estf.jurisdicionado.model.dataaccess.hibernate;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.jurisdicionado.PapelJurisdicionado;
import br.gov.stf.estf.jurisdicionado.model.dataaccess.PapelJurisdicionadoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class PapelJurisdicionadoDaoHibernate extends GenericHibernateDao<PapelJurisdicionado, Long>
	implements PapelJurisdicionadoDao {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PapelJurisdicionadoDaoHibernate() {
		super(PapelJurisdicionado.class);
	}
	
	@Override
	public PapelJurisdicionado recuperarPorId(Long id) throws DaoException {
		try {
			Session session = retrieveSession();

			Criteria c = session.createCriteria(PapelJurisdicionado.class)
		    	.add( Restrictions.eq("id", id)
				   );
			
			PapelJurisdicionado papel = (PapelJurisdicionado) c.uniqueResult();
			return papel;

		} catch (Exception e) {
			throw new DaoException(e);
		}
		
	}

	

}
