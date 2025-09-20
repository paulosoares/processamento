package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.ProcessoPlenario;
import br.gov.stf.estf.processostf.model.dataaccess.ProcessoPlenarioDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ProcessoPlenarioDaoHibernate extends GenericHibernateDao<ProcessoPlenario, Long> implements ProcessoPlenarioDao {

	private static final long serialVersionUID = 1L;

	public ProcessoPlenarioDaoHibernate() {
		super(ProcessoPlenario.class);
	}

	@Override
	public ProcessoPlenario recuperarPorObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		try {
			Query query = retrieveSession().createQuery("select pp from ProcessoPlenario pp where pp.objetoIncidente = ?");
			query.setParameter(0, objetoIncidente);
			return (ProcessoPlenario) query.uniqueResult();
		} catch (HibernateException e) {
			throw new RuntimeException(e);
		} catch (DaoException e) {
			throw new RuntimeException(e);
		}
	}

}
