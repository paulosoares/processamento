package br.gov.stf.estf.entidade.alerta;

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

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.usuario.Usuario;

@Entity
@Table(name="CADASTRO_ALERTA_USUARIO",schema="EGAB")
public class AlertaUsuario extends ESTFBaseEntity<Long>{
	
	private static final long serialVersionUID = -6610764169425818800L;
	
	private Usuario usuario;
	private Andamento tipoAndamento;
	private Boolean notificarPorEmail;
	private Setor setor;
	private Boolean concatenarFiltro;

	@Id
	@Column( name="SEQ_CADASTRO_ALERTA_USUARIO" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="EGAB.SEQ_CADASTRO_ALERTA_USUARIO", allocationSize = 1 )	
	public Long getId() {
		return id;
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
    @JoinColumn( name="COD_ANDAMENTO" )
	public Andamento getTipoAndamento() {
		return tipoAndamento;
	}

	public void setTipoAndamento(Andamento tipoAndamento) {
		this.tipoAndamento = tipoAndamento;
	}
	
	@Column( name="FLG_NOTIFICAR_EMAIL", nullable=true, insertable=true, updatable=true, unique=false)   
    @org.hibernate.annotations.Type( type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getNotificarPorEmail() {
		return notificarPorEmail;
	}

	public void setNotificarPorEmail(Boolean notificarPorEmail) {
		this.notificarPorEmail = notificarPorEmail;
	}
	
	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
    @JoinColumn(name="COD_SETOR", unique = false, nullable = false, insertable = true, updatable = true)
	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	@Column( name="TIP_CONCATENACAO_FILTRO", unique = false, nullable = true, insertable = true, updatable = true) 
	@org.hibernate.annotations.Type(type = "br.gov.stf.estf.alerta.model.dataaccess.ConcatenarFiltroEnumUserType")
	public Boolean getConcatenarFiltro() {
		return concatenarFiltro;
	}

	public void setConcatenarFiltro(Boolean concatenarFiltro) {
		this.concatenarFiltro = concatenarFiltro;
	}

}
