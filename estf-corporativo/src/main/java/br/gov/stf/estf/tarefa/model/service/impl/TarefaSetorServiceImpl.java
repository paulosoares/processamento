package br.gov.stf.estf.tarefa.model.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.tarefa.CampoTarefaValor;
import br.gov.stf.estf.entidade.tarefa.Contato;
import br.gov.stf.estf.entidade.tarefa.RelatorioAnaliticoTarefaSetor;
import br.gov.stf.estf.entidade.tarefa.TarefaSetor;
import br.gov.stf.estf.entidade.tarefa.TipoAtribuicaoTarefa;
import br.gov.stf.estf.tarefa.model.dataaccess.TarefaSetorDao;
import br.gov.stf.estf.tarefa.model.service.TarefaSetorService;
import br.gov.stf.estf.tarefa.model.util.TarefaSetorSearchData;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("tarefaSetorService")
public class TarefaSetorServiceImpl extends GenericServiceImpl<TarefaSetor, Long, TarefaSetorDao> implements TarefaSetorService {

	public TarefaSetorServiceImpl(TarefaSetorDao dao) {
		super(dao);
	}

	/**
	 * @deprecated Utilizar o método {@link #pesquisarQuantidadeTarefaSetor(TarefaSetorSearchData)} ao invés deste.
	 */
	@Deprecated
	@Override
	public Long pesquisarQuantidadeTarefaSetor(Long codigo, String Descricao, String classeProcessual, Long numeroProcesso, Long idSetorOrigem, Long idSetorDestino,
			Long idTipoTarefa, Long prioridade, Long idTipoSituacaoTarefa, Date dataCriacaoInicial, Date dataCriacaoFinal, Date dataInicioPrevistoInicial,
			Date dataInicioPrevistoFinal, Date dataFimPrevistoInicial, Date dataFimPrevistoFinal, Date dataInicioInicial, Date dataInicioFim, Date dataFimInicial,
			Date dataFimFinal, Date dataTipoCampoTarefaInicial, Date dataTipoCampoTarefaFinal, Long idTipoCampoTarefaPeriodo, String sigUsuario, Boolean urgente, Boolean sigiloso,
			Boolean iniciado, Boolean finalizado, Boolean semCampoTarefa, List<CampoTarefaValor> listaCampoTarefa, Boolean semTarefaSetor) throws ServiceException {
		Long result = null;
		try {
			result = dao.pesquisarQuantidadeTarefaSetor(codigo, Descricao, classeProcessual, numeroProcesso, idSetorOrigem, idSetorDestino, idTipoTarefa, prioridade,
					idTipoSituacaoTarefa, dataCriacaoInicial, dataCriacaoFinal, dataInicioPrevistoInicial, dataInicioPrevistoFinal, dataFimPrevistoInicial, dataFimPrevistoFinal,
					dataInicioInicial, dataInicioFim, dataFimInicial, dataFimFinal, dataTipoCampoTarefaInicial, dataTipoCampoTarefaFinal, idTipoCampoTarefaPeriodo, sigUsuario,
					urgente, sigiloso, iniciado, finalizado, semCampoTarefa, listaCampoTarefa, semTarefaSetor);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return result;
	}

	@Override
	public Long pesquisarQuantidadeTarefaSetor(TarefaSetorSearchData searchData) throws ServiceException {
		Long result = null;
		try {
			result = dao.pesquisarQuantidadeTarefaSetor(searchData);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return result;
	}

	/**
	 * @deprecated Utilizar o método {@link #pesquisarTarefaSetor(TarefaSetorSearchData)} ao invés deste.
	 */
	@Deprecated
	@Override
	public List<TarefaSetor> pesquisarTarefaSetor(Long codigo, String Descricao, String classeProcessual, Long numeroProcesso, Long idSetorOrigem, Long idSetorDestino,
			Long idTipoTarefa, Long prioridade, Long idTipoSituacaoTarefa, Date dataCriacaoInicial, Date dataCriacaoFinal, Date dataInicioPrevistoInicial,
			Date dataInicioPrevistoFinal, Date dataFimPrevistoInicial, Date dataFimPrevistoFinal, Date dataInicioInicial, Date dataInicioFim, Date dataFimInicial,
			Date dataFimFinal, Date dataTipoCampoTarefaInicial, Date dataTipoCampoTarefaFinal, Long idTipoCampoTarefaPeriodo, String sigUsuario, Boolean urgente, Boolean sigiloso,
			Boolean iniciado, Boolean finalizado, Boolean semCampoTarefa, Boolean limitarPesquisa, List<CampoTarefaValor> listaCampoTarefa, Boolean semTarefaSetor,
			Boolean readOnlyQuery) throws ServiceException {

		List<TarefaSetor> tarefas = null;
		try {
			tarefas = dao.pesquisarTarefaSetor(codigo, Descricao, classeProcessual, numeroProcesso, idSetorOrigem, idSetorDestino, idTipoTarefa, prioridade, idTipoSituacaoTarefa,
					dataCriacaoInicial, dataCriacaoFinal, dataInicioPrevistoInicial, dataInicioPrevistoFinal, dataFimPrevistoInicial, dataFimPrevistoFinal, dataInicioInicial,
					dataInicioFim, dataFimInicial, dataFimFinal, dataTipoCampoTarefaInicial, dataTipoCampoTarefaFinal, idTipoCampoTarefaPeriodo, sigUsuario, urgente, sigiloso,
					iniciado, finalizado, semCampoTarefa, limitarPesquisa, listaCampoTarefa, semTarefaSetor, readOnlyQuery);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return tarefas;
	}

	@Override
	public List<TarefaSetor> pesquisarTarefaSetor(TarefaSetorSearchData searchData) throws ServiceException {

		List<TarefaSetor> tarefas = null;
		try {
			tarefas = dao.pesquisarTarefaSetor(searchData);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return tarefas;
	}

	/**
	 * @deprecated Utilizar o método {@link #pesquisarTarefaSetorRelatorioSqlNativo(TarefaSetorSearchData)} ao invés deste.
	 */
	@Deprecated
	@Override
	public List<RelatorioAnaliticoTarefaSetor> pesquisarTarefaSetorRelatorioSqlNativo(Long codigo, String Descricao, String classeProcessual, Long numeroProcesso,
			Long idSetorOrigem, Long idSetorDestino, Long idTipoTarefa, Long prioridade, Long idTipoSituacaoTarefa, Date dataCriacaoInicial, Date dataCriacaoFinal,
			Date dataInicioPrevistoInicial, Date dataInicioPrevistoFinal, Date dataFimPrevistoInicial, Date dataFimPrevistoFinal, Date dataInicioInicial, Date dataInicioFim,
			Date dataFimInicial, Date dataFimFinal, Date dataTipoCampoTarefaInicial, Date dataTipoCampoTarefaFinal, Long idTipoCampoTarefaPeriodo, String sigUsuario,
			Boolean urgente, Boolean sigiloso, Boolean iniciado, Boolean finalizado, Boolean semCampoTarefa, List<CampoTarefaValor> listaCampoTarefa,
			List<RelatorioAnaliticoTarefaSetor.ValorCampoTipoTarefaRelatorio> listaValorCampoTipoTarefa, Boolean semTarefaSetor) throws ServiceException {

		List<RelatorioAnaliticoTarefaSetor> lista = null;

		try {
			lista = dao.pesquisarTarefaSetorRelatorioSqlNativo(codigo, Descricao, classeProcessual, numeroProcesso, idSetorOrigem, idSetorDestino, idTipoTarefa, prioridade,
					idTipoSituacaoTarefa, dataCriacaoInicial, dataCriacaoFinal, dataInicioPrevistoInicial, dataInicioPrevistoFinal, dataFimPrevistoInicial, dataFimPrevistoFinal,
					dataInicioInicial, dataInicioFim, dataFimInicial, dataFimFinal, dataTipoCampoTarefaInicial, dataTipoCampoTarefaFinal, idTipoCampoTarefaPeriodo, sigUsuario,
					urgente, sigiloso, iniciado, finalizado, semCampoTarefa, listaCampoTarefa, listaValorCampoTipoTarefa, semTarefaSetor);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return lista;
	}

	@Override
	public List<RelatorioAnaliticoTarefaSetor> pesquisarTarefaSetorRelatorioSqlNativo(TarefaSetorSearchData searchData) throws ServiceException {

		List<RelatorioAnaliticoTarefaSetor> lista = null;

		try {
			lista = dao.pesquisarTarefaSetorRelatorioSqlNativo(searchData);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return lista;
	}

	@Override
	public TarefaSetor recuperarTarefaSetor(Long id, String classeProcessual, Long numeroProcesso, Long codidoSetor) throws ServiceException {
		try {
			return dao.recuperarTarefaSetor(id, classeProcessual, numeroProcesso, codidoSetor);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<TipoAtribuicaoTarefa> pesquisarTipoAtribuicaoTarefa(Long id, String sigla, String descricao) throws ServiceException {
		List<TipoAtribuicaoTarefa> lista = null;
		try {
			lista = dao.pesquisarTipoAtribuicaoTarefa(id, sigla, descricao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return lista;
	}

	@Override
	public Boolean persistirTarefaSetor(TarefaSetor tarefaSetor) throws ServiceException {

		Boolean alterado = null;
		try {

			alterado = dao.persistirTarefaSetor(tarefaSetor);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return alterado;
	}

	@Override
	public Boolean excluirTarefaSetor(TarefaSetor tarefaSetor) throws ServiceException {
		Boolean excluido = null;
		try {

			excluido = dao.excluirTarefaSetor(tarefaSetor);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return excluido;

	}

	@Override
	public List<CampoTarefaValor> pesquisarCampoTarefaValor(Long id, Long idTarefaSetor, Long idTipoCampoTarefa, String classeProcessual, Long numeroProcesso,
			Boolean repercussaoFinalizadao) throws ServiceException {

		try {

			return dao.pesquisarCampoTarefaValor(id, idTarefaSetor, idTipoCampoTarefa, classeProcessual, numeroProcesso, repercussaoFinalizadao);

		} catch (DaoException e) {
			throw new ServiceException(e);

		}

	}

	@Override
	public TarefaSetor recuperarTarefaSetor(List<CampoTarefaValor> listaContato) throws ServiceException {

		try {

			return dao.recuperarTarefaSetor(listaContato);

		} catch (DaoException e) {
			throw new ServiceException(e);

		}

	}

	@Override
	public Boolean persistirCampoTarefaValor(CampoTarefaValor campoTarefaValor) throws ServiceException {
		try {

			return dao.persistirCampoTarefaValor(campoTarefaValor);

		} catch (DaoException e) {
			throw new ServiceException(e);

		}
	}

	@Override
	public List<Contato> pesquisarContato(Long id, Long tipoTarefaSetor, Long idTipoCampoTarefa, String valorCampo, Long idSetor) throws ServiceException {

		try {

			return dao.pesquisarContato(id, tipoTarefaSetor, idTipoCampoTarefa, valorCampo, idSetor);

		} catch (DaoException e) {
			throw new ServiceException(e);

		}

	}

}