package br.gov.stf.estf.entidade.alerta;

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
import javax.persistence.Transient;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.Protocolo;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.entidade.util.ObjetoIncidenteUtil;

@Entity
@Table(name="ALERTA_USUARIO",schema="EGAB")
public class Alerta extends ESTFBaseEntity<Long>{
	
	private static final long serialVersionUID = -5847833210063478115L;
	
	private Long id;
	private Andamento tipoAndamento;
	private Usuario usuario;
	private Date dataNotificado;
	private Date dataAndamento;
//	private Protocolo protocolo;
	private ObjetoIncidente<?> objetoIncidente;
	private Setor setor;
	
	@Id
	@Column( name="SEQ_ALERTA_USUARIO" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="EGAB.SEQ_ALERTA_USUARIO", allocationSize = 1 )	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SIG_USUARIO", unique=false, nullable=true, insertable=true, updatable=true)
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn( name="COD_ANDAMENTO", nullable=true)
	public Andamento getTipoAndamento() {
		return tipoAndamento;
	}

	public void setTipoAndamento(Andamento tipoAndamento) {
		this.tipoAndamento = tipoAndamento;
	}

	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="DAT_ANDAMENTO", unique=false, nullable=true, insertable=true, updatable=true)
	public Date getDataAndamento() {
		return dataAndamento;
	}

	public void setDataAndamento(Date dataAndamento) {
		this.dataAndamento = dataAndamento;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="DAT_NOTIFICACAO", unique=false, insertable=true, updatable=true)
	public Date getDataNotificado() {
		return dataNotificado;
	}

	public void setDataNotificado(Date dataNotificado) {
		this.dataNotificado = dataNotificado;
	}
	

	@ManyToOne(fetch = FetchType.LAZY, cascade={})
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE", updatable=false, insertable=false)
	@LazyToOne(LazyToOneOption.NO_PROXY)
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
    @JoinColumn(name="COD_SETOR", unique = false, nullable = false, insertable = true, updatable = true)	
	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}
	
	
	@Transient
	public Processo getProcesso() {
		return ObjetoIncidenteUtil.getProcesso(getObjetoIncidente());
	}
	
	@Transient
	public Protocolo getProtocolo() {
		return ObjetoIncidenteUtil.getProtocolo(getObjetoIncidente());
	}	
	
	@Transient
	public Boolean getPossuiIndentificacaoProcessual() {
		if (getProcesso() != null && getProcesso().getClasseProcessual() != null && getProcesso().getNumeroProcessual() != null)
			return true;
		else
			return false;

	}
	
	@Transient
	public Boolean getPossuiIndentificacaoProcessualProcessoProtocolado() {
		if (getProtocolo() != null && getProtocolo().getSiglaClasseProcessual() != null && getProtocolo().getNumeroProcessual() != null)
			return true;
		else
			return false;

	}	

	@Transient
	public Boolean getPossuiIndentificacaoProtocolo() {
		if (getProtocolo() != null && getProtocolo().getAnoProtocolo() != null && getProtocolo().getNumeroProtocolo() != null)
			return true;
		else
			return false;

	}
}
