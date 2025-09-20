package br.gov.stf.estf.julgamento.model.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import br.gov.stf.estf.entidade.usuario.TipoPessoa;

public class TipoPessoaType extends AbstractCustomUserType {

	private static final long serialVersionUID = 1L;
	private static final int[] SQL_TYPES = { Types.VARCHAR };
	
	public static final String CLASS_NAME = "br.gov.stf.estf.julgamento.model.util.TipoPessoaType";

	@Override
	public int[] sqlTypes() {
		return SQL_TYPES;
	}

	@Override
	public Class<TipoPessoa> returnedClass() {
		return TipoPessoa.class;
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, Object owner)
			throws HibernateException, SQLException {
		return TipoPessoa.getTipoPessoaPorSigla(rs.getString(names[0]));
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index)
			throws HibernateException, SQLException {
		if ((value == null) || (!(value instanceof TipoPessoa))) {
			st.setNull(index, Types.VARCHAR);
		} else {
			TipoPessoa tipoPessoa = (TipoPessoa) value;
			st.setString(index, tipoPessoa.getSigla());
		}
		
	}

}
