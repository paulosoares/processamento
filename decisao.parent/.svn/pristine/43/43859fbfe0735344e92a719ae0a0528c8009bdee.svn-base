package br.jus.stf.estf.decisao.mobile.assinatura.support;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class VersionUtil {

	public static String getVersion(ServletContext sc) {
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(sc);
		String version = (String)context.getBean("appVersion");
		return version;
	}
	
}
