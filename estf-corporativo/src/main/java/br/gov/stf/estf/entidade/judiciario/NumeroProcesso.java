package br.gov.stf.estf.entidade.judiciario;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name="NUMERO_PROCESSO",schema="JUDICIARIO")
public class NumeroProcesso extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = 1L;
	
	private Long numeroProcesso;
	private String sigClasseProcesso;
	private Date datAutuacao;
	private Long tipoSituacaoNumero;
	private Long numeroUnicoProcesso;
	private Date datInclusao;
	private String usuarioInclusao;
	private Date datAlteracao;
	private String usuarioAlteracao;
	private Long numeroProcessoReautuado;
	private String sigClasseProcessoReautuado;
	private String tipoReautuacao;
	private String flagRecursal;
	
	@Id
	@Column(name = "SEQ_NUMERO_PROCESSO", nullable = false)
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_NUMERO_PROCESSO", allocationSize = 1)
	public Long getId() {
		return this.id;
	}
	
	@Column(name = "NUM_PROCESSO", unique = false, nullable = false, insertable = true, updatable = true)
	public Long getNumeroProcesso() {
		return numeroProcesso;
	}
	
	public void setNumeroProcesso(Long numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}
	
	@Column(name = "SIG_CLASSE_PROCES", unique = false, nullable = false, insertable = true, updatable = true)
	public String getSigClasseProcesso() {
		return sigClasseProcesso;
	}
	
	public void setSigClasseProcesso(String sigClasseProcesso) {
		this.sigClasseProcesso = sigClasseProcesso;
	}
	
	@Column(name = "DAT_AUTUACAO", unique = false, nullable = false, insertable = true, updatable = true)
	public Date getDatAutuacao() {
		return datAutuacao;
	}
	
	public void setDatAutuacao(Date datAutuacao) {
		this.datAutuacao = datAutuacao;
	}
	
	@Column(name = "TIP_SITUACAO_NUMERO", unique = false, nullable = false, insertable = true, updatable = true)
	public Long getTipoSituacaoNumero() {
		return tipoSituacaoNumero;
	}
	
	public void setTipoSituacaoNumero(Long tipoSituacaoNumero) {
		this.tipoSituacaoNumero = tipoSituacaoNumero;
	}
	
	@Column(name = "NUM_UNICO_PROCESSO", unique = false, nullable = true, insertable = true, updatable = true)
	public Long getNumeroUnicoProcesso() {
		return numeroUnicoProcesso;
	}
	
	public void setNumeroUnicoProcesso(Long numeroUnicoProcesso) {
		this.numeroUnicoProcesso = numeroUnicoProcesso;
	}
	
	@Column(name = "DAT_INCLUSAO", unique = false, nullable = true, insertable = true, updatable = true)
	public Date getDatInclusao() {
		return datInclusao;
	}
	
	public void setDatInclusao(Date datInclusao) {
		this.datInclusao = datInclusao;
	}
	
	@Column(name = "USU_INCLUSAO", unique = false, nullable = true, insertable = true, updatable = true)
	public String getUsuarioInclusao() {
		return usuarioInclusao;
	}
	
	public void setUsuarioInclusao(String usuarioInclusao) {
		this.usuarioInclusao = usuarioInclusao;
	}
	
	@Column(name = "DAT_ALTERACAO", unique = false, nullable = true, insertable = true, updatable = true)
	public Date getDatAlteracao() {
		return datAlteracao;
	}
	
	public void setDatAlteracao(Date datAlteracao) {
		this.datAlteracao = datAlteracao;
	}
	
	@Column(name = "USU_ALTERACAO", unique = false, nullable = true, insertable = true, updatable = true)
	public String getUsuarioAlteracao() {
		return usuarioAlteracao;
	}
	
	public void setUsuarioAlteracao(String usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}
	
	@Column(name = "NUM_PROCESSO_REAUTUADO", unique = false, nullable = true, insertable = true, updatable = true)
	public Long getNumeroProcessoReautuado() {
		return numeroProcessoReautuado;
	}
	
	public void setNumeroProcessoReautuado(Long numeroProcessoReautuado) {
		this.numeroProcessoReautuado = numeroProcessoReautuado;
	}
	
	@Column(name = "SIG_CLASSE_PROCES_REAUTUADO", unique = false, nullable = true, insertable = true, updatable = true)
	public String getSigClasseProcessoReautuado() {
		return sigClasseProcessoReautuado;
	}
	
	public void setSigClasseProcessoReautuado(String sigClasseProcessoReautuado) {
		this.sigClasseProcessoReautuado = sigClasseProcessoReautuado;
	}
	
	@Column(name = "TIP_REAUTUACAO", unique = false, nullable = true, insertable = true, updatable = true)
	public String getTipoReautuacao() {
		return tipoReautuacao;
	}
	
	public void setTipoReautuacao(String tipoReautuacao) {
		this.tipoReautuacao = tipoReautuacao;
	}
	
	@Column(name = "FLG_RECURSAL", unique = false, nullable = true, insertable = true, updatable = true)
	public String getFlagRecursal() {
		return flagRecursal;
	}
	
	public void setFlagRecursal(String flagRecursal) {
		this.flagRecursal = flagRecursal;
	}
	
}
