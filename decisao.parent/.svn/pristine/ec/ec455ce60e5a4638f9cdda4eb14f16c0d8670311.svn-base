package br.jus.stf.estf.decisao.pesquisa.domain;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * Representa a pesquisa avançada solicitada pelo usuário. Armazena todos os critérios
 * de pesquisa informados na camada de apresentação.
 * 
 * @author Rodrigo Barreiros
 * @since 30.04.2010
 */
@Name("pesquisa")
@Scope(ScopeType.CONVERSATION)
public class Pesquisa {
	public static final String CHAVE_ORDENACAO = "ordenacao"; 
	public static final String ORDENACAO_DATA = "OD";
	public static final String ORDENACAO_CLASSE_PROCESSUAL = "OCP";
	
	public static final String CHAVE_FAVORITOS = "preferenciaFavoritos";
	public static final String FAVORITOS_APENAS = "FAP";
	public static final String FAVORITOS_PRIMEIRO = "FPR";

	public static final String CHAVE_TIPO_AMBIENTE = "tipoAmbiente";
	
	private Map<String, Object> parameters = new HashMap<String, Object>();
	private int firstResult;
	private int maxResults;
	private TipoPesquisa tipoPesquisa;
	
	/**
	 * Verifica se o valor de um dado parâmetro não é "branco". Por "branco",
	 * entende-se que o valor não é nulo, não é vazio, no caso de strings,
	 * e não é negativo, no caso de valores numéricos.
	 * 
	 * @param parameter o identificador do parâmetro
	 * @return true, se não é vazio, false, caso contrário
	 */
	public boolean isNotBlank(String parameter) {
		Object value = parameters.get(parameter);
		if (value != null) {
			if (value instanceof String) {
				return StringUtils.isNotBlank((String) value);
			}
			if (value instanceof Number) {
				return ((Number) value).longValue() > 0;
			}
			if (value instanceof Date) {
				return true;
			}
			if (value instanceof ObjetoIncidenteDto)
				return true;
			if (value instanceof Boolean) {
				return true;
			}
			if (value instanceof List && ((List) value).size() > 0) {
				return true;
			}
			if (value.getClass().isArray()  && (Arrays.asList(value).size() > 0)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Verifica se todos os parâmetros da pesquisa são "brancos". Ou seja,
	 * se nenhum parâmetro foi informado na pesquisa
	 *
	 * @return true, se pesquisa vazia, false, caso contrário
	 */
	public boolean isBlank() {
	    for (String id : parameters.keySet()) {
            if (!id.equals(CHAVE_ORDENACAO) && !id.equals("painelVisualizacao") && isNotBlank(id)) {
                return false;
            }
        }
	    return true;
	}
	
	/**
	 * Retorna a lista de parâmetros.
	 * 
	 * @return a lista
	 */
	public Map<String, Object> getParameters() {
		return parameters;
	}
	
	/**
	 * Adiciona um dado parâmetro à pesquisa.
	 * 
	 * @param key o identificador do parâmetro
	 * @param value o valor do parâmetro
	 */
	public void put(String key, Object value) {
		parameters.put(key, value);
	}

	/**
	 * Remove um parâmetro da pesquisa dodo seu identificador.
	 * 
	 * @param key o identificador do parâmetros
	 */
	public void remove(String key) {
		parameters.remove(key);
	}

	/**
	 * Recupera o valor de um parâmetro da pesquisa dado seu identificador.
	 * 
	 * @param parameter o identificador do parâmetro
	 * @return o valor do parâmetro
	 */
	public Object get(String parameter) {
		return parameters.get(parameter);
	}
	
	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}
	
	public int getFirstResult() {
		return firstResult;
	}
	
	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}
	
	public int getMaxResults() {
		return maxResults;
	}

	public TipoPesquisa getTipoPesquisa() {
		return tipoPesquisa;
	}

	public void setTipoPesquisa(TipoPesquisa tipoPesquisa) {
		this.tipoPesquisa = tipoPesquisa;
	}

	public static enum TipoPesquisa {
		TEXTOS(TextoDto.class), PROCESSOS(ObjetoIncidenteDto.class), LISTAS_TEXTOS(ListaTextosDto.class), LISTAS_PROCESSOS(ListaIncidentesDto.class), COMUNICACOES(ComunicacaoDto.class);
		
		private Class clazz;
		
		private TipoPesquisa(Class clazz) {
			this.clazz = clazz;
		}
		
		public Class getClazz() {
			return this.clazz;
		}
		
		public static TipoPesquisa valueOf (Class clazz) {
			for (TipoPesquisa tipoPesquisa : TipoPesquisa.values()) {
				if (tipoPesquisa.getClazz().equals(clazz)) {
					return tipoPesquisa;
				}
			}
			
			return null;
		}
	}
}
