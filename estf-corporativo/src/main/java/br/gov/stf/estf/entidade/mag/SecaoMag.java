package br.gov.stf.estf.entidade.mag;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(schema = "IMAG", name = "SECAO")
public class SecaoMag extends ESTFBaseEntity<Long> {

	private String sigla;
	private String descricao;
	
	
	@Id
	@Column(name = "SEQ_SECAO")
	public Long getId() {
		return id;
	}
	
    @Column(name = "SIG_SECAO", unique = false, nullable = false, insertable = false, updatable = false, length = 15)	
	public String getSigla() {
		return sigla;
	}
	
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	
	@Column(name = "DSC_SECAO", unique = false, nullable = false, insertable = false, updatable = false, length = 50)	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
