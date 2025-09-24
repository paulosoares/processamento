package br.jus.stf.estf.decisao.objetoincidente.support;

/**
 * @author Almir.Oliveira 
 * @since 02.02.2012. */
public class ListaGenericaReport {

	private String titulo;
	private String descricao;
	
	public ListaGenericaReport(){}
	
	public ListaGenericaReport(String titulo, String descricao){
		this.titulo = titulo;
		this.descricao = descricao;
	}

	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
