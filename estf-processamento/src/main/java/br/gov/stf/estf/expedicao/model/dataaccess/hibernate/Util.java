package br.gov.stf.estf.expedicao.model.dataaccess.hibernate;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;

import org.hibernate.Query;

/**
 *
 * @author Roberio.Fernandes
 */
public class Util {

    public static final String ESPACO = " ";
    public static final String SELECT = "SELECT" + ESPACO;
    public static final String FROM = ESPACO + "FROM" + ESPACO;
    public static final String WHERE = ESPACO + "WHERE" + ESPACO;
    public static final String AND = ESPACO + "AND" + ESPACO;
    public static final String OR = ESPACO + "OR" + ESPACO;
    public static final String IGUAL = " = ";
    public static final String MAIOR_QUE = " > ";
    public static final String MAIOR_OU_IGUAL = " >= ";
    public static final String MENOR_QUE = " < ";
    public static final String MENOR_OU_IGUAL = " <= ";
    public static final String LIKE = " LIKE ";
    public static final String ABRE_PARENTESE = "(";
    public static final String FECHA_PARENTESE = ")";
    public static final String UPPER = " UPPER ";
    public static final String BETWEEN = ESPACO + "BETWEEN" + ESPACO;
    public static final String PERCENT = "%";
    public static final String DOIS_PONTOS = ":";
    public static final String PONTO = ".";
    public static final String VIRGULA = ",";
    public static final String IS = ESPACO + "IS" + ESPACO;
    public static final String NOT = ESPACO + "NOT" + ESPACO;
    public static final String NULL = ESPACO + "null" + ESPACO;
    public static final boolean CASE_SENSITIVE_PADRAO = false;
    public static final boolean REMOVER_CARACTERES_ESPECIAIS_PADRAO = true;
    public static final String ORACLE_TRANSLATE = "TRANSLATE";
    public static final String ASPA = "'";
    public static final String TRANSLATE_ORIGEM  = ASPA + "ÁÇÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕËÜáçéíóúàèìòùâêîôûãõëü" + ASPA;
    public static final String TRANSLATE_DESTINO = ASPA + "ACEIOUAEIOUAEIOUAOEUaceiouaeiouaeiouaoeu" + ASPA;

    static {
    	if (TRANSLATE_ORIGEM.length() != TRANSLATE_DESTINO.length()) {
	    	throw new RuntimeException("listas de Translate do Oracle com tamanhos diferentes.");
    	}
    }

    /**
     * Gera um alias genérico, no caso a primeira letra do nome
     * da classe, em minusculo.
     *
     * @param classe
     * @return 
     */
    public static String gerarAliasGenerico(Class<?> classe) {
        return classe.getSimpleName().substring(0, 1).toLowerCase();
    }

    /**
     * Gera um select simples para a entidade informada na classe sem alias.
     *
     * @param classe
     * @return 
     */
    public static String gerarSelectHqlGenerico(Class<?> classe) {
        return gerarSelectHqlGenerico(classe, "");
    }

    /**
     * Gera um select simples para a entidade informada na classe com um alias
     * genérico.
     *
     * @param classe
     * @param aliasGenerico
     * @return 
     */
    public static String gerarSelectHqlGenerico(Class<?> classe, String aliasGenerico) {
        return SELECT + aliasGenerico.trim() + FROM + classe.getSimpleName() + " " + aliasGenerico;
    }

    /**
     * Cria a sintaxe 'nomeAtributo = :nomeAtributo', com o valor informado
     * para ser utilizado na criação da cláusula where de um script.
     * NÃO CASSE SENSITIVE.
     *
     * @param nomeAtributo
     * @return 
     */
    public static String criarWhereEqualHqlComParametro(String nomeAtributo) {
        return criarWhereEqualHqlComParametro("", nomeAtributo, nomeAtributo, CASE_SENSITIVE_PADRAO, REMOVER_CARACTERES_ESPECIAIS_PADRAO);
    }

    /**
     * Cria a sintaxe 'nomeAtributo = :nomeAtributo', com o valor informado
     * para ser utilizado na criação da cláusula where de um script.
     *
     * @param nomeAtributo
     * @param caseSensitive
     * @param removerCaracteresEspeciais
     * @return 
     */
    public static String criarWhereEqualHqlComParametro(String nomeAtributo, boolean caseSensitive, boolean removerCaracteresEspeciais) {
        return criarWhereEqualHqlComParametro("", nomeAtributo, nomeAtributo, caseSensitive, removerCaracteresEspeciais);
    }

