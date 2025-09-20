/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess.hibernate;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.jurisprudencia.CitacaoAcordao;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.CitacaoAcordaoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Paulo.Estevao
 * @since 17.10.2012
 */
@Repository
public class CitacaoAcordaoDaoHibernate extends GenericHibernateDao<CitacaoAcordao, Long> implements CitacaoAcordaoDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1007872124429680555L;

	public CitacaoAcordaoDaoHibernate() {
		super(CitacaoAcordao.class);
	}

	@Override
	public CitacaoAcordao alterar(CitacaoAcordao citacaoAcordao) throws DaoException {
		Session session = retrieveSession();
		try {
			session.saveOrUpdate(citacaoAcordao);
		} catch (Exception e) {
			throw new DaoException(e.getMessage(), e);
		}
		return citacaoAcordao;
	}
}
