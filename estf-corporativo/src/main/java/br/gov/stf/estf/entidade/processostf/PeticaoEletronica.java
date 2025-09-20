package br.gov.stf.estf.entidade.processostf;

import java.sql.Blob;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "PETICAO_ELETRONICA", schema = "JUDICIARIO")
public class PeticaoEletronica extends ObjetoIncidente {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1745720576773262570L;

	private String descricaoRecibo;
	private Date dataPeticionamento;
	private TipoPeticaoEletronica tipoPeticaoEletronica;
	private TipoRecurso tipoRecurso;
	private SituacaoConstante situacao;
	private ObjetoIncidente<?> objetoIncidenteVinculado;
	private String siglaClassePreferencia;
	private TipoReclamacao tipoReclamacao;
	private TipoConfidencialidade tipoConfidencialidade;
	private Blob carimbo;

	@Column(name = "DSC_RECIBO", nullable = true, updatable = false, insertable = false, unique = false, length = 2000)
	public String getDescricaoRecibo() {
		return descricaoRecibo;
	}

	public void setDescricaoRecibo(String descricaoRecibo) {
		this.descricaoRecibo = descricaoRecibo;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DAT_PETICIONAMENTO", nullable = true, updatable = false, insertable = false, unique = false)
	public Date getDataPeticionamento() {
		return dataPeticionamento;
	}

	public void setDataPeticionamento(Date dataPeticionamento) {
		this.dataPeticionamento = dataPeticionamento;
	}

	@Column(name = "TIP_PETICAO_ELETRONICA", nullable = true, updatable = false, insertable = false, unique = false, length = 2)
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.processostf.TipoPeticaoEletronica"),
			@Parameter(name = "idClass", value = "java.lang.String") })
	public TipoPeticaoEletronica getTipoPeticaoEletronica() {
		return tipoPeticaoEletronica;
	}

	public void setTipoPeticaoEletronica(TipoPeticaoEletronica tipoPeticaoEletronica) {
		this.tipoPeticaoEletronica = tipoPeticaoEletronica;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TIPO_RECURSO", nullable = true, updatable = false, insertable = false, unique = false)
	public TipoRecurso getTipoRecurso() {
		return tipoRecurso;
	}

	public void setTipoRecurso(TipoRecurso tipoRecurso) {
		this.tipoRecurso = tipoRecurso;
	}

	@Column(name = "COD_SITUACAO", nullable = false, updatable = false, insertable = false, unique = false, length = 1)
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.processostf.SituacaoConstante"),
			@Parameter(name = "idClass", value = "java.lang.String") })
	public SituacaoConstante getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoConstante situacao) {
		this.situacao = situacao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE_VINCULADO")
	@LazyToOne(LazyToOneOption.NO_PROXY)
	public ObjetoIncidente<?> getObjetoIncidenteVinculado() {
		return objetoIncidenteVinculado;
	}

	public void setObjetoIncidenteVinculado(ObjetoIncidente<?> objetoIncidenteVinculado) {
		this.objetoIncidenteVinculado = objetoIncidenteVinculado;
	}

	@Column(name = "SIG_CLASSE_REFERENCIA", nullable = true, updatable = false, insertable = false, unique = false)
	public String getSiglaClassePreferencia() {
		return siglaClassePreferencia;
	}

	public void setSiglaClassePreferencia(String siglaClassePreferencia) {
		this.siglaClassePreferencia = siglaClassePreferencia;
	}

	@Column(name = "TIP_RECLAMACAO", nullable = true, updatable = false, insertable = false, unique = false, length = 1)
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.processostf.TipoReclamacao"),
			@Parameter(name = "idClass", value = "java.lang.String") })
	public TipoReclamacao getTipoReclamacao() {
		return tipoReclamacao;
	}

	public void setTipoReclamacao(TipoReclamacao tipoReclamacao) {
		this.tipoReclamacao = tipoReclamacao;
	}

	/**
	* Tipo do Objeto Incidente. Pode ter os valores: PR (Processo), PI
	* (Protocolo), PA (Petição), RC (Recurso) ou IJ (Incidente Julgamento).
	*/
	@Column(name = "TIP_CONFIDENCIALIDADE", nullable = true, updatable = false, insertable = false, unique = false, length = 2)
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.processostf.TipoConfidencialidade"),
			@Parameter(name = "idClass", value = "java.lang.String"),
			@Parameter(name = "valueOfMethod", value = "valueOfCodigo") } )
	public TipoConfidencialidade getTipoConfidencialidade() {
		return tipoConfidencialidade;
	}

	public void setTipoConfidencialidade(TipoConfidencialidade tipoConfidencialidade) {
		this.tipoConfidencialidade = tipoConfidencialidade;
	}

	@Lob
	@Column(name = "DOC_CARIMBO", nullable = true, updatable = false, insertable = false, unique = false, length = 4000)
	@Basic(fetch = FetchType.LAZY)
	public Blob getCarimbo() {
		return carimbo;
	}

	public void setCarimbo(Blob carimbo) {
		this.carimbo = carimbo;
	}

	@Transient
	@Override
	public String getIdentificacao() {
		return null;
	}

}
