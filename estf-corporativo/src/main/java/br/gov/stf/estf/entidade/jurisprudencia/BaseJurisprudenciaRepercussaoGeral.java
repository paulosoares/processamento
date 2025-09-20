/**
 * 
 */
package br.gov.stf.estf.entidade.jurisprudencia;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;

/**
 * @author henrique.bona
 */
@Entity
@Table(schema = "BRS", name = "REPERCUSSAO_GERAL")
public class BaseJurisprudenciaRepercussaoGeral extends
		ESTFAuditavelBaseEntity<Long> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1395549420029571221L;

	// Informações do processo
	private Long id;
	private ObjetoIncidente<?> objetoIncidente;
	private Long numeroProcesso;
	private Long seqProcessoPublicados;
	private String siglaClasseUnificada;
	private String tipoSessao;

	// Análises
	private String indexacao;
	private String legislacao;
	private String doutrina;
	private String partes;
	private String publicacao;
	private String tese;
	private String tema;
	private String observacao;

	// TODO Incluir mesmo sentido - SEM CAMPO NA BASE DE DADOS

	// Campos específicos da repercussão geral
	private String processoRelacionado;

	// Controle
	// Mas qual a necessidade?
	private Boolean ativo;
	private Boolean liberado;

	@Override
	@Id
	@Column(name = "SEQ_REPERCUSSAO_GERAL")
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	//TODO Corrigir: A aplicação RepercussaoGeral[VB] usa o campo DSC_ACORDAO_CITADO para armazenar observação
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "DSC_ACORDAO_CITADO")
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@Basic(fetch = FetchType.LAZY)
	@Column(name = "DSC_INDEXACAO")
	public String getIndexacao() {
		return indexacao;
	}

	public void setIndexacao(String documentoIndexacao) {
		this.indexacao = documentoIndexacao;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "DOC_TEXTO_LEGISLACAO")
	public String getLegislacao() {
		return legislacao;
	}

	public void setLegislacao(String legislacao) {
		this.legislacao = legislacao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE")
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	@Basic(fetch = FetchType.LAZY)
	@Column(name = "DSC_TEXTO_DOUTRINA")
	public String getDoutrina() {
		return doutrina;
	}

	public void setDoutrina(String doutrina) {
		this.doutrina = doutrina;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "DOC_LISTA_PARTE")
	public String getPartes() {
		return partes;
	}

	public void setPartes(String partes) {
		this.partes = partes;
	}

	@Column(name = "NUM_PROCESSO")
	public Long getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(Long numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}


	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "DOC_PROCESSO_RELACIONADO")
	public String getProcessoRelacionado() {
		return processoRelacionado;
	}

	public void setProcessoRelacionado(String documento) {
		this.processoRelacionado = documento;
	}

	@Column(name = "DSC_INFORMACAO_PUBLICACAO")
	public String getPublicacao() {
		return publicacao;
	}

	public void setPublicacao(String publicacao) {
		this.publicacao = publicacao;
	}

	@Column(name = "TIP_AMBIENTE_SESSAO")
	public String getTipoSessao() {
		return tipoSessao;
	}

	public void setTipoSessao(String tipoSessao) {
		this.tipoSessao = tipoSessao;
	}

	@Column(name = "SIG_CLASSE_UNIF")
	public String getSiglaClasseUnificada() {
		return siglaClasseUnificada;
	}

	public void setSiglaClasseUnificada(String sigla) {
		this.siglaClasseUnificada = sigla;
	}

	// **
	/**
	 * @return the tese
	 */
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "TESE")
	public String getTese() {
		return tese;
	}

	/**
	 * @param tese
	 *            the tese to set
	 */
	public void setTese(String tese) {
		this.tese = tese;
	}

	// **
	/**
	 * @return the tema
	 */
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "TEMA")
	public String getTema() {
		return tema;
	}

	/**
	 * @param tema
	 *            the tema to set
	 */
	public void setTema(String tema) {
		this.tema = tema;
	}

	/**
	 * @return the ativo
	 */
	@Column(name = "FLG_ATIVO", nullable = false)
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAtivo() {
		return ativo;
	}

	/**
	 * @param ativo
	 *            the ativo to set
	 */
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	/**
	 * @return the liberado
	 */
	@Column(name = "FLG_LIBERADO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getLiberado() {
		return liberado;
	}

	/**
	 * @param liberado
	 *            the liberado to set
	 */
	public void setLiberado(Boolean liberado) {
		this.liberado = liberado;
	}

	/**
	 * @return the seqProcessoPublicados
	 */
	@Column(name = "SEQ_PROCESSO_PUBLICADOS")
	public Long getSeqProcessoPublicados() {
		return seqProcessoPublicados;
	}

	/**
	 * @param seqProcessoPublicados
	 *            the seqProcessoPublicados to set
	 */
	public void setSeqProcessoPublicados(Long seqProcessoPublicados) {
		this.seqProcessoPublicados = seqProcessoPublicados;
	}

}
