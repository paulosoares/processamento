package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.TipoPreferencia;
import br.gov.stf.estf.processostf.model.dataaccess.TipoPreferenciaDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TipoPreferenciaDaoHibernate extends GenericHibernateDao<TipoPreferencia, Long> implements TipoPreferenciaDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6878410961929344278L;

	public TipoPreferenciaDaoHibernate() {
		super(TipoPreferencia.class);
	}
	
	public TipoPreferencia recuperarTipoPreferencia(Long idTipoPreferencia) throws DaoException {
		
		Session sessao = retrieveSession();
		
		try {

			TipoPreferencia tipoPreferenciaRecuperado = 
				(TipoPreferencia) sessao.load(TipoPreferencia.class, idTipoPreferencia);
			
			return tipoPreferenciaRecuperado;
		} 
		catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils
					.convertHibernateAccessException(e));
		} 
		catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}
	
	public List<TipoPreferencia> recuperarTipoPreferenciaODS() throws DaoException {
		List<TipoPreferencia> resp = null;

		try {

			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append("SELECT i FROM TipoPreferencia i");
			hql.append(" WHERE i.descricao like 'ODS %' ");
			
		
			
			Query q = session.createQuery( hql.toString() );
			
			
			
			resp = q.list();			
			
		} catch ( Exception e ) {
			throw new DaoException(e);
		}

		return resp;
	}
	
}

