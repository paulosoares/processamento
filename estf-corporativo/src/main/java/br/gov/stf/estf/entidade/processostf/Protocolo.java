package br.gov.stf.estf.entidade.processostf;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.localizacao.Origem;

/**
 * Representa os protocolos do STF. A classe de protocolo será utilizada apenas para
 * recuperar o histórico dos protocolos registrados.
 * 
 * <p>Após as alterações do novo modelo do judiciário, não existirão novos protocolos.
 * 
 * <p>Protocolo será substituido por Peticao.
 * 
 * @author Rodrigo Barreiros
 * @author Demétrius Jubé
 * 
 * @since 15.07.2009
 * @see Peticao
 */
@Entity
@Table(name = "PROCESSO_PROTOCOLADOS", schema = "STF")
public class Protocolo extends ObjetoIncidente {

	private static final long serialVersionUID = 8119715537305907963L;

	private Short anoProtocolo;

	private Long numeroProtocolo;

	private Integer quantidadeApensos;

	private Integer quantidadeFolhas;

	private String numeroRegistro;

	private String descricaoAviso;

	private String descricaoObservacao;

	private String tipoDistribuicao;

	private Integer quantidadeVolumes;

	private Integer quantidadeJuntadaLinha;

	private Boolean sigiloso;

	private String siglaClasseProcedencia;

	private String numeroProcessoProcedencia;

	private TipoMeioProcesso tipoMeioProcesso;

	private TipoRecebimento tipoRecebimento;
	
	private String siglaClasseProcessual;

	private Long numeroProcessual;
	
	private Boolean resposta;
	
	private Origem origem;
	
	private List<LegislacaoProcesso> legislacaoProcesso;
	
	private List<Assunto> assuntos;

	@Column(name = "ANO_PROTOCOLO")
	public Short getAnoProtocolo() {
		return anoProtocolo;
	}

	public void setAnoProtocolo(Short anoProtocolo) {
		this.anoProtocolo = anoProtocolo;
	}

	@Column(name = "NUM_PROTOCOLO")
	public Long getNumeroProtocolo() {
		return numeroProtocolo;
	}

	public void setNumeroProtocolo(Long numeroProtocolo) {
		this.numeroProtocolo = numeroProtocolo;
	}

	@Column(name = "QTD_APENSOS")
	public Integer getQuantidadeApensos() {
		return quantidadeApensos;
	}

	public void setQuantidadeApensos(Integer quantidadeApensos) {
		this.quantidadeApensos = quantidadeApensos;
	}

	@Column(name = "QTD_FOLHAS")
	public Integer getQuantidadeFolhas() {
		return quantidadeFolhas;
	}

	public void setQuantidadeFolhas(Integer quantidadeFolhas) {
		this.quantidadeFolhas = quantidadeFolhas;
	}

	@Column(name = "NUM_REGISTRO")
	public String getNumeroRegistro() {
		return numeroRegistro;
	}

	public void setNumeroRegistro(String numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}

	@Column(name = "DSC_AVISO")
	public String getDescricaoAviso() {
		return descricaoAviso;
	}

	public void setDescricaoAviso(String descricaoAviso) {
		this.descricaoAviso = descricaoAviso;
	}

	@Column(name = "DSC_OBSERVACAO")
	public String getDescricaoObservacao() {
		return descricaoObservacao;
	}

	public void setDescricaoObservacao(String descricaoObservacao) {
		this.descricaoObservacao = descricaoObservacao;
	}

	@Column(name = "TIPO_DISTRIBUICAO")
	public String getTipoDistribuicao() {
		return tipoDistribuicao;
	}

	public void setTipoDistribuicao(String tipoDistribuicao) {
		this.tipoDistribuicao = tipoDistribuicao;
	}

	@Column(name = "QTD_VOLUMES")
	public Integer getQuantidadeVolumes() {
		return quantidadeVolumes;
	}

