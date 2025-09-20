package br.gov.stf.estf.tarefa.model.service;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.tarefa.CampoTarefaValor;
import br.gov.stf.estf.entidade.tarefa.Contato;
import br.gov.stf.estf.entidade.tarefa.RelatorioAnaliticoTarefaSetor;
import br.gov.stf.estf.entidade.tarefa.TarefaSetor;
import br.gov.stf.estf.entidade.tarefa.TipoAtribuicaoTarefa;
import br.gov.stf.estf.tarefa.model.dataaccess.TarefaSetorDao;
import br.gov.stf.estf.tarefa.model.util.TarefaSetorSearchData;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface TarefaSetorService extends GenericService<TarefaSetor, Long, TarefaSetorDao> {

	public TarefaSetor recuperarTarefaSetor(Long id, String classeProcessual, Long numeroProcesso, Long codidoSetor) throws ServiceException;

	/**
	 * @deprecated Utilizar o método {@link #pesquisarTarefaSetor(TarefaSetorSearchData)} ao invés deste.
	 */
	@Deprecated
	public List<TarefaSetor> pesquisarTarefaSetor(Long codigo, String Descricao, String classeProcessual, Long numeroProcesso, Long idSetorOrigem, Long idSetorDestino,
			Long idTipoTarefa, Long prioridade, Long idTipoSituacaoTarefa, Date dataCriacaoInicial, Date dataCriacaoFinal, Date dataInicioPrevistoInicial,
			Date dataInicioPrevistoFinal, Date dataFimPrevistoInicial, Date dataFimPrevistoFinal, Date dataInicioInicial, Date dataInicioFim, Date dataFimInicial,
			Date dataFimFinal, Date dataTipoCampoTarefaInicial, Date dataTipoCampoTarefaFinal, Long idTipoCampoTarefaPeriodo, String sigUsuario, Boolean urgente, Boolean sigiloso,
			Boolean iniciado, Boolean finalizado, Boolean semCampoTarefa, Boolean limitarPesquisa, List<CampoTarefaValor> listaCampoTarefa, Boolean semTarefaSetor,
			Boolean readOnlyQuery) throws ServiceException;

	public List<TarefaSetor> pesquisarTarefaSetor(TarefaSetorSearchData searchData) throws ServiceException;

	/**
	 * @deprecated Utilizar o método {@link #pesquisarQuantidadeTarefaSetor(TarefaSetorSearchData)} ao invés deste.
	 */
	@Deprecated
	public Long pesquisarQuantidadeTarefaSetor(Long codigo, String Descricao, String classeProcessual, Long numeroProcesso, Long idSetorOrigem, Long idSetorDestino,
			Long idTipoTarefa, Long prioridade, Long idTipoSituacaoTarefa, Date dataCriacaoInicial, Date dataCriacaoFinal, Date dataInicioPrevistoInicial,
			Date dataInicioPrevistoFinal, Date dataFimPrevistoInicial, Date dataFimPrevistoFinal, Date dataInicioInicial, Date dataInicioFim, Date dataFimInicial,
			Date dataFimFinal, Date dataTipoCampoTarefaInicial, Date dataTipoCampoTarefaFinal, Long idTipoCampoTarefaPeriodo, String sigUsuario, Boolean urgente, Boolean sigiloso,
			Boolean iniciado, Boolean finalizado, Boolean semCampoTarefa, List<CampoTarefaValor> listaCampoTarefa, Boolean semTarefaSetor) throws ServiceException;

	public Long pesquisarQuantidadeTarefaSetor(TarefaSetorSearchData searchData) throws ServiceException;

	/**
	 * @deprecated Utilizar o método {@link #pesquisarTarefaSetorRelatorioSqlNativo(TarefaSetorSearchData)} ao invés deste.
	 */
	@Deprecated
	public List<RelatorioAnaliticoTarefaSetor> pesquisarTarefaSetorRelatorioSqlNativo(Long codigo, String Descricao, String classeProcessual, Long numeroProcesso,
			Long idSetorOrigem, Long idSetorDestino, Long idTipoTarefa, Long prioridade, Long idTipoSituacaoTarefa, Date dataCriacaoInicial, Date dataCriacaoFinal,
			Date dataInicioPrevistoInicial, Date dataInicioPrevistoFinal, Date dataFimPrevistoInicial, Date dataFimPrevistoFinal, Date dataInicioInicial, Date dataInicioFim,
			Date dataFimInicial, Date dataFimFinal, Date dataTipoCampoTarefaInicial, Date dataTipoCampoTarefaFinal, Long idTipoCampoTarefaPeriodo, String sigUsuario,
			Boolean urgente, Boolean sigiloso, Boolean iniciado, Boolean finalizado, Boolean semCampoTarefa, List<CampoTarefaValor> listaCampoTarefa,
			List<RelatorioAnaliticoTarefaSetor.ValorCampoTipoTarefaRelatorio> listaValorCampoTipoTarefa, Boolean semTarefaSetor) throws ServiceException;

	public List<RelatorioAnaliticoTarefaSetor> pesquisarTarefaSetorRelatorioSqlNativo(TarefaSetorSearchData searchData) throws ServiceException;

	public Boolean persistirTarefaSetor(TarefaSetor tarefaSetor) throws ServiceException;

	public Boolean excluirTarefaSetor(TarefaSetor tarefaSetor) throws ServiceException;

	public List<TipoAtribuicaoTarefa> pesquisarTipoAtribuicaoTarefa(Long id, String sigla, String descricao) throws ServiceException;

	public List<CampoTarefaValor> pesquisarCampoTarefaValor(Long id, Long idTarefaSetor, Long idTipoCampoTarefa, String classeProcessual, Long numeroProcesso,
			Boolean repercussaoFinalizada) throws ServiceException;

	public TarefaSetor recuperarTarefaSetor(List<CampoTarefaValor> listaContato) throws ServiceException;

	public Boolean persistirCampoTarefaValor(CampoTarefaValor campoTarefaValor) throws ServiceException;

	public List<Contato> pesquisarContato(Long id, Long tipoTarefaSetor, Long idTipoCampoTarefa, String valorCampo, Long idSetor) throws ServiceException;

}
