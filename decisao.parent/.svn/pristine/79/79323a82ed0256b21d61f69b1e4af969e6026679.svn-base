package br.jus.stf.estf.decisao.support.query;

import java.sql.SQLException;
import java.sql.Types;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oracle.jdbc.OracleTypes;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.pretty.Formatter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.julgamento.Colegiado.TipoColegiadoConstante;
import br.gov.stf.estf.entidade.processostf.enuns.SituacaoIncidenteJulgadoOuNao;
import br.gov.stf.estf.processostf.model.dataaccess.hibernate.SubQuerySituacaoJulgamento;
import br.jus.stf.estf.decisao.pesquisa.domain.Pesquisa;
import br.jus.stf.estf.decisao.pesquisa.domain.Pesquisa.TipoPesquisa;
import br.jus.stf.estf.decisao.pesquisa.persistence.jdbc.mapper.IntegerMapper;
import br.jus.stf.estf.decisao.pesquisa.persistence.jdbc.mapper.StringMapper;
import br.jus.stf.estf.decisao.support.controller.faces.datamodel.PagedList;
import br.jus.stf.estf.decisao.support.security.Principal;

/**
 * Representa a pesquisa avançada de Incidentes, Textos, Lista de Incidentes e Lista de Textos.
 * 
 * <p>Monta e executa a consulta SQL de acordo com os critérios de pesquisa.
 * 
 * @author Rodrigo Barreiros
 * @since 10.05.2010
 */
@Repository
public class Query<T> {
	
	private final Log logger = LogFactory.getLog(Query.class);
	
	/**
	 * Template para a consulta paginada de registros
	 */ 
    private static final String SELECT_PATTERN = 
        "select * from " +
        "   (select row_.*, rownum rownum_ from" +
        "       (select distinct {0} from {1} where {2} order by {3}) row_" +
        "   ) " +
        "where rownum_ > {4} and rownum_ <= {5}";
    
    /**
     * Templatte para a consulta completa de registros da pesquisa rápida textual
     */
    private static final String TEXTUAL_SELECT_PATTERN = 
        "select * from " +
        "   (select row_.*, rownum rownum_ from" +
        "       (select /*+ INDEX (arquivo_eletronico2 ictx_arquivocomp) use_hash (texto arquivo_eletronico2) */ " +
        "			distinct id from (select /*+ NO_MERGE */ {0}, texto.SEQ_ARQUIVO_ELETRONICO from {1} where {2}) texto, " +
        "			DOC.ARQUIVO_ELETRONICO arquivo_eletronico2 where {3} order by {4}) row_" +
        "   ) ";
    
    /** 
	 * Template para a consulta pela quantidade de registros 
	 */
	private static final String COUNT_PATTERN = 
		"select /*+ FIRST_ROWS */ count(distinct {0}) from {1} where {2}";
    
    /**
	 * Template para a consulta paginada de textos
	 */
    private static final String TEXTOS_SELECT_PATTERN = 
    	"select /*+ FIRST_ROWS */ {0} from " +
        "   (select * from " +
        "		(select {1}, rownum rownum_ from " +
        "       	(select distinct {2} from {3} where {4} order by {5})) " +
        "		where rownum_ > {6} and rownum_ <= {7}) filtro, {8} " +
        "	where {9}" +
        "	order by rownum_";
	
    /**
	 * Template para a consulta paginada de processos
	 */
    private static final String PROCESSOS_SELECT_PATTERN =
    	"select {0} from " +
    	"	(select * from " +
    	"		(select {1}, rownum rownum_ from " + 
    	"			(select distinct {2} from {3} where {4} order by {5}) pr) " +
    	"		where rownum_ > {6} and rownum_ <= {7}) pr, {8} " +
    	"	where {9}" + 
    	"	order by rownum_";
	
	/** 
	 * Um mapa (alias, tabela) com todas as tabelas envolvidas na pesquisa.
	 */
	private static Map<String, String> allTables = new HashMap<String, String>();
	
	static {
		allTables.put("oi", "JUDICIARIO.OBJETO_INCIDENTE");
		allTables.put("processo", "JUDICIARIO.VW_PROCESSO_RELATOR");
		allTables.put("processo2", "JUDICIARIO.PROCESSO");
		allTables.put("controle_votos", "STF.CONTROLE_VOTOS");
		allTables.put("incidente_listas", "EGAB.PROCESSO_SETOR_GRUPO");
		allTables.put("lista_incidentes", "EGAB.GRUPO_PROCESSO_SETOR");
		// arquivo_eletronico utilizada somente na pesquisa textual
		allTables.put("arquivo_eletronico", "(select SEQ_ARQUIVO_ELETRONICO from DOC.ARQUIVO_ELETRONICO arquivo_eletronico " +
					"where contains (arquivo_eletronico.txt_conteudo, ?) > 0)");
		allTables.put("texto", "STF.TEXTOS");
		allTables.put("texto_listas", "DOC.TEXTO_LISTA_TEXTO");
		allTables.put("lista_textos", "DOC.LISTA_TEXTO");
		allTables.put("tipo_texto", "STF.TIPO_TEXTOS");
		allTables.put("assunto_processo", "STF.ASSUNTO_PROCESSO");
		allTables.put("parte", "JUDICIARIO.VW_JURISDIC_INCID_OTIMIZADA");
		allTables.put("assunto", "STF.ASSUNTOS");
		allTables.put("recurso", "JUDICIARIO.RECURSO_PROCESSO");
		allTables.put("incidente_julgamento", "JUDICIARIO.INCIDENTE_JULGAMENTO");
		allTables.put("ministro", "STF.MINISTROS");
		allTables.put("usuario", "STF.USUARIOS");
		allTables.put("faseTextoProcesso", "DOC.FASE_TEXTO_PROCESSO");
		allTables.put("informacaoPautaProcesso", "JULGAMENTO.INFORMACAO_PAUTA_PROCESSO");
		allTables.put("observacaoProcesso", "JUDICIARIO.OBSERVACAO_PROCESSO");
		allTables.put("processoListaJulgamento", "JULGAMENTO.PROCESSO_LISTA_JULG");
	}
	
    /**
     * A base para inclusão de todos os critérios de busca é Objeto Incidente. Para cada 
     * tipo de pesquisa será necessário definir quais são o joins para se 
     * chegar até objeto incidente.
     */ 
	private List<String> joinsParaObjetoIncidente;
	
    /**
     * Lista de colunas que devem ser retornadas como resultado da pesquisa. É a lista de 
     * colunas que aparecerá na tabela resultado.
     */ 
	private String projection;
	
	/**
	 * A coluna que deve ser usada para contar a quantidade de registros encontrados 
	 * na pesquisa. Geralmente será o identificador primário.
	 */
	private String countProjection;
	
	/**
	 * As colunas para ordenação do resultado da pesquisa.
	 */
	private String orderBy;
	
	/**
	 * Permite adicionar pré-ordenações nas pesquisas.
	 * 
	 */
	private OrderByClause preOrderBy = OrderByClause.EMPTY_ORDER_BY;
	
	/**
	 * Template Spring usado para disparar comandos SQL.
	 */
    private JdbcTemplate jdbcTemplate;
    
    /**
     * Mapper para montar o objeto resultado (TextoDto, por exemplo) a partir do ResultSet.
     */
    private RowMapper mapper;
    
    /**
     * Indica se o SQL gerado deve ser registrado, ou não.
     */
    private boolean showSql = false;
    
    /**
     * Indica se trata-se de uma pesquisa rápida de textos.
     */
    private boolean pesquisaRapidaPorTextos;
    
    /**
     * Centraliza todos os caracteres inválidos utilizados em consulta.
     */
    private String[] caracteresDeConsultaInvalidos = new String[] {"-", ",", "&", "'", "\""};

