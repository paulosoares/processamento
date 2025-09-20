package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.ConfiguracaoTextoSetorDao;
import br.gov.stf.estf.entidade.documento.ConfiguracaoTextoSetor;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ConfiguracaoTextoSetorDaoHibernate extends GenericHibernateDao<ConfiguracaoTextoSetor, Long> implements
		ConfiguracaoTextoSetorDao {

	public ConfiguracaoTextoSetorDaoHibernate() {
		super(ConfiguracaoTextoSetor.class);
	}

	private static final long serialVersionUID = 4239546872424358755L;

	public ConfiguracaoTextoSetor recuperar(Long codigoSetor) throws DaoException {
		ConfiguracaoTextoSetor cts = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(ConfiguracaoTextoSetor.class);
			c.add(Restrictions.eq("setor.id", codigoSetor));
			cts = (ConfiguracaoTextoSetor) c.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return cts;
	}

}
