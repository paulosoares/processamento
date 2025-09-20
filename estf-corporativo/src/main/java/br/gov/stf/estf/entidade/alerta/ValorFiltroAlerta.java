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

@Entity
@Table(name="VALOR_CADASTRO_ALERTA",schema="EGAB")
public class ValorFiltroAlerta extends ESTFBaseEntity<Long>{
	
	private static final long serialVersionUID = 4928344440478113332L;

	private String descricao;
	private TipoFiltroAlerta tipoFiltroAlerta;
	private AlertaUsuario alertaUsuario;
	
	
	@Id
	@Column( name="SEQ_VALOR_CADASTRO_ALERTA " )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="EGAB.SEQ_VALOR_CADASTRO_ALERTA ", allocationSize = 1 )	
	public Long getId() {
		return id;
	}

	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_CADASTRO_ALERTA_USUARIO", unique=false, nullable=true, insertable=true, updatable=true)
	public AlertaUsuario getAlertaUsuario() {
		return alertaUsuario;
	}


	public void setAlertaUsuario(AlertaUsuario alertaUsuario) {
		this.alertaUsuario = alertaUsuario;
	}

	@Column(name="DSC_VALOR_CADASTRO_ALERTA", unique=false, nullable=false, insertable=true, updatable=true, length=50)
	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_TIPO_FILTRO_ALERTA", unique=false, nullable=false, insertable=true, updatable=true)
	public TipoFiltroAlerta getTipoFiltroAlerta() {
		return tipoFiltroAlerta;
	}


	public void setTipoFiltroAlerta(TipoFiltroAlerta tipoFiltroAlerta) {
		this.tipoFiltroAlerta = tipoFiltroAlerta;
	}
}
