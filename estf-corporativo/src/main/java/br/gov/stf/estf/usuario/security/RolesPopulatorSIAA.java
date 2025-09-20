package br.gov.stf.estf.usuario.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.ldap.LdapAuthoritiesPopulator;
import org.springframework.stereotype.Component;

import br.gov.stf.estf.usuario.model.service.UsuarioService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * Classe estendida do Spring Security para recuperar as ROLES do usuário a partir do Banco de Dados,
 * e se adequar ao padrão de Roles da SSGJ.
 * @author GLADSON.LIMA
 *
 */
@Component("authoritiesPopulatorSIAA")
public class RolesPopulatorSIAA implements LdapAuthoritiesPopulator{
	public static final String ROLE_PREFIX_SIAA = "ROLE_" ;
	
	
	private final UsuarioService usuarioService;
	
	@Autowired
	public RolesPopulatorSIAA(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public GrantedAuthority[] getGrantedAuthorities(DirContextOperations userData,
			String username) {		
		try {
			return usuarioService.pesquisarRolesSIAA(username);
		} catch (ServiceException e) {
			e.printStackTrace();
			return new GrantedAuthority[0];
		}
	}

	


}
