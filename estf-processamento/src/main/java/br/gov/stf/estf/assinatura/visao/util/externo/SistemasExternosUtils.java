package br.gov.stf.estf.assinatura.visao.util.externo;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;

/**
 * Classe utilitária para geração de links de acesso a sistemas externos.
 * 
 * @author thiago.miranda
 */
public class SistemasExternosUtils {

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("sistemas-externos");
	private static final String SEPARADOR_URL = "/";
	private static final String HTTP = "http:" + SEPARADOR_URL + SEPARADOR_URL;
	private static final String LINK_SUPREMO = "supremo" + SEPARADOR_URL + "supremo.html#" + SEPARADOR_URL + "processo" + SEPARADOR_URL + "resumo" + SEPARADOR_URL;

	private SistemasExternosUtils() {

	}

	/**
	 * Gera um link de consulta de um {@link ObjetoIncidente} no sistema eJud-Consulta Processual.
	 */
	public static String gerarLinkEJudConsulta(String nomeServidor, Long seqObjetoIncidente) {
		StringBuilder linkBuilder = new StringBuilder(HTTP).append(nomeServidor).append(SEPARADOR_URL).append(getQueryStringEJudConsulta(seqObjetoIncidente));
		return linkBuilder.toString();
	}

	public static String gerarLinkDigital(String siglaNumero) {
		return MessageFormat.format(RESOURCE_BUNDLE.getString("query.string.ejud.digitalinfo"), String.valueOf(siglaNumero));
	}
	
	public static String gerarLinkDigitalPecas(String siglaNumero) {
		return MessageFormat.format(RESOURCE_BUNDLE.getString("query.string.ejud.digitalpeca"), String.valueOf(siglaNumero));
	}
	
	private static Object getQueryStringEJudConsulta(Long seqObjetoIncidente) {
		return MessageFormat.format(RESOURCE_BUNDLE.getString("query.string.ejud.consulta"), String.valueOf(seqObjetoIncidente));
	}
	
	public static String gerarLinkPortalStf(String portalStf, Long seqObjetoIncidente) {
		StringBuilder linkBuilder = new StringBuilder(HTTP).append(portalStf).append(SEPARADOR_URL).append(getQueryStringPortalStf(seqObjetoIncidente));
		return linkBuilder.toString();
	}
	
	private static Object getQueryStringPortalStf(Long seqObjetoIncidente) {
		return MessageFormat.format(RESOURCE_BUNDLE.getString("query.string.portalstf"), String.valueOf(seqObjetoIncidente));
	}

	public static String gerarLinkVisualizador(String nomeServidor, Long seqObjetoIncidente) {
		StringBuilder linkBuilder = new StringBuilder(HTTP).append(nomeServidor).append(SEPARADOR_URL).append(getQueryStringVisualizador(seqObjetoIncidente));
		return linkBuilder.toString();
	}
	
	public static String gerarLinkSupremo(String nomeServidor, Long seqObjetoIncidente) {
		StringBuilder linkBuilder = new StringBuilder(HTTP).append(nomeServidor).append(SEPARADOR_URL).append(LINK_SUPREMO).append(seqObjetoIncidente);
		return linkBuilder.toString();
	}


	private static Object getQueryStringVisualizador(Long seqObjetoIncidente) {
		return MessageFormat.format(RESOURCE_BUNDLE.getString("query.string.visualizador"), String.valueOf(seqObjetoIncidente));
	}
	
}
