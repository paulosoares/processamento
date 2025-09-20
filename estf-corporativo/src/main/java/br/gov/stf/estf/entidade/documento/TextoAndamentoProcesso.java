package br.gov.stf.estf.entidade.documento;

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

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;

@Entity
@Table(name = "TEXTOS_ANDAMENTO_PROCESSOS", schema = "STF")
public class TextoAndamentoProcesso extends ESTFAuditavelBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Texto texto;
	private AndamentoProcesso andamentoProcesso;
	private Long seqDocumento;

	@Id
	@Column(name = "SEQ_TEXTOS_ANDAMENTO_PROCESSOS")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "STF.SEQ_TEXTOS_ANDAMENTO_PROCESSOS", allocationSize = 1)
	public Long getId() {
		return id;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(name = "SEQ_TEXTOS", unique = false, insertable = true, updatable = true)
	// @Cascade(value=CascadeType.SAVE_UPDATE)
	public Texto getTexto() {
		return texto;
	}

	public void setTexto(Texto texto) {
		this.texto = texto;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(name = "SEQ_ANDAMENTO_PROCESSO", unique = true, insertable = true, updatable = true)
	// @Cascade(value=CascadeType.SAVE_UPDATE)
	public AndamentoProcesso getAndamentoProcesso() {
		return andamentoProcesso;
	}

	public void setAndamentoProcesso(AndamentoProcesso andamentoProcesso) {
		this.andamentoProcesso = andamentoProcesso;
	}

	@Column(name = "SEQ_DOCUMENTO")
	public Long getSeqDocumento() {
		return seqDocumento;
	}

	public void setSeqDocumento(Long seqDocumento) {
		this.seqDocumento = seqDocumento;
	}

}
