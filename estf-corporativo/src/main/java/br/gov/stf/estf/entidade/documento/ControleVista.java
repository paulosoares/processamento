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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;


/**
 * Essa classe é relacionada ao controle de vista.
 * Classe criada segundo a ISSUE 941.
 */

@Entity
@Table(schema = "STF", name = "CONTROLE_VISTA")
public class ControleVista extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String codigoAndamento;
	private Date dataAndamento;
	private String siglaClasseProcessual;
	private Long numeroProcessual;
	private Long numeroSequencia;
	private Date dataVista;
	private Date dataInicio;	
	private Date dataFim;
	private Date dataProrrogacao;
	private Date dataRenovacaoInicio;
	private Date dataRenovacaoProrrogacao;
	private Date dataRenovacaoFim;
	private Date dataDevolucao;
	private Long codigoMinistro;
	private Boolean reuPreso;
	private Integer quantidadeRenovacao;
	private Boolean ativo;
	private Long codigoOrgaoOrigem;
	private Ministro ministro;
    private ObjetoIncidente<?> objetoIncidente;
	private Long objetoIncidenteId;
	
	@Id
	@Column(name = "SEQ_CONTROLE_VISTA")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "STF.SEQ_CONTROLE_VISTA", allocationSize = 1)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "COD_ANDAMENTO")
	public String getCodigoAndamento() {
		return codigoAndamento;
	}
	public void setCodigoAndamento(String codigoAndamento) {
		this.codigoAndamento = codigoAndamento;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_ANDAMENTO")	
	public Date getDataAndamento() {
		return dataAndamento;
	}
	public void setDataAndamento(Date dataAndamento) {
		this.dataAndamento = dataAndamento;
	}
	
	@Column(name = "SIG_CLASSE_PROCES")
	public String getSiglaClasseProcessual() {
		return siglaClasseProcessual;
	}
	public void setSiglaClasseProcessual(String siglaClasseProcessual) {
		this.siglaClasseProcessual = siglaClasseProcessual;
	}
	
	@Column(name = "NUM_PROCESSO")	
	public Long getNumeroProcessual() {
		return numeroProcessual;
	}
	public void setNumeroProcessual(Long numeroProcessual) {
		this.numeroProcessual = numeroProcessual;
	}
	
	@Column(name = "NUM_SEQUENCIA")
	public Long getNumeroSequencia() {
		return numeroSequencia;
	}
	public void setNumeroSequencia(Long numeroSequencia) {
		this.numeroSequencia = numeroSequencia;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_VISTA")
	public Date getDataVista() {
		return dataVista;
	}
	public void setDataVista(Date dataVista) {
		this.dataVista = dataVista;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_INICIO")
	public Date getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_FIM")
	public Date getDataFim() {
		return dataFim;
	}
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_PRORROGACAO")
	public Date getDataProrrogacao() {
		return dataProrrogacao;
	}
	public void setDataProrrogacao(Date dataProrrogacao) {
		this.dataProrrogacao = dataProrrogacao;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_RENOVACAO_INICIO")	
	public Date getDataRenovacaoInicio() {
		return dataRenovacaoInicio;
	}
	public void setDataRenovacaoInicio(Date dataRenovacaoInicio) {
		this.dataRenovacaoInicio = dataRenovacaoInicio;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_RENOVACAO_PRORROGACAO")
	public Date getDataRenovacaoProrrogacao() {
		return dataRenovacaoProrrogacao;
	}
	public void setDataRenovacaoProrrogacao(Date dataRenovacaoProrrogacao) {
		this.dataRenovacaoProrrogacao = dataRenovacaoProrrogacao;
	}	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_RENOVACAO_FIM")	
	public Date getDataRenovacaoFim() {
		return dataRenovacaoFim;
	}
	public void setDataRenovacaoFim(Date dataRenovacaoFim) {
		this.dataRenovacaoFim = dataRenovacaoFim;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_DEVOLUCAO")
	public Date getDataDevolucao() {
		return dataDevolucao;
	}
	public void setDataDevolucao(Date dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
	}
	
	@Column(name = "COD_MINISTRO")	
	public Long getCodigoMinistro() {
		return codigoMinistro;
	}
	public void setCodigoMinistro(Long codigoMinistro) {
		this.codigoMinistro = codigoMinistro;
	}
	@Column(name = "FLG_REU_PRESO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getReuPreso() {
		return reuPreso;
	}
	public void setReuPreso(Boolean reuPreso) {
		this.reuPreso = reuPreso;
	}
	@Column(name = "QTD_RENOVACAO")
	public Integer getQuantidadeRenovacao() {
		return quantidadeRenovacao;
	}
	public void setQuantidadeRenovacao(Integer quantidadeRenovacao) {
		this.quantidadeRenovacao = quantidadeRenovacao;
	}	
	@Column(name = "FLG_ATIVO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")	
	public Boolean getAtivo() {
		return ativo;
	}
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	@Column(name = "COD_ORGAO_ORIGEM")
	public Long getCodigoOrgaoOrigem() {
		return codigoOrgaoOrigem;
	}
	public void setCodigoOrgaoOrigem(Long codigoOrgaoOrigem) {
		this.codigoOrgaoOrigem = codigoOrgaoOrigem;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COD_MINISTRO", updatable = false, insertable = false)
	public Ministro getMinistro() {
		return ministro;
	}

	public void setMinistro(Ministro ministro) {
		this.ministro = ministro;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE", insertable=false, updatable=false)
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}
	
	@Column(name = "SEQ_OBJETO_INCIDENTE")
	public Long getObjetoIncidenteId() {
		return objetoIncidenteId;
	}
	
	public void setObjetoIncidenteId(Long objetoIncidenteId) {
		this.objetoIncidenteId = objetoIncidenteId;
	}
	
}
