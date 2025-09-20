package br.gov.stf.estf.assinatura.visao.util;

import java.util.Locale;
import java.util.PropertyResourceBundle;

import org.junit.Ignore;

@Ignore
public class LoginProperties {

	private LoginProperties() {
	}

	private static PropertyResourceBundle oResBundle = (PropertyResourceBundle) PropertyResourceBundle.getBundle("login", Locale.getDefault(),
			LoginProperties.class.getClassLoader());

	public static String getUsername() {
		return getProperty("username");
	}

	public static String getPassword() {
		return getProperty("password");
	}

	private static String getProperty(String pKey) {
		try {
			String prop = oResBundle.getString(pKey);

			if (prop != null) {
				prop = prop.trim();
			}

			return prop;
		} catch (Throwable e) {
			return "";
		}
	}
}