	public void setQuantidadeVolumes(Integer quantidadeVolumes) {
		this.quantidadeVolumes = quantidadeVolumes;
	}

	@Column(name = "QTD_JUNTADA_LINHA")
	public Integer getQuantidadeJuntadaLinha() {
		return quantidadeJuntadaLinha;
	}

	public void setQuantidadeJuntadaLinha(Integer quantidadeJuntadaLinha) {
		this.quantidadeJuntadaLinha = quantidadeJuntadaLinha;
	}

	@Column(name = "FLG_SIGILOSO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getSigiloso() {
		return sigiloso;
	}

	public void setSigiloso(Boolean sigiloso) {
		this.sigiloso = sigiloso;
	}

	@Column(name = "SIG_CLASSE_PROCED")
	public String getSiglaClasseProcedencia() {
		return siglaClasseProcedencia;
	}

	public void setSiglaClasseProcedencia(String siglaClasseProcedencia) {
		this.siglaClasseProcedencia = siglaClasseProcedencia;
	}

	@Column(name = "NUM_PROCES_PROCED")
	public String getNumeroProcessoProcedencia() {
		return numeroProcessoProcedencia;
	}

	public void setNumeroProcessoProcedencia(String numeroProcessoProcedencia) {
		this.numeroProcessoProcedencia = numeroProcessoProcedencia;
	}

	@Column(name = "TIP_MEIO_PROCESSO")
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.processostf.TipoMeioProcesso"),
			@Parameter(name = "idClass", value = "java.lang.String") })
	public TipoMeioProcesso getTipoMeioProcesso() {
		return tipoMeioProcesso;
	}

	public void setTipoMeioProcesso(TipoMeioProcesso tipoMeioProcesso) {
		this.tipoMeioProcesso = tipoMeioProcesso;
	}

	@Column(name = "TIP_RECEBIMENTO")
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.processostf.TipoRecebimento"),
			@Parameter(name = "idClass", value = "java.lang.String") })
	public TipoRecebimento getTipoRecebimento() {
		return tipoRecebimento;
	}

	public void setTipoRecebimento(TipoRecebimento tipoRecebimento) {
		this.tipoRecebimento = tipoRecebimento;
	}
	
	@Column(name = "FLG_RESP")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")	
	public Boolean getResposta() {
		return resposta;
	}

	public void setResposta(Boolean resposta) {
		this.resposta = resposta;
	}
	
    @ManyToOne(cascade={}, fetch=FetchType.LAZY)
    @JoinColumn(name="COD_ORGAO_ORIGEM", unique=false, nullable=true, insertable=false, updatable=false)
	public Origem getOrigem() {
		return origem;
	}

	public void setOrigem(Origem origem) {
		this.origem = origem;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "objetoIncidente")
	public List<LegislacaoProcesso> getLegislacaoProcesso() {
		return legislacaoProcesso;
	}

	public void setLegislacaoProcesso(List<LegislacaoProcesso> legislacaoProcesso) {
		this.legislacaoProcesso = legislacaoProcesso;
	}	
	
	@ManyToMany(cascade = {}, fetch = FetchType.LAZY)
	@JoinTable(
		schema = "STF",
		name = "ASSUNTO_PROCESSO",
		joinColumns= @JoinColumn(name = "SEQ_OBJETO_INCIDENTE"),
		inverseJoinColumns = @JoinColumn(name = "COD_ASSUNTO")
	)
	@OrderBy(clause="NUM_ORDEM")
	public List<Assunto> getAssuntos() {
		return assuntos;
	}

	public void setAssuntos(List<Assunto> assuntos) {
		this.assuntos = assuntos;
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

	@Transient @Override
	public String getIdentificacao() {
		return getNumeroProtocolo() + "/" + getAnoProtocolo();
	}
	
	@Transient
	public Boolean getIsEletronico() {
		if( TipoMeioProcesso.ELETRONICO.getCodigo().equals(getTipoMeioProcesso()) )
			return true;
		else
			return false;
	} 

}
