/**
 * 
 */
package br.gov.stf.estf.entidade.jurisprudencia;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;


@Entity
@Table(schema = "BRS", name = "QUESTAO_ORDEM")
public class QuestaoOrdem extends ESTFAuditavelBaseEntity<Long> {


	private static final long serialVersionUID = 1143261525427241465L;	
	
	private Long id;
	private String siglaClasseUnificada;	
	private Long numeroProcesso;
	private Boolean ativo;
	private String assunto;
	private Date dataJulgamento;
	private String ementa;
	private Date dataPublicacao;
	private String ministroRelatorAcordao;
	private String ministroRelator;
	private String orgaoJulgador;
	private Date dataRepublicacao;
	private String referenciaProcesso;
	private ObjetoIncidente<?> objetoIncidente;
	
	@Override
	@Id
	@Column(name = "SEQ_QUESTAO_ORDEM")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "BRS.SEQ_QUESTAO_ORDEM", allocationSize = 1)
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}
		
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE")
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}
	
	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
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
	@Column(name = "TXT_EMENTA")
	public String getEmenta() {
		return ementa;
	}

	public void setEmenta(String ementa) {
		this.ementa = ementa;
	}
	
	@Column(name = "DSC_REFERENCIA_PROCESSO")
	public String getReferenciaProcesso() {
		return referenciaProcesso;
	}

	public void setReferenciaProcesso(String referenciaProcesso) {
		this.referenciaProcesso = referenciaProcesso;
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
	@Column(name = "SIG_CLASSE_PROCES")
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
	@Lob
	@Basic(fetch = FetchType.LAZY)
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
	 * @return the dataPublicacao
	 */
	@Column(name = "DAT_PUBLICACAO")
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
	
	@Column(name = "DAT_REPUBLICACAO")
	public Date getDataRepublicacao() {
		return dataRepublicacao;
	}

	public void setDataRepublicacao(Date dataRepublicacao) {
		this.dataRepublicacao = dataRepublicacao;
	}
	
	@Column(name = "FLG_ATIVO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
	@Column(name = "DSC_ORGAO_JULGADOR")
	public String getOrgaoJulgador() {
		return orgaoJulgador;
	}

	public void setOrgaoJulgador(String orgaoJulgador) {
		this.orgaoJulgador = orgaoJulgador;
	}

	
}
