/**
 * 
 */
package br.gov.stf.estf.entidade.processostf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

/**
 * @author Paulo.Estevao
 * @since 24.06.2011
 */
@Entity
@Table(schema="STF", name="TEMA_PAUTA")
public class TemaPauta extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9193780819776348963L;
	
	private Long id;
	private PautaPlenario pautaPlenario;
	private String descricao;
	private Integer ordem;
	
	@Id
	@Column(name="SEQ_TEMA_PAUTA", insertable = false, updatable = false)
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "STF.SEQ_TEMA_PAUTA", allocationSize=1)
	@Override
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_PAUTA_PLENARIO")
	public PautaPlenario getPautaPlenario() {
		return pautaPlenario;
	}

	public void setPautaPlenario(PautaPlenario pautaPlenario) {
		this.pautaPlenario = pautaPlenario;
	}

	@Column(name="DSC_TEMA")
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name="NUM_ORDEM")
	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
}
