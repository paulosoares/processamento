package br.gov.stf.estf.processostf.model.util;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.engine.SessionImplementor;

public class PesquisaDePartesHelper {
	private Long codigoCategoriaParte;
	private String nomeParte;
	private Session session;
	private Map<String, Query> mapaDeQueries;

	private Map<String, Query> getMapaDeQueries() {
		if (mapaDeQueries == null) {
			mapaDeQueries = new HashMap<String, Query>();
		}
		return mapaDeQueries;
	}

	public void adicionaQueryParaExecucao(String nomeDaQuery, Query query) {
		getMapaDeQueries().put(nomeDaQuery, query);
	}

	public PesquisaDePartesHelper(Session session) {
		this.session = session;
	}

	public void setCodigoCategoriaParte(Long codigoCategoriaParte) {
		this.codigoCategoriaParte = codigoCategoriaParte;
	}

	public void setNomeParte(String nomeParte) {
		this.nomeParte = nomeParte;
	}

	/*
	 * Parâmetros da Package de pesquisa de partes 1 - TIP_OBJETO_INCIDENTE:
	 * Informar o Tipo de Objeto Incidente (PR, RC, PI, PA, e etc);
	 * 
	 * 2 - SEQ_OBJETO_INCIDENTE: Informar seqüencial do objeto incidente;
	 * 
	 * 3 - SEQ_JURISDICIONADO: Informar seqüencial do jurisdicionado;
	 * 
	 * 4 - NOM_JURISDICIONADO: Informar o nome das partes, o filtro será
	 * utilizado no nome de apresentação e no nome do jurisdicionado;
	 * 
	 * 5 - COD_CATEGORIA: Informar o código da categoria;
	 */
	public List pesquisarComViewOtimizada(Query query) throws SQLException {
		String nome = query.toString();
		adicionaQueryParaExecucao(nome, query);
		Map<String, List> mapaDeResultados = pesquisarQueriesComViewOtimizada();
		return mapaDeResultados.get(nome);
	}

	public Map<String, List> pesquisarQueriesComViewOtimizada() throws HibernateException, SQLException {
		SessionImplementor sessao = (SessionImplementor) session;
		//Transaction transacao = session.beginTransaction();
		executaPackageDePartes(sessao);
		Map<String, List> mapaDeResultados = new HashMap<String, List>();
		for (String chave : getMapaDeQueries().keySet()) {
			Query query = getMapaDeQueries().get(chave);
			mapaDeResultados.put(chave, query.list());
		}
		//transacao.commit();
		return mapaDeResultados;
	}

	private void executaPackageDePartes(SessionImplementor sessao) throws SQLException {
		CallableStatement stmt = sessao.getBatcher().prepareCallableStatement(
				"{call JUDICIARIO.PKG_INCIDENTE.PRC_JURISDICIONADO_INCIDENTE(?,?,?,?,?)}");
		stmt.setObject(1, null);
		stmt.setObject(2, null);
		stmt.setObject(3, null);
		stmt.setString(4, nomeParte);
		stmt.setObject(5, codigoCategoriaParte);
		stmt.execute();
		sessao.getBatcher().closeStatement(stmt);
	}

}
