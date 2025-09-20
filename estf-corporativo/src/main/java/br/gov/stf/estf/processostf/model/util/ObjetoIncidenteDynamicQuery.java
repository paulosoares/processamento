package br.gov.stf.estf.processostf.model.util;

import br.gov.stf.estf.entidade.processostf.IncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.RecursoProcesso;
import br.gov.stf.framework.model.util.query.DynamicQuery;

/**
 * Classe que constr�i dinamicamente uma consulta de objeto incidente, de forma
 * que o usu�rio s� precise utilizar as informa��es de Sigla de Classe, N�mero
 * de Processo, Tipo de Recurso e Tipo de Julgamento
 * 
 * @author Demetrius.Jube
 * 
 */
public class ObjetoIncidenteDynamicQuery extends DynamicQuery {
	public static final String ALIAS_PROCESSO = "aliasProcesso";
	public static final String ALIAS_INCIDENTE_JULGAMENTO = "aliasIncidenteJulgamento";
	public static final String ALIAS_RECURSO_PROCESSO = "aliasRecursoProcesso";
	private String nomePropriedadeObjetoIncidente = "objetoIncidente";
	private String aliasEntidadeComObjetoIncidente = "";
	private boolean pesquisarObjetosDoProcesso = false;

	/**
	 * Cria uma inst�ncia da consulta de Objeto Incidente
	 */
	public ObjetoIncidenteDynamicQuery() {
	}

	/**
	 * Cria uma inst�ncia da consulta de Objeto Incidente, onde a consulta vai
	 * verificar todos os Objetos Incidentes que tem como
	 * ObjetoIncidentePrincipal os dados do processo informado. Nesse caso,
	 * apenas o N�mero do Processo, a Sigla da Classe e o Tipo de Processo ser�o
	 * levados em considera��o.
	 * 
	 * @param pesquisarObjetosDoProcesso
	 */
	public ObjetoIncidenteDynamicQuery(boolean pesquisarObjetosDoProcesso) {
		this.pesquisarObjetosDoProcesso = pesquisarObjetosDoProcesso;
	}

	/**
	 * Cria uma inst�ncia da consulta de ObjetoIncidente
	 * 
	 * @param aliasEntidadeComObjetoIncidente
	 *            Alias utilizado no HQL para identificar a entidade que cont�m
	 *            o atributo ObjetoIncidente
	 */
	public ObjetoIncidenteDynamicQuery(String aliasEntidadeComObjetoIncidente) {
		this.aliasEntidadeComObjetoIncidente = aliasEntidadeComObjetoIncidente + ".";
	}

	/**
	 * * Cria uma inst�ncia da consulta de Objeto Incidente, onde a consulta vai
	 * verificar todos os Objetos Incidentes que tem como
	 * ObjetoIncidentePrincipal os dados do processo informado. Nesse caso,
	 * apenas o N�mero do Processo, a Sigla da Classe e o Tipo de Processo ser�o
	 * levados em considera��o.
	 * 
	 * @param aliasEntidadeComObjetoIncidente
	 * @param pesquisarObjetosDoProcesso
	 */
	public ObjetoIncidenteDynamicQuery(String aliasEntidadeComObjetoIncidente, boolean pesquisarObjetosDoProcesso) {
		this.aliasEntidadeComObjetoIncidente = aliasEntidadeComObjetoIncidente + ".";
		this.pesquisarObjetosDoProcesso = pesquisarObjetosDoProcesso;
	}

	/**
	 * Cria uma inst�ncia da consulta de ObjetoIncidente
	 * 
	 * @param aliasEntidadeComObjetoIncidente
	 *            Alias utilizado no HQL para identificar a entidade que cont�m
	 *            o atributo ObjetoIncidente
	 * @param nomePropriedadeObjetoIncidente
	 *            Nome do atributo que mapeia o ObjetoIncidente
	 */
	public ObjetoIncidenteDynamicQuery(String aliasEntidadeComObjetoIncidente, String nomePropriedadeObjetoIncidente) {
		this.aliasEntidadeComObjetoIncidente = aliasEntidadeComObjetoIncidente + ".";
		this.nomePropriedadeObjetoIncidente = nomePropriedadeObjetoIncidente;
	}

	public void setNumeroProcesso(Long numeroProcesso) {
		if (numeroProcesso != null && numeroProcesso > 0) {
			insereCondicao(ALIAS_PROCESSO + ".numeroProcessual", numeroProcesso);
			registraJoin(ALIAS_PROCESSO, Processo.class);
		}
	}

