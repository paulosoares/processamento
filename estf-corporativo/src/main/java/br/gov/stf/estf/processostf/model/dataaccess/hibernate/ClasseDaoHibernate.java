package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.Classe;
import br.gov.stf.estf.entidade.processostf.ClasseConversao;
import br.gov.stf.estf.processostf.model.dataaccess.ClasseDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ClasseDaoHibernate extends GenericHibernateDao<Classe, String> implements ClasseDao {

	public ClasseDaoHibernate() {
		super(Classe.class);
	}

	private static final long serialVersionUID = 1L;

	public List<Classe> pesquisarClasseProcessual() throws DaoException {
		return consultaClasse(Classe.class);
	}

	public List<ClasseConversao> pesquisarClasseAntiga() throws DaoException {
		return consultaClasse(ClasseConversao.class);
	}

	private List consultaClasse(Class persistentClass) throws DaoException {
		Session session = retrieveSession();
		try {
			Criteria query = session.createCriteria(persistentClass);
			return query.list();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}

	public Classe pesquisarClassePorSigla(String siglaClasse) throws DaoException {
		Session session = retrieveSession();
		
		Criteria classe = session.createCriteria(Classe.class);
		classe.add(Restrictions.eq("id", siglaClasse).ignoreCase());
		classe.addOrder(Order.asc("id"));
		
		return (Classe) classe.uniqueResult();
	}

	@Override
	public List<Classe> pesquisarClassesAtivas() throws DaoException {
		Session session = retrieveSession();
		Criteria query = session.createCriteria(Classe.class);
		query.add(Restrictions.eq("ativo", true));
		query.addOrder(Order.asc("id"));
	
		return query.list();
	}	

}
