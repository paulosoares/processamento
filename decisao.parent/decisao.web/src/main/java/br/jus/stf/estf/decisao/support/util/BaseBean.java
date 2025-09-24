package br.jus.stf.estf.decisao.support.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


/**
 * Oferece métodos utilitários gerais.
 * 
 * @author Rodrigo Lisboa
 * @since 08.07.2010
 */
public class BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3502583497866165617L;
	
	protected String recuperarSessionId(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals("JSESSIONID")) {
					return cookies[i].getValue();
				}
			}
		}
		return null;
	}	
	
}
