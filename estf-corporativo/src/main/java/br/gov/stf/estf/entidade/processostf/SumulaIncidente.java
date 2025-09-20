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

@Entity
@Table(name="SUMULA_OBJETO_INCIDENTE", schema="JUDICIARIO")
public class SumulaIncidente extends ESTFBaseEntity<Long>{

	private static final long serialVersionUID = 4738493606056415545L;
	
	private ObjetoIncidente<?> objetoIncidente;
	private Sumula sumula;
	private int tipoVinculo;
	
	@Id
	@Column(name = "SEQ_SUMULA_OBJETO_INCIDENTE")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_SUMULA_OBJETO_INCIDENTE", allocationSize = 1)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id){
		this.id = id;
	}	
	
	//Armazena os processos precedentes
	@ManyToOne(fetch = FetchType.EAGER, cascade = {})
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE_VINCULADO", updatable = true, insertable = true)	
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}
	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}
	
	//Armazena a chave primária da tabela sumula que é o objeto incidente
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE_SUMULA", unique = false, nullable = false, insertable = true, updatable = true)	
	public Sumula getSumula() {
		return sumula;
	}
	public void setSumula(Sumula sumula) {
		this.sumula = sumula;
	}

	//tipo do vinculo para identificar se o registro criado é de um processo precedente ou originou de uma PSV
	@Column(name="TIP_VINCULO")
	public int getTipoVinculo() {
		return tipoVinculo;
	}

	public void setTipoVinculo(int tipoVinculo) {
		this.tipoVinculo = tipoVinculo;
	}

}
