/* 
* Copyright 2004 The Apache Software Foundation 
* 
* Licensed under the Apache License, Version 2.0 (the "License"); 
* you may not use this file except in compliance with the License. 
* You may obtain a copy of the License at 
* 
* http://www.apache.org/licenses/LICENSE-2.0 
* 
* Unless required by applicable law or agreed to in writing, software 
* distributed under the License is distributed on an "AS IS" BASIS, 
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
* See the License for the specific language governing permissions and 
* limitations under the License. 
*/
/* 
 * Original name : org.apache.webapp.admin.filters.SetCharacterEncodingFilter 
 * Modified by VirageGroup to match our needs. 
 */
package br.jus.stf.estf.decisao.support.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.NDC;

/**
 * <p>
 * Ce filtre permet de specifier des entÃƒÂªtes HTTP dans la reponse.
 * </p>
 * 
 * <p>
 * Il ajoute notemment le X-UA-Compatible pour Internet Explorer 8
 * </p>
 * 
 * <p>
 * (http://www.jdocs.com/tomcat/5.5.17/org/apache/webapp/admin/filters/SetCharacterEncodingFilter.html)<br/>
 * Filter that sets the character encoding to be used in parsing the incoming request, either unconditionally or only if the client did not specify a character
 * encoding. Configuration of this filter is based on the following initialization parameters:
 * <ul>
 * <li><strong>encoding</strong> - The character encoding to be configured for this request, either conditionally or unconditionally based on the
 * <code>ignore</code> initialization parameter. This parameter is required, so there is no default.</li>
 * <li><strong>ignore</strong> - If set to "true", any character encoding specified by the client is ignored, and the value returned by the
 * <code>selectEncoding()</code> method is set. If set to "false, <code>selectEncoding()</code> is called <strong>only</strong> if the client has not already
 * specified an encoding. By default, this parameter is set to "true".</li>
 * </ul>
 * </p>
 */
public class SetHttpHeadersFilter implements Filter {

	// ----------------------------------------------------- Instance Variables

	/**
	 * The default character encoding to set for requests that pass through this filter.
	 */
	protected String encoding = null;

	/**
	 * The filter configuration object we are associated with. If this value
	 * is null, this filter instance is not currently configured.
	 */
	protected FilterConfig filterConfig = null;

	/**
	 * Should a character encoding specified by the client be ignored?
	 */
	protected boolean ignore = true;

	/**
	 * The Microsoft X-UA-Compatible hack
	 */
	protected String xuacompatible = null;

	// --------------------------------------------------------- Public Methods

	/**
	 * Take this filter out of service.
	 */
	public void destroy() {
		this.encoding = null;
		this.filterConfig = null;
		this.xuacompatible = null;
	}

	/**
	 * Select and set (if specified) the character encoding to be used to
	 * interpret request parameters for this request.
	 * 
	 * @param request
	 *            The servlet request we are processing
	 * @param response
	 *            The servlet response we are creating
	 * @param chain
	 *            The filter chain we are processing
	 * 
	 * @exception IOException
	 *                if an input/output error occurs
	 * @exception ServletException
	 *                if a servlet error occurs
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		// Conditionally select and set the character encoding to be used
		if (ignore || (request.getCharacterEncoding() == null)) {
			String selectEncoding = selectEncoding(request);
			if (selectEncoding != null)
				request.setCharacterEncoding(selectEncoding);
		}

		// ajout du X-UA-Compatible pour IE8+
		// aussi present dans /inc/head.inc.jsp
		((HttpServletResponse) response).setHeader("X-UA-Compatible", this.xuacompatible);
		
		// informa o usu�rio logado ao log4j
		String username = ((HttpServletRequest) request).getRemoteUser();
		
		if (username != null)
			NDC.push(username.toLowerCase());
		
		// Pass control on to the next filter
		chain.doFilter(request, response);
		
		// remove o usu�rio logado do log4j
		NDC.pop();
		NDC.clear();
	}

	/**
	 * Place this filter into service.
	 * 
	 * @param filterConfig
	 *            The filter configuration object
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;

		this.encoding = filterConfig.getInitParameter("encoding");

		String value = filterConfig.getInitParameter("ignore");
		this.ignore = (value == null || value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes"));

		this.xuacompatible = filterConfig.getInitParameter("X-UA-Compatible");
	}

	// ------------------------------------------------------ Protected Methods

	/**
	 * Select an appropriate character encoding to be used, based on the
	 * characteristics of the current request and/or filter initialization
	 * parameters. If no character encoding should be set, return <code>null</code>.
	 * <p>
	 * The default implementation unconditionally returns the value configured by the <strong>encoding</strong> initialization parameter for this filter.
	 * 
	 * @param request
	 *            The servlet request we are processing
	 */
	protected String selectEncoding(ServletRequest request) {
		return (this.encoding);
	}
}