package br.gov.stf.estf.entidade.documento;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table( schema="STF", name="DOCUMENTO_TEXTO_PETICAOS")
public class DocumentoTextoPeticao extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = 5100795422030716200L;
	
	private Long id;
	private TextoPeticao textoPeticao;
	private DocumentoEletronico documentoEletronico;	
	private Date dataRevisao;
	private String usuarioRevisao;
	private TipoSituacaoDocumento tipoSituacaoDocumento;
	private TipoDocumentoTexto tipoDocumentoTexto;	
	private DocumentoEletronicoView documentoEletronicoView;		
	private AssinaturaDigitalView assinaturaDigitalView;
	
	
	
	@Id
	@Column( name="SEQ_DOCUMENTO_TEXTO_PETICAOS", nullable = false)	
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="STF.SEQ_DOCUMENTO_TEXTO_PETICAOS", allocationSize=1)
	public Long getId() {
		return this.id;
	}
		
	public void setId(Long id) {
		this.id = id;
	}	
	
	@ManyToOne(cascade={},  fetch = FetchType.LAZY)
	@JoinColumn(name="SEQ_TEXTO_PETICAOS", unique = true)	
	public TextoPeticao getTextoPeticao() {
		return textoPeticao;
	}

	public void setTextoPeticao(TextoPeticao textoPeticao) {
		this.textoPeticao = textoPeticao;
	}
	
	@ManyToOne(cascade={}, fetch = FetchType.LAZY)
	@JoinColumn(name="SEQ_DOCUMENTO", unique = true)	
	public DocumentoEletronico getDocumentoEletronico() {
		return documentoEletronico;
	}

	public void setDocumentoEletronico(DocumentoEletronico documento) {
		this.documentoEletronico = documento;
	}

	@Column(name = "SEQ_TIPO_SITUACAO_DOCUMENTO", insertable = true, updatable = true)
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento"),
			@Parameter(name = "identifierMethod", value = "getCodigo")})
	public TipoSituacaoDocumento getTipoSituacaoDocumento() {
		return tipoSituacaoDocumento;
	}

	public void setTipoSituacaoDocumento(TipoSituacaoDocumento tipoSituacaoDocumento) {
		this.tipoSituacaoDocumento = tipoSituacaoDocumento;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DAT_REVISAO", unique=false, nullable=true, insertable=true, updatable=true)
	public Date getDataRevisao() {
		return dataRevisao;
	}

	public void setDataRevisao(Date dataRevisao) {
		this.dataRevisao = dataRevisao;
	}

	@Column(name="USU_REVISAO", unique=false, nullable=true, insertable=true, updatable=true)
	public String getUsuarioRevisao() {
		return usuarioRevisao;
	}
	
	public void setUsuarioRevisao(String usuarioRevisao) {
		this.usuarioRevisao = usuarioRevisao;
	}

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_TIPO_DOCUMENTO_TEXTO")	
	public TipoDocumentoTexto getTipoDocumentoTexto() {
		return tipoDocumentoTexto;
	}

	public void setTipoDocumentoTexto(TipoDocumentoTexto tipoDocumentoTexto) {
		this.tipoDocumentoTexto = tipoDocumentoTexto;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name="SEQ_DOCUMENTO", referencedColumnName="SEQ_DOCUMENTO", updatable = false, insertable = false)
	@LazyToOne(value=LazyToOneOption.NO_PROXY)
	@org.hibernate.annotations.NotFound(action=NotFoundAction.IGNORE)
	public AssinaturaDigitalView getAssinaturaDigitalView() {
		return assinaturaDigitalView;
	}

	public void setAssinaturaDigitalView(AssinaturaDigitalView assinaturaDigitalView) {
		this.assinaturaDigitalView = assinaturaDigitalView;
	}
	
	@Transient
	public Boolean podeSalvar () {
		TipoSituacaoDocumento situacao = this.getTipoSituacaoDocumento();
		if ( TipoSituacaoDocumento.REVISADO.equals( situacao ) || 
				TipoSituacaoDocumento.ASSINADO_DIGITALMENTE.equals( situacao ) || 
				TipoSituacaoDocumento.LIBERADO_PARA_REVISAO.equals( situacao ) || 
				TipoSituacaoDocumento.ASSINADO_MANUALMENTE.equals( situacao )) {
			
			return false;
		} else {
			return true;
		}
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name="SEQ_DOCUMENTO", referencedColumnName="SEQ_DOCUMENTO", updatable = false, insertable = false)
	@LazyToOne(value=LazyToOneOption.NO_PROXY)
	@org.hibernate.annotations.NotFound(action=NotFoundAction.IGNORE)
	public DocumentoEletronicoView getDocumentoEletronicoView() {
		return documentoEletronicoView;
	}

	public void setDocumentoEletronicoView(
			DocumentoEletronicoView documentoEletronicoView) {
		this.documentoEletronicoView = documentoEletronicoView;
	}	

}
