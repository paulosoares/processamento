package br.gov.stf.estf.entidade.documento;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name="TIPO_CAMPO_CUSTOMIZADO", schema="JUDICIARIO")
public class TipoTagsLivresUsuario extends ESTFBaseEntity<Long>{
	
	private static final long serialVersionUID = 1L;
	
//	private Long id;
	private String dscTipoTagLivres;
	
	@Id
	@Column(name = "SEQ_TIPO_CAMPO_CUSTOMIZADO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_TIPO_CAMPO_CUSTOMIZADO", allocationSize = 1)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="DSC_TIPO_CAMPO_CUSTOMIZADO",insertable = true, updatable = true, nullable=false, unique=false)
	public String getDscTipoTagLivres() {
		return dscTipoTagLivres;
	}

	public void setDscTipoTagLivres(String dscTipoTagLivres) {
		this.dscTipoTagLivres = dscTipoTagLivres;
	}

}
