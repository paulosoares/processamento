package br.jus.stf.estf.decisao.mobile.assinatura.support;

import java.io.Serializable;

import br.gov.stf.estf.entidade.ministro.Ministro;

public class MinistroAssinadorDto implements Serializable {

	private static final long serialVersionUID = 511268524887889346L;

	private String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public static MinistroAssinadorDto from(Ministro m) {
		MinistroAssinadorDto dto = new MinistroAssinadorDto();
		dto.setNome(m.getNomeMinistroCapsulado(true, false, true));
		return dto;
	}
	
}
