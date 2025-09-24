package br.jus.stf.estf.decisao.pesquisa.persistence.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import br.jus.stf.estf.decisao.pesquisa.domain.ListaIncidentesDto;

/**
 * Mapeia os registros retornados em um {@link java.sql.ResultSet} para 
 * objetos do tipo {@link ListaIncidentesDto}.
 * 
 * @author Rodrigo Barreiros
 * @since 05.05.2010
 */
public class ListaIncidentesMapper implements RowMapper {
	
    /**
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
     */
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    	ListaIncidentesDto lista = new ListaIncidentesDto();
    	
    	lista.setId(rs.getLong("id"));
    	lista.setNome(rs.getString("nome"));
    	
    	return lista;
    }
    
}
