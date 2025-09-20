package br.gov.stf.estf.entidade.mag;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.usuario.Usuario;

@Entity
@Table(schema = "IMAG", name = "DESLOCA_MAG")
public class HistoricoDeslocamentoMag extends ESTFBaseEntity<Long> {

	private Date dataRecebimento;
	private Date dataRemessa;
	private String observacao;
	private Long numeroProcessual;
	private String siglaClasseProcessual;
	private SecaoMag secaoMagOrigem;
	private SecaoMag secaoMagDestino;
	private Usuario usuarioOrigem;
	private Usuario usuarioDestino;
	private Setor setor;
	private Setor setorDestino;
	
	
	@Id
	@Column(name = "SEQ_DESLOCA_GAB")
	public Long getId() {
		return id;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DAT_RECEBIMENTO", unique = false, nullable = true, insertable = false, updatable = false)
	public Date getDataRecebimento() {
		return dataRecebimento;
	}
	
	public void setDataRecebimento(Date dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DAT_REMESSA", unique = false, nullable = true, insertable = false, updatable = false)
	public Date getDataRemessa() {
		return dataRemessa;
	}
	
	public void setDataRemessa(Date dataRemessa) {
		this.dataRemessa = dataRemessa;
	}
	
	@Column(name = "OBS_DESLOCAMENTO", unique = false, nullable = true, insertable = false, updatable = false, 
			length = 240)
	public String getObservacao() {
		return observacao;
	}
	
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	@Column(name = "NUM_PROCESSO", unique = false, nullable = false, insertable = false, updatable = false, 
			precision = 7, scale = 0)
	public Long getNumeroProcessual() {
		return numeroProcessual;
	}
	
	public void setNumeroProcessual(Long numeroProcessual) {
		this.numeroProcessual = numeroProcessual;
	}
	
	@Column(name = "SIG_CLASSE_PROCES", unique = false, nullable = false, insertable = false, updatable = false, 
			length = 56)
	public String getSiglaClasseProcessual() {
		return siglaClasseProcessual;
	}
	
	public void setSiglaClasseProcessual(String siglaClasseProcessual) {
		this.siglaClasseProcessual = siglaClasseProcessual;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "SIG_USUARIO_ORIGEM", unique = false, nullable = true, insertable = false, updatable = false)	
	public Usuario getUsuarioOrigem() {
		return usuarioOrigem;
	}
	
	public void setUsuarioOrigem(Usuario usuarioOrigem) {
		this.usuarioOrigem = usuarioOrigem;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "SIG_USUARIO_DESTINO", unique = false, nullable = true, insertable = false, updatable = false)
	public Usuario getUsuarioDestino() {
		return usuarioDestino;
	}
	
	public void setUsuarioDestino(Usuario usuarioDestino) {
		this.usuarioDestino = usuarioDestino;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "COD_SETOR", unique = false, nullable = false, insertable = false, updatable = false)
	public Setor getSetor() {
		return setor;
	}
	
	public void setSetor(Setor setor) {
		this.setor = setor;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "COD_SETOR_DESTINO", unique = false, nullable = true, insertable = false, updatable = false)
	public Setor getSetorDestino() {
		return setorDestino;
	}
	
	public void setSetorDestino(Setor setorDestino) {
		this.setorDestino = setorDestino;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "SEQ_SECAO_ORIGEM", unique = false, nullable = true, insertable = false, updatable = false)
	public SecaoMag getSecaoMagOrigem() {
		return secaoMagOrigem;
	}

	public void setSecaoMagOrigem(SecaoMag secaoMagOrigem) {
		this.secaoMagOrigem = secaoMagOrigem;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "SEQ_SECAO_DESTINO", unique = false, nullable = true, insertable = false, updatable = false)
	public SecaoMag getSecaoMagDestino() {
		return secaoMagDestino;
	}

	public void setSecaoMagDestino(SecaoMag secaoMagDestino) {
		this.secaoMagDestino = secaoMagDestino;
	}
}
