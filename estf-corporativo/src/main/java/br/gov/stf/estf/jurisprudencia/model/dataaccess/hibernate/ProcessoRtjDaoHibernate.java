/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.jurisprudencia.ProcessoRtj;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.ProcessoRtjDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Hertony.Morais
 * @since 02.05.2013
 */
@Repository
public class ProcessoRtjDaoHibernate extends GenericHibernateDao<ProcessoRtj, Long> implements
		ProcessoRtjDao {

	private static final long serialVersionUID = 5904136024911531681L;

	public ProcessoRtjDaoHibernate() {
		super(ProcessoRtj.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProcessoRtj> pesquisarRtjPorObjetoIncidente(ObjetoIncidente<ObjetoIncidente<?>> objetoIncidente)	throws DaoException {
		try {
			Session session = retrieveSession();
			
			Criteria c = session.createCriteria(ProcessoRtj.class);
			c.add(Restrictions.eq("objetoIncidente.id", objetoIncidente.getId()));
			c.add(Restrictions.eq("fechamento", true));
			c.add(Restrictions.not(Restrictions.eq("volume", "TRAB")));
			c.add(Restrictions.not(Restrictions.eq("volume", "LIX")));

			return c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
}