	/**
	 * Construtor default. Seta o template para pesquisa e a pesquisa que será
	 * usada para adição dos critérios de busca.
	 * 
	 * @param jdbcTemplate o template spring para pesquisa
	 * @param mapper o mapper para montagem do objeto resultado
	 */
	public Query(JdbcTemplate jdbcTemplate, RowMapper mapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.mapper = mapper;
	}
	
	/**
	 * Monta e executa a pesquisa retornando uma lista paginada de registros.
	 * 
	 * @param pesquisa a pesquisa com os critérios de busca
	 * 
	 * @return a lista paginada de registros
	 */
	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = SQLException.class)
	public PagedList<T> execute(Pesquisa pesquisa) {
        long start = System.currentTimeMillis();
        
        // Identificando se os critérios incluem parametros para pesquisa textual...
        boolean textual = pesquisa.isNotBlank("palavraChave");
        
		// Setando parametros de paginação... O tamanho da página será definido em tela.
		int firstResult = pesquisa.getFirstResult();
		int maxResults = pesquisa.getMaxResults();
		
		// Criando conjunto (sem repetições) com as restrições e lista de parâmetros...
		Set<String> joinRestrictions = new LinkedHashSet<String>();
		Set<String> restrictions = new LinkedHashSet<String>();		
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		// Adicionando restrições para critérios de busca...
		addRestrictions(pesquisa, restrictions,  parameters);
		
		// Adicionando restrição para acesso a textos...
		Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		addRestrictionParaTextos(pesquisa, restrictions, parameters, principal.getMinistro() != null ? principal.getMinistro().getId() : null);
		
		// Adicionando restrição para acesso a listas...
		addRestrictionParaListas(pesquisa, restrictions, parameters);
		
		String query = null;
		String countQuery = null;
		
		if(TipoPesquisa.TEXTOS.equals(pesquisa.getTipoPesquisa()) && !pesquisaRapidaPorTextos) {
			// Montando SQL quando for consulta de textos...
			String restrictionsTextoProjection = "texto.SEQ_TEXTOS, texto.SEQ_OBJETO_INCIDENTE, " + preOrderBy.getProjection() + orderBy.replaceAll(" desc", "").replaceAll(" asc", "");
			if(restrictionsTextoProjection.indexOf("cadeia") > 0) {
				restrictionsTextoProjection = restrictionsTextoProjection.replaceFirst("cadeia", "COALESCE (JUDICIARIO.PKG_CONSULTA.FNC_SIGLA_CADEIA(oi.SEQ_OBJETO_INCIDENTE, oi.TIP_OBJETO_INCIDENTE), ' ') cadeia");
			}
			
			// Tendo as restrições, podemos adicionar os joins necessários.
			addJoins(projection, joinRestrictions, pesquisa.getTipoPesquisa());
			
			// Tendo todas as restrições e todos os joins, verificamos quais tabelas serão necessárias.
			String tablesAsString = getTablesAsString(projection, joinRestrictions);
			
			// Formatando restrições para montagem do SQL...
			String joinRestrictionsAsString = getRestrictionsAsString(joinRestrictions, parameters);
			
			
			addJoins(restrictionsTextoProjection, restrictions, false);
			String restrictionsTextoAsString = getRestrictionsAsString(restrictions, parameters);
			String restrictionsTextoTablesAsString = getTablesAsString(restrictionsTextoProjection, restrictions);
			if (textual) {
				restrictionsTextoTablesAsString = getTablesAsStringPesquisaTextual(restrictionsTextoTablesAsString, (String) pesquisa.get("palavraChave"));
			}			

			String subqueryProjection = "SEQ_TEXTOS";

			String orderByQuery = "";
			
			orderByQuery = preOrderBy.getFullClause() + orderBy;
			
			// Montando SQL para consulta normal...
			query = MessageFormat.format(
						TEXTOS_SELECT_PATTERN, 
						projection,
						subqueryProjection,
						restrictionsTextoProjection,
						restrictionsTextoTablesAsString,
						restrictionsTextoAsString,
						orderByQuery,
						Integer.toString(firstResult), 
						Integer.toString(firstResult + maxResults),
						tablesAsString,
						joinRestrictionsAsString);
			
			// Montando SQL para número total de registros...
			countQuery = MessageFormat.format(
						COUNT_PATTERN, 
						countProjection, 
						restrictionsTextoTablesAsString,
						restrictionsTextoAsString);
		} else if (pesquisaRapidaPorTextos && textual) {
			// Tendo as restrições, podemos adicionar os joins necessários.
			addJoins(projection, restrictions, pesquisa.getTipoPesquisa());
			
			// Tendo todas as restrições e todos os joins, verificamos quais tabelas serão necessárias.
			String tablesAsString = getTablesAsString(projection, restrictions);
			
			// Formatando restrições para montagem do SQL...
			String restrictionsAsString = getRestrictionsAsString(restrictions, parameters);
			
			
			Set<String> restrictionsPesquisaTextual = new LinkedHashSet<String>();		
			Map<String, Object> parametersPesquisaTextual = new HashMap<String, Object>();
			
			restrictionsPesquisaTextual.add("arquivo_eletronico2.SEQ_ARQUIVO_ELETRONICO = texto.SEQ_ARQUIVO_ELETRONICO");
			String palavraChave = (String) jdbcTemplate.queryForObject(
					"SELECT BRS.FNC_CONVERTE_PESQUISA(?) as valor from dual",
					new Object[] { pesquisa.get("palavraChave") },
					String.class);
			
			palavraChave = adequarCaracteresDeConsultaInvalidos(palavraChave);
					
//			addRestriction(restrictionsPesquisaTextual, parametersPesquisaTextual, "contains(arquivo_eletronico2.USU_ALTERACAO, ?) > 0", palavraChave); novo índice textual
			addRestriction(restrictionsPesquisaTextual, parametersPesquisaTextual, "contains(arquivo_eletronico2.TXT_CONTEUDO, ?) > 0", palavraChave);
			
			String restrictionsPesquisaTextualAsString = getRestrictionsAsString(restrictionsPesquisaTextual, parametersPesquisaTextual);
			
			// Montando SQL para consulta normal...
			query = MessageFormat.format(
					TEXTUAL_SELECT_PATTERN, 
					projection,
					tablesAsString, 
					restrictionsAsString,
					restrictionsPesquisaTextualAsString, 
					orderBy);
		} 
		else if(TipoPesquisa.PROCESSOS.equals(pesquisa.getTipoPesquisa())) {
			// Montando SQL quando for consulta de processos...
			StringBuffer restrictionsProcessoProjection = new StringBuffer();
			String[] tokens = projection.split(", ");
			StringBuffer newProjection = new StringBuffer();
			StringBuffer subqueryProjection = new StringBuffer();
			for(int i = 0; i < tokens.length; i++) {
				String campo = tokens[i];
				if(hasAlias(campo, null, "processo") || hasAlias(campo, null, "oi") || hasAlias(campo, null, "cadeia")) {
					String[] aliasTokens = campo.split(" ");
					String alias = "";
					if(aliasTokens.length > 0) {
						alias = aliasTokens[aliasTokens.length - 1];
					}
					
					if(subqueryProjection.length() > 0) {
						subqueryProjection.append(", ");
					}
					subqueryProjection.append(alias);

					
					if(newProjection.length() > 0) {
						newProjection.append(", ");
					}
					newProjection.append(alias);
					
					if(restrictionsProcessoProjection.length() > 0) {
						restrictionsProcessoProjection.append(", ");
					}
					restrictionsProcessoProjection.append(campo);
				} else {
					if(newProjection.length() > 0) {
						newProjection.append(", ");
					}
					newProjection.append(campo);
				}
			}
			
			// Restrição de Recursos e Incidentes Julgamento ativo
			addRestriction(restrictions, parameters, "(recurso.FLG_ATIVO IS NULL OR recurso.FLG_ATIVO = ?)", "S");
			addRestriction(restrictions, parameters, "(incidente_julgamento.FLG_ATIVO IS NULL OR incidente_julgamento.FLG_ATIVO = ?)", "S");
			
			// Restrição de Recursos e Incidentes Julgamento em cadastramento - L
			addRestriction(restrictions, parameters, "(recurso.COD_SITUACAO IS NULL OR recurso.COD_SITUACAO <> ?)", "L");
			addRestriction(restrictions, parameters, "(incidente_julgamento.COD_SITUACAO IS NULL OR incidente_julgamento.COD_SITUACAO <> ?)", "L");
			
			// Tendo as restrições, podemos adicionar os joins necessários.
			addJoins(newProjection.toString(), joinRestrictions, pesquisa.getTipoPesquisa());
			
			// Tendo todas as restrições e todos os joins, verificamos quais tabelas serão necessárias.
			String tablesAsString = getTablesAsString(newProjection.toString(), joinRestrictions);
			
			// Formatando restrições para montagem do SQL...
			String joinRestrictionsAsString = getRestrictionsAsString(joinRestrictions, parameters);			
			
			addJoins(restrictionsProcessoProjection.toString(), restrictions, false);
			String restrictionsProcessoAsString = getRestrictionsAsString(restrictions, parameters);
			String restrictionsProcessoTablesAsString = getTablesAsString(restrictionsProcessoProjection.toString(), restrictions);

			if (textual) {
				restrictionsProcessoTablesAsString = getTablesAsStringPesquisaTextual(restrictionsProcessoTablesAsString, (String) pesquisa.get("palavraChave"));
			}
			
			// Montando SQL para consulta normal...
			query = MessageFormat.format(
						PROCESSOS_SELECT_PATTERN, 
						newProjection.toString(),
						subqueryProjection.toString(),
						restrictionsProcessoProjection.toString(),
						restrictionsProcessoTablesAsString,
						restrictionsProcessoAsString,
						orderBy,
						Integer.toString(firstResult), 
						Integer.toString(firstResult + maxResults),
						tablesAsString,
						joinRestrictionsAsString);
			
			// Montando SQL para número total de registros...
			countQuery = MessageFormat.format(
						COUNT_PATTERN, 
						countProjection, 
						restrictionsProcessoTablesAsString,
						restrictionsProcessoAsString);
		} else {
			// Tendo as restrições, podemos adicionar os joins necessários.
			addJoins(projection, restrictions, pesquisa.getTipoPesquisa());
			
			// Tendo todas as restrições e todos os joins, verificamos quais tabelas serão necessárias.
			String tablesAsString = getTablesAsString(projection, restrictions);
			
			// Formatando restrições para montagem do SQL...
			String restrictionsAsString = getRestrictionsAsString(restrictions, parameters);
			
			if (textual) {
				tablesAsString = getTablesAsStringPesquisaTextual(tablesAsString, (String) pesquisa.get("palavraChave"));
			}
			
			// Montando SQL para consulta normal...
			query = MessageFormat.format(
				SELECT_PATTERN, 
				projection, 
				tablesAsString, 
				restrictionsAsString, 
				orderBy, 
				Integer.toString(firstResult), 
				Integer.toString(firstResult + maxResults)
			);
			
			countQuery = MessageFormat.format(
					COUNT_PATTERN, 
					countProjection, 
					tablesAsString, 
					restrictionsAsString
				);
		}
		
		// Executando a package de pesquisa de partes caso algum filtro de partes tenha sido preenchido.
		if(pesquisa.isNotBlank("idCategoriaParte") || pesquisa.isNotBlank("nomeParte")) {
			SimpleJdbcCall procedure = new SimpleJdbcCall(jdbcTemplate);
	        procedure.withProcedureName("PKG_INCIDENTE.PRC_JURISDICIONADO_INCIDENTE");
	        procedure.withCatalogName("JUDICIARIO");
	        procedure.withoutProcedureColumnMetaDataAccess();
	        procedure.declareParameters(
	            new SqlParameter("tipoObjetoIncidente", Types.VARCHAR),
	            new SqlParameter("seqObjetoIncidente", Types.NUMERIC),
	            new SqlParameter("seqJurisdicionado", Types.NUMERIC),
	            new SqlParameter("nomeJurisdicionado", Types.VARCHAR),
	            new SqlParameter("codCategoria", Types.NUMERIC),
	            new SqlParameter("limparTemporaria", Types.VARCHAR)
	        );
	        
	        // Montando mapa com os parâmetros de entrada...
	        Map<String,Object> in = new HashMap<String, Object>();
	        in.put("tipoObjetoIncidente", null);
	        in.put("seqObjetoIncidente", null);
	        in.put("seqJurisdicionado", null);
	        in.put("nomeJurisdicionado", pesquisa.get("nomeParte"));
	        in.put("codCategoria", pesquisa.get("idCategoriaParte") != null && (Long) pesquisa.get("idCategoriaParte") == -1 ? null : pesquisa.get("idCategoriaParte"));
	        in.put("limparTemporaria", null);
	        
	        // Disparando consulta...
	        procedure.execute(in);
		}
		
		
		if (showSql) {
			logger.info(new Formatter(query).format());
		}
		
		// Disparando consulta e montando lista de resultados...
		List<T> results = (List<T>) executeQuery(List.class, query, textual, mapper);
		// TODO
		// teste novo índice textual
//		List<T> results = (List<T>) executeQuery(List.class, query, mapper);
		
		int total = 0;
		Boolean morePages = null;
		
		if (!pesquisaRapidaPorTextos) {
			if (showSql) {
				logger.info(new Formatter(countQuery).format());
			}
			
			// Disparando consulta e recuperando resultado...
			total = executeQuery(Integer.class, countQuery, textual, new IntegerMapper());
			// TODO
			// teste novo índice textual
//			total = executeQuery(Integer.class, countQuery, new IntegerMapper());
		} else {
			total = results.size();
			morePages = Boolean.TRUE;
		}
		
        long end = System.currentTimeMillis();
        logger.info(String.format("[%s] milisegundos para pesquisa avancada.", (end - start)));
        
		// Montando e restornando lista paginada...
		return new PagedList<T>(results, firstResult, total, morePages);
	}


	/**
	 * Dispara a consulta avançada de acordo com os parâmetros de entrada. Se os parâmetros
	 * incluirem critério para busca textual, executa a consulta via procedure de pesquisa
	 * textual. Caso contrário, executa a consulta diretamente.
	 * 
	 * @param resultClass o tipo de resultado da consulta
	 * @param query a consulta sql
	 * @param mapper o objeto para mapear o registro encontra no objeto desejado
	 * @return o resultado da consulta
	 */
	private <R> R executeQuery(Class<R> resultClass, String query, boolean textual, RowMapper mapper) {
//	private <R> R executeQuery(Class<R> resultClass, String query, RowMapper mapper) {
		List<?> result = null;
		// Se a pesquisa incluir critério para busca textual, devemos usar a procedure
		// passando a consulta montada para o resultado desejado.
		if (textual) {
			// Montando objeto de pesquisa...
			SimpleJdbcCall procedure = new SimpleJdbcCall(jdbcTemplate);
	        procedure.withProcedureName("pkg_pesquisa_textual.prc_pesquisa_textual");
	        procedure.withCatalogName("doc");
	        procedure.withoutProcedureColumnMetaDataAccess();
	        procedure.declareParameters(
	            new SqlParameter("query", Types.VARCHAR),
	            new SqlOutParameter("rs", OracleTypes.CURSOR, mapper)
	        );
	        
	        // Montando mapa com os parâmetros de entrada...
	        Map<String,Object> in = new HashMap<String, Object>();
	        in.put("query", query);
	        
	        // Disparando consulta...
	        result = (List<?>) procedure.execute(in).get("rs");
		} else {
			// Caso não haja parâmetro textual, disparar consulta diretamente
			result = jdbcTemplate.query(query, mapper);
			
		}

		// TODO
		// teste novo índice textual
//		result = jdbcTemplate.query(query, mapper);
			
		// Se for a consulta pelo total de registros, retornar um único resultado (count).
        if (resultClass.equals(Integer.class)) {
    		return resultClass.cast(result.get(0));
        }
        
        // Se for a consulta normal, retornar os registros.
		return resultClass.cast(result);
	}

	/**
	 * Retorna a lista de restrições concatenando com " and " entre elas.
	 * 
	 * <p>IMPORTANTE: Por restrições em relação à pesquisa textual, os parâmetros estão sendo
	 * inseridos na consulta manualmente. Considerar nova solução assim que possível.
	 * 
	 * <p>Retorna "1 = 1" se não houver restrições
	 * 
	 * @param restrictions o conjunto, sem repetição, de restrições
	 * @param parameters os parametros para montagem da consulta
	 * @return as restrições como uma string
	 */
	private String getRestrictionsAsString(Set<String> restrictions, Map<String, Object> parameters) {
		// Concatenando lista de restrição separando por "and"...
		String restrictionsAsString = (!restrictions.isEmpty())? StringUtils.join(restrictions, " and ") : "1 = 1";
		// Aplicando parâmetros nas restrições...
        for (String restriction : restrictions) {
        	Object value = parameters.get(restriction);
        	if(value != null) {
        		if (value instanceof String) {
	        		restrictionsAsString = restrictionsAsString.replaceFirst("\\?", "'" + value.toString() + "'");
	        	} else if (value instanceof List) {
	        		for (Object object : (List) value) {
	        			if (object instanceof String) {
	        				restrictionsAsString = restrictionsAsString.replaceFirst("\\?", "'" + object.toString() + "'");
	        			} else {
	        				restrictionsAsString = restrictionsAsString.replaceFirst("\\?", object.toString());
	        			}
	        		}
	        	} else {
	        		restrictionsAsString = restrictionsAsString.replaceFirst("\\?", value.toString());
	        	}
        	}
        }
        return restrictionsAsString;
	}
	
	/**
	 * Retorna todas as tabelas necessárias de acordo com o que aparecerá no resultado (projection)
	 * e o que será usado como critério de pesquisa (restrictions).
	 * @param projection
	 * @param restrictions
	 * @return
	 */
	private String getTablesAsString(String projection, Set<String> restrictions) {
		Set<String> fromTables = new LinkedHashSet<String>();
		
		// Adicionando as tabelas necessários, de acordo com as colunas usadas na projeção...
		if(projection != null)
			fromTables.addAll(getTablesForAlias(getAlias(projection)));
		
		// Adicionando as tabelas necessários, de acordo com as colunas usadas nas restrições...
		for (String restriction : restrictions) {
			fromTables.addAll(getTablesForAlias(getAlias(restriction)));
		}
		
		// Concatena todas as tabelas, separando por ", "...
		return StringUtils.join(fromTables, ", ");
	}
	
	
	
	// Até o momento, identificamos que os caracteres "-" e "," geram erro na pesquisa.
	public String[] getCaracteresDeConsultaInvalidos(){
		return caracteresDeConsultaInvalidos;
	} 

	/**
	 * Esse método é responsável por retirar, da consulta, os
	 * caracteres inválidos. Até o momento identificamos 2: "-" e ","
	 *
	 * @param consultaProblematica String com os parâmetros de consulta
	 * @return consultaLivre String com os parâmetros de consulta corretos.
	 * */
	public String adequarCaracteresDeConsultaInvalidos(String consultaProblematica){
		String consultaLivre = new String(consultaProblematica);
		String[] caracteresProblemas = getCaracteresDeConsultaInvalidos();
		for(String caracter: caracteresProblemas){
			consultaLivre = consultaLivre.replace(caracter, "\\" + caracter);
		}
		return consultaLivre;
	}
	
	
	private String getTablesAsStringPesquisaTextual(String tableAsString, String palavraChave) {
		palavraChave = (String) jdbcTemplate.queryForObject("SELECT BRS.FNC_CONVERTE_PESQUISA(?) as valor from dual", new Object[] {palavraChave}, String.class);
		// Até o momento, identificamos que os caracteres "-" e "," geram erro na pesquisa.
		palavraChave = adequarCaracteresDeConsultaInvalidos(palavraChave);
		return tableAsString.replaceFirst("\\?", "'" + palavraChave + "'");
	}

	/**
	 * Retorna as tabelas, dados os alias correspondentes.
	 * 
	 * @param aliasList lista de alias
	 * 
	 * @return a tabelas correspondentes
	 */
	private Set<String> getTablesForAlias(List<String> aliasList) {
		Set<String> restrictionTables = new LinkedHashSet<String>();
		for (String alias : aliasList) {
			if(allTables.get(alias) != null)
				restrictionTables.add(String.format("%s %s", allTables.get(alias), alias));
		}
		return restrictionTables;
	}

	/**
	 * Retorna a lista de alias de uma dada restrição.
	 * 
	 * <p>Exemplo:
	 * <br/>Input: "texto.SEQ_OBJETO_INCIDENTE = oi.SEQ_OBJETO_INCIDENTE(+)"; Output: [texto,oi]
	 * <br/>Input: "texto.SEQ_OBJETO_INCIDENTE > ?"; Output: [texto]
	 * <br/>Input: "texto.SEQ_OBJETO_INCIDENTE > to_date(?, 'dd/MM/yyyy')"; Output: [texto]
	 * <br/>Input: "lower(assunto.DSC_ASSUNTO) like lower(?)"; Output: [assunto]
	 * 
	 * @param restriction a restrição
	 * @return a lista de alias
	 */
	private List<String> getAlias(String restriction) {
        Pattern p = Pattern.compile("(\\w*)\\.\\w*");
        Matcher m = p.matcher(restriction);
        List<String> tokens = new ArrayList<String>();
        while (m.find()) {
            tokens.add(m.group(1));
        }
        return tokens;
	}
	
	/**
	 * Verifica se um dado alias será usado na consulta, seja como resultado, seja como restrição.
	 * 
	 * @param projection
	 * @param restrictions o conjunto, sem repetição, de restrições
	 * @param alias o alias procurado
	 * @return true, se está, false, caso contrário
	 */
	public boolean hasAlias(String projection, Set<String> restrictions, String alias) {
		if (getAlias(projection).contains(alias)) {
			return true;
		}
		if (hasRestrictionForAlias(restrictions, alias)) {
			return true;
		}
		return false;
	}

	/**
	 * Verifica se um dado alias será usado em alguma restrição.
	 * 
	 * @param restrictions o conjunto, sem repetição, de restrições
	 * @param alias o alias procurado
	 * @return true, se está, false, caso contrário
	 */
	public boolean hasRestrictionForAlias(Set<String> restrictions, String alias) {
		if(restrictions != null) {
			for (String restriction : restrictions) {
				List<String> aliasList = getAlias(restriction);
				if (aliasList.contains(alias)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Adiciona restrições para acesso a textos. São 4 as situações:
	 * 
	 * <p>1ª: Se o usuário informou o ministro do texto e esse ministro é diferente do ministro do usuário,
	 * devemos consultar textos do ministro informado, que sejam públicos;
	 * <br/>2ª: Se o usuário informou o ministro do texto e esse ministro é o ministro do usuário, 
	 * devemos consultar somente os textos do ministro;
	 * <br/>3ª: Se o usuário não informou o ministro do texto e não busca por textos, devemos consultar 
	 * os textos do ministro do usuário e todos os textos públicos (apenas se existir outra 
	 * restrição para texto).
	 * <br/>4ª: Se o usuário não informou o ministro do texto e busca por textos, devemos consultar 
	 * os textos do ministro do usuário e todos os textos públicos.
	 * 
	 * @param pesquisa a pesquisa contendo todos o critérios de busca
	 * @param parameters os valores dos parâmetros para pesquisa
	 * @param restrictions as restrições sql
	 * @param idMinistroDoUsuarioLogado
	 */
	private void addRestrictionParaTextos(Pesquisa pesquisa, Set<String> restrictions, Map<String, Object> parameters, Long idMinistroDoUsuarioLogado) {
		if (idMinistroDoUsuarioLogado != null) {
			if (pesquisa.isNotBlank("idMinistroTexto")) {
				Long idMinistroDoTexto = (Long) pesquisa.get("idMinistroTexto");
				if (!idMinistroDoTexto.equals(idMinistroDoUsuarioLogado)) {
					// 1ª Situação: Veja javadoc acima.
					addRestriction(restrictions, parameters, "(texto.COD_MINISTRO = ? and texto.FLG_PUBLICO = 'S')", idMinistroDoTexto);
				} else {
					// 2ª Situação: Veja javadoc acima.
					addRestriction(restrictions, parameters, "texto.COD_MINISTRO = ?", idMinistroDoTexto);
				}
			} else {
				if (!TipoPesquisa.TEXTOS.equals(pesquisa.getTipoPesquisa()) && !pesquisaRapidaPorTextos) {
					if (hasRestrictionForAlias(restrictions, "texto")) {
						// 3ª Situação: Veja javadoc acima.
						addRestriction(restrictions, parameters, "(texto.COD_MINISTRO = ? or texto.FLG_PUBLICO = 'S')", idMinistroDoUsuarioLogado);
					}
				} else {
					// 4ª Situação: Veja javadoc acima.
					addRestriction(restrictions, parameters, "(texto.COD_MINISTRO = ? or texto.FLG_PUBLICO = 'S')", idMinistroDoUsuarioLogado);
				}
			}
		} else {
			// Quando o usuário não estiver em um gabinete, ele poderá ver apenas os textos públicos
			if (pesquisa.isNotBlank("idMinistroTexto")) {
				restrictions.add("texto.FLG_PUBLICO = 'S'");
				Long idMinistroDoTexto = (Long) pesquisa.get("idMinistroTexto");
				addRestriction(restrictions, parameters, "texto.COD_MINISTRO = ?", idMinistroDoTexto);
			} else {
				if (!TipoPesquisa.TEXTOS.equals(pesquisa.getTipoPesquisa()) && !pesquisaRapidaPorTextos) {
					if (hasRestrictionForAlias(restrictions, "texto")) {
						restrictions.add("texto.FLG_PUBLICO = 'S'");		
					}
				} else {
					restrictions.add("texto.FLG_PUBLICO = 'S'");
				}
			}
		}
		
//		restrictions.add("texto.FLG_ORIGEM_DIGITAL <> 'S'");
		
		if(hasRestrictionForAlias(restrictions, "texto")) {
			addRestriction(restrictions, parameters, "texto.COD_TIPO_TEXTO <> ?", TipoTexto.DECISAO.getCodigo());
		}
	}
	
	private void addRestrictionParaListas(Pesquisa pesquisa,
			Set<String> restrictions, Map<String, Object> parameters) {
		if (pesquisa.isNotBlank("idSetorMinistroLogado")) {
			if (pesquisa.getTipoPesquisa().equals(TipoPesquisa.LISTAS_TEXTOS)) {
				addRestriction(restrictions, parameters, "lista_textos.COD_SETOR = ?", (Long) pesquisa.get("idSetorMinistroLogado"));
			} else if (pesquisa.getTipoPesquisa().equals(TipoPesquisa.LISTAS_PROCESSOS)) {
				addRestriction(restrictions, parameters, "lista_incidentes.COD_SETOR = ?", (Long) pesquisa.get("idSetorMinistroLogado"));
			}
		} else {
			if (pesquisa.getTipoPesquisa().equals(TipoPesquisa.LISTAS_TEXTOS)) {
				restrictions.add("lista_textos.COD_SETOR is null");
			} else if (pesquisa.getTipoPesquisa().equals(TipoPesquisa.LISTAS_PROCESSOS)) {
				restrictions.add("lista_incidentes.COD_SETOR is null");
			}
		}
	}

	/**
	 * Adiciona restrições para todos os tipos de critérios de pesquisa.
	 * 
	 * @param pesquisa a pesquisa contendo todos o critérios de busca
	 * @param parameters os valores dos parâmetros para pesquisa
	 * @param restrictions as restrições sql
	 */
	private void addRestrictions(Pesquisa pesquisa, Set<String> restrictions, Map<String, Object> parameters) {
		// Critérios para lista de incidentes...
		if (pesquisa.isNotBlank("idListaIncidentes")) {
			addRestriction(restrictions, parameters, "lista_incidentes.SEQ_GRUPO_PROCESSO_SETOR = ?", pesquisa.get("idListaIncidentes"));
		}
		else if (pesquisa.isNotBlank("nomeListaIncidentes")) {
			addRestriction(restrictions, parameters, "lower(lista_incidentes.NOM_GRUPO_PROCESSO_SETOR) like lower(?)", "%" + pesquisa.get("nomeListaIncidentes") + "%");
		}
		
		// Critérios para lista de textos...
		if (pesquisa.isNotBlank("idListaTextos")) {
			addRestriction(restrictions, parameters, "lista_textos.SEQ_LISTA_TEXTO = ?", pesquisa.get("idListaTextos"));
		}
		else if (pesquisa.isNotBlank("nomeListaTextos")) {
			addRestriction(restrictions, parameters, "lower(lista_textos.DSC_LISTA_TEXTO) like lower(?)", "%" + pesquisa.get("nomeListaTextos") + "%");
		}
		
		// Critérios para assuntos...
		if (pesquisa.isNotBlank("idAssunto")) {
			addRestriction(restrictions, parameters, "assunto.COD_ASSUNTO = ?", pesquisa.get("idAssunto"));
		}
		else if (pesquisa.isNotBlank("descricaoAssunto")) {
			addRestriction(restrictions, parameters, "lower(assunto.DSC_ASSUNTO_COMPLETO) like lower(?)", "%" + pesquisa.get("descricaoAssunto") + "%");
		}
		
		// Critérios para objeto incidente...
		if (pesquisa.isNotBlank("idObjetoIncidente")) {
			addRestriction(restrictions, parameters, "oi.SEQ_OBJETO_INCIDENTE = ?", pesquisa.get("idObjetoIncidente"));
		}
		
		if (pesquisa.isNotBlank("idTipoIncidente")) {
			restrictions.add("((recurso.SEQ_TIPO_RECURSO = " + pesquisa.get("idTipoIncidente")
							+ " AND incidente_julgamento.SEQ_TIPO_RECURSO is null) OR " 
							+ "(incidente_julgamento.SEQ_TIPO_RECURSO = " + pesquisa.get("idTipoIncidente") 
							+ " AND recurso.SEQ_TIPO_RECURSO is null))");
		}
		
		// Critérios para processo...
		if (pesquisa.isNotBlank("idRelatorAtual")) {
			addRestriction(restrictions, parameters, "processo.COD_RELATOR_ATUAL = ?", pesquisa.get("idRelatorAtual"));
		}
		
		if (pesquisa.isNotBlank("siglaProcesso")) {
			String siglaProcesso = converterSigla(pesquisa.get("siglaProcesso").toString());
			addRestriction(restrictions, parameters, "processo.SIG_CLASSE_PROCES = ?", siglaProcesso);
		}
		
		if (pesquisa.isNotBlank("numeroProcesso")) {
			addRestriction(restrictions, parameters, "processo.NUM_PROCESSO = ?", pesquisa.get("numeroProcesso"));
		}
		
		if (pesquisa.isNotBlank("originario")) {
			if (pesquisa.get("originario").equals("S")) {
				restrictions.add("processo.SIG_CLASSE_PROCES NOT IN ('AI', 'RE', 'ARE')");
			} else if (pesquisa.get("originario").equals("N")) {
				restrictions.add("processo.SIG_CLASSE_PROCES IN ('AI', 'RE', 'ARE')");
			}
		}
		
		if (pesquisa.isNotBlank("repercussaoGeral")) {
			addRestriction(restrictions, parameters, "processo.FLG_REPERCUSSAO_GERAL = ?", pesquisa.get("repercussaoGeral"));
		}
		
		if (pesquisa.isNotBlank("controversiaOrigem")) {
			addRestriction(restrictions, parameters, "processo.FLG_REPR_CONTROVERSIA = ?", pesquisa.get("controversiaOrigem"));
		}
		
		if (pesquisa.isNotBlank("tipoProcesso")) {
			addRestriction(restrictions, parameters, "processo.TIP_MEIO_PROCESSO = ?", pesquisa.get("tipoProcesso"));
		}
		
		// Critérios para julgamento...
		
		if (pesquisa.isNotBlank("agendamento")) {
			StringBuffer subSelect = new StringBuffer();
			List<Object> parametrosSubselect = new ArrayList<Object>();
			subSelect.append(" EXISTS (");
			subSelect.append(" SELECT agendamento.SEQ_OBJETO_INCIDENTE, agendamento.COD_CAPITULO, ");
			subSelect.append(" agendamento.COD_MATERIA from STF.AGENDAMENTOS agendamento ");
			subSelect.append(" WHERE agendamento.SEQ_OBJETO_INCIDENTE = oi.SEQ_OBJETO_INCIDENTE AND ");
			subSelect.append(" agendamento.COD_MATERIA = ? ");
			parametrosSubselect.add(pesquisa.get("agendamento"));
			if (pesquisa.isNotBlank("colegiado")) {
				subSelect.append(" AND agendamento.COD_CAPITULO = ? ");
				parametrosSubselect.add(pesquisa.get("colegiado"));
			}
			subSelect.append(" )");
			addRestriction(restrictions, parameters, subSelect.toString(), parametrosSubselect);
		}
		
		if (pesquisa.isNotBlank("idListaJulgamento")) {
			StringBuffer subSelect = new StringBuffer();
			List<Object> parametrosSubSelect = new ArrayList<Object>();
			subSelect.append(" EXISTS ( ");
			subSelect.append(" SELECT 1 FROM JULGAMENTO.PROCESSO_LISTA_JULG processoListaJulgamento ");
			subSelect.append(" WHERE oi.seq_objeto_incidente = processoListaJulgamento.seq_objeto_incidente_candidato ");
			subSelect.append(" AND processoListaJulgamento.seq_lista_julgamento = ? ) ");
			parametrosSubSelect.add(Long.parseLong(String.valueOf(pesquisa.get("idListaJulgamento"))));
			addRestriction(restrictions, parameters, subSelect.toString(), parametrosSubSelect);
		} else if (pesquisa.isNotBlank("inicioDataSessaoJulgamento") || pesquisa.isNotBlank("fimDataSessaoJulgamento")) {
			StringBuffer subSelect = new StringBuffer();
			List<Object> parametrosSubSelect = new ArrayList<Object>();
			subSelect.append(" EXISTS (");
			subSelect.append(" SELECT sessao.SEQ_SESSAO FROM JULGAMENTO.SESSAO sessao, JULGAMENTO.JULGAMENTO_PROCESSO julgamentoProcesso ");
			subSelect.append(" WHERE sessao.SEQ_SESSAO = julgamentoProcesso.SEQ_SESSAO ");
			subSelect.append(" AND julgamentoProcesso.SEQ_OBJETO_INCIDENTE = oi.SEQ_OBJETO_INCIDENTE ");
			if (pesquisa.isNotBlank("colegiado")) {
				TipoColegiadoConstante colegiado = TipoColegiadoConstante.valueOfCodigoCapitulo(((Long) pesquisa.get("colegiado")).intValue());
				subSelect.append(" AND sessao.COD_COLEGIADO = ? ");
				parametrosSubSelect.add(colegiado.getSigla());
			}
			if (pesquisa.isNotBlank("inicioDataSessaoJulgamento")) {
				String inicio = new SimpleDateFormat("dd/MM/yyyy").format((Date) pesquisa.get("inicioDataSessaoJulgamento"));
				subSelect.append(" AND trunc(sessao.DAT_INICIO) >= to_date(? , 'dd/MM/yyyy')");
				parametrosSubSelect.add(inicio);
			}
			if (pesquisa.isNotBlank("fimDataSessaoJulgamento")) {
				String fim = new SimpleDateFormat("dd/MM/yyyy").format((Date) pesquisa.get("fimDataSessaoJulgamento"));
				subSelect.append(" AND trunc(sessao.DAT_INICIO) <= to_date(? , 'dd/MM/yyyy')");
				parametrosSubSelect.add(fim);
			}
			subSelect.append(" ) ");
			addRestriction(restrictions, parameters, subSelect.toString(), parametrosSubSelect);			
		}
		
		if (pesquisa.isNotBlank("pautaExtra")) {
			if ("S".equals(pesquisa.get("pautaExtra").toString())) {
				addRestriction(restrictions, parameters, "informacaoPautaProcesso.FLG_PAUTA_EXTRA = ?", "S");
			} else {
				addRestriction(restrictions, parameters, "(informacaoPautaProcesso.FLG_PAUTA_EXTRA = ? OR informacaoPautaProcesso.FLG_PAUTA_EXTRA IS NULL)", "N");
			}
		}
		
		if (pesquisa.isNotBlank("controleVoto")) {
			if ("S".equals(pesquisa.get("controleVoto").toString())) {
				addRestriction(restrictions, parameters, "texto.SEQ_VOTOS IS NOT NULL AND texto.SEQ_VOTOS <> ?", 0L);
			} else {
				addRestriction(restrictions, parameters, "(texto.SEQ_VOTOS IS NULL OR texto.SEQ_VOTOS = ?)", 0L);
			}
		}
		
		// Critérios para textos...
		if (pesquisa.isNotBlank("idTipoTexto")) {
			addRestriction(restrictions, parameters, "texto.COD_TIPO_TEXTO = ?", pesquisa.get("idTipoTexto"));
		}
		
		if (pesquisa.isNotBlank("tiposTexto")) {
			StringBuffer restriction = new StringBuffer("texto.COD_TIPO_TEXTO IN (");
			boolean first = true;
			for (Long idTipoTexto : (List<Long>) pesquisa.get("tiposTexto")) {
				if (!first) {
					restriction.append(",");
				}
				restriction.append("?");
				first = false;
			}
			restriction.append(")");
			addRestriction(restrictions, parameters, restriction.toString(), pesquisa.get("tiposTexto"));
		}
		
		if (pesquisa.isNotBlank("inicioDataInclusao")) {
			String inicio = new SimpleDateFormat("dd/MM/yyyy").format((Date) pesquisa.get("inicioDataInclusao"));
			addRestriction(restrictions, parameters, "trunc(texto.DAT_INCLUSAO) >= to_date(? , 'dd/MM/yyyy')", inicio);
		}
		
		if (pesquisa.isNotBlank("fimDataInclusao")) {
			String fim = new SimpleDateFormat("dd/MM/yyyy").format((Date) pesquisa.get("fimDataInclusao"));
			addRestriction(restrictions, parameters, "trunc(texto.DAT_INCLUSAO) <= to_date(? , 'dd/MM/yyyy')", fim);
		}
		
		if (pesquisa.isNotBlank("inicioDataSessao")) {
			String inicio = new SimpleDateFormat("dd/MM/yyyy").format((Date) pesquisa.get("inicioDataSessao"));
			addRestriction(restrictions, parameters, "texto.DAT_SESSAO >= to_date(? , 'dd/MM/yyyy')", inicio);
		}
		
		if (pesquisa.isNotBlank("fimDataSessao")) {
			String fim = new SimpleDateFormat("dd/MM/yyyy").format((Date) pesquisa.get("fimDataSessao"));
			addRestriction(restrictions, parameters, "texto.DAT_SESSAO <= to_date(? , 'dd/MM/yyyy')", fim);
		}
		
		if (pesquisa.isNotBlank("textosIguais")) {
			addRestriction(restrictions, parameters, "texto.FLG_TEXTOS_IGUAIS = ?", pesquisa.get("textosIguais"));
		}
		
		if (pesquisa.isNotBlank("idFaseTexto")) {
			if (pesquisa.isNotBlank("inicioDataFase") || pesquisa.isNotBlank("fimDataFase")) {
				Boolean ultimaFase = (Boolean) pesquisa.get("ultimaFase");
				addRestriction(restrictions, parameters, "faseTextoProcesso.COD_TIPO_FASE_TEXTO_DESTINO = ?", pesquisa.get("idFaseTexto"));
				if (pesquisa.isNotBlank("inicioDataFase")){
					String inicio = new SimpleDateFormat("dd/MM/yyyy").format((Date) pesquisa.get("inicioDataFase"));
					addRestriction(restrictions, parameters, "trunc(faseTextoProcesso.DAT_FASE) >= to_date(?, 'dd/MM/yyyy')", inicio);
				}
				if (pesquisa.isNotBlank("fimDataFase")){
					String fim = new SimpleDateFormat("dd/MM/yyyy").format((Date) pesquisa.get("fimDataFase"));
					addRestriction(restrictions, parameters, "trunc(faseTextoProcesso.DAT_FASE) <= to_date(?, 'dd/MM/yyyy')", fim);
				}
				if (ultimaFase.booleanValue()) {
					addRestriction(restrictions, parameters, "texto.COD_TIPO_FASE_TEXTO = ?", pesquisa.get("idFaseTexto"));
				}
			} else {
				addRestriction(restrictions, parameters, "texto.COD_TIPO_FASE_TEXTO = ?", pesquisa.get("idFaseTexto"));
			}
		}
		
		if (pesquisa.isNotBlank("idResponsavel")) {
			if (pesquisa.get("idResponsavel") instanceof Long) {
				addRestriction(restrictions, parameters, "texto.SEQ_GRUPO_RESPONSAVEL = ?", pesquisa.get("idResponsavel"));
			} else {
				addRestriction(restrictions, parameters, "texto.USU_AUTOR_INTELECTUAL = ?", pesquisa.get("idResponsavel"));
			}
		}

		if (pesquisa.isNotBlank("idCriador")) {
			addRestriction(restrictions, parameters, "texto.USU_INCLUSAO = ?", pesquisa.get("idCriador"));
		}
		
		if (pesquisa.isNotBlank(Pesquisa.CHAVE_FAVORITOS)) {
			String[] opcoesSelecionadas = (String[]) pesquisa.get(Pesquisa.CHAVE_FAVORITOS);
			if (ArrayUtils.contains(opcoesSelecionadas, Pesquisa.FAVORITOS_APENAS)){
				addRestriction(restrictions, parameters, "texto.FLG_FAVORITO_GABINETE = ?", "S");
			}
		}
		
		if (pesquisa.isNotBlank("palavraChave")) {
			if (!pesquisaRapidaPorTextos) {
				addJoin(restrictions, "arquivo_eletronico.SEQ_ARQUIVO_ELETRONICO = texto.SEQ_ARQUIVO_ELETRONICO");
			}
		}
		
		if (pesquisa.isNotBlank("observacao")) {
			String observacao = (String) jdbcTemplate.queryForObject("SELECT BRS.FNC_CONVERTE_PESQUISA(?) as valor from dual", new Object[] {pesquisa.get("observacao")}, String.class);
			observacao = adequarCaracteresDeConsultaInvalidos(observacao);
			addRestriction(restrictions, parameters, "contains (texto.OBS_TEXTO, ?) > 0", observacao);
		}
		
		// Critério para parte...
		// Somente faz o join com a view de partes otimizada, preenchida pela package
		if (pesquisa.isNotBlank("idCategoriaParte") || pesquisa.isNotBlank("nomeParte")) {
			addJoin(restrictions, "parte.SEQ_OBJETO_INCIDENTE = oi.SEQ_OBJETO_INCIDENTE");
		}
		
		if (pesquisa.isNotBlank("idsTextos")) {
			addRestriction(restrictions, parameters, "texto.SEQ_TEXTOS IN (?)", pesquisa.get("idsTextos"));
		}
		
		if (pesquisa.isNotBlank("situacaoJulgamento")) {
			montarSubQuerySituacaoJulgamento(restrictions, parameters, pesquisa);
		}
		
		if(pesquisa.isNotBlank("observacaoProcesso")){
			addRestriction(restrictions, parameters, " (observacaoProcesso.seq_objeto_incidente = oi.seq_objeto_incidente "+
					" AND CONTAINS(observacaoProcesso.txt_observacao, ?) > 0 )", 
					pesquisa.get("observacaoProcesso"));
			
			
		}
	}
	
	private void montarSubQuerySituacaoJulgamento(Set<String> restrictions, Map<String, Object> parameters,
			Pesquisa pesquisa) {
		StringBuffer subSelect = new StringBuffer();
		List<Object> parametrosSubSelect = new ArrayList<Object>();
		
		String subQueryJulgado = SubQuerySituacaoJulgamento.buildClausulaSituacaoJulgado("oi");
		
		if (pesquisa.get("situacaoJulgamento").equals(SituacaoIncidenteJulgadoOuNao.JULGADO.getSigla())) {
			subSelect.append(subQueryJulgado);
		} else if (pesquisa.get("situacaoJulgamento").equals(SituacaoIncidenteJulgadoOuNao.NAO_JULGADO.getSigla())) {
			subSelect.append(" NOT ");
			subSelect.append(subQueryJulgado);
		} else {
			throw new RuntimeException("Situação de Julgamento inválida.");
		}

		addRestriction(restrictions, parameters, subSelect.toString(), parametrosSubSelect);
	}

	/**
	 * Converte a sigla informada na sigla correta, corrigindo
	 * letras maiúsculas e minúsculas.
	 * @param siglaInformada
	 * @return
	 */
	private String converterSigla(String siglaInformada) {
		List<?> result = null;
		String siglaConvertida = null;
		StringBuffer query = new StringBuffer("select classe.SIG_CLASSE ");
		query.append("from JUDICIARIO.CLASSE classe ");
		query.append("where upper(classe.SIG_CLASSE) = ?");
		if (showSql) {
			logger.info(new Formatter(query.toString()).format());
		}
		result = jdbcTemplate.query(query.toString(), new String[] {siglaInformada.toUpperCase()}, new StringMapper());
		if(result != null && result.size() > 0)
			siglaConvertida = (String) result.get(0);
		
		if(siglaConvertida == null) {
			query = new StringBuffer("select SIG_CLASSE_NOVA ");
			query.append("from CLASSE_CONVERSAO ");
			query.append("where upper(SIG_CLASSE_VELHA) = ?");
			if (showSql) {
				logger.info(new Formatter(query.toString()).format());
			}
			result = jdbcTemplate.query(query.toString(), new String[] {siglaInformada.toUpperCase()}, new StringMapper());
			siglaConvertida = (String) result.get(0);
		}
		if(result != null && result.size() > 0)
			siglaConvertida = (String) result.get(0);
		
		return siglaConvertida;
	}

	/**
	 * Adiciona os Joins necessários. Os Joins serão incluidos somente se existirem restrições para a tabela.
	 * Por exemplo: somente será feito um join com "lista_incidentes" se existirem restrições para
	 * para essa tabela. A verificação é feita checando se o alista para a tabela aparece na lista
	 * de restrições.
	 * 
	 * @param projection
     * @param restrictions o conjunto, sem repetição, de restrições
     * @param tipoPesquisa
	 */
	private void addJoins(String projection, Set<String> restrictions, TipoPesquisa tipoPesquisa) {
		if(TipoPesquisa.TEXTOS.equals(tipoPesquisa) && !pesquisaRapidaPorTextos) {
			restrictions.add("filtro.SEQ_TEXTOS = texto.SEQ_TEXTOS");
		} else if(TipoPesquisa.PROCESSOS.equals(tipoPesquisa)) {
			restrictions.add("pr.idRelator = ministro.COD_MINISTRO (+)");
		}
				
		if(joinsParaObjetoIncidente != null) {
			addJoins(projection, restrictions, true);
		} else {
			addJoins(projection, restrictions, false);
		}		
	}
	
	private void addJoins(String projection, Set<String> restrictions, boolean adicionarJoinsObjetoIncidente) {
		// IMPORTANTE: A ordem na qual a verificação é feita é importante já que algumas condições dependem
		// de outras... Por exemplo: se assunto depende de processo, primeiro devemos verificar se
		// existe a necessidade de incluir assunto para depois verificar se é necessário incluir
		// processo.
		if (hasAlias(projection, restrictions, "controle_votos")) {
			addJoin(restrictions, "texto.SEQ_OBJETO_INCIDENTE = controle_votos.SEQ_OBJETO_INCIDENTE (+)");
			addJoin(restrictions, "texto.COD_TIPO_TEXTO = controle_votos.COD_TIPO_TEXTO (+)");
			addJoin(restrictions, "texto.SEQ_VOTOS = controle_votos.SEQ_VOTO (+)");
			addJoin(restrictions, "texto.DAT_SESSAO = controle_votos.DAT_SESSAO (+)");
			addJoin(restrictions, "texto.COD_MINISTRO = controle_votos.COD_MINISTRO (+)");
		}
		
		if (hasAlias(projection, restrictions, "arquivo_eletronico")) {
			addJoin(restrictions, "texto.SEQ_ARQUIVO_ELETRONICO = arquivo_eletronico.SEQ_ARQUIVO_ELETRONICO");
		}
		
		if (hasAlias(projection, restrictions, "faseTextoProcesso")) {
			addJoin(restrictions, "faseTextoProcesso.SEQ_TEXTOS = texto.SEQ_TEXTOS");
		}
		
		if (hasAlias(projection, restrictions, "tipo_texto")) {
			addJoin(restrictions, "tipo_texto.COD_TIPO_TEXTO = texto.COD_TIPO_TEXTO");
		}
		
		if (hasAlias(projection, restrictions, "assunto")) {
			addJoin(restrictions, "processo.SEQ_OBJETO_INCIDENTE = oi.SEQ_OBJETO_INCIDENTE_PRINCIPAL");
			addJoin(restrictions, "assunto_processo.SEQ_OBJETO_INCIDENTE = processo.SEQ_OBJETO_INCIDENTE");
			addJoin(restrictions, "assunto.COD_ASSUNTO = assunto_processo.COD_ASSUNTO");
		}
		
		if (hasAlias(projection, restrictions, "processo")) {
			addJoin(restrictions, "processo.SEQ_OBJETO_INCIDENTE = oi.SEQ_OBJETO_INCIDENTE_PRINCIPAL");
		}
		
		if (hasAlias(projection, restrictions, "lista_incidentes")) {
			if (hasAlias(projection, restrictions, "oi") || hasAlias(projection, restrictions, "texto")) {
				addJoin(restrictions, "incidente_listas.SEQ_OBJETO_INCIDENTE = oi.SEQ_OBJETO_INCIDENTE");
				addJoin(restrictions, "lista_incidentes.SEQ_GRUPO_PROCESSO_SETOR = incidente_listas.SEQ_GRUPO_PROCESSO_SETOR");
			}
		}
		
		if (hasAlias(projection, restrictions, "lista_textos")) {
			if(hasAlias(projection, restrictions, "texto") || hasAlias(projection, restrictions, "oi")) {
				addJoin(restrictions, "texto_listas.SEQ_TEXTOS = texto.SEQ_TEXTOS");
				addJoin(restrictions, "lista_textos.SEQ_LISTA_TEXTO = texto_listas.SEQ_LISTA_TEXTO");
			}
		}
				
		if (hasAlias(projection, restrictions, "texto") && !pesquisaRapidaPorTextos) {
			addJoin(restrictions, "texto.SEQ_OBJETO_INCIDENTE = oi.SEQ_OBJETO_INCIDENTE");
		} else if (pesquisaRapidaPorTextos) {
			addJoin(restrictions, "texto.NUM_PROCESSO = processo2.NUM_PROCESSO and texto.SIG_CLASSE_PROCES = processo2.SIG_CLASSE_PROCES");
		}
		
		if (hasAlias(projection, restrictions, "recurso")) {
			addJoin(restrictions, "recurso.SEQ_OBJETO_INCIDENTE (+) = oi.SEQ_OBJETO_INCIDENTE");
		}
		
		if (hasAlias(projection, restrictions, "incidente_julgamento")) {
			addJoin(restrictions, "incidente_julgamento.SEQ_OBJETO_INCIDENTE (+) = oi.SEQ_OBJETO_INCIDENTE");
		}
		
		if (hasAlias(projection, restrictions, "informacaoPautaProcesso")) {
			addJoin(restrictions, "informacaoPautaProcesso.SEQ_OBJETO_INCIDENTE (+) = oi.SEQ_OBJETO_INCIDENTE");
		}
		
		if (hasAlias(projection, restrictions, "processoListaJulgamento"))
			addJoin(restrictions, "processoListaJulgamento.SEQ_OBJETO_INCIDENTE_CANDIDATO (+) = oi.SEQ_OBJETO_INCIDENTE");
			
		if (hasAlias(projection, restrictions, "oi")) {
			restrictions.add("oi.TIP_OBJETO_INCIDENTE in ('PR', 'IJ', 'RC')");
			
			if (adicionarJoinsObjetoIncidente) {
				for (String restriction : joinsParaObjetoIncidente) {
					addJoin(restrictions, restriction);
				}
			}
		}
	}

	/**
	 * Adiciona uma restrição para a consulta. É sempre a comparação de uma coluna com um 
	 * dado valor.
	 * 
	 * @param restrictions o conjunto, sem repetição, de restrições
	 * @param parameters os critérios de pesquisa (valores)
	 * @param restriction a restrição contendo a coluna para comparação
	 * @param object o objeto representando o critério de busca
	 */
	private void addRestriction(Set<String> restrictions, Map<String, Object> parameters, String restriction, Object object) {
		restrictions.add(restriction);
		parameters.put(restriction, object);
	}
	
	/**
	 * Adiciona um join entre duas tabelas.
	 * 
	 * @param restrictions o conjunto, sem repetição, de restrições
	 * @param join a restrição representando o join
	 */
	private void addJoin(Set<String> restrictions, String join) {
		// Antes de incluir o join, verifica se ele já existe com inner join. Se sim, remove o inner join
		// e adiciona o right/left join.
		if (restrictions.contains(join.replaceAll("\\(\\+\\)", "").trim())) {
			restrictions.remove(join.replaceAll("\\(\\+\\)", "").trim());
		}
		restrictions.add(join);
	}
	
	public void addProjection(String field) {
		projection = projection + ", " + field;
	}

	public void setJoinsParaObjetoIncidente(String... joinsParaObjetoIncidente) {
		this.joinsParaObjetoIncidente = Arrays.asList(joinsParaObjetoIncidente);
	}
	
	public void setPesquisaRapidaPorTextos(boolean pesquisaRapidaPorTextos) {
		this.pesquisaRapidaPorTextos = pesquisaRapidaPorTextos;
	}

	public void setCountProjection(String countProjection) {
		this.countProjection = countProjection;
	}
	
	public void setProjection(String projection) {
		this.projection = projection;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public void setPreOrderBy(OrderByClause preOrderBy) {
		this.preOrderBy = preOrderBy;
	}
	
}
