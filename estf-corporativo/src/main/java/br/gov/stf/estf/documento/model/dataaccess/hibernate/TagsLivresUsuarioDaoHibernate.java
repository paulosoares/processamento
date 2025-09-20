package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.TagsLivresUsuarioDao;
import br.gov.stf.estf.entidade.documento.TagsLivresUsuario;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TagsLivresUsuarioDaoHibernate extends GenericHibernateDao<TagsLivresUsuario, Long>
	implements TagsLivresUsuarioDao{

	private static final long serialVersionUID = -6708292808436652006L;

	public TagsLivresUsuarioDaoHibernate() {
		super(TagsLivresUsuario.class);
	}

	@SuppressWarnings("unchecked")
	public List<TagsLivresUsuario> pesquisarNomeRotuloOuDescricao(String nomeRotulo, Long codTipoTag, String dscTag) throws DaoException {
		
		List<TagsLivresUsuario> listaTags = null;
	    
	    try {
	    	
	    	Session session = retrieveSession();
	    	
	    	StringBuilder hql = new StringBuilder(" SELECT tl FROM TagsLivresUsuario tl ");
	    	hql.append(" WHERE (1=1) ");

	    	if (nomeRotulo != null && nomeRotulo.length() > 0){
	    		hql.append(" AND tl.nomeRotulo LIKE ('%" + nomeRotulo + "%')");
	    	}
	    	
	    	if (dscTag != null && dscTag.length() > 0){
	    		hql.append(" AND UPPER(tl.dscTagLivres) LIKE ('%" + dscTag.toUpperCase() + "%')");
	    	}	    	
	    	
	    	if (codTipoTag != null){
	    		hql.append(" AND tl.tipoTagsLivres.id = " + codTipoTag );
	    	}
	    	
	    	if (nomeRotulo != null && nomeRotulo.length() > 0){
	    		hql.append(" ORDER BY tl.nomeRotulo asc ");
	    	}
	    	
	    	if (dscTag != null && dscTag.length() > 0){
	    		hql.append(" ORDER BY tl.dscTagLivres asc ");
	    	}
	    	
			Query q = session.createQuery(hql.toString());
	    	listaTags = (List<TagsLivresUsuario>)q.list();
	    	
		} catch (Exception e) {
			throw new DaoException(e);
		}
	    
		return listaTags;
	}
	

}
