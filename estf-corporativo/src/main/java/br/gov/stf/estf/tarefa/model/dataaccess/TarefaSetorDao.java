package br.gov.stf.estf.tarefa.model.dataaccess;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.tarefa.CampoTarefaValor;
import br.gov.stf.estf.entidade.tarefa.Contato;
import br.gov.stf.estf.entidade.tarefa.RelatorioAnaliticoTarefaSetor;
import br.gov.stf.estf.entidade.tarefa.TarefaSetor;
import br.gov.stf.estf.entidade.tarefa.TipoAtribuicaoTarefa;
import br.gov.stf.estf.tarefa.model.util.TarefaSetorSearchData;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TarefaSetorDao extends GenericDao<TarefaSetor, Long> {

	public TarefaSetor recuperarTarefaSetor(Long id, String classeProcessual, Long numeroProcesso, Long codidoSetor) throws DaoException;

	/**
	 * @deprecated Utilizar o método {@link #pesquisarTarefaSetor(TarefaSetorSearchData)} ao invés deste.
	 */
	@Deprecated
	public List<TarefaSetor> pesquisarTarefaSetor(Long codigo, String Descricao, String classeProcessual, Long numeroProcesso, Long idSetorOrigem, Long idSetorDestino,
			Long idTipoTarefa, Long prioridade, Long idTipoSituacaoTarefa, Date dataCriacaoInicial, Date dataCriacaoFinal, Date dataInicioPrevistoInicial,
			Date dataInicioPrevistoFinal, Date dataFimPrevistoInicial, Date dataFimPrevistoFinal, Date dataInicioInicial, Date dataInicioFim, Date dataFimInicial,
			Date dataFimFinal, Date dataTipoCampoTarefaInicial, Date dataTipoCampoTarefaFinal, Long idTipoCampoTarefaPeriodo, String sigUsuario, Boolean urgente, Boolean sigiloso,
			Boolean iniciado, Boolean finalizado, Boolean semCampoTarefa, Boolean limitarPesquisa, List<CampoTarefaValor> listaCampoTarefa, Boolean semTarefaSetor,
			Boolean readOnlyQuery) throws DaoException;

	public List<TarefaSetor> pesquisarTarefaSetor(TarefaSetorSearchData searchData) throws DaoException;

	/**
	 * @deprecated Utilizar o método {@link #pesquisarQuantidadeTarefaSetor(TarefaSetorSearchData)} ao invés deste.
	 */
	@Deprecated
	public Long pesquisarQuantidadeTarefaSetor(Long codigo, String Descricao, String classeProcessual, Long numeroProcesso, Long idSetorOrigem, Long idSetorDestino,
			Long idTipoTarefa, Long prioridade, Long idTipoSituacaoTarefa, Date dataCriacaoInicial, Date dataCriacaoFinal, Date dataInicioPrevistoInicial,
			Date dataInicioPrevistoFinal, Date dataFimPrevistoInicial, Date dataFimPrevistoFinal, Date dataInicioInicial, Date dataInicioFim, Date dataFimInicial,
			Date dataFimFinal, Date dataTipoCampoTarefaInicial, Date dataTipoCampoTarefaFinal, Long idTipoCampoTarefaPeriodo, String sigUsuario, Boolean urgente, Boolean sigiloso,
			Boolean iniciado, Boolean finalizado, Boolean semCampoTarefa, List<CampoTarefaValor> listaCampoTarefa, Boolean semTarefaSetor) throws DaoException;

	public Long pesquisarQuantidadeTarefaSetor(TarefaSetorSearchData searchData) throws DaoException;

	/**
	 * @deprecated Utilizar o método {@link #pesquisarTarefaSetorRelatorioSqlNativo(TarefaSetorSearchData)} ao invés deste.
	 */
	@Deprecated
	public List<RelatorioAnaliticoTarefaSetor> pesquisarTarefaSetorRelatorioSqlNativo(Long codigo, String Descricao, String classeProcessual, Long numeroProcesso,
			Long idSetorOrigem, Long idSetorDestino, Long idTipoTarefa, Long prioridade, Long idTipoSituacaoTarefa, Date dataCriacaoInicial, Date dataCriacaoFinal,
			Date dataInicioPrevistoInicial, Date dataInicioPrevistoFinal, Date dataFimPrevistoInicial, Date dataFimPrevistoFinal, Date dataInicioInicial, Date dataInicioFim,
			Date dataFimInicial, Date dataFimFinal, Date dataTipoCampoTarefaInicial, Date dataTipoCampoTarefaFinal, Long idTipoCampoTarefaPeriodo, String sigUsuario,
			Boolean urgente, Boolean sigiloso, Boolean iniciado, Boolean finalizado, Boolean semCampoTarefa, List<CampoTarefaValor> listaCampoTarefa,
			List<RelatorioAnaliticoTarefaSetor.ValorCampoTipoTarefaRelatorio> listaValorCampoTipoTarefa, Boolean semTarefaSetor) throws DaoException;

	public List<RelatorioAnaliticoTarefaSetor> pesquisarTarefaSetorRelatorioSqlNativo(TarefaSetorSearchData searchData) throws DaoException;

	public Boolean persistirTarefaSetor(TarefaSetor tarefaSetor) throws DaoException;

	public Boolean excluirTarefaSetor(TarefaSetor tarefaSetor) throws DaoException;

	public List<TipoAtribuicaoTarefa> pesquisarTipoAtribuicaoTarefa(Long id, String sigla, String descricao) throws DaoException;

	public List<CampoTarefaValor> pesquisarCampoTarefaValor(Long id, Long idTarefaSetor, Long idTipoCampoTarefa, String classeProcessual, Long numeroProcesso,
			Boolean repercussaoFinalizada) throws DaoException;

	public TarefaSetor recuperarTarefaSetor(List<CampoTarefaValor> listaContato) throws DaoException;

	public List<Contato> pesquisarContato(Long id, Long tipoTarefaSetor, Long idTipoCampoTarefa, String valorCampo, Long idSetor) throws DaoException;

	public Boolean persistirCampoTarefaValor(CampoTarefaValor campoTarefaValor) throws DaoException;

}
