package br.gov.stf.estf.entidade.processostf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table( schema="STF", name="ASSUNTOS")
public class Assunto extends ESTFBaseEntity<String> {
	
	private static final long serialVersionUID = 8759035902318087057L;
	private String descricaoCompleta;
	private String descricao;
	private Boolean ativo;
	private Assunto assuntoPai;
	private Assunto assuntoVinculado;
	
	@Id
	@Column(name="COD_ASSUNTO")
	public String getId() {
		return id;
	}
	
	@Column(name="DSC_ASSUNTO")	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Column(name="DSC_ASSUNTO_COMPLETO")	
	public String getDescricaoCompleta() {
		return descricaoCompleta;
	}

	public void setDescricaoCompleta(String descricaoCompleta) {
		this.descricaoCompleta = descricaoCompleta;
	}
	
	
	@Column(name = "FLG_ASSUNTO", unique = false, nullable = false, insertable = true, updatable = true)
    @Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")	
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_ASSUNTO_PAI", unique=false, nullable=true, insertable=true, updatable=true)
	public Assunto getAssuntoPai() {
		return assuntoPai;
	}

	public void setAssuntoPai(Assunto assuntoPai) {
		this.assuntoPai = assuntoPai;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_ASSUNTO_VINC", unique=false, nullable=true, insertable=true, updatable=true)
	public Assunto getAssuntoVinculado() {
		return assuntoVinculado;
	}

	public void setAssuntoVinculado(Assunto assuntoVinculado) {
		this.assuntoVinculado = assuntoVinculado;
	}
}
