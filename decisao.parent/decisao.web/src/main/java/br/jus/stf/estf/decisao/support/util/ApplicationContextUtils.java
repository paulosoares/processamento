package br.jus.stf.estf.decisao.support.util;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Rodrigo Barreiros
 * @see 26.05.2010
 */
public final class ApplicationContextUtils {
	
	/**
	 * Construtor de classes utilitárias deve ser escondido.
	 */
	public ApplicationContextUtils() {
	}

	public static ApplicationContext getApplicationContext() {
	    ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	    return WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	}
	
}
