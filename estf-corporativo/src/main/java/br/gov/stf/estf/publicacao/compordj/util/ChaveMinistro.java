package br.gov.stf.estf.publicacao.compordj.util;

import java.io.Serializable;

import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.framework.model.entity.TipoSexo;

public class ChaveMinistro implements Serializable, Comparable<ChaveMinistro> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7961154816021732249L;
	
	private String nomeMinistro;
	private Long codigoMinistro;
	private boolean isFemea;
	
	public ChaveMinistro (Ministro ministro) {
		nomeMinistro = ministro.getNome();
		codigoMinistro = ministro.getId();
		isFemea = false;
		if ( ministro.getTipoSexo().getValor().equals( TipoSexo.FEMININO ) ) {
			isFemea = true;
		}
	}
	
	public ChaveMinistro (String nomeMinistro, Long codigoMinistro, boolean isFemea) {
		this.nomeMinistro = nomeMinistro;
		this.codigoMinistro = codigoMinistro;
		this.isFemea = isFemea;
	}
	
	public String getNomeMinistro() {
		return nomeMinistro;
	}
	public void setNomeMinistro(String nomeMinistro) {
		this.nomeMinistro = nomeMinistro;
	}
	public boolean isFemea() {
		return isFemea;
	}
	public void setFemea(boolean isFemea) {
		this.isFemea = isFemea;
	}
	
	public boolean equals(Object obj) {
		if ( obj instanceof ChaveMinistro ) {
			ChaveMinistro chave = (ChaveMinistro) obj;
			if ( chave.getNomeMinistro().equals( this.nomeMinistro ) ) {
				return true;
			}
		}
		return false;
	}

	public int compareTo(ChaveMinistro o) {
		return this.codigoMinistro.compareTo( o.getCodigoMinistro() );
	}
	
	@Override
	public String toString() {
		return this.nomeMinistro;
	}
	
	@Override
	public int hashCode() {
		return this.nomeMinistro.hashCode();
	}

	public Long getCodigoMinistro() {
		return codigoMinistro;
	}

	public void setCodigoMinistro(Long codigoMinistro) {
		this.codigoMinistro = codigoMinistro;
	}
	
}
