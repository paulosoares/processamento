package br.gov.stf.estf.julgamento.model.dataaccess;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.julgamento.JulgamentoProcesso;
import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoAmbienteConstante;
import br.gov.stf.estf.entidade.julgamento.TipoSituacaoProcessoSessao;
import br.gov.stf.estf.entidade.julgamento.VotoJulgamentoProcesso;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProcessoImagem;
import br.gov.stf.estf.julgamento.model.util.JulgamentoProcessoSearchData;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;
import br.gov.stf.framework.util.SearchResult;

public interface JulgamentoProcessoDao extends GenericDao<JulgamentoProcesso, Long> {

	public List<JulgamentoProcesso> pesquisarProcessosSessao( Long idSessao ) throws DaoException;
	
	public JulgamentoProcesso recuperarJulgamentoProcesso(Long id,  Long idObjetoIncidente,
			TipoAmbienteConstante tipoAmbienteSessao, String colegiado)throws DaoException;
	
	public SearchResult<JulgamentoProcesso> pesquisarJulgamentoProcesso(JulgamentoProcessoSearchData searchData)throws DaoException;
	
	public List<JulgamentoProcesso> pesquisarJulgamentoProcesso(Long idOpjetoIncidentePrincipal, Long idMinistro, String ... sigTipoRecursos)throws DaoException;
	
	public List<JulgamentoProcesso> pesquisarJulgamentoProcessoRG(Long idOpjetoIncidentePrincipal, Long idMinistro, String ... sigTipoRecursos)throws DaoException;
	
	public List<ProcessoImagem> recuperarInteiroTeor(Long idObjetoIncidente, Processo processo) throws DaoException;
	
	public List<ProcessoImagem> recuperarInteiroTeorRG(Long idObjetoIncidente, Processo processo) throws DaoException;

	public JulgamentoProcesso recuperar(ObjetoIncidente<?> objetoIncidente, Sessao sessao) throws DaoException;
	/**
	 * Recupera os registros de julgamentoProcesso de um objetoIncidente em uma sessão.
	 * @param objetoIncidente
	 * @param sessao
	 * @param dataBase
	 * @return
	 * @throws DaoException
	 */
	public JulgamentoProcesso recuperar(ObjetoIncidente<?> objetoIncidente,
			Sessao sessao, Date dataBase) throws DaoException;

	/**
	 * Pesquisa registro de julgamentoProcesso de um processo de sessões diferentes da sessão passada como parâmetro. 
	 * @param objetoIncidente
	 * @param sessao
	 * @return
	 * @throws DaoException
	 */
	public List<JulgamentoProcesso> pesquisarJulgamentoProcessoNaoJulgado(ObjetoIncidente<?> objetoIncidente, Sessao sessao) throws DaoException;

	public JulgamentoProcesso pesquisaSessaoNaoFinalizada(
			ObjetoIncidente<?> objetoIncidente,
			TipoAmbienteConstante tipoAmbiente) throws DaoException;

	public Integer recuperarUltimaOrdemSessao(Sessao sessao) throws DaoException;

	public void atualizarOrdenacaoProcessos(
			JulgamentoProcesso julgamentoProcessoBase, Integer ordemDestino) throws DaoException;

	void refresh(JulgamentoProcesso julgamentoProcesso) throws DaoException;
	
	public List<JulgamentoProcesso> pesquisarJulgamentoProcessoListaJulgamento(ListaJulgamento listaJulgamento) throws DaoException;

	public JulgamentoProcesso clonarJulgamentoProcesso(Long julgamentoProcessoId, Long idSessao, Boolean clonarRascunhos) throws DaoException;

	List<VotoJulgamentoProcesso> recuperarVotosProcesso(Long julgamentoProcessoId) throws DaoException;

	public JulgamentoProcesso pesquisaUltimoJulgamentoProcesso(ObjetoIncidente<?> oi, Ministro ministroDestaque, TipoSituacaoProcessoSessao situacaoProcessoSessao, Boolean destaqueCancelado) throws DaoException;
}
