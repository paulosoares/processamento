package br.gov.stf.estf.entidade.documento;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;

@Entity
@org.hibernate.annotations.Entity(persister = "br.gov.stf.estf.entidade.documento.DocumentoEletronicoPersister")
@Table(schema = "DOC", name = "DOCUMENTO_TEMP")
public class DocumentoEletronico extends ESTFAuditavelBaseEntity<Long> {

	private static final long serialVersionUID = -3482371350083541325L;

	private String siglaSistema;
	private String descricaoStatusDocumento;
	private String tipoAcesso;
	private TipoArquivo tipoArquivo;
	private byte[] arquivo;
	private String descricaoMotivoCancelamento;
	private Date dataCancelamento;
	private String siglaUsuarioCancelamento;
	private String hashValidacao;

	public static final String TIPO_ACESSO_PUBLICO = "PUB";
	public static final String TIPO_ACESSO_INTERNO = "INT";
	public static final String SIGLA_DESCRICAO_STATUS_ASSINADO = "ASS";
	public static final String SIGLA_DESCRICAO_STATUS_RASCUNHO = "RAS";
	public static final String SIGLA_DESCRICAO_STATUS_DIGITALIZADO = "DIG";
	public static final String SIGLA_DESCRICAO_STATUS_CANCELADO = "CAN";
	public static final String SIGLA_DESCRICAO_STATUS_AGUARDANDO = "AGD";
	public static final String SIGLA_DESCRICAO_STATUS_EXTERNO = "EXT";

	@Id
	@Column(name = "SEQ_DOCUMENTO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "DOC.SEQ_DOCUMENTO", allocationSize = 1)
	public Long getId() {
		return id;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "BIN_ARQUIVO", unique = false, nullable = false, insertable = true, updatable = true, length = 4000)
	public byte[] getArquivo() {
		return arquivo;
	}

	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DAT_CANCELAMENTO", unique = false, nullable = true, insertable = true, updatable = true)
	public Date getDataCancelamento() {
		return dataCancelamento;
	}

	public void setDataCancelamento(Date dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}

	@Column(name = "DSC_MOTIVO_CANCELAMENTO", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
	public String getDescricaoMotivoCancelamento() {
		return descricaoMotivoCancelamento;
	}

	public void setDescricaoMotivoCancelamento(String descricaoMotivoCancelamento) {
		this.descricaoMotivoCancelamento = descricaoMotivoCancelamento;
	}

	@Column(name = "DSC_STATUS_DOCUMENTO", unique = false, nullable = false, insertable = true, updatable = true, length = 3)
	public String getDescricaoStatusDocumento() {
		return descricaoStatusDocumento;
	}

	public void setDescricaoStatusDocumento(String descricaoStatusDocumento) {
		this.descricaoStatusDocumento = descricaoStatusDocumento;
	}

	@Column(name = "SIG_SISTEMA", unique = false, nullable = true, insertable = true, updatable = true, length = 15)
	public String getSiglaSistema() {
		return siglaSistema;
	}

	public void setSiglaSistema(String siglaSistema) {
		this.siglaSistema = siglaSistema;
	}

	@Column(name = "SIG_USUARIO_CANCELAMENTO", unique = false, nullable = true, insertable = true, updatable = true, length = 100)
	public String getSiglaUsuarioCancelamento() {
		return siglaUsuarioCancelamento;
	}

	public void setSiglaUsuarioCancelamento(String siglaUsuarioCancelamento) {
		this.siglaUsuarioCancelamento = siglaUsuarioCancelamento;
	}

	@Column(name = "TIP_ACESSO", unique = false, nullable = true, insertable = true, updatable = true, length = 3)
	public String getTipoAcesso() {
		return tipoAcesso;
	}

	public void setTipoAcesso(String tipoAcesso) {
		this.tipoAcesso = tipoAcesso;
	}

	@Column(name = "SEQ_TIPO_ARQUIVO", insertable = true, updatable = true)
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.documento.TipoArquivo"),
			@Parameter(name = "idClass", value = "java.lang.Long") })
	public TipoArquivo getTipoArquivo() {
		return tipoArquivo;
	}

	public void setTipoArquivo(TipoArquivo tipoArquivo) {
		this.tipoArquivo = tipoArquivo;
	}

	/**
	 * Verifica se o status atual do documento corresponde ao status representando pela String passada como parâmetro. 
	 * Se a String não for um status válido, sempre será retornado false.
	 * 
	 * @param siglaStatusDocumento
	 * @return
	 * @see #SIGLA_DESCRICAO_STATUS_AGUARDANDO
	 * @see #SIGLA_DESCRICAO_STATUS_ASSINADO
	 * @see #SIGLA_DESCRICAO_STATUS_CANCELADO
	 * @see #SIGLA_DESCRICAO_STATUS_DIGITALIZADO
	 * @see #SIGLA_DESCRICAO_STATUS_RASCUNHO
	 */
	@Transient
	public boolean isStatusDocumento(String siglaStatusDocumento) {
		return StringUtils.equals(getDescricaoStatusDocumento(), siglaStatusDocumento);
	}

	@Column(name="TXT_HASH_VALIDACAO")
	public String getHashValidacao() {
		return hashValidacao;
	}

	public void setHashValidacao(String hashValidacao) {
		this.hashValidacao = hashValidacao;
	}
}