    /**
     * Cria a sintaxe 'nomeAtributo = :nomeAtributo', com o valor informado
     * para ser utilizado na criação da cláusula where de um script.
     * NÃO CASSE SENSITIVE.
     *
     * @param alias
     * @param nomeAtributo
     * @return 
     */
    public static String criarWhereEqualHqlComParametro(String alias, String nomeAtributo) {
        return criarWhereEqualHqlComParametro(alias, nomeAtributo, nomeAtributo, CASE_SENSITIVE_PADRAO, REMOVER_CARACTERES_ESPECIAIS_PADRAO);
    }

    /**
     * Cria a sintaxe 'nomeAtributo = :nomeAtributo', com o valor informado
     * para ser utilizado na criação da cláusula where de um script.
     *
     * @param alias
     * @param nomeAtributo
     * @param caseSensitive
     * @param removerCaracteresEspeciais
     * @return 
     */
    public static String criarWhereEqualHqlComParametro(String alias, String nomeAtributo, boolean caseSensitive, boolean removerCaracteresEspeciais) {
        return criarWhereEqualHqlComParametro(alias, nomeAtributo, nomeAtributo, caseSensitive, removerCaracteresEspeciais);
    }

    /**
     * Cria a sintaxe 'nomeAtributo = :nomeAtributo', com o valor informado
     * para ser utilizado na criação da cláusula where de um script.
     * NÃO CASSE SENSITIVE.
     *
     * @param alias
     * @param nomeAtributo
     * @param nomeParametro
     * @return 
     */
    public static String criarWhereEqualHqlComParametro(String alias, String nomeAtributo, String nomeParametro) {
    	return criarWhereEqualHqlComParametro(alias, nomeAtributo, nomeParametro, CASE_SENSITIVE_PADRAO, REMOVER_CARACTERES_ESPECIAIS_PADRAO);
    }

    /**
     * Cria a sintaxe 'nomeAtributo = :nomeAtributo', com o valor informado
     * para ser utilizado na criação da cláusula where de um script.
     *
     * @param alias
     * @param nomeAtributo
     * @param nomeParametro
     * @param caseSensitive
     * @param removerCaracteresEspeciais
     * @return 
     */
    public static String criarWhereEqualHqlComParametro(String alias, String nomeAtributo, String nomeParametro, boolean caseSensitive, boolean removerCaracteresEspeciais) {
    	String nomeAtributoAux = nomeAtributoCaseSensitive(alias, nomeAtributo, caseSensitive);
    	String resultado = nomeAtributoAux;
    	if (removerCaracteresEspeciais) {
    		resultado = adicionarFuncaoTranslate(resultado);
    	}
    	resultado = resultado + IGUAL + DOIS_PONTOS + nomeParametro;
        return resultado;
    }

    /**
     * Cria a sintaxe 'nomeAtributo like %:nomeAtributo%', com o valor informado
     * para ser utilizado na criação da cláusula where de um script.
     * NÃO CASSE SENSITIVE.
     *
     * @param nomeAtributo
     * @return 
     */
    public static String criarWhereLikeHqlComParametro(String nomeAtributo) {
        return criarWhereLikeHqlComParametro("", nomeAtributo, CASE_SENSITIVE_PADRAO, REMOVER_CARACTERES_ESPECIAIS_PADRAO);
    }

    /**
     * Cria a sintaxe 'nomeAtributo like %:nomeAtributo%', com o valor informado
     * para ser utilizado na criação da cláusula where de um script.
     *
     * @param nomeAtributo
     * @param caseSensitive
     * @param removerCaracteresEspeciais
     * @return 
     */
    public static String criarWhereLikeHqlComParametro(String nomeAtributo, boolean caseSensitive, boolean removerCaracteresEspeciais) {
        return criarWhereLikeHqlComParametro("", nomeAtributo, caseSensitive, removerCaracteresEspeciais);
    }

    /**
     * Cria a sintaxe 'nomeAtributo like %:nomeAtributo%', com o valor informado
     * para ser utilizado na criação da cláusula where de um script.
     * NÃO CASSE SENSITIVE.
     *
     * @param alias
     * @param nomeAtributo
     * @return 
     */
    public static String criarWhereLikeHqlComParametro(String alias, String nomeAtributo) {
        return criarWhereLikeHqlComParametro(alias, nomeAtributo, nomeAtributo, CASE_SENSITIVE_PADRAO, REMOVER_CARACTERES_ESPECIAIS_PADRAO);
    }

