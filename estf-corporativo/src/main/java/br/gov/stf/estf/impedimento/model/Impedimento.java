package br.gov.stf.estf.impedimento.model;

import javax.persistence.Entity;

import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;

@Entity
public class Impedimento {
	
	private Ministro ministro;
	private ObjetoIncidente objetoIncidente;
	
	public Ministro getMinistro() {
		return ministro;
	}
	public void setMinistro(Ministro ministro) {
		this.ministro = ministro;
	}
	public ObjetoIncidente getObjetoIncidente() {
		return objetoIncidente;
	}
	public void setObjetoIncidente(ObjetoIncidente objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}
	
}
