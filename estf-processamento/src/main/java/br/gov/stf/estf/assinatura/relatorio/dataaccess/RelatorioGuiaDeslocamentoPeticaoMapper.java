package br.gov.stf.estf.assinatura.relatorio.dataaccess;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import br.gov.stf.estf.assinatura.relatorio.RelatorioGuiaDeslocamentoPeticao;

public class RelatorioGuiaDeslocamentoPeticaoMapper implements RowMapper {
	public static final String NUMERO_GUIA = "numeroDaGuia";
	public static final String ANO_GUIA = "anoDaGuia";
	public static final String CODIGO_ORGAO_ORIGEM = "codigoOrgaoOrigem";
	public static final String DESCRICAO_ORGAO_ORIGEM = "descricaoOrgaoOrigem";
	public static final String CODIGO_ORGAO_DESTINO = "codigoOrgaoDestino";
	public static final String DESCRICAO_ORGAO_DESTINO = "descricaoOrgaoDestino";
	public static final String SIGLA_CLASSE_PROCESSO = "siglaClasseProcesso";
	public static final String NUMERO_PROCESSO = "numeroProcesso";
	public static final String NUMERO_PETICAO = "numeroPeticao";
	public static final String ANO_PETICAO = "anoPeticao";
	public static final String OBSERVACAO = "observacao";
	public static final String DATA_REMESSA = "dataRemessa";
	public static final String DATA_RECEBIMENTO = "dataRecebimento";
	public static final String TIPO_MEIO = "tipoMeio";
	//
	public static final String ENDERECO_ORGAO_DESTINO = "enderecoOrgaoDestino";
	public static final String MUNICIPIO_ORGAO_DESTINO = "municipioOrgaoDestino";
	public static final String UF_ORGAO_DESTINO = "ufOrgaoDestino";
	public static final String CEP_ORGAO_DESTINO = "cepOrgaoDestino";

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		RelatorioGuiaDeslocamentoPeticao registro = new RelatorioGuiaDeslocamentoPeticao();
		registro.setNumeroDaGuia(rs.getLong(NUMERO_GUIA));
		registro.setAnoDaGuia(rs.getShort(ANO_GUIA));
		registro.setCodigoOrgaoOrigem(rs.getLong(CODIGO_ORGAO_ORIGEM));
		registro.setDescricaoOrgaoOrigem(rs.getString(DESCRICAO_ORGAO_ORIGEM));
		registro.setCodigoOrgaoDestino(rs.getLong(CODIGO_ORGAO_DESTINO));
		registro.setDescricaoOrgaoDestino(rs.getString(DESCRICAO_ORGAO_DESTINO));
		registro.setSiglaClasseProcesso(rs.getString(SIGLA_CLASSE_PROCESSO));
		registro.setNumeroProcesso(rs.getLong(NUMERO_PROCESSO));
		registro.setNumeroPeticao(rs.getLong(NUMERO_PETICAO));
		registro.setAnoPeticao(rs.getShort(ANO_PETICAO));
		registro.setObservacao(rs.getString(OBSERVACAO));
		registro.setDataRemessa(rs.getDate(DATA_REMESSA));
		registro.setDataRecebimento(rs.getDate(DATA_RECEBIMENTO));
		registro.setTipoMeio(rs.getString(TIPO_MEIO));
		//
		registro.setEnderecoOrgaoDestino(rs.getString(ENDERECO_ORGAO_DESTINO));
		registro.setMunicipioOrgaoDestino(rs.getString(MUNICIPIO_ORGAO_DESTINO));
		registro.setUfOrgaoDestino(rs.getString(UF_ORGAO_DESTINO));
		registro.setCepOrgaoDestino(rs.getString(CEP_ORGAO_DESTINO));
		return registro;
	}

}
