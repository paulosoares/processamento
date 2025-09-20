package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.IncidentePreferencia;
import br.gov.stf.estf.processostf.model.dataaccess.IncidentePreferenciaDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class IncidentePreferenciaDaoHibernate extends GenericHibernateDao<IncidentePreferencia, Long> implements
		IncidentePreferenciaDao {

	public IncidentePreferenciaDaoHibernate() {
		super(IncidentePreferencia.class);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @seebr.gov.stf.estf.processostf.model.dataaccess.hibernate.
	 * IncidentePreferenciaDao#recuperarPreferenciasDoIncidente(java.lang.Long)
	 */
	public List<IncidentePreferencia> recuperarPreferenciasDoIncidente(Long seqObjetoIncidente) throws DaoException {
		String alias = "ip";
		Criteria criteria = retrieveSession().createCriteria(getPersistentClass(), alias);
		criteria.add(Restrictions.eq(alias + ".objetoIncidente.id", seqObjetoIncidente));
		criteria.addOrder(Order.desc(alias + ".tipoPreferencia"));
		return criteria.list();
	}

	public void inserirPreferenciaConvertidoEletronico(IncidentePreferencia incidentePreferencia) throws DaoException {
			
		try{
			Session session = retrieveSession();
		
			session.save(incidentePreferencia);
		
		} catch (HibernateException e) {
				throw new DaoException(e);
		}
	}

	public void removerPreferenciaConvertidoEletronico(IncidentePreferencia incidentePreferencia) throws DaoException {
		
		try{		
			Session session = retrieveSession();
			
			session.delete(incidentePreferencia);			
			
		} catch (HibernateException e) {
				throw new DaoException(e);
		}		
	}
		
}
