package br.gov.stf.estf.jurisdicionado.model.dataaccess.hibernate;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.jurisdicionado.TipoJurisdicionado;
import br.gov.stf.estf.jurisdicionado.model.dataaccess.TipoJurisdicionadoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TipoJurisdicionadoDaoHibernate extends GenericHibernateDao<TipoJurisdicionado, Long> 
	implements TipoJurisdicionadoDao {

	private static final long serialVersionUID = -2319023057673249762L;
	
	public TipoJurisdicionadoDaoHibernate() {
		super(TipoJurisdicionado.class);
	}

	@Override
	public TipoJurisdicionado buscaTipoPelaSigla(String sigla) throws DaoException {
		TipoJurisdicionado identificacao = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(TipoJurisdicionado.class);

			c.add(Restrictions
					.eq("siglaTipoJurisdicionado", sigla));

			identificacao = (TipoJurisdicionado) c.uniqueResult();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return identificacao;
	}
	
}
