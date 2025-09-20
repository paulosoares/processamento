package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.ClasseConversao;
import br.gov.stf.estf.processostf.model.dataaccess.ClasseConversaoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ClasseConversaoDaoHibernate extends GenericHibernateDao<ClasseConversao, String> implements ClasseConversaoDao { 
    public ClasseConversaoDaoHibernate() {
		super(ClasseConversao.class);
	}

	private static final long serialVersionUID = 1L;
	
    public List pesquisarClasseAntiga() throws DaoException {            
        Session session = retrieveSession();
        List classes = null;
        try {
            Query query = session.createQuery("FROM ClasseConversao");            
            classes = query.list();
        } catch (HibernateException e) {
            throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
        } catch (RuntimeException e) {
            throw new DaoException("RuntimeException", e);
        }
        return classes;
    }
}
