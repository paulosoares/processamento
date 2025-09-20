package br.gov.stf.estf.log.model.dataaccess.hibernate;

import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.log.LogControleProcess;
import br.gov.stf.estf.log.model.dataaccess.LogControleProcessDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;


@Repository("logControleProcessDAO")
public class LogControleProcessDaoHibernate extends GenericHibernateDao<LogControleProcess, Long> implements LogControleProcessDao {

	private static final long serialVersionUID = 1827266187851767500L;

	public LogControleProcessDaoHibernate() {
		super(LogControleProcess.class);
	}
	
	@Override
	public Date findMaxDataByUsuarioExterno(Long seqUsuarioExterno) throws DaoException {
		
		try {
			Session session = retrieveSession();
			
			StringBuffer hql = new StringBuffer();
			hql.append(" select MAX (oEntidade.dataLog) from LogControleProcess oEntidade ");
			hql.append(" where oEntidade.usuarioExternoID.id = :id");
			
			Query q = session.createQuery(hql.toString());
			q.setLong("id", seqUsuarioExterno);
			
			return (Date) q.uniqueResult();
			
		}catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch(RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}
}

