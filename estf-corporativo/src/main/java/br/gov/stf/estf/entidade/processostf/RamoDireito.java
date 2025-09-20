package br.gov.stf.estf.entidade.processostf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(schema = "STF", name = "RAMO_DIREITO")
public class RamoDireito extends ESTFBaseEntity<Long> {

	private String descricao;

	@Id
	@Column(name = "COD_RAMO_DIREITO")
	public Long getId() {
		return id;
	}
	
	@Column(name = "DSC_RAMO_DIREITO", unique = false, nullable = false, insertable = false, updatable = false, 
			length = 50)
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
