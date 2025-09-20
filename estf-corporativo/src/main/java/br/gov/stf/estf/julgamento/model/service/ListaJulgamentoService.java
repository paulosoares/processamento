/**
 * 
 */
package br.gov.stf.estf.julgamento.model.service;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.julgamento.Colegiado.TipoColegiadoConstante;
import br.gov.stf.estf.entidade.julgamento.JulgamentoProcesso;
import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.ProcessoListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.julgamento.model.dataaccess.ListaJulgamentoDao;
import br.gov.stf.estf.julgamento.model.util.ListaJulgamentoSearchData;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.util.SearchResult;


/**
 * @author Paulo.Estevao
 * @since 09.06.2011
 */
public interface ListaJulgamentoService extends GenericService<ListaJulgamento, Long, ListaJulgamentoDao>{

	List<ListaJulgamento> pesquisar(ObjetoIncidente<?> objetoIncidente) throws ServiceException;
	
	ListaJulgamento pesquisar(ObjetoIncidente<?> objetoIncidente, JulgamentoProcesso julgamentoProcesso) throws ServiceException;

	Integer recuperarMaiorOrdemSessao(Sessao sessao) throws ServiceException;

	List<ListaJulgamento> pesquisar(ObjetoIncidente<?> objetoIncidente, boolean pesquisarApenasListasPrevistasParaJulgamento) throws ServiceException;

	List<ListaJulgamento> pesquisar(Ministro ministro) throws ServiceException;
	
	List<ListaJulgamento> pesquisarPorColegiado(TipoColegiadoConstante colegiado) throws ServiceException;

	Long consultaQuantidadeListasSemSessao(TipoColegiadoConstante colegiado) throws ServiceException;

	void definirSessaoJulgamento(ListaJulgamento listaJulgamento, Sessao sessao) throws ServiceException;

	Integer recuperarMaiorOrdemSessaoMinistro(Sessao sessao, Ministro ministro) throws ServiceException;
	
	Integer recuperarMaiorOrdemSessaoMinistroListaJulgamentoAno(Ministro ministro, short ano) throws ServiceException;

	void reordenarListas(ListaJulgamento listaJulgamentoBase, Integer ordemDestino) throws ServiceException;

	void excluir(ListaJulgamento listaJulgamento) throws ServiceException;
	
	SearchResult<ListaJulgamento> pesquisarListaJulgamentoPlenarioVirtual(ListaJulgamentoSearchData searchData) throws ServiceException;
	
	ConteudoPublicacao publicarProcesso(ListaJulgamento listaJulgamento, List<ProcessoListaJulgamento> processosPublicacao, 
			Usuario usuario, Setor setor, Long ministroParaRelatorAcordaoRA, String motivoDesignacaoRelatorAcordao) throws ServiceException;
	
	public void registrarResultadoJulgamento(List<ListaJulgamento> listasJulgamento) throws ServiceException;

	List<ListaJulgamento> pesquisarListasDeJulgamentoPorDataInicioSessao(ListaJulgamento listaJulgamentoExample, Date dataInicio, Date dataFim);
	
	public byte[] gerarRelatorioExcel(List<ListaJulgamento> dadosRelatorio, String descricaoPesquisa, String caminhoArquivoModelo) throws ServiceException;
	
	public byte[] gerarRelatorioExcelProcesso(List<ListaJulgamento> dadosRelatorio, String descricaoPesquisa, String caminhoArquivoModelo,  Ministro ministro) throws ServiceException;
	
	public String montarDecisaoListaPublicacaoManual(ListaJulgamento listaJulgamento) throws ServiceException;

	List<ListaJulgamento> pesquisarListaPorObjetoIncidenteSessao(ObjetoIncidente<?> objetoIncidente, String colegiado) throws ServiceException;

	Long obterQuantidadeRascunhosPorMinistro(ListaJulgamento lista, Ministro ministro) throws ServiceException;

	public ListaJulgamento carregarListaJulgamentoParaPlenarioVirtual(ListaJulgamento listaJulgamento) throws ServiceException;

} 