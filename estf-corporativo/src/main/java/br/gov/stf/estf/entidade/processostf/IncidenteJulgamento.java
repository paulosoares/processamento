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

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.util.ObjetoIncidenteUtil;

/**
 * Guarda a descrição da cadeia de um processo relativo a cada Incidente
 * acontecido no julgamento, que não é considerado nem um processo, nem um
 * recurso.
 * 
 * <p>
 * Ex.: Questão de Ordem levantada por um ministro durante um julgamento.
 * 
 * @author Rodrigo Barreiros
 * @author Demétrius Jubé
 * 
 * @since 15.07.2009
 */
@Entity
@Table(name = "INCIDENTE_JULGAMENTO", schema = "JUDICIARIO")
public class IncidenteJulgamento extends ObjetoIncidente<Processo> {

	private static final long serialVersionUID = -7379483443485283600L;

	private Date dataCriacao;

	private String descricaoCadeiaIncidente;

	private String siglaCadeiaIncidente;

	private Integer numeroSequenciaCadeia;

	private Boolean ativo;
	
	private Long codigoRecurso;

	private TipoIncidenteJulgamento tipoJulgamento;
	
	private String siglaClasseProcessual;

	private Long numeroProcessual;
	

	/**
	 * Data de criação do Incidente Julgamento.
	 */
	@Column(name = "DAT_CRIACAO")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
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
	 * Número de sequência da cadeia, quando esta se repete para o mesmo
	 * processo.
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
	 * Indica se o registro está ativo ou não. Os registros inativos foram
	 * aqueles criados pela migração para viabilizar a hierarquia de recursos
	 * para os incidentes de julgamento que existiam mas não existia o incidente
	 * de julgamento anterior (pai), neste caso o incidente de julgamento pai
	 * foi criado na tabela com FLG_ATIVO = 'N'.
	 */
	@Column(name = "FLG_ATIVO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	/**
	 * Código numérico do recurso.
	 */
	@Column(name = "COD_RECURSO")
	public Long getCodigoRecurso() {
		return codigoRecurso;
	}

	public void setCodigoRecurso(Long codigoRecurso) {
		this.codigoRecurso = codigoRecurso;
	}
	
	
	/**
	 * Identifica o tipo de recurso.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TIPO_RECURSO")
	public TipoIncidenteJulgamento getTipoJulgamento() {
		return tipoJulgamento;
	}

	public void setTipoJulgamento(TipoIncidenteJulgamento tipoJulgamento) {
		this.tipoJulgamento = tipoJulgamento;
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
