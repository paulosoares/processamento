/**
 * 
 */
package br.gov.stf.estf.entidade.jurisprudencia;

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

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.publicacao.ProcessoPublicado;

@Entity
@Table(schema = "BRS", name = "DESPACHO")
public class Despacho extends ESTFAuditavelBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1395549420029571221L;
	
	private Long id;
	private String referenciaProcesso;
	private String siglaClasseUnificada;
	private String classeUnificada;
	private Long numeroProcesso;
	private Date dataJulgamento;
	private String procedenciaGeografica;
	private String ministroRelator;
	private String numeroMinistroRelator;	
	private Boolean despachoPresidente;
	private String nomePresidente;
	private String nomeMinistroDecisaoMonocratica;
	private Boolean isLegislacao;
	private String doutrina;
	private String despachoMesmoSentido;
	private String textoDespacho;	
	private String indexacao;
	private String legislacao;
	private String observacao;
	private String partes;
	private String publicacao;
	private ObjetoIncidente<?> objetoIncidente;
	private ProcessoPublicado processoPublicado;
	private Boolean liberadoInternet;
	
	
	@Override
	@Id
	@Column(name = "SEQ_DESPACHO")
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "DOC_DESPACHO_MESMO_SENTIDO")
	public String getDespachoMesmoSentido() {
		return despachoMesmoSentido;
	}
	
	public void setDespachoMesmoSentido(String despachoMesmoSentido) {
		this.despachoMesmoSentido = despachoMesmoSentido;
	}
	
	
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "DSC_INDEXACAO")
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
	
	
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "DSC_TEXTO_DOUTRINA")
	public String getDoutrina() {
		return doutrina;
	}
	
	public void setDoutrina(String doutrina) {
		this.doutrina = doutrina;
	}
		
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "DSC_OBSERVACAO")
	public String getObservacao() {
		return observacao;
	}
	
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}


	@Basic(fetch = FetchType.LAZY)
	@Column(name = "DSC_INFORMACAO_PUBLICACAO")
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
	
	@Column(name = "DSC_CLASSE_PROCESSO")
	public String getClasseUnificada() {
		return classeUnificada;
	}

	public void setClasseUnificada(String classeUnificada) {
		this.classeUnificada = classeUnificada;
	}
	@Column(name = "NOM_RELATOR")
	public String getMinistroRelator() {
		return ministroRelator;
	}

	public void setMinistroRelator(String ministroRelator) {
		this.ministroRelator = ministroRelator;
	}
	
	
	/**
	 * @return the siglaClasseUnificada
	 */
	@Column(name = "SIG_CLASSE_PROCESSO")
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
	 * @return the dataJulgamento
	 */
	@Column(name = "DAT_JULGAMENTO")
	public Date getDataJulgamento() {
		return dataJulgamento;
	}

	/**
	 * @param dataJulgamento the dataJulgamento to set
	 */
	public void setDataJulgamento(Date dataJulgamento) {
		this.dataJulgamento = dataJulgamento;
	}

	

	/**
	 * @return the numeroMinistroRelator
	 */
	@Column(name = "NUM_RELATOR")
	public String getNumeroMinistroRelator() {
		return numeroMinistroRelator;
	}

	/**
	 * @param numeroMinistroRelator the numeroMinistroRelator to set
	 */
	public void setNumeroMinistroRelator(String numeroMinistroRelator) {
		this.numeroMinistroRelator = numeroMinistroRelator;
	}

	@Column(name = "FLG_DESPACHO_PRESIDENTE")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getDespachoPresidente() {
		return despachoPresidente;
	}

	public void setDespachoPresidente(Boolean despachoPresidente) {
		this.despachoPresidente = despachoPresidente;
	}

	@Column(name="NOM_PRESIDENTE")
	public String getNomePresidente() {
		return nomePresidente;
	}

	public void setNomePresidente(String nomePresidente) {
		this.nomePresidente = nomePresidente;
	}

	@Column(name="NOM_MIN_DECISAO_MONOCRATICA")
	public String getNomeMinistroDecisaoMonocratica() {
		return nomeMinistroDecisaoMonocratica;
	}

	public void setNomeMinistroDecisaoMonocratica(
			String nomeMinistroDecisaoMonocratica) {
		this.nomeMinistroDecisaoMonocratica = nomeMinistroDecisaoMonocratica;
	}

	@Column(name = "FLG_LEGISLACAO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getIsLegislacao() {
		return isLegislacao;
	}

	public void setIsLegislacao(Boolean isLegislacao) {
		this.isLegislacao = isLegislacao;
	}
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name="DOC_TEXTO_DESPACHO")
	public String getTextoDespacho() {
		return textoDespacho;
	}

	public void setTextoDespacho(String textoDespacho) {
		this.textoDespacho = textoDespacho;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_PROCESSO_PUBLICADOS")
	public ProcessoPublicado getProcessoPublicado() {
		return processoPublicado;
	}

	public void setProcessoPublicado(ProcessoPublicado processoPublicado) {
		this.processoPublicado = processoPublicado;
	}

	@Column(name = "FLG_LIBERADO_INTERNET")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getLiberadoInternet() {
		return liberadoInternet;
	}

	public void setLiberadoInternet(Boolean liberadoInternet) {
		this.liberadoInternet = liberadoInternet;
	}
	
	
}
