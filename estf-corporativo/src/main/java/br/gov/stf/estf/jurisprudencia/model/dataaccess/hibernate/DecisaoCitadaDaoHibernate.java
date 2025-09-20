/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess.hibernate;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.jurisprudencia.DecisaoCitada;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.DecisaoCitadaDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Paulo.Estevao
 * @since 17.10.2012
 */
@Repository
public class DecisaoCitadaDaoHibernate extends GenericHibernateDao<DecisaoCitada, Long> implements DecisaoCitadaDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5430775127869740776L;

	public DecisaoCitadaDaoHibernate() {
		super(DecisaoCitada.class);
	}
	
	@Override
	public DecisaoCitada alterar(DecisaoCitada decisaoCitada) throws DaoException {
		Session session = retrieveSession();
		try {
			session.saveOrUpdate(decisaoCitada);
		} catch (Exception e) {
			throw new DaoException(e.getMessage(), e);
		}
		return decisaoCitada;
	}
}
