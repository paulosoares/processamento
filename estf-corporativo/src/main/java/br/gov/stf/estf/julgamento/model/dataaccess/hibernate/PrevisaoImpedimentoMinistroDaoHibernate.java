package br.gov.stf.estf.julgamento.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.julgamento.PrevisaoImpedimentoMinistro;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.processostf.model.dataaccess.PrevisaoImpedimentoMinistroDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class PrevisaoImpedimentoMinistroDaoHibernate extends GenericHibernateDao<PrevisaoImpedimentoMinistro, Long> implements PrevisaoImpedimentoMinistroDao {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PrevisaoImpedimentoMinistroDaoHibernate() {
		super(PrevisaoImpedimentoMinistro.class);
	}
	
	@Override
	public List<PrevisaoImpedimentoMinistro> recuperar(ObjetoIncidente<?> objetoIncidente) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(PrevisaoImpedimentoMinistro.class);
			c.add(Restrictions.eq("id.objetoIncidente.id", objetoIncidente.getId()));
			return c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public PrevisaoImpedimentoMinistro recuperar(Ministro ministro, ObjetoIncidente<?> objetoIncidente) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(PrevisaoImpedimentoMinistro.class);
			c.add(Restrictions.eq("id.objetoIncidente.id", objetoIncidente.getId()));
			c.add(Restrictions.eq("id.ministro.id", ministro.getId()));
			return (PrevisaoImpedimentoMinistro) c.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}


}
