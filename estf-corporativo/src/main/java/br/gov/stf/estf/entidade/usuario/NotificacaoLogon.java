package br.gov.stf.estf.entidade.usuario;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(schema="GLOBAL",name="NOTIFICACAO_LOGON")
public class NotificacaoLogon extends ESTFBaseEntity<Long> {


	private static final long serialVersionUID = 8606950195843664096L;

	private Integer prioridade;
	private String descricao;
	private String grupoTopico;
	private Date dataInicioNotificacao;
	private Date dataFimNotificacao;
	private String usuarioInclusao;
	           
	@Id
	@Column( name="SEQ_NOTIFICACAO_LOGON" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="GLOBAL.SEQ_NOTIFICACAO_LOGON", allocationSize = 1 )    
    public Long getId() {
        return id;
    }
	
	@Column(name="NUM_PRIORIDADE")
	public Integer getPrioridade() {
		return prioridade;
	}
	public void setPrioridade(Integer prioridade) {
		this.prioridade = prioridade;
	}
	
	@Lob
	@Column(name="DSC_NOTIFICACAO")
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Column(name="SIG_GRUPO_TOPICO")
	public String getGrupoTopico() {
		return grupoTopico;
	}
	public void setGrupoTopico(String grupoTopico) {
		this.grupoTopico = grupoTopico;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DAT_INICIO_NOTIFICACAO")
	public Date getDataInicioNotificacao() {
		return dataInicioNotificacao;
	}
	public void setDataInicioNotificacao(Date dataInicioNotificacao) {
		this.dataInicioNotificacao = dataInicioNotificacao;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DAT_FINAL_NOTIFICACAO")
	public Date getDataFimNotificacao() {
		return dataFimNotificacao;
	}
	public void setDataFimNotificacao(Date dataFimNotificacao) {
		this.dataFimNotificacao = dataFimNotificacao;
	}

	@Column(name="USU_INCLUSAO", updatable = false)
	public String getUsuarioInclusao() {
		return usuarioInclusao;
	}

	public void setUsuarioInclusao(String usuarioInclusao) {
		this.usuarioInclusao = usuarioInclusao;
	}
   
}


