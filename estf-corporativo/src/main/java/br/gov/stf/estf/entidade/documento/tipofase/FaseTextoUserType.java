package br.gov.stf.estf.entidade.documento.tipofase;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

public class FaseTextoUserType implements UserType {

	private static final int[] SQL_TYPES = new int[] { Types.INTEGER };

	public Object assemble(Serializable cached, Object owner) {
		return cached;
	}

	public Object deepCopy(Object value) {
		return value;
	}

	public Serializable disassemble(Object value) {
		return (Serializable) value;
	}

	public boolean equals(Object x, Object y) throws HibernateException {
		return (x == null && y == null) || (x != null && x.equals(y));
	}

	public int hashCode(Object x) {
		return x == null ? 0 : x.hashCode();
	}

	public boolean isMutable() {
		return false;
	}

	public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {

		// Number codeNum = (Number) rs.getObject(names[0]);
		Long codeNum = rs.getLong(names[0]);
		if (rs.wasNull())
			return FaseTexto.NAO_ELABORADO;

		long code = codeNum.longValue();

		FaseTexto[] fases = FaseTexto.values();
		for (int i = 0; i < fases.length; i++) {
			Long codigoFase = fases[i].getCodigoFase();
			if (codigoFase == null)
				continue;
			if (codigoFase.longValue() == code)
				return fases[i];
		}
		throw new HibernateException("Código de tipo de fase inválido: " + code);
	}

	public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
		if (value == null) {
			st.setObject(index, null);
		}
		st.setObject(index, Long.valueOf(((FaseTexto) value).getCodigoFase()));

	}

	public Object replace(Object original, Object target, Object owner) {
		return original;
	}

	public Class returnedClass() {
		return FaseTexto.class;
	}

	public int[] sqlTypes() {
		return SQL_TYPES;
	}

}
