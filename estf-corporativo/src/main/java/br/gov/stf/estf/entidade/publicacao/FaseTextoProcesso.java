package br.gov.stf.estf.entidade.publicacao;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.gov.stf.estf.entidade.usuario.Usuario;

@Entity
@Table(name="FASE_TEXTO_PROCESSO", schema="DOC")
public class FaseTextoProcesso extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = 1793709233531445139L;
	private Texto texto;
	private CabecalhoTexto cabecalhoTexto;
	private ArquivoEletronico arquivoEletronico;
	private byte[] hashArquivoEletronico;
	
	private FaseTexto tipoFaseTextoDocumento = FaseTexto.NAO_ELABORADO;
	private FaseTexto tipoFaseTextoDocumentoDestino;
	private DocumentoEletronico documentoEletronico;
	private Date dataTransicao;
	private Usuario usuarioTransicao;
	private String observacao;
	private String assinadorCertificado;
	
	@Id
	@Column( name="SEQ_FASE_TEXTO_PROCESSO" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="DOC.SEQ_FASE_TEXTO_PROCESSO", allocationSize=1 )
	public Long getId() {
		return id;
	}	
    
	@ManyToOne( cascade={}, fetch=FetchType.LAZY )
	@JoinColumn( name="SEQ_TEXTOS", unique=false, nullable=true, insertable=true, updatable=true )    
	public Texto getTexto() {
		return texto;
	}
	public void setTexto(Texto texto) {
		this.texto = texto;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_CABECALHO_TEXTO")
	@Cascade(value = {CascadeType.ALL, CascadeType.DELETE_ORPHAN})
	public CabecalhoTexto getCabecalhoTexto() {
		return cabecalhoTexto;
	}

	public void setCabecalhoTexto(CabecalhoTexto cabecalhoTexto) {
		this.cabecalhoTexto = cabecalhoTexto;
	}

	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_ARQUIVO_ELETRONICO", unique=false, nullable=true, insertable=true, updatable=true)
	public ArquivoEletronico getArquivoEletronico() {
		return arquivoEletronico;
	}
	public void setArquivoEletronico(ArquivoEletronico arquivoEletronico) {
		this.arquivoEletronico = arquivoEletronico;
	}

	@ManyToOne(cascade={}, fetch = FetchType.LAZY)
	@JoinColumn(name="SEQ_DOCUMENTO")
	public DocumentoEletronico getDocumentoEletronico() {
		return documentoEletronico;
	}

	public void setDocumentoEletronico(DocumentoEletronico documento) {
		this.documentoEletronico = documento;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DAT_FASE")
	public Date getDataTransicao() {
		return dataTransicao;
	}

	public void setDataTransicao(Date dataTransicao) {
		this.dataTransicao = dataTransicao;
	}

	@Column(name="COD_TIPO_FASE_TEXTO", insertable = true, updatable = true, nullable = true)
	@Type(type="br.gov.stf.framework.util.GenericEnumUserType", parameters={
				@Parameter( name = "enumClass", 
						    value = "br.gov.stf.estf.entidade.documento.tipofase.FaseTexto"),
				@Parameter( name = "identifierMethod",
							value = "getCodigoFase" ),
				@Parameter( name = "nullValue",
						    value = "NAO_ELABORADO")})
	public FaseTexto getTipoFaseTextoDocumento() {
		return tipoFaseTextoDocumento;
	}

	public void setTipoFaseTextoDocumento(FaseTexto tipoFaseTextoDocumento) {
		this.tipoFaseTextoDocumento = tipoFaseTextoDocumento;
	}
	
	@Column(name="COD_TIPO_FASE_TEXTO_DESTINO", insertable = true, updatable = true, nullable = true)
	@Type(type="br.gov.stf.framework.util.GenericEnumUserType", parameters={
				@Parameter( name = "enumClass", 
						    value = "br.gov.stf.estf.entidade.documento.tipofase.FaseTexto"),
				@Parameter( name = "identifierMethod",
							value = "getCodigoFase" ),
				@Parameter( name = "nullValue",
						    value = "NAO_ELABORADO")})
	public FaseTexto getTipoFaseTextoDocumentoDestino() {
		return tipoFaseTextoDocumentoDestino;
	}
	
	public void setTipoFaseTextoDocumentoDestino(FaseTexto tipoFaseTextoDocumentoDestino) {
		this.tipoFaseTextoDocumentoDestino = tipoFaseTextoDocumentoDestino;
	}

	@Column(name="BIN_HASH_TEXTO")
	public byte[] getHashArquivoEletronico() {
		return hashArquivoEletronico;
	}
	
	public void setHashArquivoEletronico(byte[] hashArquivoEletronico) {
		this.hashArquivoEletronico = hashArquivoEletronico;
	}

	/**
	 * @param usuarioTransicao the usuarioTransicao to set
	 */
	public void setUsuarioTransicao(Usuario usuarioTransicao) {
		this.usuarioTransicao = usuarioTransicao;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USU_INCLUSAO", unique = false, nullable = true)
	@org.hibernate.annotations.NotFound(action = NotFoundAction.IGNORE)
	public Usuario getUsuarioTransicao() {
		return usuarioTransicao;
	}

	@Column(name="TXT_OBSERVACAO")
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@Column(name="DSC_ASSINADOR_DOCUMENTO")
	public String getAssinadorCertificado() {
		return assinadorCertificado;
	}

	public void setAssinadorCertificado(String assinadorCertificado) {
		this.assinadorCertificado = assinadorCertificado;
	}
}