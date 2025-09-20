package br.gov.stf.estf.entidade.documento;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.ObjectUtils;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity()
@Table(schema = "JUDICIARIO", name = "DOCUMENTO_COMUNICACAO")
public class DocumentoComunicacao extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = 5100795422030716200L;

	private Long id;
	private Comunicacao comunicacao;
	private DocumentoEletronico documentoEletronico;
	// private Date dataRevisao;
	private String usuarioRevisao;
	private TipoSituacaoDocumento tipoSituacaoDocumento;
	private DocumentoEletronicoView documentoEletronicoView;
	private List<AssinaturaDigitalView> assinaturaDigitalView;
	private String dscObservacao;

	@Override
	@Id
	@Column(name = "SEQ_DOCUMENTO_COMUNICACAO", nullable = false)
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_DOCUMENTO_COMUNICACAO", allocationSize = 1)
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_COMUNICACAO", unique = true)
	public Comunicacao getComunicacao() {
		return comunicacao;
	}

	public void setComunicacao(Comunicacao comunicacao) {
		this.comunicacao = comunicacao;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_DOCUMENTO", unique = true)
	public DocumentoEletronico getDocumentoEletronico() {
		return documentoEletronico;
	}

	public void setDocumentoEletronico(DocumentoEletronico documento) {
		this.documentoEletronico = documento;
	}

	@Column(name = "SEQ_TIPO_SITUACAO_DOCUMENTO", insertable = true, updatable = true)
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento"),
			@Parameter(name = "identifierMethod", value = "getCodigo") })
	public TipoSituacaoDocumento getTipoSituacaoDocumento() {
		return tipoSituacaoDocumento;
	}

	public void setTipoSituacaoDocumento(TipoSituacaoDocumento tipoSituacaoDocumento) {
		this.tipoSituacaoDocumento = tipoSituacaoDocumento;
	}

	/*	@Temporal(TemporalType.TIMESTAMP)
		@Column(name="DAT_REVISAO", unique=false, nullable=true, insertable=true, updatable=true)
		public Date getDataRevisao() {
			return dataRevisao;
		}

		public void setDataRevisao(Date dataRevisao) {
			this.dataRevisao = dataRevisao;
		}*/

	@Column(name = "USU_REVISAO", unique = false, nullable = true, insertable = true, updatable = true)
	public String getUsuarioRevisao() {
		return usuarioRevisao;
	}

	public void setUsuarioRevisao(String usuarioRevisao) {
		this.usuarioRevisao = usuarioRevisao;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_DOCUMENTO", referencedColumnName = "SEQ_DOCUMENTO", updatable = false, insertable = false)
	@LazyToOne(value = LazyToOneOption.NO_PROXY)
	public List<AssinaturaDigitalView> getAssinaturaDigitalView() {
		return assinaturaDigitalView;
	}

	public void setAssinaturaDigitalView(List<AssinaturaDigitalView> assinaturaDigitalView) {
		this.assinaturaDigitalView = assinaturaDigitalView;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_DOCUMENTO", referencedColumnName = "SEQ_DOCUMENTO", updatable = false, insertable = false)
	@LazyToOne(value = LazyToOneOption.NO_PROXY)
	public DocumentoEletronicoView getDocumentoEletronicoView() {
		return documentoEletronicoView;
	}

	public void setDocumentoEletronicoView(DocumentoEletronicoView documentoEletronicoView) {
		this.documentoEletronicoView = documentoEletronicoView;
	}

	@Column(name = "DSC_OBSERVACAO", unique = false, nullable = true, insertable = true, updatable = true)
	public String getDscObservacao() {
		return dscObservacao;
	}

	public void setDscObservacao(String dscObservacao) {
		this.dscObservacao = dscObservacao;
	}

	@Transient
	public Boolean podeSalvar() {
		TipoSituacaoDocumento situacao = this.getTipoSituacaoDocumento();

		if (TipoSituacaoDocumento.REVISADO.equals(situacao) || TipoSituacaoDocumento.ASSINADO_DIGITALMENTE.equals(situacao)
				|| TipoSituacaoDocumento.LIBERADO_PARA_REVISAO.equals(situacao) || TipoSituacaoDocumento.ASSINADO_MANUALMENTE.equals(situacao)) {

			return false;
		} else {
			return true;
		}
	}

	/**
	 * Verifica se o {@link TipoSituacaoDocumento} atual corresponde ao informado.
	 * 
	 * @param situacaoDocumento
	 *            a situação a ser verificada
	 * @return <code>true</code> caso a situação atual seja a informada, <code>false</code> caso contrário
	 */
	public boolean isTipoSituacaoDocumento(TipoSituacaoDocumento situacaoDocumento) {
		return ObjectUtils.equals(getTipoSituacaoDocumento() != null ? getTipoSituacaoDocumento().getCodigo() : null, situacaoDocumento.getCodigo());
	}
}
