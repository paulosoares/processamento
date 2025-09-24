package br.jus.stf.estf.decisao.config;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.jus.stf.stfoffice.bootstrap.ArgumentosPlugin;

public class DecisaoParameters {
	private static final Log log = LogFactory.getLog(DecisaoParameters.class);
	private final URL decisaoServiceUrl;	
	public DecisaoParameters(ArgumentosPlugin argumentos) throws MalformedURLException  {
		//TODO Inserir os argumentos aqui
		this.decisaoServiceUrl = new URL(new URL(argumentos.getBaseUrl()),"services/wsdecisao");	
		log.info("decisaoServiceUrl = " + decisaoServiceUrl);
	}
	
	public URL getDecisaoServiceUrl() {
		return decisaoServiceUrl;
	}
}