    /**
     * Cria a sintaxe 'nomeAtributo like %:nomeAtributo%', com o valor informado
     * para ser utilizado na criação da cláusula where de um script.
     *
     * @param alias
     * @param nomeAtributo
     * @param caseSensitive
     * @param removerCaracteresEspeciais
     * @return 
     */
    public static String criarWhereLikeHqlComParametro(String alias, String nomeAtributo, boolean caseSensitive, boolean removerCaracteresEspeciais) {
        return criarWhereLikeHqlComParametro(alias, nomeAtributo, nomeAtributo, caseSensitive, removerCaracteresEspeciais);
    }

    /**
     * Cria a sintaxe 'nomeAtributo like %:nomeAtributo%', com o valor informado
     * para ser utilizado na criação da cláusula where de um script.
     * NÃO CASSE SENSITIVE.
     *
     * @param alias
     * @param nomeAtributo
     * @param nomeParametro
     * @return 
     */
    public static String criarWhereLikeHqlComParametro(String alias, String nomeAtributo, String nomeParametro) {
    	return criarWhereLikeHqlComParametro(alias, nomeAtributo, nomeParametro, CASE_SENSITIVE_PADRAO, REMOVER_CARACTERES_ESPECIAIS_PADRAO);
    }

    /**
     * Cria a sintaxe 'nomeAtributo like %:nomeAtributo%', com o valor informado
     * para ser utilizado na criação da cláusula where de um script.
     *
     * @param alias
     * @param nomeAtributo
     * @param nomeParametro
     * @param caseSensitive
     * @param removerCaracteresEspeciais
     * @return 
     */
    public static String criarWhereLikeHqlComParametro(String alias, String nomeAtributo, String nomeParametro, boolean caseSensitive, boolean removerCaracteresEspeciais) {
    	String resultado = nomeAtributoCaseSensitive(alias, nomeAtributo, caseSensitive);
    	if (removerCaracteresEspeciais) {
    		resultado = adicionarFuncaoTranslate(resultado);
    	}
    	resultado = resultado + LIKE + DOIS_PONTOS + nomeParametro;
        return resultado;
    }

	private static String nomeAtributoCaseSensitive(String alias, String nomeAtributo, boolean caseSensitive) {
		String aliasAux = getAlias(alias);
		String nomeAtributoAux = aliasAux + nomeAtributo;
    	if (!caseSensitive) {
    		nomeAtributoAux = UPPER + ABRE_PARENTESE + nomeAtributoAux + FECHA_PARENTESE;
    	}
		return nomeAtributoAux;
	}

	/**
	 * Cria a sintaxe de uso da função "translate" do Oracle, passando 
	 * @param valor
	 * @return
	 */
	private static String adicionarFuncaoTranslate(String valor) {
		return ORACLE_TRANSLATE + ABRE_PARENTESE + valor + VIRGULA + ESPACO + TRANSLATE_ORIGEM + VIRGULA + ESPACO + TRANSLATE_DESTINO + FECHA_PARENTESE;
	}

    /**
     * Cria a sintaxe 'nomeAtributo like %:nomeAtributo%', com o valor informado
     * para ser utilizado na criação da cláusula where de um script.
     *
     * @param nomeAtributo
     * @param nomeParametroDataInicio
     * @param nomeParametroDataFim
     * @param dataInicio
     * @param dataFim
     * @return 
     */
    public static String criarWhereBetweenHqlComParametro(String nomeAtributo, String nomeParametroDataInicio, String nomeParametroDataFim, Date dataInicio, Date dataFim) {
        return criarWhereBetweenHqlComParametro("", nomeAtributo, nomeParametroDataInicio, nomeParametroDataFim, dataInicio, dataFim);
    }

