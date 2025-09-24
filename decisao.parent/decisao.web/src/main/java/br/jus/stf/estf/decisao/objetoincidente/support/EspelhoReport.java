/**
 * 
 */
package br.jus.stf.estf.decisao.objetoincidente.support;

import java.util.ArrayList;
import java.util.List;

import br.gov.stf.estf.entidade.julgamento.InformacaoPautaProcesso;
import br.jus.stf.estf.decisao.pesquisa.web.incidente.IncidenteFacesBean.ItemEspelho;

/**
 * @author Paulo.Estevao 
 * @since 07.02.2013. */
public class EspelhoReport {

	public static final String TEMPLATE_RELATORIO = "/relatorio/template/espelhoReportTemplate.html";
	
	private InformacaoPautaProcesso informacaoPautaProcesso;
	private List<String> decisoes;
	private List<ItemEspelho> itensEspelho;
	private String sufixoRelator;
	private String nomeRelator;
	private List<ItemParte> partes;
	
	public InformacaoPautaProcesso getInformacaoPautaProcesso() {
		return informacaoPautaProcesso;
	}
	
	public void setInformacaoPautaProcesso(InformacaoPautaProcesso informacaoPautaProcesso) {
		this.informacaoPautaProcesso = informacaoPautaProcesso;
	}
	
	public List<String> getDecisoes() {
		return decisoes;
	}
	
	public void setDecisoes(List<String> decisoes) {
		this.decisoes = decisoes;
	}

	public List<ItemEspelho> getItensEspelho() {
		return itensEspelho;
	}

	public void setItensEspelho(List<ItemEspelho> itensEspelho) {
		this.itensEspelho = itensEspelho;
	}
	
	public String getSufixoRelator() {
		return sufixoRelator;
	}
	
	public void setSufixoRelator(String sufixoRelator) {
		this.sufixoRelator = sufixoRelator;
	}
	
	public String getNomeRelator() {
		return nomeRelator;
	}
	
	public void setNomeRelator(String nomeRelator) {
		this.nomeRelator = nomeRelator;
	}
	
	public List<ItemParte> getPartes() {
		if (partes == null) {
			partes = new ArrayList<ItemParte>();
		}
		return partes;
	}
	
	public void setPartes(List<ItemParte> partes) {
		this.partes = partes;
	}
	
	public static class ItemParte {
		private String categoria;
		private String nome;
		
		public ItemParte(String categoria, String nome) {
			setCategoria(categoria);
			setNome(nome);
		}
		
		public String getCategoria() {
			return categoria;
		}
		
		public void setCategoria(String categoria) {
			this.categoria = categoria;
		}
		
		public String getNome() {
			return nome;
		}
		
		public void setNome(String nome) {
			this.nome = nome;
		}
	}
}
