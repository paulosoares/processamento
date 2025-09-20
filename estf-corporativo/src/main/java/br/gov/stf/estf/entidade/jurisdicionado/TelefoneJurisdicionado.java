package br.gov.stf.estf.entidade.jurisdicionado;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.jurisdicionado.enuns.EnumTipoTelefone;

@Entity
@Table(schema = "JUDICIARIO", name = "TELEFONE_JURISDICIONADO")
public class TelefoneJurisdicionado extends ESTFBaseEntity<Long> {
	
	private Long id;
	private Jurisdicionado jurisdicionado;
	private EnumTipoTelefone tipoTelefone;
	private String numero;
	
	private Integer DDD;
	private Integer DDI;
	private Integer ramal;
	private String observacao;
	
	
	@Column(name="DSC_OBSERVACAO")
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@Column(name="NUM_TELEFONE")
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Id
	@Column(name="SEQ_TELEFONE_JURISDICIONADO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_TELEFONE_JURISDICIONADO", allocationSize=1)
	public Long getId() {
		return this.id;
	}

	public void setId(Long identifier) {
		this.id = identifier;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_JURISDICIONADO")
	public Jurisdicionado getJurisdicionado() {
		return jurisdicionado;
	}
	public void setJurisdicionado(Jurisdicionado jurisdicionado) {
		this.jurisdicionado = jurisdicionado;
	}


	@Column(name = "TIP_TELEFONE")
	@Enumerated(EnumType.STRING)
	public EnumTipoTelefone getTipoTelefone() {
		return tipoTelefone;
	}

	public void setTipoTelefone(EnumTipoTelefone tipoTelefone) {
		this.tipoTelefone = tipoTelefone;
	}

	@Column(name="NUM_DDD")
	public Integer getDDD() {
		return DDD;
	}

	public void setDDD(Integer ddd) {
		DDD = ddd;
	}

	@Column(name="NUM_DDI")
	public Integer getDDI() {
		return DDI;
	}

	public void setDDI(Integer ddi) {
		DDI = ddi;
	}

	@Column(name="NUM_RAMAL")
	public Integer getRamal() {
		return ramal;
	}

	public void setRamal(Integer ramal) {
		this.ramal = ramal;
	}

}
