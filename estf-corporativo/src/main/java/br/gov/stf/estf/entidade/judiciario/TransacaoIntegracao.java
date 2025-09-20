package br.gov.stf.estf.entidade.judiciario;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(schema = "JUDICIARIO", name = "TRANSACAO_INTEGRACAO")
public class TransacaoIntegracao extends ESTFBaseEntity<Long> {
	
	private static final long serialVersionUID = 3546536186618501192L;

	@Id
	@Column(name = "SEQ_TRANSACAO_INTEGRACAO")
	private Long id;
	
	@Column(name="SEQ_OBJETO_INCIDENTE", insertable=false, updatable=false)
	private Long seqObjetoIncidente;
	
	@Column(name = "DSC_FLUXO_NEGOCIO")
	private String fluxoNegocio;
	
	@Column(name = "TIP_SITUACAO")
	private String situacao;
	
	@Column(name = "NUM_UNICO_PROCESSO")
	private String numeroUnicoProcesso;
	
	@Column(name = "COD_ORIGEM")
	private Long codigoOrigem;
	
	@Column(name = "COD_DESTINO")
	private Long codigoDestino;
	
	@Column(name = "NUM_PROTOCOLO_DESTINO")
	private String numeroProtocoloDestino;
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSeqObjetoIncidente() {
		return seqObjetoIncidente;
	}

	public void setSeqObjetoIncidente(Long seqObjetoIncidente) {
		this.seqObjetoIncidente = seqObjetoIncidente;
	}

	public String getFluxoNegocio() {
		return fluxoNegocio;
	}

	public void setFluxoNegocio(String fluxoNegocio) {
		this.fluxoNegocio = fluxoNegocio;
	}

	

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getNumeroUnicoProcesso() {
		return numeroUnicoProcesso;
	}

	public void setNumeroUnicoProcesso(String numeroUnicoProcesso) {
		this.numeroUnicoProcesso = numeroUnicoProcesso;
	}

	public Long getCodigoOrigem() {
		return codigoOrigem;
	}

	public void setCodigoOrigem(Long codigoOrigem) {
		this.codigoOrigem = codigoOrigem;
	}

	public Long getCodigoDestino() {
		return codigoDestino;
	}

	public void setCodigoDestino(Long codigoDestino) {
		this.codigoDestino = codigoDestino;
	}

	public String getNumeroProtocoloDestino() {
		return numeroProtocoloDestino;
	}

	public void setNumeroProtocoloDestino(String numeroProtocoloDestino) {
		this.numeroProtocoloDestino = numeroProtocoloDestino;
	}
	
	
	
}
