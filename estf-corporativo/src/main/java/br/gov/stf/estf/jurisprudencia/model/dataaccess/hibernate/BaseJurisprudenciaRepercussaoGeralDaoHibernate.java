/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess.hibernate;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.jurisprudencia.BaseJurisprudenciaRepercussaoGeral;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.BaseJurisprudenciaRepercussaoGeralDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author henrique.bona
 *
 */
@Repository
public class BaseJurisprudenciaRepercussaoGeralDaoHibernate extends GenericHibernateDao<BaseJurisprudenciaRepercussaoGeral, Long> implements BaseJurisprudenciaRepercussaoGeralDao {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7017219003860607651L;

	/**
	 * Construtor padrão
	 */
	public BaseJurisprudenciaRepercussaoGeralDaoHibernate() {
		super(BaseJurisprudenciaRepercussaoGeral.class);
	}

	@Override
	public BaseJurisprudenciaRepercussaoGeral recuperarPorObjetoIncidente(
			ObjetoIncidente<?> oi) throws DaoException {

		try {
			Session session = retrieveSession();
			
			Criteria c = session.createCriteria(BaseJurisprudenciaRepercussaoGeral.class);
			
			c.add(Restrictions.eq("objetoIncidente", oi));
			
			return (BaseJurisprudenciaRepercussaoGeral) c.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}


}
