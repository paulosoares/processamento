/**
 * 
 */
package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.ListaProcessos;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidenteListaProcessos;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidenteListaProcessos.ObjetoIncidenteListaProcessosId;
import br.gov.stf.estf.processostf.model.dataaccess.ObjetoIncidenteListaProcessosDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Paulo.Estevao
 * @since 25.05.2011
 */
@Repository
public class ObjetoIncidenteListaProcessosDaoHibernate extends
		GenericHibernateDao<ObjetoIncidenteListaProcessos, ObjetoIncidenteListaProcessosId> implements
		ObjetoIncidenteListaProcessosDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1315173597077666700L;

	public ObjetoIncidenteListaProcessosDaoHibernate() {
		super(ObjetoIncidenteListaProcessos.class);
	}

	@Override
	public ObjetoIncidenteListaProcessos recuperar(
			ListaProcessos listaProcessos, ObjetoIncidente<?> objetoIncidente) throws DaoException {
		Session session;
		
		try {
			session = retrieveSession();
			Criteria c = session.createCriteria(ObjetoIncidenteListaProcessos.class);
			c.add(Restrictions.eq("id.listaProcessos.id", listaProcessos.getId()));
			c.add(Restrictions.eq("id.objetoIncidente.id", objetoIncidente.getId()));
			
			return (ObjetoIncidenteListaProcessos) c.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
