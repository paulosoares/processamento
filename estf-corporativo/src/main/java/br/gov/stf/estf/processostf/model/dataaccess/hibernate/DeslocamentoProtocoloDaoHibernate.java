package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.DeslocaProtocolo;
import br.gov.stf.estf.processostf.model.dataaccess.DeslocamentoProtocoloDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class DeslocamentoProtocoloDaoHibernate extends GenericHibernateDao<DeslocaProtocolo, Long> implements DeslocamentoProtocoloDao {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8726298512651874872L;

	public DeslocamentoProtocoloDaoHibernate() {
		super(DeslocaProtocolo.class);
	}
	
	public void persistirDeslocamentoProtocolo(DeslocaProtocolo deslocamentoProtocolo) 
	throws DaoException {
		
		Session session = retrieveSession();
		
		try {
			session.persist(deslocamentoProtocolo);
			session.flush();
		}
		catch(HibernateException e) {
            throw new DaoException("HibernateException",
                    SessionFactoryUtils.convertHibernateAccessException(e));
        }
        catch( RuntimeException e ) {
            throw new DaoException("RuntimeException", e);
        }            
	}
}
