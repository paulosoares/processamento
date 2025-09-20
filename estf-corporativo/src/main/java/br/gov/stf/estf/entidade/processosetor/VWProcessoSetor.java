package br.gov.stf.estf.entidade.processosetor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(schema = "EGAB", name = "VW_PROCESSO_SETOR")
public class VWProcessoSetor extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6297063193853278789L;

	private Long id;
	private Long numeroProtocolo;
	private Short anoProtocolo;
	private String siglaClasseProcessual;
	private Long numeroProcessual;
	private Long codigoTipoRecurso;
	private Boolean repercussaoGeral;
	private Long codigoRelatorAtual;
	private String tipoJulgamento;
	private String tipoMeioProcesso;
	private Boolean liminar;
	

	@Id
	@Column(name = "SEQ_PROCESSO_SETOR")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NUM_PROTOCOLO", unique = false, nullable = false, insertable = false, updatable = false, precision = 7, scale = 0)
	public Long getNumeroProtocolo() {
		return this.numeroProtocolo;
	}

	public void setNumeroProtocolo(Long numeroProtocolo) {
		this.numeroProtocolo = numeroProtocolo;
	}

	@Column(name = "ANO_PROTOCOLO", unique = false, nullable = false, insertable = false, updatable = false, precision = 4, scale = 0)
	public Short getAnoProtocolo() {
		return this.anoProtocolo;
	}

	public void setAnoProtocolo(Short anoProtocolo) {
		this.anoProtocolo = anoProtocolo;
	}

	@Column(name = "SIG_CLASSE_PROCES", unique = false, nullable = true, insertable = false, updatable = false, length = 6)
	public String getSiglaClasseProcessual() {
		return this.siglaClasseProcessual;
	}

	public void setSiglaClasseProcessual(String siglaClasseProcessual) {
		this.siglaClasseProcessual = siglaClasseProcessual;
	}

	@Column(name = "NUM_PROCESSO", unique = false, nullable = true, insertable = false, updatable = false, precision = 7, scale = 0)
	public Long getNumeroProcessual() {
		return this.numeroProcessual;
	}

	public void setNumeroProcessual(Long numeroProcessual) {
		this.numeroProcessual = numeroProcessual;
	}

	@Column(name = "SEQ_TIPO_RECURSO", unique = false, nullable = true, insertable = false, updatable = false)
	public Long getCodigoTipoRecurso() {
		return codigoTipoRecurso;
	}

	public void setCodigoTipoRecurso(Long codigoTipoRecurso) {
		this.codigoTipoRecurso = codigoTipoRecurso;
	}

	@Column(name = "FLG_REPERCUSSAO_GERAL", unique = false, nullable = true, insertable = false, updatable = false, length=1)
	@org.hibernate.annotations.Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getRepercussaoGeral() {
		return repercussaoGeral;
	}

	public void setRepercussaoGeral(Boolean repercussaoGeral) {
		this.repercussaoGeral = repercussaoGeral;
	}

	@Column(name = "COD_RELATOR_ATUAL", unique = false, nullable = true, insertable = false, updatable = false)
	public Long getCodigoRelatorAtual() {
		return codigoRelatorAtual;
	}

	public void setCodigoRelatorAtual(Long codigoRelatorAtual) {
		this.codigoRelatorAtual = codigoRelatorAtual;
	}

	@Column(name = "TIP_JULGAMENTO", unique = false, nullable = true, insertable = false, updatable = false)
	public String getTipoJulgamento() {
		return tipoJulgamento;
	}

	public void setTipoJulgamento(String tipoJulgamento) {
		this.tipoJulgamento = tipoJulgamento;
	}

	@Column(name = "TIP_MEIO_PROCESSO", unique = false, nullable = false, insertable = true, updatable = true, length = 1)
	public String getTipoMeioProcesso() {
		return this.tipoMeioProcesso;
	}

	public void setTipoMeioProcesso(String tipoMeioProcesso) {
		this.tipoMeioProcesso = tipoMeioProcesso;
	}
	
	@Column(name = "FLG_LIMINAR", unique = false, insertable = true, updatable = true, length = 1)
	@org.hibernate.annotations.Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getLiminar() {
		return this.liminar;
	}

	public void setLiminar(Boolean liminar) {
		this.liminar = liminar;
	}
	

}
