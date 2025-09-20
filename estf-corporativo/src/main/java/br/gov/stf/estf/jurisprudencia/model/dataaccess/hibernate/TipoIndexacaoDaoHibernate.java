/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess.hibernate;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.jurisprudencia.TipoIndexacao;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.TipoIndexacaoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Paulo.Estevao
 * @since 10.07.2012
 */
@Repository
public class TipoIndexacaoDaoHibernate extends GenericHibernateDao<TipoIndexacao, Long> implements TipoIndexacaoDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9148838942201676278L;

	public TipoIndexacaoDaoHibernate() {
		super(TipoIndexacao.class);
	}

	@Override
	public TipoIndexacao recuperarPorSigla(String sigla) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(TipoIndexacao.class);
			
			c.add(Restrictions.eq("sigla", sigla));
			
			return (TipoIndexacao) c.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
}
