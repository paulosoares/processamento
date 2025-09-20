package br.gov.stf.estf.usuario.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.usuario.CustomizacaoUsuario;
import br.gov.stf.estf.entidade.usuario.TipoCustomizacao;
import br.gov.stf.estf.usuario.model.dataaccess.CustomizacaoUsuarioDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class CustomizacaoUsuarioDaoHibernate  extends GenericHibernateDao<CustomizacaoUsuario, Long> implements CustomizacaoUsuarioDao{
	

	private static final long serialVersionUID = -576864687364257672L;

	public CustomizacaoUsuarioDaoHibernate() {
		super(CustomizacaoUsuario.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public CustomizacaoUsuario retornaCustomizacaoSetor(TipoCustomizacao tipoCustomizacao, Long codSetor) throws DaoException {
		CustomizacaoUsuario customizacaoUsuario = null;
	    
	    try {
			List<CustomizacaoUsuario> listaCustomizacao = null;
	    	
	    	Session session = retrieveSession();
	    	
	    	StringBuilder hql = new StringBuilder(" SELECT c FROM CustomizacaoUsuario c ");
	    	hql.append(" WHERE (1=1) ");

	    	if (codSetor != null ){
	    		hql.append(" AND upper(c.tipo.id) = " + tipoCustomizacao.getId() );
	    	}
	    	
	    	if (tipoCustomizacao != null ){
	    		hql.append(" AND upper(c.setor.id) = " + codSetor );
	    	}
	    	
			Query q = session.createQuery(hql.toString());
			listaCustomizacao = (List<CustomizacaoUsuario>)q.list();
			if (listaCustomizacao.size() > 0)
				customizacaoUsuario =  listaCustomizacao.get(0);
	    	
		} catch (Exception e) {
			throw new DaoException(e);
		}
	    
		return customizacaoUsuario;
	}
}
