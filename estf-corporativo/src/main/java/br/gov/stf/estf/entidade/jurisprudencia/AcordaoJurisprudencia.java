/**
 * 
 */
package br.gov.stf.estf.entidade.jurisprudencia;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;

/**
 * @author Paulo.Estevao
 * @since 20.08.2012
 */
@Entity
@Table(schema = "BRS", name = "SJUR")
public class AcordaoJurisprudencia extends ESTFAuditavelBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1395549420029571221L;
	
	private Long id;
	private String acordaoMesmoSentido;
	private String indexacao;
	private String legislacao;
	private String doutrina;
	private String observacao;
	private String observacaoLink;
	private String publicacao;
	private String partes;
	private ObjetoIncidente<?> objetoIncidente;
	private Long numeroProcesso;
	private String ementa;
	private String decisao;
	private String sessao;
	private String procedenciaGeografica;
	private String referenciaProcesso;
	private String classeUnificada;
	private String ministroRelator;
	private String descricaoObservacao;
	private String tese;
	private String siglaClasseUnificada;
	private String assunto;
	private String dataJulgamento;
	private Date dataPublicacao;
	private String ministroRevisor;
	private String ministroRelatorAcordao;
	private Long numeroMinistroRelator;
	private Long numeroMinistroRevisor;
	private Long numeroMinistroRelatorAcordao;
	private Long idDocumento;
	private Long idInteiroTeorAcordao;
	private String tema;
	private String tipoTese;
	
	@Override
	@Id
	@Column(name = "SEQ_SJUR")
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "DOC_ACORDAO_MESMO_SENTIDO")
	public String getAcordaoMesmoSentido() {
		return acordaoMesmoSentido;
	}
	
	public void setAcordaoMesmoSentido(String acordaoMesmoSentido) {
		this.acordaoMesmoSentido = acordaoMesmoSentido;
	}
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "DOC_INDEXACAO")
	public String getIndexacao() {
		return indexacao;
	}
	
	public void setIndexacao(String indexacao) {
		this.indexacao = indexacao;
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
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "DOC_TEXTO_DOUTRINA")
	public String getDoutrina() {
		return doutrina;
	}
	
	public void setDoutrina(String doutrina) {
		this.doutrina = doutrina;
	}
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "DSC_OBSERVACAO")
	public String getObservacao() {
		return observacao;
	}
	
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	// Removido o updatable, pois os usuários da coordenadoria de analise podem fazer correções na data de publicação.
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "DSC_INFORMACAO_PUBLICACAO", insertable=false)
	public String getPublicacao() {
		return publicacao;
	}
	
	public void setPublicacao(String publicacao) {
		this.publicacao = publicacao;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "DOC_NOME_PARTE")
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
	@Column(name = "DOC_TEXTO_EMENTA")
	public String getEmenta() {
		return ementa;
	}

	public void setEmenta(String ementa) {
		this.ementa = ementa;
	}
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "DOC_DECISAO")
	public String getDecisao() {
		return decisao;
	}

	public void setDecisao(String decisao) {
		this.decisao = decisao;
	}
	
	@Column(name = "DSC_SESSAO")
	public String getSessao() {
		return sessao;
	}

	public void setSessao(String sessao) {
		this.sessao = sessao;
	}
	@Column(name = "DSC_PROCEDENCIA_GEOGRAFICA")
	public String getProcedenciaGeografica() {
		return procedenciaGeografica;
	}

	public void setProcedenciaGeografica(String procedenciaGeografica) {
		this.procedenciaGeografica = procedenciaGeografica;
	}
	@Column(name = "DSC_REFERENCIA_PROCESSO")
	public String getReferenciaProcesso() {
		return referenciaProcesso;
	}

	public void setReferenciaProcesso(String referenciaProcesso) {
		this.referenciaProcesso = referenciaProcesso;
	}
	@Column(name = "DSC_CLASSE_UNIF")
	public String getClasseUnificada() {
		return classeUnificada;
	}

	public void setClasseUnificada(String classeUnificada) {
		this.classeUnificada = classeUnificada;
	}
	@Column(name = "NOM_MINISTRO_RELATOR")
	public String getMinistroRelator() {
		return ministroRelator;
	}

	public void setMinistroRelator(String ministroRelator) {
		this.ministroRelator = ministroRelator;
	}
	
	//Existe o campo observação -> Lazy
	//Existe descricaoObservacao -> Eager [utilizado em relatórios]
	@Column(name = "DSC_OBSERVACAO", insertable = false, updatable = false)
	public String getDescricaoObservacao() {
		return descricaoObservacao;
	}

	public void setDescricaoObservacao(String descricaoObservacao) {
		this.descricaoObservacao = descricaoObservacao;
	}
	
	/**
	 * @return the tema
	 */
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "DSC_TESE")
	public String getTese() {
		return tese;
	}

	/**
	 * @param tese the tema to set
	 */
	public void setTese(String tese) {
		this.tese = tese;
	}

	/**
	 * @return the siglaClasseUnificada
	 */
	@Column(name = "SIG_CLASSE_UNIF")
	public String getSiglaClasseUnificada() {
		return siglaClasseUnificada;
	}

	/**
	 * @param siglaClasseUnificada the siglaClasseUnificada to set
	 */
	public void setSiglaClasseUnificada(String siglaClasseUnificada) {
		this.siglaClasseUnificada = siglaClasseUnificada;
	}

	/**
	 * @return the descricaoAssunto
	 */
	@Column(name = "DSC_ASSUNTO")
	public String getAssunto() {
		return assunto;
	}

	/**
	 * @param descricaoAssunto the descricaoAssunto to set
	 */
	public void setAssunto(String descricaoAssunto) {
		this.assunto = descricaoAssunto;
	}

	/**
	 * @return the dataJulgamento
	 */
	@Column(name = "DAT_JULGAMENTO")
	public String getDataJulgamento() {
		return dataJulgamento;
	}

	/**
	 * @param dataJulgamento the dataJulgamento to set
	 */
	public void setDataJulgamento(String dataJulgamento) {
		this.dataJulgamento = dataJulgamento;
	}

	@Transient
	public Date getDateDataJulgamento(){
		
		try {
			return new SimpleDateFormat("yyyyMMdd").parse(this.getDataJulgamento());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Formula("(SELECT STF.FNC_DATA_ACORDAO(SEQ_OBJETO_INCIDENTE) FROM DUAL)")
	public Date getDataPublicacao() {
		return dataPublicacao;
	}

	/**
	 * @param dataPublicacao the dataPublicacao to set
	 */
	public void setDataPublicacao(Date dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}

	/**
	 * @return the ministroRevisor
	 */
	@Column(name = "NOM_MINISTRO_REVISOR")
	public String getMinistroRevisor() {
		return ministroRevisor;
	}

	/**
	 * @param ministroRevisor the ministroRevisor to set
	 */
	public void setMinistroRevisor(String ministroRevisor) {
		this.ministroRevisor = ministroRevisor;
	}

	/**
	 * @return the ministroRelatorAcordao
	 */
	@Column(name = "NOM_RELATOR_ACORDAO")
	public String getMinistroRelatorAcordao() {
		return ministroRelatorAcordao;
	}

	/**
	 * @param ministroRelatorAcordao the ministroRelatorAcordao to set
	 */
	public void setMinistroRelatorAcordao(String ministroRelatorAcordao) {
		this.ministroRelatorAcordao = ministroRelatorAcordao;
	}

	/**
	 * @return the numeroMinistroRelator
	 */
	@Column(name = "NUM_MINISTRO_RELATOR")
	public Long getNumeroMinistroRelator() {
		return numeroMinistroRelator;
	}

	/**
	 * @param numeroMinistroRelator the numeroMinistroRelator to set
	 */
	public void setNumeroMinistroRelator(Long numeroMinistroRelator) {
		this.numeroMinistroRelator = numeroMinistroRelator;
	}

	/**
	 * @return the numeroMinistroRedator
	 */
	@Column(name = "NUM_MINISTRO_REVISOR")
	public Long getNumeroMinistroRevisor() {
		return numeroMinistroRevisor;
	}

	/**
	 * @param numeroMinistroRevisor the numeroMinistroRedator to set
	 */
	public void setNumeroMinistroRevisor(Long numeroMinistroRevisor) {
		this.numeroMinistroRevisor = numeroMinistroRevisor;
	}

	/**
	 * @return the numeroMinistroRelatorAcordao
	 */
	@Column(name = "NUM_RELATOR_ACORDAO")
	public Long getNumeroMinistroRelatorAcordao() {
		return numeroMinistroRelatorAcordao;
	}

	/**
	 * @param numeroMinistroRelatorAcordao the numeroMinistroRelatorAcordao to set
	 */
	public void setNumeroMinistroRelatorAcordao(
			Long numeroMinistroRelatorAcordao) {
		this.numeroMinistroRelatorAcordao = numeroMinistroRelatorAcordao;
	}

	/**
	 * @return the tema
	 */
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "DSC_TEMA")
	public String getTema() {
		return tema;
	}

	/**
	 * @param tema the tema to set
	 */
	public void setTema(String tema) {
		this.tema = tema;
	}

	/**
	 * @return the tipoTese
	 */
	@Column(name = "TIP_TESE")
	public String getTipoTese() {
		return tipoTese;
	}

	/**
	 * @param tipoTese the tipoTese to set
	 */
	public void setTipoTese(String tipoTese) {
		this.tipoTese = tipoTese;
	}
	
	/**
	 * @return the idDocumento
	 */
	@Column(name = "SEQ_DOCUMENTO")
	public Long getIdDocumento() {
		return idDocumento;
	}

	/**
	 * @param idDocumento the idDocumento to set
	 */
	public void setIdDocumento(Long idDocumento) {
		this.idDocumento = idDocumento;
	}

	/**
	 * @return the idInteiroTeorAcordao
	 */
	@Column(name = "SEQ_INTEIRO_TEOR_ACORDAO")
	public Long getIdInteiroTeorAcordao() {
		return idInteiroTeorAcordao;
	}

	/**
	 * @param idInteiroTeorAcordao the idInteiroTeorAcordao to set
	 */
	public void setIdInteiroTeorAcordao(Long idInteiroTeorAcordao) {
		this.idInteiroTeorAcordao = idInteiroTeorAcordao;
	}
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "DSC_OBSERVACAO_LINK")
	public String getObservacaoLink() {
		return observacaoLink;
	}

	public void setObservacaoLink(String observacaoLink) {
		this.observacaoLink = observacaoLink;
	}
}
