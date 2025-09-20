package br.gov.stf.estf.entidade.processosetor;

import java.util.List;

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
import javax.persistence.Transient;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.usuario.GrupoUsuario;
import br.gov.stf.estf.entidade.usuario.Usuario;


@SuppressWarnings("serial")
@Entity
@Table(schema="EGAB",name="CONTROLE_DISTRIBUICAO")
public class ControleDistribuicao  extends ESTFBaseEntity<Long>{

	private String siglaClasseProcessual;
	private String tipoJulgamento;
	private Usuario usuario;
	private GrupoUsuario grupoUsuario;
	private Integer qtdDebido;
	
	//transient
	private Integer distribuido;
	private Integer distribuicao;
	private Integer qtdDebitoAnterior;
	private List<HistoricoDistribuicao> processosDistribuidos;
	
	@Id
	@Column( name="SEQ_CONTROLE_DISTRIBUICAO" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="EGAB.SEQ_CONTROLE_DISTRIBUICAO", allocationSize = 1 )	
	public Long getId() {
		return id;
	}	    
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="SIG_USUARIO", nullable=true, insertable=true, updatable=true)
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="SEQ_GRUPO_USUARIO", nullable=true, insertable=true, updatable=true)
	public GrupoUsuario getGrupoUsuario() {
		return grupoUsuario;
	}
	public void setGrupoUsuario(GrupoUsuario grupoUsuario) {
		this.grupoUsuario = grupoUsuario;
	}
	
	@Column(name="TIPO_JULGAMENTO", nullable=false, insertable=true, updatable=true, length=10)
	public String getTipoJulgamento() {
		return tipoJulgamento;
	}
	public void setTipoJulgamento(String tipoJulgamento) {
		this.tipoJulgamento = tipoJulgamento;
	}
	
	@Column(name="QTD_DEBITO",nullable=false)
	public Integer getQtdDebito() {
		return qtdDebido;
	}
	public void setQtdDebito(Integer qtdDebido) {
		this.qtdDebido = qtdDebido;
	}
	
	@Column(name="SIG_CLASSE", nullable=false, insertable=true, updatable=true, length=6)
	public String getSiglaClasseProcessual() {
		return siglaClasseProcessual;
	}
	public void setSiglaClasseProcessual(String siglaClasseProcessual) {
		this.siglaClasseProcessual = siglaClasseProcessual;
	}
	
	@Transient
	public Integer getDistribuido() {
		if( processosDistribuidos != null )
			distribuido = processosDistribuidos.size();
		return distribuido;
	}

	@Transient
	public void setDistribuido(Integer distribuido) {
		this.distribuido = distribuido;
	}
	
	@Transient
	public String getIndentificacaoClasseTipoJulgamento(){
		return getSiglaClasseProcessual()+"-"+getTipoJulgamento();
	}
	
	@Transient
	public List<HistoricoDistribuicao> getProcessosDistribuidos() {
		return processosDistribuidos;
	}

	@Transient
	public void setProcessosDistribuidos(List<HistoricoDistribuicao> processosDistribuidos) {
		this.processosDistribuidos = processosDistribuidos;
	}
	
	@Transient
	public Integer getQtdDebitoAnterior() {
		return qtdDebitoAnterior;
	}

	@Transient
	public void setQtdDebitoAnterior(Integer qtdDebitoAnterior) {
		this.qtdDebitoAnterior = qtdDebitoAnterior;
	}
	
	@Transient
	public boolean getPossuiDebitoAnterior(){
		if( getQtdDebitoAnterior() != null && getQtdDebitoAnterior() > 0 ){
			return true;
		}else{
			return false;
		}
	}
	
	@Transient
	public boolean getPossuiDebito(){
		if( getQtdDebito() != null && getQtdDebito() > 0 ){
			return true;
		}else{
			return false;
		}
	}
	
	@Transient
	public boolean getPossuiProcessoDistribuido(){
		if( getDistribuido() != null && getDistribuido() > 0 ){
			return true;
		}else{
			return false;
		}
	}
	
	@Transient
	public Integer getDistribuicao() {
		return distribuicao;
	}

	@Transient
	public void setDistribuicao(Integer distribuicao) {
		this.distribuicao = distribuicao;
	}

	
}
