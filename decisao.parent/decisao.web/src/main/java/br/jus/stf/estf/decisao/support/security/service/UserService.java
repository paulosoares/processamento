package br.jus.stf.estf.decisao.support.security.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.context.SecurityContextHolder;

@Path("/security/user")
public class UserService {

	@GET
    @Path("/logged")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean isLoggedOn(){
		return !hasRole("ROLE_ANONYMOUS");
    }
	
	private boolean hasRole(String role) {
		GrantedAuthority[] authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		
		if(authorities == null || authorities.length == 0){
			return false;
		}
		
		for (GrantedAuthority authority : authorities) {
			if (authority.getAuthority().endsWith(role)) {
				return true;
			}
		}
		
		return false;
	}

}