    /**
     * Cria a sintaxe 'nomeAtributo like %:nomeAtributo%', com o valor informado
     * para ser utilizado na criação da cláusula where de um script.
     *
     * @param alias
     * @param nomeAtributo
     * @param nomeParametroDataInicio
     * @param nomeParametroDataFim
     * @param dataInicio
     * @param dataFim
     * @return 
     */
    public static String criarWhereBetweenHqlComParametro(String alias, String nomeAtributo, String nomeParametroDataInicio, String nomeParametroDataFim, Date dataInicio, Date dataFim) {
        String clausula = "";
        if (dataInicio != null || dataFim != null) {
            if (dataInicio != null && dataFim != null) {
            	String aliasAux = getAlias(alias);
                clausula = aliasAux + nomeAtributo + BETWEEN + DOIS_PONTOS + nomeParametroDataInicio + AND + DOIS_PONTOS + nomeParametroDataFim;
            } else if (dataInicio != null) {
                clausula = criarWhereMaiorQueHqlComParametro(true, alias, nomeAtributo, nomeParametroDataInicio, dataInicio);
            } else {
                clausula = criarWhereMenorQueHqlComParametro(true, alias, nomeAtributo, nomeParametroDataFim, dataFim);
            }
        }
        return clausula;
    }

    /**
     * Cria a sintaxe 'nomeAtributo > :nomeAtributo' ou 'nomeAtributo >= :nomeAtributo',
     * com o valor informado para ser utilizado na criação da cláusula where de um script.
     * 
     * @param igual
     * @param alias
     * @param nomeAtributo
     * @param data
     * @return
     */
    public static String criarWhereMaiorQueHqlComParametro(boolean igual, String alias, String nomeAtributo, Date data) {
        return criarWhereMaiorQueHqlComParametro(igual, alias, nomeAtributo, nomeAtributo, data);
    }

    /**
     * Cria a sintaxe 'nomeAtributo > :nomeAtributo' ou 'nomeAtributo >= :nomeAtributo',
     * com o valor informado para ser utilizado na criação da cláusula where de um script.
     * 
     * @param igual
     * @param alias
     * @param nomeAtributo
     * @param nomeParametro
     * @param data
     * @return
     */
    public static String criarWhereMaiorQueHqlComParametro(boolean igual, String alias, String nomeAtributo, String nomeParametro, Date data) {
        String resultado = "";
        if (data != null) {
        	resultado = getAlias(alias) + nomeAtributo;
        	if (igual) {
        		resultado += MAIOR_OU_IGUAL;
        	} else {
        		resultado += MAIOR_QUE;
        	}
        	resultado += DOIS_PONTOS + nomeParametro;
        }
        return resultado;
    }

    /**
     * Cria a sintaxe 'nomeAtributo < :nomeAtributo' ou 'nomeAtributo <= :nomeAtributo',
     * com o valor informado para ser utilizado na criação da cláusula where de um script.
     * 
     * @param igual
     * @param alias
     * @param nomeAtributo
     * @param data
     * @return
     */
    public static String criarWhereMenorQueHqlComParametro(boolean igual, String alias, String nomeAtributo, Date data) {
        return criarWhereMenorQueHqlComParametro(igual, alias, nomeAtributo, nomeAtributo, data);
    }

    /**
     * Cria a sintaxe 'nomeAtributo < :nomeAtributo' ou 'nomeAtributo <= :nomeAtributo',
     * com o valor informado para ser utilizado na criação da cláusula where de um script.
     * 
     * @param igual
     * @param alias
     * @param nomeAtributo
     * @param nomeParametro
     * @param data
     * @return
     */
    public static String criarWhereMenorQueHqlComParametro(boolean igual, String alias, String nomeAtributo, String nomeParametro, Date data) {
        String resultado = "";
        if (data != null) {
        	resultado = getAlias(alias) + nomeAtributo;
        	if (igual) {
        		resultado += MENOR_OU_IGUAL;
        	} else {
        		resultado += MENOR_QUE;
        	}
        	resultado += DOIS_PONTOS + nomeParametro;
        }
        return resultado;
    }

    /**
     * Cria a sintaxe 'nomeAtributo IS NOT NULL', com o valor informado
     * para ser utilizado na criação da cláusula where de um script.
     *
     * @param alias
     * @param nomeAtributo
     * @return
     */
    public static String criarWhereNotNull(String alias, String nomeAtributo) {
        String aliasAux = getAlias(alias);
        String clausula = aliasAux + nomeAtributo + IS + NOT + NULL;
        return clausula;
    }

