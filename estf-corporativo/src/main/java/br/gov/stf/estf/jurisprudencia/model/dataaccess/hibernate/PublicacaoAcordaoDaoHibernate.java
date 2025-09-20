/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.jurisprudencia.AcordaoJurisprudencia;
import br.gov.stf.estf.entidade.jurisprudencia.PublicacaoAcordao;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.PublicacaoAcordaoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class PublicacaoAcordaoDaoHibernate extends GenericHibernateDao<PublicacaoAcordao, Long> implements
		PublicacaoAcordaoDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7526485484619432727L;

	public PublicacaoAcordaoDaoHibernate() {
		super(PublicacaoAcordao.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PublicacaoAcordao> pesquisarPorAcordaoJurisprudencia(AcordaoJurisprudencia acordaoJurisprudencia) throws DaoException {
		try {
			Session session = retrieveSession();
			
			Criteria c = session.createCriteria(PublicacaoAcordao.class);
			c.add(Restrictions.eq("acordaoJurisprudencia.id", acordaoJurisprudencia.getId()));

			return c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
}
