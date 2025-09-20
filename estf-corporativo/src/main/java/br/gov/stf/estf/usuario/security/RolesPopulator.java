package br.gov.stf.estf.usuario.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.ldap.LdapAuthoritiesPopulator;
import org.springframework.stereotype.Component;

import br.gov.stf.estf.usuario.model.service.UsuarioService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * Classe estendida do Spring Security para recuperar as ROLES do usuário a partir do Banco de Dados
 * @author leonardo.borges
 *
 */
@Component("authoritiesPopulator")
public class RolesPopulator implements LdapAuthoritiesPopulator{
	public static final String ROLE_PREFIX = "RS_" ;
	
	
	private final UsuarioService usuarioService;
	
	@Autowired
	public RolesPopulator(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public GrantedAuthority[] getGrantedAuthorities(DirContextOperations userData,
			String username) {		
		try {
			return usuarioService.pesquisarRoles(username);
		} catch (ServiceException e) {
			e.printStackTrace();
			return new GrantedAuthority[0];
		}
	}

	


}