    /**
     * Cria a sintaxe 'nomeAtributo IS NULL', com o valor informado
     * para ser utilizado na criação da cláusula where de um script.
     *
     * @param alias
     * @param nomeAtributo
     * @return
     */
    public static String criarWhereNull(String alias, String nomeAtributo) {
        String aliasAux = getAlias(alias);
        String clausula = aliasAux + nomeAtributo + IS + NULL;
        return clausula;
    }

    /**
     * Adiciona uma cláusula 'and' na query informada.
     * Caso a query informada esteja vazia, NÃO será inserido o 'and' ou o 'or
     * do início.
     *
     * @param query
     * @param clausula
     * @return 
     */
    public static String inserirClausulaWhereAnd(String query, String clausula) {
        return inserirClausulaWhere(query, true, clausula);
    }

    /**
     * Adiciona uma cláusula 'or' na query informada.
     * Caso a query informada esteja vazia, NÃO será inserido o 'and' ou o 'or
     * do início.
     *
     * @param query
     * @param clausula
     * @return 
     */
    public static String inserirClausulaWhereOr(String query, String clausula) {
        return inserirClausulaWhere(query, false, clausula);
    }

    /**
     * Adiciona uma cláusula 'and' ou 'or' na query informada.
     * Caso a query informada esteja vazia, NÃO será inserido o 'and' ou o 'or
     * do início.
     *
     * @param query
     * @param isAnd
     * @param clausula
     * @return 
     */
    public static String inserirClausulaWhere(String query, boolean isAnd, String clausula) {
        String resultado = query;
        if (!clausula.isEmpty()) {
	        if (!resultado.isEmpty()) {
	            String andOr;
	            if (isAnd) {
	                andOr = AND;
	            } else {
	                andOr = OR;
	            }
	            resultado += andOr;
	        }
        }
        return resultado + clausula;
    }

    /**
     * Adiciona uma cláusula 'and' na query informada, caso o objeto seja não
     * nulo e, em caso de String, não vazio.
     * Caso a query informada esteja vazia, NÃO será inserido o 'and' ou o 'or
     * do início.
     *
     * @param query
     * @param clausula
     * @param valor
     * @return 
     */
    public static String inserirClausulaWhereAnd(String query, String clausula, Object valor) {
        return inserirClausulaWhere(query, true, clausula, valor);
    }

    /**
     * Adiciona uma cláusula 'or' na query informada, caso o objeto seja não
     * nulo e, em caso de String, não vazio.
     * Caso a query informada esteja vazia, NÃO será inserido o 'and' ou o 'or
     * do início.
     *
     * @param query
     * @param clausula
     * @param valor
     * @return 
     */
    public static String inserirClausulaWhereOr(String query, String clausula, Object valor) {
        return inserirClausulaWhere(query, false, clausula, valor);
    }

    /**
     * Adiciona uma cláusula 'and' ou 'or' na query informada, caso o objeto seja não
     * nulo e, em caso de String, não vazio.
     * Caso a query informada esteja vazia, NÃO será inserido o 'and' ou o 'or
     * do início.
     *
     * @param query
     * @param isAnd
     * @param clausula
     * @param valor
     * @return 
     */
    public static String inserirClausulaWhere(String query, boolean isAnd, String clausula, Object valor) {
        String resultado = query;
        if (valor != null) {
            if (!(valor instanceof String) || !((String) valor).trim().isEmpty()) {
                if (!query.isEmpty()) {
                    String andOr;
                    if (isAnd) {
                        andOr = AND;
                    } else {
                        andOr = OR;
                    }
                    resultado += andOr;
                }
                resultado += clausula;
            }
        }
        return resultado;
    }

    /**
     * Adiciona o parametro do tipo String na query informada, com o nome informado,
     * caso o valor do mesmo seja diferente de null e não seja vazio, adicionando os
     * percents (%) antes e depois.
     *
     * @param query
     * @param nomeParametro
     * @param objetoParametro
     */
    public static void setParametroStringQuery(Query query, String nomeParametro, String objetoParametro) {
    	setParametroStringQuery(query, nomeParametro, objetoParametro, CASE_SENSITIVE_PADRAO, REMOVER_CARACTERES_ESPECIAIS_PADRAO);
    }

