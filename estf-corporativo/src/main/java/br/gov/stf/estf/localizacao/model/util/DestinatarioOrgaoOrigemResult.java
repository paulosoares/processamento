package br.gov.stf.estf.localizacao.model.util;

import java.io.Serializable;

public class DestinatarioOrgaoOrigemResult implements Serializable{
	
	private static final long serialVersionUID = 8957856167961270246L;
	
	private String nomeDestinatario;
	private Long codigoDestinatario;
	private String enderecoDestinatario;
	private String nomeOrgao;
	private String nomeProcedencia;
	private String nomeOrigem;
	private Boolean ativo;
	
	public DestinatarioOrgaoOrigemResult() {
	}

	public String getNomeDestinatario() {
		return nomeDestinatario;
	}

	public void setNomeDestinatario(String nomeDestinatario) {
		this.nomeDestinatario = nomeDestinatario;
	}

	public Long getCodigoDestinatario() {
		return codigoDestinatario;
	}

	public void setCodigoDestinatario(Long codigoDestinatario) {
		this.codigoDestinatario = codigoDestinatario;
	}

	public String getEnderecoDestinatario() {
		return enderecoDestinatario;
	}

	public void setEnderecoDestinatario(String enderecoDestinatario) {
		this.enderecoDestinatario = enderecoDestinatario;
	}

	public String getNomeOrgao() {
		return nomeOrgao;
	}

	public void setNomeOrgao(String nomeOrgao) {
		this.nomeOrgao = nomeOrgao;
	}

	public String getNomeProcedencia() {
		return nomeProcedencia;
	}

	public void setNomeProcedencia(String nomeProcedencia) {
		this.nomeProcedencia = nomeProcedencia;
	}

	public String getNomeOrigem() {
		return nomeOrigem;
	}

	public void setNomeOrigem(String nomeOrigem) {
		this.nomeOrigem = nomeOrigem;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
	

}
