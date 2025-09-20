package br.gov.stf.estf.entidade.processostf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(schema = "JUDICIARIO", name = "TIPO_PREFERENCIA")
public class TipoPreferencia extends ESTFBaseEntity<Long> {

	private String descricao;
	private Boolean ativo;
	
	@Id
	@Column(name = "SEQ_TIPO_PREFERENCIA", precision =6, scale = 0)
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="SINP.SEQ_TIPO_PREFERENCIA", allocationSize=1) 
	public Long getId() {
		return id;
	}
	
	@Column(name = "DSC_PREFERENCIA", length = 200, nullable = false, unique = false)
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Column (name = "FLG_ATIVO", nullable = false, unique = false)
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAtivo() {
		return ativo;
	}
	
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
}
