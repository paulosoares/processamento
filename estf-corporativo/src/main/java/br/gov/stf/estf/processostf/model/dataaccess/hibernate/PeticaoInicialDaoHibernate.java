package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.PeticaoInicial;
import br.gov.stf.estf.entidade.processostf.PeticaoInicial.PeticaoInicialId;
import br.gov.stf.estf.processostf.model.dataaccess.PeticaoInicialDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Bruno da Silva Abreu
 * @since 31/05/2008
 */
@Repository
public class PeticaoInicialDaoHibernate extends GenericHibernateDao<PeticaoInicial, Long> implements PeticaoInicialDao {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4104229607441487514L;

	public PeticaoInicialDaoHibernate() {
		super(PeticaoInicial.class);
	}
	/**
	 * @see br.gov.stf.estf.processostf.model.dataaccess.IDaoPeticaoInicial#alterarPeticaoInicial(br.gov.stf.estf.processostf.modelo.PeticaoInicial)
	 */
	public void alterarPeticaoInicial(PeticaoInicial peticaoInicial) throws DaoException {
		Session session = retrieveSession();

		try {
			session.save(peticaoInicial);
			session.flush();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils
					.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}


	/**
	 * @see br.gov.stf.estf.processostf.model.dataaccess.IDaoPeticaoInicial#pesquisarPeticaoInicial(br.gov.stf.estf.processostf.modelo.PeticaoInicial)
	 */
	public List<PeticaoInicial> pesquisarPeticaoInicial(PeticaoInicial peticaoInicial) throws DaoException {
		Session session = retrieveSession();

		try {
			Example example = Example.create(peticaoInicial).ignoreCase();

			List<PeticaoInicial> resultado = session.createCriteria(PeticaoInicial.class).add(example).list();

			if (resultado != null) {
				return resultado;
			} else {
				return new ArrayList<PeticaoInicial>();
			}
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils
					.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}
	
	public PeticaoInicial recuperarPeticaoInicial(PeticaoInicialId peticaoInicialId) throws DaoException {
		
		Session session = retrieveSession();
		
		try {
			
			PeticaoInicial peticaoInicial = new PeticaoInicial();
			
			peticaoInicial.setNumero(peticaoInicialId.getNumero());
			peticaoInicial.setAno(peticaoInicialId.getAno());
			
			Example example = Example.create(peticaoInicial).ignoreCase();

			PeticaoInicial resultado = (PeticaoInicial)
				session.createCriteria(PeticaoInicial.class).add(example).uniqueResult();

			return resultado;	
		} 
		catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils
					.convertHibernateAccessException(e));
		} 
		catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}
}
