package br.gov.stf.estf.usuario.model.dataaccess.hibernate;

import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.usuario.TipoConfiguracaoUsuario;
import br.gov.stf.estf.usuario.model.dataaccess.TipoConfiguracaoUsuarioDao;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TipoConfiguracaoUsuarioDaoHibernate extends GenericHibernateDao<TipoConfiguracaoUsuario, Long> implements TipoConfiguracaoUsuarioDao { 

	
    /**
	 * 
	 */
	private static final long serialVersionUID = -60419044155970658L;

	public TipoConfiguracaoUsuarioDaoHibernate() {
    	super(TipoConfiguracaoUsuario.class);
    }

	
}