    /**
     * Adiciona o parametro do tipo String na query informada, com o nome informado,
     * caso o valor do mesmo seja diferente de null e não seja vazio, adicionando os
     * percents (%) antes e depois.
     *
     * @param query
     * @param nomeParametro
     * @param objetoParametro
     * @param caseSensitive
     * @param removerCaracteresEspeciais
     */
    public static void setParametroStringQuery(Query query, String nomeParametro, String objetoParametro, boolean caseSensitive, boolean removerCaracteresEspeciais) {
        setParametroStringQuery(query, nomeParametro, objetoParametro, true, true, caseSensitive, removerCaracteresEspeciais);
    }

    /**
     * Adiciona o parametro do tipo String na query informada, com o nome informado,
     * caso o valor do mesmo seja diferente de null e não seja vazio, adicionando os
     * percents (%) nos locais indicados.
     *
     * @param query
     * @param nomeParametro
     * @param objetoParametro
     * @param percentAntes
     * @param percentDepois
     */
    public static void setParametroStringQueryLike(Query query, String nomeParametro, String objetoParametro, boolean percentAntes, boolean percentDepois) {
        setParametroStringQuery(query, nomeParametro, objetoParametro, percentAntes, percentDepois, CASE_SENSITIVE_PADRAO, REMOVER_CARACTERES_ESPECIAIS_PADRAO);
    }

    /**
     * Adiciona o parametro do tipo String na query informada, com o nome informado,
     * caso o valor do mesmo seja diferente de null e não seja vazio, adicionando os
     * percents (%) nos locais indicados.
     *
     * @param query
     * @param nomeParametro
     * @param objetoParametro
     * @param percentAntes
     * @param percentDepois
     * @param caseSensitive
     * @param removerCaracteresEspeciais
     */
    public static void setParametroStringQuery(Query query, String nomeParametro, String objetoParametro, boolean percentAntes, boolean percentDepois, boolean caseSensitive, boolean removerCaracteresEspeciais) {
        if (objetoParametro != null && !objetoParametro.trim().isEmpty()) {
        	String objetoParametroAux;
        	if (caseSensitive) {
        		objetoParametroAux = objetoParametro;
        	} else {
        		objetoParametroAux = objetoParametro.toUpperCase();
        	}
            if (removerCaracteresEspeciais) {
            	objetoParametroAux = aplicarTranslateEmValor(objetoParametroAux);
            }
            if (percentAntes) {
            	objetoParametroAux = PERCENT + objetoParametroAux;
            }
            if (percentDepois) {
            	objetoParametroAux += PERCENT;
            }
            query.setString(nomeParametro, objetoParametroAux);
        }
    }

    private static String aplicarTranslateEmValor(String valor) {
    	String resultado = valor;
    	for (int posicao = 0; posicao < TRANSLATE_ORIGEM.length(); posicao++) {
			char caracterOrigem = TRANSLATE_ORIGEM.charAt(posicao);
			char caracterDestino = TRANSLATE_DESTINO.charAt(posicao);
			resultado = resultado.replace(caracterOrigem, caracterDestino);
		}
    	return resultado;
    }

    /**
     * Adiciona o parametro do tipo Date na query informada, com o nome informado,
     * caso o valor do mesmo seja diferente de null.
     *
     * @param query
     * @param nomeParametro
     * @param objetoParametro
     */
    public static void setParametroDateQuery(Query query, String nomeParametro, Date objetoParametro) {
        if (objetoParametro != null) {
            query.setDate(nomeParametro, objetoParametro);
        }
    }

    /**
     * Adiciona o parametro do tipo Date na query informada, com o nome informado,
     * caso o valor do mesmo seja diferente de null.
     *
     * @param query
     * @param nomeParametro
     * @param objetoParametro
     */
    public static void setParametroTimestampQuery(Query query, String nomeParametro, Date objetoParametro) {
        setParametroTimestampQuery(query, nomeParametro, objetoParametro, AjusteDataEnum.NENHUM);
    }

    /**
     * Adiciona o parametro do tipo Date na query informada, com o nome informado,
     * caso o valor do mesmo seja diferente de null.
     *
     * @param query
     * @param nomeParametro
     * @param objetoParametro
     * @param ajusteData
     */
    public static void setParametroTimestampQuery(Query query, String nomeParametro, Date objetoParametro, AjusteDataEnum ajusteData) {
        if (objetoParametro != null) {
        	Date objetoParametroAux;
        	switch (ajusteData) {
			case PRIMEIRA_HORA_DIA:
				objetoParametroAux = inicioDia(objetoParametro);
				break;
			case ULTIMA_HORA_DIA:
				objetoParametroAux = fimDia(objetoParametro);
				break;
			default:
				objetoParametroAux = objetoParametro;
				break;
			}
            query.setTimestamp(nomeParametro, objetoParametroAux);
        }
    }

