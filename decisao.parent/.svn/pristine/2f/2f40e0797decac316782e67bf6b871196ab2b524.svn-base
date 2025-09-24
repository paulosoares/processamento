package br.jus.stf.estf.decisao.pesquisa.persistence.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * Mapeia o resultado retornado em um {@link java.sql.ResultSet} para um
 * objeto do tipo string.
 * 
 * @author Paulo.Estevao
 * @since 07.09.2010
 */
public class StringMapper implements RowMapper {

	/**
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
	 */
	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		return rs.getString(1);
	}

}
