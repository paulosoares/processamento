package br.gov.stf.estf.alerta.model.dataaccess;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

import br.gov.stf.estf.entidade.alerta.ConcatenarFiltroConstante;
import br.gov.stf.framework.model.entity.ValuedEnum;

public class ConcatenarFiltroEnumUserType<T extends ValuedEnum> implements UserType {
	
	private Class<T> theClass;
	private static final int[] SQL_TYPES = { Types.VARCHAR };

	public int[] sqlTypes() {
		return SQL_TYPES;
	}

	public Class returnedClass() {
		return this.theClass;
	}

	// UTILIZADO QUANDO RECUPERA O REGISTRO
	public Object nullSafeGet(ResultSet resultSet, String[] names, Object owner)
			throws HibernateException, SQLException {
		String flag = resultSet.getString(names[0]);

		Boolean result = null;

		if (!(resultSet.wasNull())) {
			if ((flag != null)
					&& (flag.equals(ConcatenarFiltroConstante.E.getCodigo())))
				result = Boolean.TRUE;
			else if ((flag != null)
					&& (flag.equals(ConcatenarFiltroConstante.OU.getCodigo())))
				result = Boolean.FALSE;
		} else { 
			result = Boolean.FALSE; 
		}
		 

		return result;
	}

	// UTILIZADO PARA SALVAR O REGISTRO (tem que estar mapeado assim: insertable=true, updatable=true)
	public void nullSafeSet(PreparedStatement preparedStatement, Object value,
			int index) throws HibernateException, SQLException {
		if ((value == null) || (!(value instanceof Boolean))) {
			preparedStatement.setNull(index, Types.VARCHAR);
		} else {
			String flag = ConcatenarFiltroConstante.OU.getCodigo();
			Boolean valor = (Boolean) value;

			if (valor.booleanValue())
				flag = ConcatenarFiltroConstante.E.getCodigo();

			preparedStatement.setString(index, flag);
		}
	}

	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	public boolean isMutable() {
		return false;
	}

	public Object assemble(Serializable cached, Object owner)
			throws HibernateException {
		return cached;
	}

	public Serializable disassemble(Object value) throws HibernateException {
		return ((Serializable) value);
	}

	public Object replace(Object original, Object target, Object owner)
			throws HibernateException {
		return original;
	}

	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	public boolean equals(Object x, Object y) throws HibernateException {
		if (x == y)
			return true;
		if ((null == x) || (null == y))
			return false;
		return x.equals(y);
	}
}