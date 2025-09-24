/**
 * 
 */
package br.jus.stf.estf.decisao.objetoincidente.support;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Almir.Oliveira 
 * @since 30.01.2012. */
public class PreListaJulgamentoReport {

	public static final String CABECALHO_HTML  = "cabecalhoHtml";
	public static final String CABECALHO_HTML_VISTOR  = "cabecalhoHtmlVistor";
	public static final String LISTA_PROCESSOS = "listaProcessos";	
	public static final String MINISTRO        = "ministro";
	public static final String MINISTRO_VISTOR        = "ministroVistor";
	public static final String NUMERO_LISTA    = "numeroLista";
	public static final String SESSAO          = "sessao";
	public static final String TEMPLATE_RELATORIO = "/relatorio/template/preListaJulgamentoTemplate.html";
	public static final String DESCRICAO_TIPO_LISTA = "descricaoTipo";
	
	private String identificacaoCompletaProcesso;
	private List<ListaGenericaReport> informacoesProcesso;
	
	private String textoEmenta;
	
	private String textoObservacao;

	private String textoVotoVista;
	
	private String numItemLista;

	public List<ListaGenericaReport> getInformacoesProcesso() {
		if ( informacoesProcesso == null ){
			informacoesProcesso = new ArrayList<ListaGenericaReport>();
		}
		return informacoesProcesso;
	}
	public void setInformacoesProcesso(List<ListaGenericaReport> informacoesProcesso) {
		this.informacoesProcesso = informacoesProcesso;
	}
	public String getIdentificacaoCompletaProcesso() {
		return identificacaoCompletaProcesso;
	}
	public void setIdentificacaoCompletaProcesso(String identificacaoCompletaProcesso) {
		this.identificacaoCompletaProcesso = identificacaoCompletaProcesso;
	}

	public String getTextoObservacao() {
		return textoObservacao;
	}
	
	public void setTextoObservacao(String textoObservacao) {
		this.textoObservacao = textoObservacao;
	}
	
	public String getTextoEmenta() {
		return textoEmenta;
	}
	
	public void setTextoEmenta(String textoEmenta) {
		this.textoEmenta = textoEmenta;
	}
	public String getNumItemLista() {
		return numItemLista;
	}
	public void setNumItemLista(String numItemLista) {
		this.numItemLista = numItemLista;
	}
	public String getTextoVotoVista() {
		return textoVotoVista;
	}
	public void setTextoVotoVista(String textoVotoVista) {
		this.textoVotoVista = textoVotoVista;
	}
	
}
