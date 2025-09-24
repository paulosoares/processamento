/**
 * 
 */
package br.jus.stf.estf.decisao.objetoincidente.support;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Almir.Oliveira 
 * @since 30.01.2012. */
public class ListaProcessosReport {

	public static final String CABECALHO_HTML = "cabecalhoHtml";
	public static final String LISTA_PROCESSOS = "listaProcessos";
	public static final String TEMPLATE_RELATORIO = "/relatorio/template/listaProcessoTemplate.html";
	
	private String identificacaoCompletaProcesso;
	private List<ListaGenericaReport> informacoesProcesso;

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

}
