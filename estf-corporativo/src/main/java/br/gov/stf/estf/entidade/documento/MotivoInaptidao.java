package br.gov.stf.estf.entidade.documento;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;
import br.gov.stf.framework.model.entity.BaseEntity;

@Entity
@Table(name = "MOTIVO_INAPTIDAO", schema = "JUDICIARIO")
public class MotivoInaptidao extends ESTFAuditavelBaseEntity<Long> {
	private static final long serialVersionUID = 1L;

	private String descricao;
	private Boolean ativo;
	
	@Id
	@Column(name = "COD_MOTIVO_INAPTIDAO")
	public Long getId() {
		return this.id;
	}

	@Column(name = "DSC_MOTIVO_INAPTIDAO")
	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "FLG_ATIVO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
	@Override
	public int compareTo(BaseEntity object) {
		if (object instanceof MotivoInaptidao && this.getDescricao() != null && ((MotivoInaptidao) object).getDescricao() != null)
			return this.getDescricao().compareTo(((MotivoInaptidao) object).getDescricao());
		
		return super.compareTo(object);
	}

}