    /**
     * Adiciona o parametro do tipo Long na query informada, com o nome informado,
     * caso o valor do mesmo seja diferente de null.
     *
     * @param query
     * @param nomeParametro
     * @param objetoParametro
     */
    public static void setParametroLongQuery(Query query, String nomeParametro, Long objetoParametro) {
        if (objetoParametro != null) {
            query.setLong(nomeParametro, objetoParametro);
        }
    }

	private static String getAlias(String alias) {
		String aliasAux = alias.trim();
    	if (!aliasAux.isEmpty() && !alias.endsWith(PONTO)) {
    		aliasAux = aliasAux + PONTO;
    	}
		return aliasAux;
	}

	/**
	 * Retorna a coluna mapeada para o atributo informado, buscando pela anotação {@link javax.persistence.Column }
	 * no atributto ou no seu método Getter padrão.
	 *
	 * @param classeEntidade
	 * @param nomeAtributo
	 * @return
	 */
	public static String getNomeColunaMapeadaEntidade(Class<?> classeEntidade, String nomeAtributo) {
		String nomeColuna = getNomeColunaMapeadaAtributoEntidade(classeEntidade, nomeAtributo);
		if (nomeColuna == null) {
			nomeColuna = getNomeColunaMapeadaGetterEntidade(classeEntidade, nomeAtributo);
		}
		return nomeColuna;
	}

	private static String getNomeColunaMapeadaGetterEntidade(Class<?> classeEntidade, String nomeAtributo) {
		String nomeColuna = null;
		Method method;
		try {
			String metodoGetter = "get" + nomeAtributo.substring(0, 1).toUpperCase() + nomeAtributo.substring(1);
			method = classeEntidade.getDeclaredMethod(metodoGetter);
			Column columnAnnotation = method.getAnnotation(Column.class);
	    	if (columnAnnotation != null) {
	    		nomeColuna = columnAnnotation.name();
	    	} else {
	    		JoinColumn joinColumnAnnotation = method.getAnnotation(JoinColumn.class);
	    		
	    		if (joinColumnAnnotation != null) {
	    			nomeColuna = joinColumnAnnotation.name();
	    		}
	    	}
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		}
		return nomeColuna;
	}

	private static String getNomeColunaMapeadaAtributoEntidade(Class<?> classeEntidade, String nomeAtributo) {
		String nomeColuna = null;
		Field field;
		try {
			field = classeEntidade.getDeclaredField(nomeAtributo);
			Column columnAnnotation = field.getAnnotation(Column.class);
	    	if (columnAnnotation != null) {
	    		nomeColuna = columnAnnotation.name();
	    	} else {
	    		JoinColumn joinColumnAnnotation = field.getAnnotation(JoinColumn.class);
	    		
	    		if (joinColumnAnnotation != null) {
	    			nomeColuna = joinColumnAnnotation.name();
	    		}
	    	}
		} catch (SecurityException e) {
		} catch (NoSuchFieldException e) {
		}
		return nomeColuna;
	}

    /**
     * Retorna o ano corrente.
     *
     * @return
     */
	public static int anoCorrente() {
	    Calendar calendar = Calendar.getInstance();
	    return calendar.get(Calendar.YEAR);
	}

