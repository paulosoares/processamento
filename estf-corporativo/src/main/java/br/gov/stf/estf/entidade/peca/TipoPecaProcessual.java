package br.gov.stf.estf.entidade.peca;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;

@Entity
@Table(name = "TIPO_PECA_PROCESSUAL", schema = "JUDICIARIO")
public class TipoPecaProcessual extends ESTFAuditavelBaseEntity<Long> {

	private static final long serialVersionUID = 676715333977123665L;

	@Id
	@Column(name = "COD_TIPO_PECA_PROCESSUAL")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_TIPO_PECA_PROCESSUAL", allocationSize = 1)
	private Long id;

	@Column(name = "DSC_TIPO_PECA_PROCESSUAL")
	private String descricao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
