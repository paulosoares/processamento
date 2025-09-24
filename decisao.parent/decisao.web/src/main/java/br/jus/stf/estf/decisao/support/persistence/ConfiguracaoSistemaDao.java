/**
 * 
 */
package br.jus.stf.estf.decisao.support.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author Paulo.Estevao
 * @since 29.10.2011
 */
@Repository
public class ConfiguracaoSistemaDao {

	private final String SIGLA_SISTEMA = "ESTFDECISAO";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void salvarConfiguracao(String chave, String valor) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT TXT_VALOR_CONFIGURACAO FROM GLOBAL.CONFIGURACAO_SISTEMA ");
		sql.append(" WHERE SIG_SISTEMA = ? ");
		sql.append(" AND DSC_CHAVE_CONFIGURACAO = ? ");
		String valorAnterior = (String) jdbcTemplate.queryForObject(sql.toString(), new Object[]{SIGLA_SISTEMA, chave}, String.class);
		if (valorAnterior != null) {
			sql = new StringBuffer();
			sql.append(" UPDATE GLOBAL.CONFIGURACAO_SISTEMA SET TXT_VALOR_CONFIGURACAO = '" + valor + "'");
			sql.append(" WHERE SIG_SISTEMA = '" + SIGLA_SISTEMA + "'");
			sql.append(" AND DSC_CHAVE_CONFIGURACAO = '" + chave + "'");
			
			jdbcTemplate.execute(sql.toString());
		} else {
			sql = new StringBuffer();
			sql.append(" INSERT INTO GLOBAL.CONFIGURACAO_SISTEMA ");
			sql.append(" (SIG_SISTEMA, DSC_CHAVE_CONFIGURACAO, TXT_VALOR_CONFIGURACAO)");
			sql.append(" VALUES ('" + SIGLA_SISTEMA + "', '" + chave + "', " + valor + "'");
			
			jdbcTemplate.execute(sql.toString());
		}
	}
	
	
}
