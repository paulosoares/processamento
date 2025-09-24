/**
 * 
 */
package br.jus.stf.estf.decisao.pesquisa.persistence.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;

import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;

/**
 * @author Paulo.Estevao
 * @since 16.11.2010
 */
public class SimplifiedTextoMapper implements RowMapper {

	private Log logger = LogFactory.getLog(SimplifiedTextoMapper.class);

	/* (non-Javadoc)
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
	 */
	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		TextoDto texto = new TextoDto();

		texto.setId(rs.getLong("id"));
//		texto.setProcesso(getIdentificacaoProcesso(rs.getString("sigla"), rs.getLong("numero"), null));
//		try {
//			texto.setFase(FaseTexto.valueOf(rs.getLong("codFase")));
//		} catch (Exception e) {
//			logger.error(String.format("Texto [%s] nao possui fase. Setando como [Nao elaborado]...", texto.getId()));
//			texto.setFase(FaseTexto.NAO_ELABORADO);
//		}
//		texto.setTipoTexto(TipoTexto.valueOf(rs.getLong("codTipoTexto")));
//		texto.setObservacao(rs.getString("observacao"));
//		texto.setIdArquivoEletronico(rs.getLong("seqArquivoEletronico"));
//		texto.setIdObjetoIncidente(rs.getLong("idObjetoIncidente"));
//		texto.setSequenciaVotos(rs.getLong("sequenciaVotos"));
		
		return texto;
	}
	
	/**
	 * Formata e retorna a identificação do processo com base na sigla, numero e cadeia do incidente.
	 */
	private String getIdentificacaoProcesso(String sigla, Long numero, String cadeia) {
		String cadeiaFormatada = StringUtils.isNotBlank(cadeia)? cadeia.replaceFirst(sigla + "-", ""):"Mérito";
		
		return String.format("%s %s %s", sigla, numero, cadeiaFormatada);
	}

}
