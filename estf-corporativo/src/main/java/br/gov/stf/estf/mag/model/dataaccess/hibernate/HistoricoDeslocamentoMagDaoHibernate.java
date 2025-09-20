package br.gov.stf.estf.mag.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.mag.HistoricoDeslocamentoMag;
import br.gov.stf.estf.mag.model.dataaccess.HistoricoDeslocamentoMagDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class HistoricoDeslocamentoMagDaoHibernate extends GenericHibernateDao<HistoricoDeslocamentoMag, Long> 
implements HistoricoDeslocamentoMagDao {
	
	public HistoricoDeslocamentoMagDaoHibernate () {
		super(HistoricoDeslocamentoMag.class);
	}

	public HistoricoDeslocamentoMag recuperarHistoricoDeslocamentoMag(Long id) 
	throws DaoException {
		
		HistoricoDeslocamentoMag historicoRecuperado = null;
		
		try {
			
			Session session = retrieveSession();
			
			Criteria criteria = session.createCriteria(HistoricoDeslocamentoMag.class);
			
			criteria.add(Restrictions.eq("id", id));
			
			historicoRecuperado = (HistoricoDeslocamentoMag) criteria.uniqueResult();
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}
		
		return historicoRecuperado;
	}
	
	public List<HistoricoDeslocamentoMag> pesquisarHistoricoDeslocamentoMag(
			Long codigoSetor, String siglaClasseProcessual, Long numeroProcessual) 
	throws DaoException {
		
		List<HistoricoDeslocamentoMag> historicosRecuperados = null;
		
		try {
			
			Session session = retrieveSession();
			
			Criteria criteria = session.createCriteria(HistoricoDeslocamentoMag.class);
			
			if(codigoSetor != null)
				criteria.add(Restrictions.eq("setor.id", codigoSetor));			
			
			if(siglaClasseProcessual != null && siglaClasseProcessual.trim().length() > 0)
				criteria.add(Restrictions.eq("siglaClasseProcessual", siglaClasseProcessual));
			
			if(numeroProcessual != null)
				criteria.add(Restrictions.eq("numeroProcessual", numeroProcessual));
			
			criteria.addOrder(Order.desc("dataRemessa"));
			criteria.addOrder(Order.desc("id"));
			
			historicosRecuperados = criteria.list();
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}
		
		return historicosRecuperados;
	}
}
