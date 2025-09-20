package br.gov.stf.estf.documento.model.util;

import java.io.Serializable;

public enum TipoSessaoControleVoto implements Serializable {
	PLENARIO("P", "Plenário"), PRIMEIRA_TURMA("1", "Primeira Turma"), SEGUNDA_TURMA("2", "Segunda Turma");
	
	private String codigo;
	private String descricao;
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setCodigo(String sessao) {
		this.codigo = sessao;
	}

	public String getCodigo() {
		return codigo;
	}

	private TipoSessaoControleVoto (String sessao, String descricao) {
		this.codigo = sessao;
		this.descricao = descricao;
	}
	
	public static TipoSessaoControleVoto valueOfCodigo(String codigo) {
		if (codigo != null) {
			for (TipoSessaoControleVoto tipo : values()) {
				if (codigo.equals(tipo.getCodigo())) {
					return tipo;
				}
			}
		}
		throw new RuntimeException("Nao existe TipoSessaoControleVoto com codigo: " + codigo);
	}
}
