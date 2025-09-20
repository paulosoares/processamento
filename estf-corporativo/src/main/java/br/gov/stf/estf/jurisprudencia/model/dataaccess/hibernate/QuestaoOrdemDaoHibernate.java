/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess.hibernate;

import org.hibernate.Session;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import br.gov.stf.estf.entidade.jurisprudencia.QuestaoOrdem;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.QuestaoOrdemDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;


@Repository
public class QuestaoOrdemDaoHibernate extends GenericHibernateDao<QuestaoOrdem, Long> implements
	QuestaoOrdemDao {

	private static final long serialVersionUID = 3845403230319531049L;	
	
	public QuestaoOrdemDaoHibernate() {
		super(QuestaoOrdem.class);
	}

	

	@Override
	public QuestaoOrdem recuperarPorObjetoIncidente(
			ObjetoIncidente<?> oi) throws DaoException {

		try {
			Session session = retrieveSession();
			
			Criteria c = session.createCriteria(QuestaoOrdem.class);
			
			c.add(Restrictions.eq("objetoIncidente", oi));
			
			return (QuestaoOrdem) c.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}

	}

	
}
