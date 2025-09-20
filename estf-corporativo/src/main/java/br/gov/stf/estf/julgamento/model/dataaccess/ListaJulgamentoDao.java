
package br.gov.stf.estf.julgamento.model.dataaccess;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.julgamento.Colegiado.TipoColegiadoConstante;
import br.gov.stf.estf.entidade.julgamento.JulgamentoProcesso;
import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.julgamento.model.util.ListaJulgamentoSearchData;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;
import br.gov.stf.framework.util.SearchResult;

/**
 * @author Paulo.Estevao
 * @since
 */
public interface ListaJulgamentoDao extends GenericDao<ListaJulgamento, Long> {

	List<ListaJulgamento> pesquisar(ObjetoIncidente<?> objetoIncidente) throws DaoException;
	
	List<ListaJulgamento> pesquisar(ObjetoIncidente<?> objetoIncidente, boolean pesquisarApenasListasPrevistasParaJulgamento) throws DaoException;

	Integer recuperarMaiorOrdemSessao(Sessao sessao) throws DaoException;

	List<ListaJulgamento> pesquisar(Ministro ministro) throws DaoException;

	List<ListaJulgamento> pesquisarPorColegiado(TipoColegiadoConstante colegiado) throws DaoException;

	Long consultaQuantidadeListasSemSessao(TipoColegiadoConstante colegiado) throws DaoException;

	Integer recuperarMaiorOrdemSessaoMinistro(Sessao sessao, Ministro ministro) throws DaoException;
	
	Integer recuperarMaiorOrdemSessaoMinistroListaJulgamentoAno(Ministro ministro, short ano) throws DaoException ;

	void atualizarOrdenacaoListas(ListaJulgamento listaJulgamentoBase,
			Integer ordemDestino) throws DaoException;

	void refresh(ListaJulgamento listaJulgamento) throws DaoException;
	
	SearchResult pesquisarListaJulgamentoPlenarioVirtual(ListaJulgamentoSearchData searchData) 
			throws DaoException;
	
	List<ListaJulgamento> pesquisarListasDeJulgamentoPorDataInicioSessao(ListaJulgamento listaJulgamentoExample, Date dataInicio, Date dataFim);
	
	public ListaJulgamento pesquisar(ObjetoIncidente<?> objetoIncidente, JulgamentoProcesso julgamentoProcesso) throws DaoException;

	List<ListaJulgamento> pesquisarListaPorObjetoIncidenteSessao(ObjetoIncidente<?> objetoIncidente, String colegiado)
			throws DaoException;

	Long obterQuantidadeRascunhosPorMinistro(ListaJulgamento lista, Ministro ministro) throws DaoException;
	
}