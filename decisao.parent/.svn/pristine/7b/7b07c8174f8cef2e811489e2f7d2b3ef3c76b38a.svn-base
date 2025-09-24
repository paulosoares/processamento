package br.jus.stf.estf.decisao.pesquisa.persistence.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import br.gov.stf.estf.entidade.processostf.TipoMeioProcesso;
import br.gov.stf.estf.entidade.processostf.TipoObjetoIncidente;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;

/**
 * Mapeia os registros retornados em um {@link java.sql.ResultSet} para 
 * objetos do tipo {@link ObjetoIncidenteDto}.
 * 
 * @author Rodrigo Barreiros
 * @since 05.05.2010
 */
public class ObjetoIncidenteMapper implements RowMapper {
	
    /**
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
     */
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    	ObjetoIncidenteDto objetoIncidente = new ObjetoIncidenteDto();
    	
    	objetoIncidente.setId(getLong(rs, "id"));
    	objetoIncidente.setTipo(TipoObjetoIncidente.valueOf(getString(rs, "tipo")));
    	if (objetoIncidente.getTipo() != null) {
	    	if (objetoIncidente.getTipo().equals(TipoObjetoIncidente.PROCESSO)) {
	        	objetoIncidente.setCadeia("Mérito");
	    	} else {
	    		objetoIncidente.setCadeia(getString(rs, "cadeia"));
		    }
    	}
    	objetoIncidente.setTipoProcesso(TipoMeioProcesso.valueOf(getString(rs, "tipoProcesso")));
    	objetoIncidente.setSiglaProcesso(getString(rs, "sigla"));
    	objetoIncidente.setNumeroProcesso(getLong(rs, "numero"));
    	objetoIncidente.setNomeRelator(getString(rs, "nomeRelator"));
    	objetoIncidente.setIdRelator(getLong(rs, "idRelator"));
    	
    	return objetoIncidente;
    }
    
    private Long getLong(ResultSet rs, String alias) {
    	try {
			return rs.getLong(alias);
		} catch (SQLException e) {
			return null;
		}
    }
    
    private String getString(ResultSet rs, String alias) {
    	try {
			return rs.getString(alias);
		} catch (SQLException e) {
			return null;
		}
    }
    
}
