package br.jus.stf.estf.decisao.pesquisa.persistence.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * Mapeia o resultado retornado em um {@link java.sql.ResultSet} para um
 * objeto do tipo inteiro.
 * 
 * @author Rodrigo Barreiros
 * @since 06.11.2010
 */
public class IntegerMapper implements RowMapper {
	
    /**
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
     */
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		return rs.getInt(1);
    }
    
}
