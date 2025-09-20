package br.gov.stf.estf.usuario.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.usuario.TipoGrupoControle;
import br.gov.stf.estf.usuario.model.dataaccess.TipoGrupoControleDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TipoGrupoControleDaoHibernate extends GenericHibernateDao<TipoGrupoControle, Long> 
	implements TipoGrupoControleDao{

	private static final long serialVersionUID = 6762027070360127120L;

	public TipoGrupoControleDaoHibernate(){
		super (TipoGrupoControle.class);
	}

	
	@SuppressWarnings("unchecked")
	public List<TipoGrupoControle> pesquisarTipoGrupoControle(String nomeGrupo)
			throws DaoException {
		
		List<TipoGrupoControle> listaGrupo = null;
	    
	    try {
	    	
	    	Session session = retrieveSession();
	    	
	    	StringBuilder hql = new StringBuilder(" SELECT tg FROM TipoGrupoControle tg ");
	    	hql.append(" WHERE (1=1) ");

	    	if (nomeGrupo != null && nomeGrupo.length() > 0){
	    		hql.append(" AND upper(tg.dscTipoGrupoControle) LIKE ('%" + nomeGrupo.toUpperCase() + "%')");
	    	}
	    	
			Query q = session.createQuery(hql.toString());
			listaGrupo = (List<TipoGrupoControle>)q.list();
	    	
		} catch (Exception e) {
			throw new DaoException(e);
		}
	    
		return listaGrupo;
	}

}
