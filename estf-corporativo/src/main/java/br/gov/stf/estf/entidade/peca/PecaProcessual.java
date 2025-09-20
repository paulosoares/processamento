package br.gov.stf.estf.entidade.peca;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;

@Entity
@Table(name = "PECA_PROCESSUAL", schema = "JUDICIARIO")
public class PecaProcessual extends ESTFAuditavelBaseEntity<Long> {

	private static final long serialVersionUID = -7379483443485283600L;

	@Id
	@Column(name = "SEQ_PECA_PROCESSUAL")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_PECA_PROCESSUAL", allocationSize = 1)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_TIPO_PECA_PROCESSUAL")
	private TipoPecaProcessual tipoPecaProcessual;

	@Column(name = "DSC_PECA_PROCESSUAL")
	private String descricao;

	@Column(name = "SEQ_OBJETO_INCIDENTE")
	public ObjetoIncidente<?> objetoIncidente;

	@Column(name = "NUM_ORDEM_PECA")
	private Long ordem;

	@Column(name = "SEQ_DOCUMENTO")
	private Long documento;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoPecaProcessual getTipoPecaProcessual() {
		return tipoPecaProcessual;
	}

	public void setTipoPecaProcessual(TipoPecaProcessual tipoPecaProcessual) {
		this.tipoPecaProcessual = tipoPecaProcessual;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	public Long getOrdem() {
		return ordem;
	}

	public void setOrdem(Long ordem) {
		this.ordem = ordem;
	}

	public Long getDocumento() {
		return documento;
	}

	public void setDocumento(Long documento) {
		this.documento = documento;
	}

}