	/**
	 * Retorna o primeiro dia do ano informado.
	 *
	 * @param ano
	 * @return
	 */
	public static Date primeiroDiaAno(int ano) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.YEAR, ano);
	    calendar.set(Calendar.DAY_OF_YEAR, 1);
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	    return calendar.getTime();
	}

	/**
	 * Retorna o ultimo dia do ano informado.
	 *
	 * @param ano
	 * @return
	 */
	public static Date ultimoDiaAno(int ano) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.YEAR, ano + 1);
	    calendar.set(Calendar.DAY_OF_YEAR, 1);
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	    calendar.add(Calendar.MILLISECOND, -1);
	    return calendar.getTime();
	}

	/**
	 * Retorna o primeiro momento do dia informado, hora 0:0:0.
	 *
	 * @param data
	 * @return
	 */
	public static Date inicioDia(Date data) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(data);
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	    return calendar.getTime();
	}

	/**
	 * Retorna o ultimo momento do dia informado, hora 23:59:59.
	 *
	 * @param data
	 * @return
	 */
	public static Date fimDia(Date data) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(inicioDia(data));
	    calendar.add(Calendar.DAY_OF_YEAR, 1);
	    calendar.add(Calendar.MILLISECOND, -1);
	    return calendar.getTime();
	}

	/**
	 * Retorna o dia no mês da data informada.
	 *
	 * @param data
	 * @return
	 */
	public static int getDiaMes(Date data) {
	    return get(data, Calendar.DAY_OF_MONTH);
	}

	/**
	 * Retorna o mês da data informada.
	 * Retorno entre 1 e 12.
	 *
	 * @param data
	 * @return
	 */
	public static int getMes(Date data) {
	    return get(data, Calendar.MONTH) + 1;
	}

	/**
	 * Retorna o ano da data informada.
	 *
	 * @param data
	 * @return
	 */
	public static int getAno(Date data) {
	    return get(data, Calendar.YEAR);
	}

	/**
	 * Retorna a hora no dia da data informada.
	 *
	 * @param data
	 * @return
	 */
	public static int getHora(Date data) {
	    return get(data, Calendar.HOUR_OF_DAY);
	}

	/**
	 * Retorna o minuto no dia da data informada.
	 *
	 * @param data
	 * @return
	 */
	public static int getMinuto(Date data) {
	    return get(data, Calendar.MINUTE);
	}

	/**
	 * Retorna o segundo no dia da data informada.
	 *
	 * @param data
	 * @return
	 */
	public static int getSegundo(Date data) {
	    return get(data, Calendar.SECOND);
	}

	/**
	 * Retorna o dia no ano da data informada.
	 *
	 * @param data
	 * @return
	 */
	public static int getDiaAno(Date data) {
	    return get(data, Calendar.DAY_OF_YEAR);
	}

	private static int get(Date data, int campo) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(data);
	    return calendar.get(campo);
	}

	/**
	 * Verifica se o conteúdo de uma String é nulo ou vazio.
	 *
	 * @param texto
	 * @return
	 */
	public static boolean isStringNulaOuVazia(String texto) {
		return (texto == null || texto.trim().isEmpty());
	}

	/**
	 * Verifica se alguma das Strings informadas é nula ou vazia.
	 *
	 * @param textos
	 * @return
	 */
	public static boolean isPossuiStringNulaOuVazia(Object...textos) {
		return verificarObjetoNulloOuTextoVazio(false, textos);
	}

	/**
	 * Verifica se o objeto informado é nulo, e, caso seja uma String, se o mesmo é vazio.
	 *
	 * @param objeto
	 * @return
	 */
	public static boolean isObjetoNulloOuTextoVazio(Object objeto) {
		boolean resultado = objeto == null;
		if (!resultado && objeto instanceof String) {
			resultado = ((String) objeto).trim().isEmpty();
		}
		return resultado;
	}

	/**
	 * Verifica se algum objeto informado é nulo, e, caso seja uma String, se o mesmo é vazio.
	 *
	 * @param objetos
	 * @return
	 */
	public static boolean isPossuiObjetoNulloOuTextoVazio(Object...objetos) {
		return verificarObjetoNulloOuTextoVazio(false, objetos);
	}

	/**
	 * Verifica se algum objeto informado é nulo, e, caso seja uma String, se o mesmo é vazio,
	 * caso o parâmetro 'tipoVerificacaoE' seja false, caso contrário, verifica se TODOS os objetos
	 * informados são nulos ou vazios.
	 *
	 * @param tipoVerificacaoE
	 * @param objetos
	 * @return
	 */
	public static boolean verificarObjetoNulloOuTextoVazio(boolean tipoVerificacaoE, Object...objetos) {
		int qtdNulosVazios = 0;
		for (Object objeto : objetos) {
			if (isObjetoNulloOuTextoVazio(objeto)) {
				qtdNulosVazios++;
				if (!tipoVerificacaoE) {
					break;
				}
			}
		}
		boolean resultado;
		if (tipoVerificacaoE) {
			resultado = qtdNulosVazios == objetos.length;
		} else {
			resultado = qtdNulosVazios > 0;
		}
		return resultado;
	}
}

enum AjusteDataEnum {

	NENHUM,
	PRIMEIRA_HORA_DIA,
	ULTIMA_HORA_DIA;
}