	public void setSiglaClasseProcessual(String sigla) {
		if (sigla != null && sigla.trim().length() > 0) {
			insereCondicao(ALIAS_PROCESSO + ".siglaClasseProcessual", sigla);
			registraJoin(ALIAS_PROCESSO, Processo.class);
		}
	}

	public void setTipoMeioProcesso(String tipoMeioProcesso) {
		if (tipoMeioProcesso != null) {
			insereCondicao(ALIAS_PROCESSO + ".tipoMeioProcesso", tipoMeioProcesso);
			registraJoin(ALIAS_PROCESSO, Processo.class);
		}
	}

	public void setCodigoRecurso(Long codigoRecurso) {
		if (codigoRecurso != null && codigoRecurso > 0 && !pesquisarObjetosDoProcesso) {
			insereCondicao(ALIAS_RECURSO_PROCESSO + ".tipoRecursoProcesso.id", codigoRecurso);
			registraJoin(ALIAS_RECURSO_PROCESSO, RecursoProcesso.class, ALIAS_RECURSO_PROCESSO + ".principal = " + ALIAS_PROCESSO);
		}

	}

	public void setTipoJulgamento(Long tipoJulgamento) {
		if (tipoJulgamento != null && tipoJulgamento > 0 && !pesquisarObjetosDoProcesso) {
			insereCondicao(ALIAS_INCIDENTE_JULGAMENTO + ".tipoJulgamento.id", tipoJulgamento);
			registraJoin(ALIAS_INCIDENTE_JULGAMENTO, IncidenteJulgamento.class, ALIAS_INCIDENTE_JULGAMENTO + ".principal = "
					+ ALIAS_PROCESSO);
		}
	}

	@Override
	public String getStringSqlSemWhere() {
		return getQuery(false);
	}

	@Override
	public String getStringSqlWhere() {
		return getQuery(true);
	}

	private String getQuery(boolean incluirWhere) {
		StringBuffer condicao = new StringBuffer();
		if (getParametros().length > 0) {
			if (incluirWhere) {
				condicao.append(INICIO_CLAUSULA_WHERE);
			}
			condicao.append(aliasEntidadeComObjetoIncidente);
			condicao.append(nomePropriedadeObjetoIncidente);
			if (pesquisarObjetosDoProcesso) {
				// Consulta os objetos que fa�am refer�ncia ao processo
				// informado (ObjetoIncidentePrincipal)
				condicao.append(".principal.id");
			} else {
				condicao.append(".id");

			}
			condicao.append(" IN (");
			condicao.append(getConsultaDeObjetoIncidente());
			condicao.append(") ");
		}
		return condicao.toString();

	}

	public String getConsultaDeObjetoIncidente() {
		StringBuffer condicao = new StringBuffer();
		condicao.append("SELECT ");
		defineEntidadeDaPesquisa(condicao);
		condicao.append(".id FROM ");
		String sqlWhere = getStringSqlFormatado();
		if (sqlWhere.startsWith(",")) {
			sqlWhere = sqlWhere.substring(1);
		}
		condicao.append(sqlWhere);
		return condicao.toString();
	}

	private void defineEntidadeDaPesquisa(StringBuffer condicao) {
		if (pesquisarObjetosDoProcesso) {
			// Caso v� pesquisar os objetos do processo, deve utilizar apenas a
			// entidade ALIAS_PROCESSO
			condicao.append(ALIAS_PROCESSO);
		} else {
			if (getJoinsUtilizados().containsKey(ALIAS_INCIDENTE_JULGAMENTO)) {
				condicao.append(ALIAS_INCIDENTE_JULGAMENTO);
			} else if (getJoinsUtilizados().containsKey(ALIAS_RECURSO_PROCESSO)) {
				condicao.append(ALIAS_RECURSO_PROCESSO);
			} else if (getJoinsUtilizados().containsKey(ALIAS_PROCESSO)) {
				condicao.append(ALIAS_PROCESSO);
			}
		}
	}

	public static void main(String[] args) {
		ObjetoIncidenteDynamicQuery consulta = new ObjetoIncidenteDynamicQuery("pp", "objetoInc");
		consulta.setNumeroProcesso(123456l);
		consulta.setSiglaClasseProcessual("RE");
		consulta.setCodigoRecurso(0l);
		consulta.setTipoJulgamento(9l);
		consulta.setTipoMeioProcesso("E");
		System.out.println(consulta.getStringSqlWhere());
	}

}
