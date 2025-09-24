package br.jus.stf.estf.decisao.pesquisa.persistence.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.pretty.Formatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import br.jus.stf.estf.decisao.pesquisa.domain.ListaIncidentesDto;
import br.jus.stf.estf.decisao.pesquisa.domain.ListaTextosDto;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.pesquisa.domain.Pesquisa;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.pesquisa.domain.TipoPermissaoTexto;
import br.jus.stf.estf.decisao.pesquisa.persistence.PesquisaDao;
import br.jus.stf.estf.decisao.pesquisa.persistence.jdbc.mapper.ListaIncidentesMapper;
import br.jus.stf.estf.decisao.pesquisa.persistence.jdbc.mapper.ListaTextosMapper;
import br.jus.stf.estf.decisao.pesquisa.persistence.jdbc.mapper.ObjetoIncidenteMapper;
import br.jus.stf.estf.decisao.pesquisa.persistence.jdbc.mapper.SimplifiedTextoMapper;
import br.jus.stf.estf.decisao.pesquisa.persistence.jdbc.mapper.TextoMapper;
import br.jus.stf.estf.decisao.support.controller.faces.datamodel.PagedList;
import br.jus.stf.estf.decisao.support.query.OrderByClause;
import br.jus.stf.estf.decisao.support.query.Query;
import br.jus.stf.estf.decisao.support.security.Principal;

/**
 * Implementação JDBC para interface Dao <code>PesquisaDao</code>.
 * 
 * @author Rodrigo Barreiros
 * @since 30.04.2010
 */
@Repository
public class PesquisaDaoImpl implements PesquisaDao {

	/**
	 * Define a quantidade máxima de resultados que será apresentada na
	 * SuggesitonBox
	 */
	private static final int MAXIMO_RESULTADOS_SUGGESTION_BOX = 2000;

	@Qualifier("oiSuggestionQuery")
	@Autowired
	private String oiSuggestionQuery;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * Indica se o SQL gerado deve ser registrado, ou não.
	 */
	private boolean showSql = false;

	private final Log logger = LogFactory.getLog(PesquisaDaoImpl.class);

	private final String RELATOR_INCIDENTE_QUERY_PATTERN = "select sm.cod_ministro idMinistro, ministro.nom_ministro nomeMinistro from "
			+ "	(select seq_objeto_incidente from judiciario.objeto_incidente oi "
			+ "    start with oi.seq_objeto_incidente = ? "
			+ "	 connect by prior oi.seq_objeto_incidente_pai = oi.seq_objeto_incidente "
			+ "	 order by seq_objeto_incidente desc) x, stf.sit_min_processos sm, stf.ministros ministro "
			+ "where x.seq_objeto_incidente = sm.seq_objeto_incidente "
			+ "and sm.cod_ministro = ministro.cod_ministro "
			+ "and (sm.flg_relator_incidente = 'S' or sm.cod_ocorrencia = 'RI') "
			+ "and rownum = 1";

	/**
	 * @see br.jus.stf.estf.decisao.pesquisa.persistence.PesquisaDao#pesquisarObjetosIncidente(br.jus.stf.estf.decisao.pesquisa.domain.Pesquisa)
	 */
	@Override
	public PagedList<ObjetoIncidenteDto> pesquisarObjetosIncidente(
			Pesquisa pesquisa) {
		Query<ObjetoIncidenteDto> query = new Query<ObjetoIncidenteDto>(
				jdbcTemplate, new ObjetoIncidenteMapper());
		query.setProjection("oi.SEQ_OBJETO_INCIDENTE id, oi.TIP_OBJETO_INCIDENTE tipo, processo.SIG_CLASSE_PROCES sigla, "
				+ "processo.NUM_PROCESSO numero, processo.DAT_AUTUACAO dataAutuacao, processo.TIP_MEIO_PROCESSO tipoProcesso, "
				+ "COALESCE(JUDICIARIO.PKG_CONSULTA.FNC_SIGLA_CADEIA(oi.SEQ_OBJETO_INCIDENTE,oi.TIP_OBJETO_INCIDENTE),' ') cadeia, "
				+ "JUDICIARIO.PKG_RELATORIA.FNC_RECUPERA_RELATOR(oi.SEQ_OBJETO_INCIDENTE) idRelator, ministro.NOM_MINISTRO nomeRelator");
		query.setCountProjection("oi.SEQ_OBJETO_INCIDENTE");

		if (pesquisa.isNotBlank(Pesquisa.CHAVE_ORDENACAO)) {
			if (pesquisa.get("ordenacao").equals(Pesquisa.ORDENACAO_DATA)) {
				query.setOrderBy("dataAutuacao desc, sigla, numero, cadeia");
			} else if (pesquisa.get("ordenacao").equals(
					Pesquisa.ORDENACAO_CLASSE_PROCESSUAL)) {
				query.setOrderBy("sigla, numero, cadeia");
			}
		} else {
			query.setOrderBy("dataAutuacao desc, sigla, numero, cadeia");
		}

		return query.execute(pesquisa);
	}

