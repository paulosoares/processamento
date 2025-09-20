package br.gov.stf.estf.processosetor.model.dataaccess.hibernate;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.localizacao.TipoFaseSetor;
import br.gov.stf.estf.entidade.processosetor.HistoricoFase;
import br.gov.stf.estf.processosetor.model.dataaccess.FaseDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class FaseDaoHibernate 
extends GenericHibernateDao<HistoricoFase, Long>
implements FaseDao {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3264276183258239451L;

	public FaseDaoHibernate(){
		super(HistoricoFase.class);
	}
	
	public Boolean excluirHistoricoFase(HistoricoFase fase) 
	throws DaoException {
		Session session = retrieveSession();
		Boolean alterado = Boolean.FALSE;
		
		try{

			session.delete(fase);
			session.flush();

			alterado = Boolean.TRUE;

		}catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}    	

		return alterado;			
	}
	
	public List<HistoricoFase> pesquisarFases(TipoFaseSetor faseSetor)
	throws DaoException {
		Session session = retrieveSession();
		List<HistoricoFase> listaFases = new LinkedList<HistoricoFase>();
		
		try {
			
			Criteria criteria = session.createCriteria(HistoricoFase.class);
			
			criteria.add(Restrictions.eq("tipoFaseSetor.id", faseSetor.getId()));
			
			listaFases = criteria.list();
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}
		
		return listaFases;
		
	}

}
