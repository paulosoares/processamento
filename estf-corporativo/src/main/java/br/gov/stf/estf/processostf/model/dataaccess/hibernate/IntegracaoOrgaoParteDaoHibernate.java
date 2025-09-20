package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.processostf.IntegracaoOrgaoParte;
import br.gov.stf.estf.entidade.processostf.IntegracaoOrgaoParte.IntegracaoOrgaoParteId;
import br.gov.stf.estf.processostf.model.dataaccess.IntegracaoOrgaoParteDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class IntegracaoOrgaoParteDaoHibernate extends GenericHibernateDao<IntegracaoOrgaoParte, IntegracaoOrgaoParteId> 
	implements IntegracaoOrgaoParteDao {

	public IntegracaoOrgaoParteDaoHibernate() {
		super(IntegracaoOrgaoParte.class);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4741217875320615162L;

	public IntegracaoOrgaoParte recuperar(Long parte, Origem origem)
			throws DaoException {

		IntegracaoOrgaoParte integracaoOrgaoParte = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(IntegracaoOrgaoParte.class);
			if ( parte!=null ) {
				c.add( Restrictions.eq("id.parte.id", parte) );
			}
			
			if ( origem!=null ) {
				c.add( Restrictions.eq("id.origem", origem) );
			}
			
			integracaoOrgaoParte = (IntegracaoOrgaoParte) c.uniqueResult();
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		
		return integracaoOrgaoParte;
	}

}