	/**
	 * @see br.jus.stf.estf.decisao.pesquisa.persistence.PesquisaDao#pesquisarTextos(br.jus.stf.estf.decisao.pesquisa.domain.Pesquisa)
	 */
	@Override
	public PagedList<TextoDto> pesquisarTextos(Pesquisa pesquisa,
			boolean pesquisaTextualRapida, Principal principal) {
		if (pesquisaTextualRapida) {
			Query<TextoDto> query = new Query<TextoDto>(jdbcTemplate,
					new SimplifiedTextoMapper());
			query.setProjection("texto.SEQ_TEXTOS id");
			query.setCountProjection("texto.SEQ_TEXTOS");
			query.setOrderBy("1 desc");
			query.setPesquisaRapidaPorTextos(true);
			query.setJoinsParaObjetoIncidente("texto.SEQ_OBJETO_INCIDENTE = oi.SEQ_OBJETO_INCIDENTE");
			return query.execute(pesquisa);
		} else {
			Query<TextoDto> query = new Query<TextoDto>(jdbcTemplate,
					new TextoMapper());
			query.setProjection("texto.SEQ_TEXTOS id, processo.SIG_CLASSE_PROCES sigla, processo.NUM_PROCESSO numero, oi.SEQ_OBJETO_INCIDENTE idObjetoIncidente, texto.FLG_LIBERACAO_ANTECIPADA liberacaoAntecipada, texto.COD_TIPO_VOTO tipoVoto, "
					+ "texto.COD_TIPO_TEXTO codTipoTexto, texto.OBS_TEXTO observacao, texto.COD_TIPO_FASE_TEXTO codFase, "
					+ "texto.FLG_TEXTOS_IGUAIS textosIguais, texto.DAT_INCLUSAO dataInclusao, texto.DAT_ALTERACAO dataAlteracao, texto.FLG_FAVORITO_GABINETE favoritoNoGabinete, "
					+ "texto.SEQ_ARQUIVO_ELETRONICO seqArquivoEletronico, ministro.SIG_MINISTRO ministro, ministro.NOM_MINISTRO nomeMinistro, ministro.COD_SETOR idSetorMinistro, usuario.NOM_USUARIO responsavel, "
					+ "tipo_texto.dsc_tipo descricaoTipoTexto, texto.FLG_PUBLICO publico, "
					+ "COALESCE (JUDICIARIO.PKG_CONSULTA.FNC_SIGLA_CADEIA(oi.SEQ_OBJETO_INCIDENTE, oi.TIP_OBJETO_INCIDENTE), ' ') cadeia, "
					+ "(select max(DAT_FASE) from DOC.FASE_TEXTO_PROCESSO where SEQ_TEXTOS = texto.SEQ_TEXTOS) dataFase, "
					+ "texto.COD_MINISTRO idMinistro, texto.SEQ_VOTOS sequenciaVotos, texto.DAT_SESSAO dataSessao, controle_votos.SESSAO tipoSessao, controle_votos.SEQ_TIPO_SITUACAO_TEXTO as situacaoTexto, "
					+ "texto.TIP_RESTRICAO tipoRestricao, texto.USU_INCLUSAO idUsuarioInclusao, processo.TIP_MEIO_PROCESSO tipoMeioProcesso, texto.FLG_ORIGEM_DIGITAL origemDigital, texto.FLG_JULGAMENTO_DIGITAL julgamentoDigital, "
					
					// Comentado para otimizar a busca de textos. A observacao agora é buscada pelo método adicionarObservacaoFase()
					// + "(select TXT_OBSERVACAO from DOC.FASE_TEXTO_PROCESSO ftp where ftp.SEQ_FASE_TEXTO_PROCESSO = (select max(SEQ_FASE_TEXTO_PROCESSO) from DOC.FASE_TEXTO_PROCESSO ftp2 where ftp2.SEQ_TEXTOS = texto.SEQ_TEXTOS and ftp2.TXT_OBSERVACAO IS NOT NULL and length(ftp2.TXT_OBSERVACAO) > 0)) observacaoFase, "
					+ " null observacaoFase, "

					// Comentado para otimizar a busca de textos. A observacao agora é buscada pelo método adicionarPermissaoGrupo()
					//+ " CASE WHEN texto.seq_grupo_responsavel IS NOT NULL THEN (SELECT 'GRUPO' FROM egab.usuario_grupo WHERE sig_usuario = " + "'"+ principal.getUsuario().getId().toUpperCase() + "'" + " AND ROWNUM = 1)  ELSE 'USUARIO' END permgrupo, "
					+ "null permgrupo, "
					
					// Comentado para otimizar a busca de textos. A observacao agora é buscada pelo método adicionarNomeGrupo()
					//+ " (SELECT gu.dsc_grupo FROM egab.grupo_usuario gu WHERE gu.seq_grupo_usuario =  texto.seq_grupo_responsavel) nomeGrupo, "
					+ "null nomeGrupo, "
					
					+ "CASE WHEN texto.USU_AUTOR_INTELECTUAL IS NOT NULL THEN texto.USU_AUTOR_INTELECTUAL ELSE TO_CHAR(texto.seq_grupo_responsavel) END idResponsavel ");
			
			query.setCountProjection("texto.SEQ_TEXTOS");
			query.setJoinsParaObjetoIncidente(
					"texto.SEQ_OBJETO_INCIDENTE = oi.SEQ_OBJETO_INCIDENTE",
					"texto.COD_MINISTRO = ministro.COD_MINISTRO (+)",
					"texto.USU_AUTOR_INTELECTUAL = usuario.SIG_USUARIO (+)",
					"processo.SEQ_OBJETO_INCIDENTE = oi.SEQ_OBJETO_INCIDENTE_PRINCIPAL ");

			if (pesquisa.isNotBlank(Pesquisa.CHAVE_ORDENACAO)) {
				if (pesquisa.get("ordenacao").equals(Pesquisa.ORDENACAO_DATA)) {
					// Obs.: não colocar alias no order by
					query.setOrderBy("texto.DAT_ALTERACAO desc");
				} else if (pesquisa.get("ordenacao").equals(
						Pesquisa.ORDENACAO_CLASSE_PROCESSUAL)) {
					// Obs.: não colocar alias no order by, com exceção da
					// cadeia
					query.setOrderBy("processo.SIG_CLASSE_PROCES, processo.NUM_PROCESSO, cadeia, texto.SEQ_VOTOS, tipo_texto.DSC_TIPO, texto.OBS_TEXTO");
				}
			} else {
				// Obs.: não colocar alias no order by
				query.setOrderBy("processo.SIG_CLASSE_PROCES, processo.NUM_PROCESSO, cadeia, texto.SEQ_VOTOS, tipo_texto.DSC_TIPO, texto.OBS_TEXTO");
			}
			
			if (pesquisa.isNotBlank(Pesquisa.CHAVE_FAVORITOS)) {
				String[] opcoesSelecionadas = (String[]) pesquisa.get(Pesquisa.CHAVE_FAVORITOS);
				if (ArrayUtils.contains(opcoesSelecionadas, Pesquisa.FAVORITOS_PRIMEIRO)){
					query.setPreOrderBy(OrderByClause.buildOrderBy().addItem("texto.FLG_FAVORITO_GABINETE", "DESC NULLS LAST"));
				}
			}
			PagedList<TextoDto> lista = query.execute(pesquisa);
			adicionarObservacaoFase(lista);
			adicionarPermissaoGrupo(lista, principal);
			adicionarNomeGrupo(lista);
			return lista;
		}
	}

