package br.gov.stf.estf.julgamento.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.julgamento.Envolvido;
import br.gov.stf.estf.julgamento.model.dataaccess.EnvolvidoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class EnvolvidoDaoHibernate extends GenericHibernateDao<Envolvido, Long> implements EnvolvidoDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3331057537592313131L;

	public EnvolvidoDaoHibernate() {
		super(Envolvido.class);
	}
	
	@Override
	public List<Envolvido> pesquisar(String sugestaoNome)
			throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(Envolvido.class);
			c.add(Restrictions.ilike("nome", "%" + sugestaoNome + "%"));
			return c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
}
