package br.gov.stf.estf.julgamento.model.util;

import java.io.Serializable;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.hibernate.usertype.UserType;

/**
 * Classe abstrata que generaliza todas as implementações customizadas de {@link UserType}.
 * 
 * @author thiago.miranda
 */
public abstract class AbstractCustomUserType implements Serializable, UserType {

	private static final long serialVersionUID = -5629210982085305205L;

	@Override
	public Object deepCopy(Object value) {
		return value;
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Object assemble(Serializable cached, Object owner) {
		return cached;
	}

	@Override
	public Serializable disassemble(Object value) {
		return ((Serializable) value);
	}

	@Override
	public Object replace(Object original, Object target, Object owner) {
		return original;
	}

	@Override
	public int hashCode(Object x) {
		return ObjectUtils.hashCode(x);
	}

	@Override
	public boolean equals(Object x, Object y) {
		return new EqualsBuilder().append(x, y).isEquals();
	}
}
