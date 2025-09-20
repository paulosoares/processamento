package br.gov.stf.estf.processosetor.model.dataaccess.hibernate;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processosetor.TarefaProcesso;
import br.gov.stf.estf.processosetor.model.dataaccess.TarefaProcessoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TarefaProcessoDaoHibernate extends GenericHibernateDao<TarefaProcesso, Long> 
implements TarefaProcessoDao {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4351312279019308967L;

	public TarefaProcessoDaoHibernate () {
		super(TarefaProcesso.class);
	}

	public TarefaProcesso recuperarTarefaProcesso(Long id)
	throws DaoException {
		
		Session sessao = retrieveSession();
		
		TarefaProcesso tarefaProcessoRecuperada = null;
		
		try
		{
			Criteria criteria = sessao.createCriteria(TarefaProcesso.class);
			
			criteria.add(Restrictions.eq("id", id));
			
			tarefaProcessoRecuperada = (TarefaProcesso) criteria.uniqueResult();
		}
		catch(HibernateException e)
		{
			throw new DaoException(e);
		}
		
		return tarefaProcessoRecuperada;
	}

}
