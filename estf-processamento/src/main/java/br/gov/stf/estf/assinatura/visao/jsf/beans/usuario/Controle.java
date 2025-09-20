package br.gov.stf.estf.assinatura.visao.jsf.beans.usuario;

public class Controle {

	private Long id;
	private String descricao;

	public Controle(Long id, String descricao) {
		this.id = id;
		this.descricao = descricao;

	}
	
	public Controle() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
