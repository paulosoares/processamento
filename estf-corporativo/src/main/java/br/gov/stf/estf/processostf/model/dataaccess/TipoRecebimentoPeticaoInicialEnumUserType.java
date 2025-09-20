package br.gov.stf.estf.processostf.model.dataaccess;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

import br.gov.stf.estf.entidade.processostf.TipoRecebimentoPeticaoInicial;

public class TipoRecebimentoPeticaoInicialEnumUserType implements UserType {

	private static final int[]	SQL_TYPES	= { Hibernate.STRING.sqlType() };


	public int[] sqlTypes() {
		return SQL_TYPES;
	}


	public Class returnedClass() {
		return TipoRecebimentoPeticaoInicial.class;
	}


	public Object nullSafeGet(ResultSet resultSet, String[] names, Object owner) throws HibernateException,
			SQLException {
		String codigoTipoMeioProcesso = resultSet.getString(names[0]);

		TipoRecebimentoPeticaoInicial tipo = TipoRecebimentoPeticaoInicial.X;

		if (!resultSet.wasNull()) {
			tipo = tipo.getTipoRecebimentoPeticaoInicial(codigoTipoMeioProcesso);
		}

		return tipo;
	}


	public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index) throws HibernateException,
			SQLException {
		if ((value == null) || !(value instanceof TipoRecebimentoPeticaoInicial)) {
			preparedStatement.setNull(index, Types.VARCHAR);
		} else {

			TipoRecebimentoPeticaoInicial tipo = (TipoRecebimentoPeticaoInicial) value;

			preparedStatement.setString(index, tipo.getCodigo());
		}
	}


	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return cached;
	}


	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) value;
	}


	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}


	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}


	public boolean isMutable() {
		return false;
	}


	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}


	public boolean equals(Object x, Object y) throws HibernateException {
		if (x == y)
			return true;
		if (null == x || null == y)
			return false;
		return x.equals(y);
	}
}
