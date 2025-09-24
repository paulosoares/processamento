package br.jus.stf.estf.decisao.support.security;

import java.util.List;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.context.ApplicationContext;
import org.springframework.security.context.HttpSessionContextIntegrationFilter;
import org.springframework.security.context.SecurityContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Listener de sesão utilizado para remoção do usuário da lista de usuários online.
 * 
 * @author Rodrigo Barreiros
 * @since 07.06.2010
 */
public class SessionListener implements HttpSessionListener {

    /**
     * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
     */
    @Override
	public void sessionDestroyed(HttpSessionEvent se) {
    	ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(se.getSession().getServletContext());
    	List<Principal> users = ((AuthenticationProvider) context.getBean("casAuthenticationProvider")).getUsers();
        
        SecurityContext securityContext = (SecurityContext) se.getSession().getAttribute(HttpSessionContextIntegrationFilter.SPRING_SECURITY_CONTEXT_KEY);
        if (securityContext != null) {
        	users.remove(securityContext.getAuthentication().getPrincipal());
        }
	}

    /**
     * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
     */
    @Override
    public void sessionCreated(HttpSessionEvent se) {
    }

}
