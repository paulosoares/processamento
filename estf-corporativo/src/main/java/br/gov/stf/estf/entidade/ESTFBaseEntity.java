package br.gov.stf.estf.entidade;

import java.io.Serializable;

import br.gov.stf.framework.model.entity.BaseEntity;

public abstract class ESTFBaseEntity<ID extends Serializable> extends BaseEntity<ID> {
	
	private static final long serialVersionUID = 1L;
	
	protected ID id;

	@Override
	public void setId(ID id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		if( getId() != null )
			return this.getClass().getName() + "-id: " + getId().toString();
		else
			return this.getClass().getName();
	}
}
