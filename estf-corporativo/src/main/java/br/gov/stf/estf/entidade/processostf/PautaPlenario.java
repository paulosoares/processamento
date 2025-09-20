/**
 * 
 */
package br.gov.stf.estf.entidade.processostf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

/**
 * @author Paulo.Estevao
 * @since 24.06.2011
 */
@Entity
@Table(schema="STF", name="PAUTA_PLENARIO")
public class PautaPlenario extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8032676787397474977L;
	
	private Long id;
	private String descricao;
	private Boolean publico;
	private Integer numero;
	
	@Id
	@Column(name="SEQ_PAUTA_PLENARIO", insertable = false, updatable = false)
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "STF.SEQ_PAUTA_PLENARIO", allocationSize=1)
	@Override
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="DSC_PAUTA")
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "FLG_PUBLICO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getPublico() {
		return publico;
	}

	public void setPublico(Boolean publico) {
		this.publico = publico;
	}

	@Column(name="NUM_PAUTA")
	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

}
