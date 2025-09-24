package br.jus.stf.estf.decisao.pesquisa.persistence.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import br.jus.stf.estf.decisao.pesquisa.domain.ListaTextosDto;

/**
 * Mapeia os registros retornados em um {@link java.sql.ResultSet} para 
 * objetos do tipo {@link ListaTextosDto}.
 * 
 * @author Rodrigo Barreiros
 * @since 05.05.2010
 */
public class ListaTextosMapper implements RowMapper {
	
    /**
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
     */
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    	ListaTextosDto lista = new ListaTextosDto();
    	
    	lista.setId(rs.getLong("id"));
    	lista.setNome(rs.getString("nome"));
    	
    	return lista;
    }
    
}
