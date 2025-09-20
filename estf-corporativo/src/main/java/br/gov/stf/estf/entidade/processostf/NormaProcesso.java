package br.gov.stf.estf.entidade.processostf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(schema = "STF", name = "NORMA_PROCESSO")
public class NormaProcesso extends ESTFBaseEntity<Long> {
	
	private String descricao;
	private String normaJurisprudencia;
	private Short ano;

	
	@Id
	@Column( name="COD_NORMA" )	
	public Long getId() {
		return id;
	}	
	
	@Column( name="DSC_NORMA")
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Column( name="NORMA_JURISP")
	public String getNormaJurisprudencia() {
		return normaJurisprudencia;
	}
	public void setNormaJurisprudencia(String normaJurisprudencia) {
		this.normaJurisprudencia = normaJurisprudencia;
	}
	
	@Column( name="ANO_NORMA")
	public Short getAno() {
		return ano;
	}
	public void setAno(Short ano) {
		this.ano = ano;
	}
	
}
