package br.gov.stf.estf.entidade.processostf;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.util.ObjetoIncidenteUtil;

/**
 * Representa os recursos de um processo.
 * 
 * @author Rodrigo Barreiros
 * @author Demétrius Jubé
 * 
 * @since 15.07.2009
 */
@Entity
@Table(name = "RECURSO_PROCESSO", schema = "JUDICIARIO")
public class RecursoProcesso extends ObjetoIncidente<Processo> {

	private static final long serialVersionUID = -1093799518710941733L;

	private TipoConfidencialidade tipoConfidencialidade;

	private Date dataInterposicao;

	private String descricaoCadeiaIncidente;

	private String siglaCadeiaIncidente;

	private Long codigoRecurso;

	private Integer numeroSequenciaCadeia;

	private TipoRecursoProcesso tipoRecursoProcesso;

	private Andamento tipoAndamento;

	private SituacaoProcesso situacao;

	private Ministro ministroRelator;

	private Boolean ativo;
	
	private String siglaClasseProcessual;

	private Long numeroProcessual;

	/**
	 * Classificação da petição quanto a permissão de acesso as informações por
	 * usuários específicos.
	 */
	@Column(name = "TIP_CONFIDENCIALIDADE")
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

	/**
	 * Data de protocolo do recurso.
	 */
	@Column(name = "DAT_INTERPOSICAO")
	@Temporal(TemporalType.DATE)
	public Date getDataInterposicao() {
		return dataInterposicao;
	}

	public void setDataInterposicao(Date dataInterposicao) {
		this.dataInterposicao = dataInterposicao;
	}

	/**
	 * Descrição da cadeia de incidentes em abreviações de maneira cronológica
	 * inversa dos incidentes de processo, recurso ou incidentes de julgamento.
	 * 
	 * <p>
	 * Ex: EMB.DECL.NO AG.REG.NOS EMB.DIV.NOS EMB.DECL.NOS EMB.DECL.NO AG.REG.
	 * NO AGRAVO DE INSTRUMENTO
	 */
	@Column(name = "DSC_CADEIA_INCIDENTE")
	public String getDescricaoCadeiaIncidente() {
		return descricaoCadeiaIncidente;
	}

	public void setDescricaoCadeiaIncidente(String descricaoCadeiaIncidente) {
		this.descricaoCadeiaIncidente = descricaoCadeiaIncidente;
	}
	
	@Transient
	public String getSiglaTipoRecurso () {
		String dsc = null;
		if ( siglaCadeiaIncidente!=null && siglaCadeiaIncidente.trim().length()>0 ) {
			String classe = getPrincipal().getSiglaClasseProcessual();
			dsc = siglaCadeiaIncidente.replace(classe+"-", "").trim();
		}
		return dsc;
	}

	/**
	 * Descrição da cadeia de incidentes em siglas de maneira cronológica dos
	 * incidentes de processo, recurso ou incidentes de julgamento.
	 * 
	 * <p>
	 * Ex: AI-AgR-ED-ED-EDv-AgR-ED.
	 */
	@Column(name = "SIG_CADEIA_INCIDENTE")
	public String getSiglaCadeiaIncidente() {
		return siglaCadeiaIncidente;
	}

	public void setSiglaCadeiaIncidente(String siglaCadeiaIncidente) {
		this.siglaCadeiaIncidente = siglaCadeiaIncidente;
	}

	/**
	 * Código numérico do recurso advindo da tabela de processos protocolados.
	 */
	@Column(name = "COD_RECURSO")
	public Long getCodigoRecurso() {
		return codigoRecurso;
	}

	public void setCodigoRecurso(Long codigoRecurso) {
		this.codigoRecurso = codigoRecurso;
	}

	/**
	 * Número de sequência da cadeia, quando esta se repete para o mesmo
	 * processo.
	 * 
	 * <p>
	 * Ex.:</br> SS-AgR, num_sequencia_cadeia = 1.</br> SS-AgR-segundo,
	 * num_sequencia_cadeia = 2.</br> SS-AgR-terceiro, num_sequencia_cadeia = 3.
	 */
	@Column(name = "NUM_SEQUENCIA_CADEIA")
	public Integer getNumeroSequenciaCadeia() {
		return numeroSequenciaCadeia;
	}

	public void setNumeroSequenciaCadeia(Integer numeroSequenciaCadeia) {
		this.numeroSequenciaCadeia = numeroSequenciaCadeia;
	}

	/**
	 * Identifica o tipo de recurso.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TIPO_RECURSO")
	public TipoRecursoProcesso getTipoRecursoProcesso() {
		return tipoRecursoProcesso;
	}

	public void setTipoRecursoProcesso(TipoRecursoProcesso tipoRecurso) {
		this.tipoRecursoProcesso = tipoRecurso;
	}

	/**
	 * Identifica o andamento.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_ANDAMENTO")
	public Andamento getTipoAndamento() {
		return tipoAndamento;
	}

	public void setTipoAndamento(Andamento tipoAndamento) {
		this.tipoAndamento = tipoAndamento;
	}

	/**
	 * Situação do processo.
	 */
	@Column(name = "COD_SITUACAO")
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.processostf.SituacaoProcesso"),
			@Parameter(name = "idClass", value = "java.lang.String") })
	public SituacaoProcesso getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoProcesso situacao) {
		this.situacao = situacao;
	}

	/**
	 * Ministro Relator.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_MIN_RELATOR")
	public Ministro getMinistroRelator() {
		return ministroRelator;
	}

	public void setMinistroRelator(Ministro ministroRelator) {
		this.ministroRelator = ministroRelator;
	}

	/**
	 * Indica se o registro está ativo ou não. Os registros inativos foram
	 * aqueles criados pela migração para viabilizar a hierarquia de recursos
	 * para os recursos que existiam mas não existia o recurso anterior (pai),
	 * neste caso o recurso pai foi criado na tabela com FLG_ATIVO = 'N'.
	 */
	@Column(name = "FLG_ATIVO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
	@Column(name = "SIG_CLASSE_PROCES")
	public String getSiglaClasseProcessual() {
		return siglaClasseProcessual;
	}

	public void setSiglaClasseProcessual(String siglaClasseProcessual) {
		this.siglaClasseProcessual = siglaClasseProcessual;
	}

	/**
	 * Numero do processo.
	 */
	@Column(name = "NUM_PROCESSO")
	public Long getNumeroProcessual() {
		return numeroProcessual;
	}

	public void setNumeroProcessual(Long numeroProcessual) {
		this.numeroProcessual = numeroProcessual;
	}
	

//	@Transient
//	@Override
//	public String getIdentificacao() {
//		return ObjetoIncidenteUtil.getIdentificacao(getPrincipal().getIdentificacao(), getSiglaCadeiaIncidente());
//	}
//
//	@Transient
//	@Override
//	public String getIdentificacaoCompleta() {
//		return ObjetoIncidenteUtil.getIdentificacaoCompleta(getPrincipal().getIdentificacaoCompleta(),
//				getDescricaoCadeiaIncidente());
//	}
	
	@Transient
	public String getIdentificacao() {
		return ObjetoIncidenteUtil.getIdentificacao(getSiglaCadeiaIncidente(),getNumeroSequenciaCadeia(), getPrincipal().getNumeroProcessual());
		
	}

	@Transient
	public String getIdentificacaoCompleta() {
		return ObjetoIncidenteUtil.getIdentificacaoCompleta(getDescricaoCadeiaIncidente(),
				getPrincipal().getNumeroProcessual());
	}
	
	@Transient 
	public String getDescricao() {
		return getDescricaoCadeiaIncidente();
	}

}
