package br.gov.stf.estf.assinatura.relatorio.dataaccess;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import br.gov.stf.estf.assinatura.relatorio.RelatorioAutosEmprestadosAdvogados;

public class RelatorioAutosEmprestadosAdvogadosMapper implements RowMapper {
	public static final String NOME_ADVOGADO = "nomeAdvogado";
	public static final String CODIGO_ADVOGADO = "codigoAdvogado";
	public static final String SIGLA_CLASSE_PROCESSO = "siglaClasseProcesso";
	public static final String NUMERO_PROCESSO = "numeroProcesso";
	public static final String DATA_REMESSA = "dataRemssa";
	public static final String RELATOR = "relator";
	public static final String TIPO_ORGAO_DESTINO = "tipoOrgaoDestino";
//	public static final String DESCRICAO_ORGAO_DESTINO = "descricaoOrgaoDestino";

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		RelatorioAutosEmprestadosAdvogados registro = new RelatorioAutosEmprestadosAdvogados();
		registro.setNomeAdvogado(rs.getString(NOME_ADVOGADO));
		registro.setCodigoAdvogado(rs.getLong(CODIGO_ADVOGADO));
		registro.setSiglaClasseProcesso(rs.getString(SIGLA_CLASSE_PROCESSO));
		registro.setNumeroProcesso(rs.getLong(NUMERO_PROCESSO));
		registro.setDataRemessa(rs.getDate(DATA_REMESSA));
		registro.setRelator(rs.getString(RELATOR));
		registro.setTipoOrgaoDestino(rs.getInt(TIPO_ORGAO_DESTINO));
		registro.setDescricaoDestino(rs.getString(NOME_ADVOGADO));
		return registro;
	}

}
