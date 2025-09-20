package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.TipoTagsLivresUsuarioDao;
import br.gov.stf.estf.entidade.documento.TipoTagsLivresUsuario;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TipoTagsLivresUsuarioDaoHibernate extends GenericHibernateDao<TipoTagsLivresUsuario, Long>
implements TipoTagsLivresUsuarioDao{
	

	private static final long serialVersionUID = 579504255547561243L;

	public TipoTagsLivresUsuarioDaoHibernate() {
		super(TipoTagsLivresUsuario.class);
	}

	
	public List<TipoTagsLivresUsuario> pesquisar(String nome)
			throws DaoException {
		
		List<TipoTagsLivresUsuario> listaTipoTags = null;
	    
	    try {
	    	
	    	Session session = retrieveSession();
	    	
	    	StringBuilder hql = new StringBuilder(" SELECT ttl FROM TipoTagsLivresUsuario ttl ");
	    	hql.append(" WHERE (1=1) ");

	    	if (nome != null && nome.length() > 0){
	    		hql.append(" AND ttl.dscTipoTagLivres LIKE ('%" + nome + "%')");
	    		hql.append(" ORDER BY ttl.dscTipoTagLivres asc ");
	    	}
			Query q = session.createQuery(hql.toString());
			listaTipoTags = (List<TipoTagsLivresUsuario>)q.list();
	    	
		} catch (Exception e) {
			throw new DaoException(e);
		}
	    
		return listaTipoTags;
	}
	
	

}
