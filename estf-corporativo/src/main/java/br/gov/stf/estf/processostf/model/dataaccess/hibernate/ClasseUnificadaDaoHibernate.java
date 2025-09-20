package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.ClasseUnificada;
import br.gov.stf.estf.processostf.model.dataaccess.ClasseUnificadaDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ClasseUnificadaDaoHibernate 
			extends GenericHibernateDao<ClasseUnificada, ClasseUnificada.ClasseUnificadaId>
			implements ClasseUnificadaDao {
	
	public ClasseUnificadaDaoHibernate() {
		super(ClasseUnificada.class);
	}

	private static final long serialVersionUID = 6486263806674274886L;

	public Short recuperarCodigoClasseUnificada(String sigClasse,
			String tipJulgamento, Long codRecurso) throws DaoException {
		Short codClasse = null;
		
		try {
			Session session = retrieveSession();
			
			Criteria c = session.createCriteria(ClasseUnificada.class);
			
			c.setProjection( Projections.property("codigoClasse") );
			c.add( Restrictions.eq("id.sigla", sigClasse) );
			c.add( Restrictions.eq("id.tipoJulgamento", tipJulgamento) );
			c.add( Restrictions.eq("id.codigoRecurso", codRecurso) );
			
			codClasse =  (Short) c.uniqueResult();
		
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
		
		return codClasse;
	}

}

