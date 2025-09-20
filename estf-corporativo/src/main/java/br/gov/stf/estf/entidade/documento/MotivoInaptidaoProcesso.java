package br.gov.stf.estf.entidade.documento;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;

@Entity
@Table(name = "PROCESSO_MOTIVO_INAPTIDAO", schema = "JUDICIARIO")
public class MotivoInaptidaoProcesso extends ESTFAuditavelBaseEntity<Long> {
	private static final long serialVersionUID = 1L;

	private ObjetoIncidente objetoIncidente;
	private MotivoInaptidao motivoInaptidao;
	private String observacao;
	
	@Id
	@Column(name = "SEQ_PROCESSO_MOTIVO_INAPTIDAO")
	public Long getId() {
		return this.id;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.EAGER)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE")
	public ObjetoIncidente getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.EAGER)
	@JoinColumn(name = "COD_MOTIVO_INAPTIDAO")
	public MotivoInaptidao getMotivoInaptidao() {
		return motivoInaptidao;
	}

	public void setMotivoInaptidao(MotivoInaptidao motivoInaptidao) {
		this.motivoInaptidao = motivoInaptidao;
	}
	
	@Column(name = "TXT_OBSERVACAO")
	public String getObservacao() {
		return this.observacao;
	}

	public void setObservacao(String descricao) {
		this.observacao = descricao;
	}

}
