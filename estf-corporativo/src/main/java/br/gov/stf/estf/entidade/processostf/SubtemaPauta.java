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
@Table(schema="STF", name="SUBTEMA_PAUTA")
public class SubtemaPauta extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2234261245273755821L;
	
	private Long id;
	private TemaPauta temaPauta;
	private Integer ordem;
	private String descricao;
	
	@Override
	@Id
	@Column(name="SEQ_SUBTEMA_PAUTA")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "STF.SEQ_SUBTEMA_PAUTA", allocationSize=1)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="SEQ_TEMA_PAUTA")
	public TemaPauta getTemaPauta() {
		return temaPauta;
	}

	public void setTemaPauta(TemaPauta temaPauta) {
		this.temaPauta = temaPauta;
	}

	@Column(name="NUM_ORDEM")
	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	@Column(name="DSC_SUBTEMA")
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
