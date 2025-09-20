package br.gov.stf.estf.entidade.processostf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name = "TIPO_IMPRESSAO", schema = "STF")
public class TipoImpressao extends ESTFBaseEntity<Short> {

	private static final long serialVersionUID = 2845551373040812115L;
	private String descricao;

	@Id
	@Column(name = "COD_TIPO_IMPRESSAO", nullable = false, length = 1)
	public Short getId() {
		return id;
	}

	@Column(name = "DSC_TIPO_IMPRESSAO", length = 50)
	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String dscTipoImpressao) {
		this.descricao = dscTipoImpressao;
	}
	
}
