package br.gov.stf.estf.usuario.model.dataaccess.hibernate;


import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.usuario.AdvogadoPeticionamento;
import br.gov.stf.estf.usuario.model.dataaccess.AdvogadoPeticionamentoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class AdvogadoPeticionamentoDaoHibernate extends GenericHibernateDao<AdvogadoPeticionamento, Long> implements AdvogadoPeticionamentoDao {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1189230633780525062L;


	public AdvogadoPeticionamentoDaoHibernate() {
		super(AdvogadoPeticionamento.class);
	}
	
	public void persistirAdvogado(AdvogadoPeticionamento advogado) throws DaoException {
		Session session = retrieveSession();
		try {
			session.save(advogado);
			session.flush();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils
					.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}


	public List<AdvogadoPeticionamento> pesquisarAdvogado(Long cpf) throws DaoException {
		Session session = retrieveSession();
		List<AdvogadoPeticionamento> listaAdvogados = null;		

		try {
			Criteria criteria = session.createCriteria(AdvogadoPeticionamento.class);

			if (cpf != null) {
				criteria.add(Restrictions.eq("cpf", cpf));
			}
			listaAdvogados = criteria.list();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils
					.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return listaAdvogados;
	}
}
