package br.gov.stf.estf.entidade.documento;

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

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;

@Entity
@Table(schema="DOC", name="VW_DOCUMENTO")
public class DocumentoEletronicoView extends ESTFAuditavelBaseEntity<Long> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1429072535861598857L;
	private String siglaSistema;
	private String descricaoStatusDocumento;
	private String tipoAcesso;
	private TipoArquivo tipoArquivo;	
	private String descricaoMotivoCancelamento;
	private Date dataCancelamento;
	private String siglaUsuarioCancelamento;
	private AssinaturaDigitalView assinaturaDigitalView;
	private Long id;
	private String hashValidacao;
	private Long numTamanhoDocumento;
	
	@Id
	@Column( name="SEQ_DOCUMENTO", insertable = false, updatable = false)		
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name="DAT_CANCELAMENTO", unique=false, nullable=true, insertable=false, updatable=false)
	public Date getDataCancelamento() {
		return dataCancelamento;
	}
	public void setDataCancelamento(Date dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}
		
	@Column(name="DSC_MOTIVO_CANCELAMENTO", unique=false, nullable=true, insertable = false, updatable = false, length=200)
	public String getDescricaoMotivoCancelamento() {
		return descricaoMotivoCancelamento;
	}
	public void setDescricaoMotivoCancelamento(String descricaoMotivoCancelamento) {
		this.descricaoMotivoCancelamento = descricaoMotivoCancelamento;
	}
	
	@Column(name="DSC_STATUS_DOCUMENTO", unique=false, nullable=false, insertable = false, updatable = false, length=3)
	public String getDescricaoStatusDocumento() {
		return descricaoStatusDocumento;
	}
	public void setDescricaoStatusDocumento(String descricaoStatusDocumento) {
		this.descricaoStatusDocumento = descricaoStatusDocumento;
	}
	
	@Column(name="SIG_SISTEMA", unique=false, nullable=true, insertable = false, updatable = false, length=15)
	public String getSiglaSistema() {
		return siglaSistema;
	}
	public void setSiglaSistema(String siglaSistema) {
		this.siglaSistema = siglaSistema;
	}
	
	@Column(name="SIG_USUARIO_CANCELAMENTO", unique=false, nullable=true, insertable = false, updatable = false, length=100)
	public String getSiglaUsuarioCancelamento() {
		return siglaUsuarioCancelamento;
	}
	public void setSiglaUsuarioCancelamento(String siglaUsuarioCancelamento) {
		this.siglaUsuarioCancelamento = siglaUsuarioCancelamento;
	}
	
	@Column(name="TIP_ACESSO", unique=false, nullable=true, insertable = false, updatable = false, length=3)
	public String getTipoAcesso() {
		return tipoAcesso;
	}
	public void setTipoAcesso(String tipoAcesso) {
		this.tipoAcesso = tipoAcesso;
	}
	
	@Column(name = "SEQ_TIPO_ARQUIVO", insertable = true, updatable = true)
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.documento.TipoArquivo"),
			@Parameter(name = "identifierMethod", value = "getCodigo")})
	public TipoArquivo getTipoArquivo() {
		return tipoArquivo;
	}
	public void setTipoArquivo(TipoArquivo tipoArquivo) {
		this.tipoArquivo = tipoArquivo;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name="SEQ_DOCUMENTO", referencedColumnName="SEQ_DOCUMENTO", updatable = false, insertable = false)
	@LazyToOne(value=LazyToOneOption.NO_PROXY)
	public AssinaturaDigitalView getAssinaturaDigitalView() {
		return assinaturaDigitalView;
	}

	public void setAssinaturaDigitalView(AssinaturaDigitalView assinaturaDigitalView) {
		this.assinaturaDigitalView = assinaturaDigitalView;
	}

	@Column(name = "TXT_HASH_VALIDACAO")
	public String getHashValidacao() {
		return hashValidacao;
	}

	public void setHashValidacao(String hashValidacao) {
		this.hashValidacao = hashValidacao;
	}

	@Column(name = "NUM_TAMANHO_DOCUMENTO")
	public Long getNumTamanhoDocumento() {
		return numTamanhoDocumento;
	}

	public void setNumTamanhoDocumento(Long numTamanhoDocumento) {
		this.numTamanhoDocumento = numTamanhoDocumento;
	}
	
	
}
