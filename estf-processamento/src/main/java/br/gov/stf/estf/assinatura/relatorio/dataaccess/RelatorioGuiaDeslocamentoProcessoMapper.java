package br.gov.stf.estf.assinatura.relatorio.dataaccess;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import br.gov.stf.estf.assinatura.relatorio.RelatorioGuiaDeslocamentoProcesso;

public class RelatorioGuiaDeslocamentoProcessoMapper implements RowMapper {
	public static final String NUMERO_GUIA = "numeroDaGuia";
	public static final String ANO_GUIA = "anoDaGuia";
	public static final String CODIGO_ORGAO_ORIGEM = "codigoOrgaoOrigem";
	public static final String DESCRICAO_ORGAO_ORIGEM = "descricaoOrgaoOrigem";
	public static final String CODIGO_ORGAO_DESTINO = "codigoOrgaoDestino";
	public static final String DESCRICAO_ORGAO_DESTINO = "descricaoOrgaoDestino";
	public static final String SIGLA_CLASSE_PROCESSO = "siglaClasseProcesso";
	public static final String NUMERO_PROCESSO = "numeroProcesso";
	public static final String QUANTIDADE_VOLUMES = "quantidadeVolumes";
	public static final String QUANTIDADE_APENSOS = "quantidadeApensos";
	public static final String QUANTIDADE_JUNTADA_LINHA = "quantidadeJuntadaLinha";
	public static final String QUANTIDADE_VINCULO = "quantidadeVinculo";
	public static final String OBSERVACAO = "observacao";
	public static final String DATA_REMESSA = "dataRemessa";
	public static final String DATA_RECEBIMENTO = "dataRecebimento";
	public static final String VINCULADO = "vinculado";
	public static final String TIPO_MEIO = "tipoMeio";
	//
	public static final String ENDERECO_ORGAO_DESTINO = "enderecoOrgaoDestino";
	public static final String MUNICIPIO_ORGAO_DESTINO = "municipioOrgaoDestino";
	public static final String UF_ORGAO_DESTINO = "ufOrgaoDestino";
	public static final String CEP_ORGAO_DESTINO = "cepOrgaoDestino";
	public static final String NUM_LOCALIZACAO = "numLocalizacao";
	public static final String BAIRRO = "bairro";
	public static final String COMPLEMENTO = "complemento";
	public static final String DESTINATARIO = "destinatario";
	public static final String CODIGO_DESTINATARIO = "codigoDestinatario";

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		RelatorioGuiaDeslocamentoProcesso registro = new RelatorioGuiaDeslocamentoProcesso();
		registro.setNumeroDaGuia(rs.getLong(NUMERO_GUIA));
		registro.setAnoDaGuia(rs.getShort(ANO_GUIA));
		registro.setCodigoOrgaoOrigem(rs.getLong(CODIGO_ORGAO_ORIGEM));
		registro.setDescricaoOrgaoOrigem(rs.getString(DESCRICAO_ORGAO_ORIGEM));
		registro.setCodigoOrgaoDestino(rs.getLong(CODIGO_ORGAO_DESTINO));
		registro.setDescricaoOrgaoDestino(rs.getString(DESCRICAO_ORGAO_DESTINO));
		registro.setSiglaClasseProcesso(rs.getString(SIGLA_CLASSE_PROCESSO));
		registro.setNumeroProcesso(rs.getLong(NUMERO_PROCESSO));
		registro.setQuantidadeVolumes(rs.getInt(QUANTIDADE_VOLUMES));
		registro.setQuantidadeApensos(rs.getInt(QUANTIDADE_APENSOS));
		registro.setQuantidadeJuntadaLinha(rs.getInt(QUANTIDADE_JUNTADA_LINHA));
		registro.setQuantidadeVinculo(rs.getInt(QUANTIDADE_VINCULO));
		registro.setObservacao(rs.getString(OBSERVACAO));
		registro.setDataRemessa(rs.getDate(DATA_REMESSA));
		registro.setDataRecebimento(rs.getDate(DATA_RECEBIMENTO));
		registro.setVinculado(rs.getInt(VINCULADO));
		registro.setTipoMeio(rs.getString(TIPO_MEIO));
		//
		registro.setEnderecoOrgaoDestino(rs.getString(ENDERECO_ORGAO_DESTINO));
		registro.setMunicipioOrgaoDestino(rs.getString(MUNICIPIO_ORGAO_DESTINO));
		registro.setUfOrgaoDestino(rs.getString(UF_ORGAO_DESTINO));
		registro.setCepOrgaoDestino(rs.getString(CEP_ORGAO_DESTINO));
		registro.setNumLocalizacao(rs.getString(NUM_LOCALIZACAO));
		registro.setBairro(rs.getString(BAIRRO));
		registro.setComplemento(rs.getString(COMPLEMENTO));
		registro.setDestinatario(rs.getString(DESTINATARIO));
		registro.setCodigoDestinatario(rs.getLong(CODIGO_DESTINATARIO));
		return registro;
	}

}
