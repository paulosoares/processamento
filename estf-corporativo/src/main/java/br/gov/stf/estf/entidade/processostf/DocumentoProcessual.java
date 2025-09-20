package br.gov.stf.estf.entidade.processostf;

import java.io.Serializable;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

public abstract class DocumentoProcessual<ID extends Serializable> extends ESTFBaseEntity<ID> {
	private Long numero;
	private Short ano;
	
	public Short getAno() {
		return ano;
	}
	public void setAno(Short ano) {
		this.ano = ano;
	}
	public Long getNumero() {
		return numero;
	}
	public void setNumero(Long numero) {
		this.numero = numero;
	}
}
