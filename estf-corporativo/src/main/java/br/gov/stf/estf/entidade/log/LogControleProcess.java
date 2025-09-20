package br.gov.stf.estf.entidade.log;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.usuario.UsuarioExterno;

@Entity
@Table(name = "LOG_CONTROLE_PROCESSAMENTO", schema = "ESTF")
public class LogControleProcess extends ESTFBaseEntity<Long>{

	private static final long serialVersionUID = -7805203148352839608L;
	
	private String descreveLog;
	private Date dataLog;
	private int seqControleProcess;
	private String menssagemLog;
	private UsuarioExterno usuarioExternoID;
	
	@Override
	@Id
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="DSC_LOG")
	public String getDescreveLog() {
		return descreveLog;
	}

	public void setDescreveLog(String descreveLog) {
		this.descreveLog = descreveLog;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DAT_HORA_LOG")
	public Date getDataLog() {
		return dataLog;
	}

	public void setDataLog(Date dataLog) {
		this.dataLog = dataLog;
	}

	@Column(name="SEQ_CONTROLE_PROCESSAMENTO")
	public int getSeqControleProcess() {
		return seqControleProcess;
	}

	public void setSeqControleProcess(int seqControleProcess) {
		this.seqControleProcess = seqControleProcess;
	}

	@Column(name="DSC_MENSAGEM_LOG")
	public String getMenssagemLog() {
		return menssagemLog;
	}

	public void setMenssagemLog(String menssagemLog) {
		this.menssagemLog = menssagemLog;
	}

	@ManyToOne
	@JoinColumn(name="SEQ_USUARIO_EXTERNO")
	public UsuarioExterno getUsuarioExternoID() {
		return usuarioExternoID;
	}

	public void setUsuarioExternoID(UsuarioExterno usuarioExternoID) {
		this.usuarioExternoID = usuarioExternoID;
	}
}