	private void adicionarObservacaoFase(final PagedList<TextoDto> lista) {
		List<Long> listaIds = new ArrayList<Long>();
		
		for (TextoDto textoDto : lista.getResults())
			listaIds.add(textoDto.getId());
		
		if (listaIds != null && !listaIds.isEmpty()) {
			String ids = StringUtils.join(listaIds,",");
			String sql = "SELECT ftp.SEQ_TEXTOS, ftp.TXT_OBSERVACAO observacaoFase FROM DOC.FASE_TEXTO_PROCESSO ftp WHERE ftp.SEQ_TEXTOS IN ("+ids+")";
			jdbcTemplate.query(sql, new RowMapper() {
				
				@Override
				public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
					Long seqTexto = rs.getLong("SEQ_TEXTOS");
					
					for (TextoDto textoDto : lista.getResults())
						if (textoDto.getId().equals(seqTexto))
							textoDto.setObservacaoFase(rs.getString("observacaoFase"));
					
					return null;
				}
			});
		}
	}
   
	private void adicionarPermissaoGrupo(final PagedList<TextoDto> lista, Principal principal) {
		List<Long> listaIds = new ArrayList<Long>();
		
		for (TextoDto textoDto : lista.getResults())
			listaIds.add(textoDto.getId());
		
		if (listaIds != null && !listaIds.isEmpty()) {
			String ids = StringUtils.join(listaIds,",");
			String sql = "SELECT texto.seq_textos, CASE WHEN texto.seq_grupo_responsavel IS NOT NULL THEN (SELECT 'GRUPO' FROM egab.usuario_grupo WHERE sig_usuario = " + "'"+ principal.getUsuario().getId().toUpperCase() + "'" + " AND ROWNUM = 1)  ELSE 'USUARIO' END permgrupo FROM STF.TEXTOS texto WHERE texto.seq_textos IN ("+ids+") ";
			jdbcTemplate.query(sql, new RowMapper() {
				
				@Override
				public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
					Long seqTexto = rs.getLong("SEQ_TEXTOS");
					
					for (TextoDto textoDto : lista.getResults())
						if (textoDto.getId().equals(seqTexto) && rs.getString("permgrupo") != null)
							textoDto.setTipoPermissaoTexto(TipoPermissaoTexto.valueOf(rs.getString("permgrupo")));
					
					return null;
				}
			});
		}
	}
	
	private void adicionarNomeGrupo(final PagedList<TextoDto> lista) {
		List<Long> listaIds = new ArrayList<Long>();
		
		for (TextoDto textoDto : lista.getResults())
			listaIds.add(textoDto.getId());
		
		if (listaIds != null && !listaIds.isEmpty()) {
			String ids = StringUtils.join(listaIds,",");
			String sql = "SELECT texto.seq_textos, (SELECT gu.dsc_grupo FROM egab.grupo_usuario gu WHERE gu.seq_grupo_usuario =  texto.seq_grupo_responsavel) nomeGrupo FROM STF.TEXTOS texto WHERE texto.seq_textos IN ("+ids+")";
			jdbcTemplate.query(sql, new RowMapper() {
				
				@Override
				public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
					Long seqTexto = rs.getLong("SEQ_TEXTOS");
					
					for (TextoDto textoDto : lista.getResults())
						if (textoDto.getId().equals(seqTexto)) {
							textoDto.setNomeGrupo(rs.getString("nomeGrupo"));
							if (textoDto.getResponsavel() == null || textoDto.getResponsavel().isEmpty())
								textoDto.setResponsavel(rs.getString("nomeGrupo"));
						}
					return null;
				}
			});
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.jus.stf.estf.decisao.pesquisa.persistence.PesquisaDao#pesquisarTextos
	 * (br.jus.stf.estf.decisao.pesquisa.domain.Pesquisa)
	 */
	@Override
	public PagedList<TextoDto> pesquisarTextos(Pesquisa pesquisa, Principal principal) {
		return pesquisarTextos(pesquisa, false, principal);
	}

	/**
	 * @see br.jus.stf.estf.decisao.pesquisa.persistence.PesquisaDao#pesquisarListasIncidentes(br.jus.stf.estf.decisao.pesquisa.domain.Pesquisa)
	 */
	@Override
	public PagedList<ListaIncidentesDto> pesquisarListasIncidentes(
			Pesquisa pesquisa) {
		Query<ListaIncidentesDto> query = new Query<ListaIncidentesDto>(
				jdbcTemplate, new ListaIncidentesMapper());
		query.setProjection("lista_incidentes.SEQ_GRUPO_PROCESSO_SETOR id, lista_incidentes.NOM_GRUPO_PROCESSO_SETOR nome");
		query.setCountProjection("lista_incidentes.SEQ_GRUPO_PROCESSO_SETOR");
		query.setJoinsParaObjetoIncidente(
				"lista_incidentes.SEQ_GRUPO_PROCESSO_SETOR = incidente_listas.SEQ_GRUPO_PROCESSO_SETOR(+)",
				"incidente_listas.SEQ_OBJETO_INCIDENTE = oi.SEQ_OBJETO_INCIDENTE(+)");
		query.setOrderBy("nome");

		return query.execute(pesquisa);
	}

	/**
	 * @see br.jus.stf.estf.decisao.pesquisa.persistence.PesquisaDao#pesquisarListasTextos(br.jus.stf.estf.decisao.pesquisa.domain.Pesquisa)
	 */
	@Override
	public PagedList<ListaTextosDto> pesquisarListasTextos(Pesquisa pesquisa) {
		Query<ListaTextosDto> query = new Query<ListaTextosDto>(jdbcTemplate,
				new ListaTextosMapper());
		query.setProjection("lista_textos.SEQ_LISTA_TEXTO id, lista_textos.DSC_LISTA_TEXTO nome");
		query.setCountProjection("lista_textos.SEQ_LISTA_TEXTO");
		query.setJoinsParaObjetoIncidente(
				"lista_textos.SEQ_LISTA_TEXTO = texto_listas.SEQ_LISTA_TEXTO(+)",
				"texto_listas.SEQ_TEXTOS = texto.SEQ_TEXTOS(+)",
				"texto.SEQ_OBJETO_INCIDENTE = oi.SEQ_OBJETO_INCIDENTE(+)");
		query.setOrderBy("nome");

		return query.execute(pesquisa);
	}

	/**
	 * @throws NumeroProcessoNaoInformadoException Quando o número do processo não for informado
	 * @see br.jus.stf.estf.decisao.pesquisa.persistence.PesquisaDao#pesquisarObjetosIncidente(java.lang.String,
	 *      java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ObjetoIncidenteDto> pesquisarObjetosIncidente(String sigla,
			Long numero) throws NumeroProcessoNaoInformadoException {
		List<Object> parameters = new ArrayList<Object>();
		List<String> clauses = new ArrayList<String>();
		if (StringUtils.isNotBlank(sigla)) {
			clauses.add("lower(principal.SIG_CLASSE_PROCES) = ? ");
			parameters.add(sigla.toLowerCase());
		}
		if (numero != null) {
			clauses.add("principal.NUM_PROCESSO = ? ");
			parameters.add(numero);
		} else {
			// Não permite que consultas sem um número sejam realizadas no banco
			// de dados, pois trazem uma quantidade grande de informações, que
			// pode derrubar o servidor.
			throw new NumeroProcessoNaoInformadoException(
					"O número do processo deve ser informado!");
		}

		clauses.add("oi.tip_objeto_incidente in (?, ?, ?)");
		parameters.add("PR");
		parameters.add("RC");
		parameters.add("IJ");

		clauses.add("(recurso.cod_situacao is null or recurso.cod_situacao <> ?)");
		parameters.add("L");
		clauses.add("(processo.cod_situacao is null or processo.cod_situacao <> ?)");
		parameters.add("L");
		
		// apenas recursos ativos
		// conforme ISSUE DECISAO-1407
		clauses.add("(recurso.flg_ativo is null or recurso.flg_ativo = ?)");
		parameters.add("S");

		adicionaLimitacaoResultadoSuggestionBox(parameters, clauses);

		String query = MessageFormat.format(oiSuggestionQuery,
				StringUtils.join(clauses, " and "));

		if (showSql) {
			logger.info(new Formatter(query).format());
		}

		return jdbcTemplate.query(query, parameters.toArray(),
				new ObjetoIncidenteMapper());

	}

	/**
	 * Impõe uma limitação na quantidade de registros que podem ser retornados
	 * para o SuggestionBox. Isso evita que o servidor caia caso a quantidade de
	 * registros para ser renderizado seja muito grande
	 * 
	 * @param parameters
	 * @param clauses
	 */
	private void adicionaLimitacaoResultadoSuggestionBox(
			List<Object> parameters, List<String> clauses) {
		// Utiliza a cláusula do ORACLE. Caso o banco de dados mude, a limitação
		// deve ser alterada também.
		clauses.add("(ROWNUM < ?)");
		parameters.add(MAXIMO_RESULTADOS_SUGGESTION_BOX);
	}

	// @SuppressWarnings("unchecked")
	// @Override
	// public MinistroDto recuperarRelatorIncidente(Long idObjetoIncidente) {
	// if(showSql) {
	// logger.info(new Formatter(RELATOR_INCIDENTE_QUERY_PATTERN).format());
	// }
	//
	// List<Object> parameters = new ArrayList<Object>();
	// parameters.add(idObjetoIncidente);
	//
	// List<MinistroDto> ministros = jdbcTemplate.query(
	// RELATOR_INCIDENTE_QUERY_PATTERN, parameters.toArray(),
	// new MinistroMapper());
	//
	// if (ministros.size() > 0) {
	// return (MinistroDto) ministros.get(0);
	// }
	//
	// return null;
	// }

}
