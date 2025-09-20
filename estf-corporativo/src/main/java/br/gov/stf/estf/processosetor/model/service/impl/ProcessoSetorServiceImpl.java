package br.gov.stf.estf.processosetor.model.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.localizacao.SecaoSetor;
import br.gov.stf.estf.entidade.localizacao.TipoFaseSetor;
import br.gov.stf.estf.entidade.processosetor.EstatisticaProcessoSetor;
import br.gov.stf.estf.entidade.processosetor.ProcessoSetor;
import br.gov.stf.estf.entidade.processosetor.RelatorioAnaliticoProcessoSetor;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.Classe;
import br.gov.stf.estf.entidade.processostf.ClasseConversao;
import br.gov.stf.estf.processosetor.model.dataaccess.ProcessoSetorDao;
import br.gov.stf.estf.processosetor.model.service.ProcessoSetorService;
import br.gov.stf.estf.processosetor.model.util.ProcessoSetorSearchData;
import br.gov.stf.estf.processosetor.model.util.ProcessoSetorUtil;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;
import br.gov.stf.framework.util.SearchData.DateRange;
import br.gov.stf.framework.util.SearchResult;

@Service("processoSetorService")
public class ProcessoSetorServiceImpl extends GenericServiceImpl<ProcessoSetor, Long, ProcessoSetorDao> implements ProcessoSetorService {

	public ProcessoSetorServiceImpl(ProcessoSetorDao dao) {
		super(dao);
	}

	public ProcessoSetor recuperarProcessoSetor(String sigla, Long numero, Short recurso, Long idSetor) throws ServiceException {

		ProcessoSetor processo = null;

		try {

			processo = dao.recuperarProcessoSetor(sigla, numero, recurso, idSetor);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return processo;
	}

	public ProcessoSetor recuperarProcessoSetor(Long numeroProtocolo, Short anoProtocolo, Long idSetor) throws ServiceException {

		ProcessoSetor processo = null;

		try {

			processo = dao.recuperarProcessoSetor(numeroProtocolo, anoProtocolo, idSetor);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return processo;
	}

	@SuppressWarnings("unchecked")
	@Deprecated
	public Long pesquisarQuantidadeProcessoSetor(Short anoProtocolo, Long numeroProtocolo, String siglasClassesProcessuaisAgrupadas, String sigla, Long numeroProcesso,
			Short recurso, Boolean possuiRecurso, String siglaRecursoUnificada, String siglaTipoJulgamento, String codigoTipoMeioProcesso, Long numeroPeticao,
			String codigoAssunto, String descricaoAssunto, String complementoAssunto, Long codigoMinistroRelator, String numeroSala, String numeroArmario, String numeroEstante,
			String numeroPrateleira, String numeroColuna, String obsDeslocamento, Boolean pesquisarAssuntoEmTodosNiveis, Boolean pesquisarInicio, Long idSecaoUltimoDeslocamento,
			String siglaUsuarioDistribuicao, Long idGrupoProcessoSetor, Date dataDistribuicaoMinistroInicial, Date dataDistribuicaoMinistroFinal, Date dataDistribuicaoInicial,
			Date dataDistribuicaoFinal, Date dataFaseInicial, Date dataFaseFinal, Date dataRemessaInicial, Date dataRemessaFinal, Date dataRecebimentoInicial,
			Date dataRecebimentoFinal, Date dataEntradaInicial, Date dataEntradaFinal, Date dataSaidaInicial, Date dataSaidaFinal, Long idSetor, Long idTipoUltimaFaseSetor,
			Long idTipoUltimoStatusSetor, Boolean faseAtualProcesso, Boolean repercussaoGeralCheckbox, Boolean protocoloNaoAutuadoCheckbox, Boolean semLocalizacao,
			Boolean semFase, Boolean semDistribuicao, Boolean semVista, Long idCategoriaPartePesquisa, String nomeParte, Long idTipoTarefa, Boolean localizadosNoSetor,
			Boolean emTramiteNoSetor, Boolean possuiLiminar, Boolean possuiPreferencia, Boolean sobrestado, Boolean julgado, Boolean mostraProcessoReautuadoRejeitadoCheckbox,
			List<Andamento> listaIncluirAndamentos,	Date dataInicialIncluirAndamentos, Date dataFinalIncluirAndamentos, List<Andamento> listaNaoIncluirAndamentos,
			Date dataInicialNaoIncluirAndamentos, Date dataFinalNaoIncluirAndamentos) throws ServiceException {

		Long quantidade = null;
		SearchResult srProcessoSetor = null;
		ProcessoSetorSearchData pssd = null;

		try {
			/*
			 * quantidade = dao.pesquisarQuantidadeProcessoSetor( anoProtocolo, numeroProtocolo, siglasClassesProcessuaisAgrupadas, sigla, numeroProcesso,
			 * recurso, possuiRecurso, siglaRecursoUnificada, siglaTipoJulgamento, codigoTipoMeioProcesso, numeroPeticao, codigoAssunto, descricaoAssunto,
			 * complementoAssunto, codigoMinistroRelator, numeroSala, numeroArmario, numeroEstante, numeroPrateleira, numeroColuna, obsDeslocamento,
			 * pesquisarAssuntoEmTodosNiveis, pesquisarInicio, idSecaoUltimoDeslocamento, siglaUsuarioDistribuicao, idGrupoProcessoSetor,
			 * dataDistribuicaoMinistroInicial, dataDistribuicaoMinistroFinal, dataDistribuicaoInicial, dataDistribuicaoFinal, dataFaseInicial, dataFaseFinal,
			 * dataRemessaInicial, dataRemessaFinal, dataRecebimentoInicial, dataRecebimentoFinal, dataEntradaInicial, dataEntradaFinal, dataSaidaInicial,
			 * dataSaidaFinal, idSetor, idTipoUltimaFaseSetor, idTipoUltimoStatusSetor,faseAtualProcesso, repercussaoGeralCheckbox, protocoloNaoAutuadoCheckbox,
			 * semLocalizacao, semFase, semDistribuicao, semVista, idCategoriaPartePesquisa, nomeParte, idTipoTarefa, localizadosNoSetor, emTramiteNoSetor,
			 * possuiLiminar, possuiPreferencia, sobrestado,julgado, listaIncluirAndamentos, dataInicialIncluirAndamentos, dataFinalIncluirAndamentos,
			 * listaNaoIncluirAndamentos, dataInicialNaoIncluirAndamentos, dataFinalNaoIncluirAndamentos);
			 */

			pssd = converterParametros(anoProtocolo, numeroProtocolo, siglasClassesProcessuaisAgrupadas, sigla, numeroProcesso, recurso, possuiRecurso, siglaRecursoUnificada,
					siglaTipoJulgamento, codigoTipoMeioProcesso, numeroPeticao, codigoAssunto, descricaoAssunto, complementoAssunto, codigoMinistroRelator, numeroSala,
					numeroArmario, numeroEstante, numeroPrateleira, numeroColuna, obsDeslocamento, pesquisarAssuntoEmTodosNiveis, pesquisarInicio, idSecaoUltimoDeslocamento,
					siglaUsuarioDistribuicao, idGrupoProcessoSetor, dataDistribuicaoMinistroInicial, dataDistribuicaoMinistroFinal, dataDistribuicaoInicial, dataDistribuicaoFinal,
					dataFaseInicial, dataFaseFinal, dataRemessaInicial, dataRemessaFinal, dataRecebimentoInicial, dataRecebimentoFinal, dataEntradaInicial, dataEntradaFinal,
					dataSaidaInicial, dataSaidaFinal, idSetor, idTipoUltimaFaseSetor, idTipoUltimoStatusSetor, faseAtualProcesso, repercussaoGeralCheckbox,
					protocoloNaoAutuadoCheckbox, mostraProcessoReautuadoRejeitadoCheckbox, semLocalizacao, semFase, semDistribuicao, semVista, idCategoriaPartePesquisa, nomeParte, idTipoTarefa, localizadosNoSetor,
					emTramiteNoSetor, possuiLiminar, possuiPreferencia, sobrestado, julgado, listaIncluirAndamentos, dataInicialIncluirAndamentos, dataFinalIncluirAndamentos,
					listaNaoIncluirAndamentos, dataInicialNaoIncluirAndamentos, dataFinalNaoIncluirAndamentos);
			pssd.setHasOnlyCount(Boolean.TRUE);
			srProcessoSetor = dao.pesquisarProcessoSetor(pssd);

			if (srProcessoSetor != null && srProcessoSetor.getTotalResult() > 0)
				quantidade = (Long) srProcessoSetor.getTotalResult();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return quantidade;
	}

	@Override
	public Long pesquisarQuantidadeProcessoSetor(ProcessoSetorSearchData searchData) throws ServiceException {
		Long quantidade = null;
		SearchResult srProcessoSetor = null;

		try {
			srProcessoSetor = dao.pesquisarProcessoSetor(searchData);
			if (srProcessoSetor != null && srProcessoSetor.getTotalResult() > 0)
				quantidade = (Long) srProcessoSetor.getTotalResult();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return quantidade;
	}

	@SuppressWarnings("unchecked")
	public List<ProcessoSetor> pesquisarProcessoSetor(Short anoProtocolo, Long numeroProtocolo, String siglasClassesProcessuaisAgrupadas, String sigla, Long numeroProcesso,
			Short recurso, Boolean possuiRecurso, String siglaRecursoUnificada, String siglaTipoJulgamento, String codigoTipoMeioProcesso, Long numeroPeticao,
			String codigoAssunto, String descricaoAssunto, String complementoAssunto, Long codigoMinistroRelator, String numeroSala, String numeroArmario, String numeroEstante,
			String numeroPrateleira, String numeroColuna, String obsDeslocamento, Boolean pesquisarAssuntoEmTodosNiveis, Boolean pesquisarInicio, Long idSecaoUltimoDeslocamento,
			String siglaUsuarioDistribuicao, Long idGrupoProcessoSetor, Date dataDistribuicaoMinistroInicial, Date dataDistribuicaoMinistroFinal, Date dataDistribuicaoInicial,
			Date dataDistribuicaoFinal, Date dataFaseInicial, Date dataFaseFinal, Date dataRemessaInicial, Date dataRemessaFinal, Date dataRecebimentoInicial,
			Date dataRecebimentoFinal, Date dataEntradaInicial, Date dataEntradaFinal, Date dataSaidaInicial, Date dataSaidaFinal, Long idSetor, Long idTipoUltimaFaseSetor,
			Long idTipoUltimoStatusSetor, Boolean faseAtualProcesso, Boolean repercussaoGeralCheckbox, Boolean protocoloNaoAutuadoCheckbox,
			Boolean mostraProcessoReautuadoRejeitadoCheckbox, Boolean semLocalizacao, Boolean semFase, Boolean semDistribuicao, Boolean semVista, Long idCategoriaPartePesquisa,
			String nomeParte, Long idTipoTarefa, Boolean localizadosNoSetor, Boolean emTramiteNoSetor, Boolean possuiLiminar, Boolean possuiPreferencia, Boolean sobrestado,
			Boolean julgado, Boolean preFetchAssunto, Boolean readOnlyQuery, Boolean limitarPesquisa, Boolean orderByProcesso, Boolean orderByProtocolo, Boolean orderByValorGut,
			Boolean orderByDataEntrada, Boolean orderByAssunto,	Boolean orderByCrescente, List<Andamento> listaIncluirAndamentos, Date dataInicialIncluirAndamentos,
			Date dataFinalIncluirAndamentos, List<Andamento> listaNaoIncluirAndamentos, Date dataInicialNaoIncluirAndamentos, Date dataFinalNaoIncluirAndamentos)
			throws ServiceException {

		List<ProcessoSetor> listaProcessoSetor = null;
		SearchResult srProcessoSetor = null;
		ProcessoSetorSearchData pssd = null;

		try {
			/*
			 * listaProcessoSetor = dao.pesquisarProcessoSetor(anoProtocolo, numeroProtocolo, siglasClassesProcessuaisAgrupadas, sigla, numeroProcesso, recurso,
			 * possuiRecurso, siglaRecursoUnificada, siglaTipoJulgamento, codigoTipoMeioProcesso, numeroPeticao, codigoAssunto, descricaoAssunto,
			 * complementoAssunto, codigoMinistroRelator, numeroSala, numeroArmario, numeroEstante, numeroPrateleira, numeroColuna, obsDeslocamento,
			 * pesquisarAssuntoEmTodosNiveis, pesquisarInicio, idSecaoUltimoDeslocamento, siglaUsuarioDistribuicao, idGrupoProcessoSetor,
			 * dataDistribuicaoMinistroInicial, dataDistribuicaoMinistroFinal, dataDistribuicaoInicial, dataDistribuicaoFinal, dataFaseInicial, dataFaseFinal,
			 * dataRemessaInicial, dataRemessaFinal, dataRecebimentoInicial, dataRecebimentoFinal, dataEntradaInicial, dataEntradaFinal, dataSaidaInicial,
			 * dataSaidaFinal, idSetor, idTipoUltimaFaseSetor, idTipoUltimoStatusSetor, faseAtualProcesso, repercussaoGeralCheckbox,
			 * protocoloNaoAutuadoCheckbox, semLocalizacao, semFase, semDistribuicao, semVista, idCategoriaPartePesquisa, nomeParte, idTipoTarefa,
			 * localizadosNoSetor, emTramiteNoSetor, possuiLiminar, possuiPreferencia, sobrestado,julgado, preFetchAssunto, readOnlyQuery,
			 * limitarPesquisa,orderByProcesso,orderByProtocolo,orderByValorGut,orderByDataEntrada, orderByAssunto, orderByCrescente, listaIncluirAndamentos,
			 * dataInicialIncluirAndamentos, dataFinalIncluirAndamentos, listaNaoIncluirAndamentos, dataInicialNaoIncluirAndamentos,
			 * dataFinalNaoIncluirAndamentos);
			 */

			pssd = converterParametros(anoProtocolo, numeroProtocolo, siglasClassesProcessuaisAgrupadas, sigla, numeroProcesso, recurso, possuiRecurso, siglaRecursoUnificada,
					siglaTipoJulgamento, codigoTipoMeioProcesso, numeroPeticao, codigoAssunto, descricaoAssunto, complementoAssunto, codigoMinistroRelator, numeroSala,
					numeroArmario, numeroEstante, numeroPrateleira, numeroColuna, obsDeslocamento, pesquisarAssuntoEmTodosNiveis, pesquisarInicio, idSecaoUltimoDeslocamento,
					siglaUsuarioDistribuicao, idGrupoProcessoSetor, dataDistribuicaoMinistroInicial, dataDistribuicaoMinistroFinal, dataDistribuicaoInicial, dataDistribuicaoFinal,
					dataFaseInicial, dataFaseFinal, dataRemessaInicial, dataRemessaFinal, dataRecebimentoInicial, dataRecebimentoFinal, dataEntradaInicial, dataEntradaFinal,
					dataSaidaInicial, dataSaidaFinal, idSetor, idTipoUltimaFaseSetor, idTipoUltimoStatusSetor, faseAtualProcesso, repercussaoGeralCheckbox,
					protocoloNaoAutuadoCheckbox, mostraProcessoReautuadoRejeitadoCheckbox, semLocalizacao, semFase, semDistribuicao, semVista, idCategoriaPartePesquisa, nomeParte, idTipoTarefa, localizadosNoSetor,
					emTramiteNoSetor, possuiLiminar, possuiPreferencia, sobrestado, julgado, listaIncluirAndamentos, dataInicialIncluirAndamentos, dataFinalIncluirAndamentos,
					listaNaoIncluirAndamentos, dataInicialNaoIncluirAndamentos, dataFinalNaoIncluirAndamentos);
			pssd.setHasOnlyCount(Boolean.FALSE);
			srProcessoSetor = dao.pesquisarProcessoSetor(pssd);

			if (srProcessoSetor != null && srProcessoSetor.getResultCollection() != null)
				listaProcessoSetor = (List<ProcessoSetor>) srProcessoSetor.getResultCollection();

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return listaProcessoSetor;
	}

	public List<EstatisticaProcessoSetor> pesquisarProcessoSetor(Short anoProtocolo, Long numeroProtocolo, String siglasClassesProcessuaisAgrupadas, String sigla,
			Long numeroProcesso, Short recurso, Boolean possuiRecurso, String siglaRecursoUnificada, String siglaTipoJulgamento, String codigoTipoMeioProcesso, Long numeroPeticao,
			String codigoAssunto, String descricaoAssunto, String complementoAssunto, Long codigoMinistroRelator, String numeroSala, String numeroArmario, String numeroEstante,
			String numeroPrateleira, String numeroColuna, String obsDeslocamento, Boolean pesquisarAssuntoEmTodosNiveis, Boolean pesquisarInicio, Long idSecaoUltimoDeslocamento,
			String siglaUsuarioDistribuicao, Long idGrupoProcessoSetor, Date dataDistribuicaoMinistroInicial, Date dataDistribuicaoMinistroFinal, Date dataDistribuicaoInicial,
			Date dataDistribuicaoFinal, Date dataFaseInicial, Date dataFaseFinal, Date dataRemessaInicial, Date dataRemessaFinal, Date dataRecebimentoInicial,
			Date dataRecebimentoFinal, Date dataEntradaInicial, Date dataEntradaFinal, Date dataSaidaInicial, Date dataSaidaFinal, Long idSetor, Long idTipoUltimaFaseSetor,
			Long idTipoUltimoStatusSetor, Boolean faseAtualProcesso, Boolean repercussaoGeralCheckbox, Boolean protocoloNaoAutuadoCheckbox, Boolean semLocalizacao,
			Boolean semFase, Boolean semDistribuicao, Boolean semVista, Long idCategoriaPartePesquisa, String nomeParte, Long idTipoTarefa, Boolean localizadosNoSetor,
			Boolean emTramiteNoSetor, Boolean possuiLiminar, Boolean possuiPreferencia, Boolean sobrestado, Boolean julgado, List<Andamento> listaIncluirAndamentos,
			Date dataInicialIncluirAndamentos, Date dataFinalIncluirAndamentos, List<Andamento> listaNaoIncluirAndamentos, Date dataInicialNaoIncluirAndamentos,
			Date dataFinalNaoIncluirAndamentos, Boolean groupByFase, Boolean groupByFaseStatus, Boolean groupByDistribuicao, Boolean groupByDeslocamento, Boolean groupByAssunto)
			throws ServiceException {

		List<EstatisticaProcessoSetor> listaProcessoSetor = null;

		try {
			listaProcessoSetor = dao.pesquisarProcessoSetor(anoProtocolo, numeroProtocolo, siglasClassesProcessuaisAgrupadas, sigla, numeroProcesso, recurso, possuiRecurso,
					siglaRecursoUnificada, siglaTipoJulgamento, codigoTipoMeioProcesso, numeroPeticao, codigoAssunto, descricaoAssunto, complementoAssunto, codigoMinistroRelator,
					numeroSala, numeroArmario, numeroEstante, numeroPrateleira, numeroColuna, obsDeslocamento, pesquisarAssuntoEmTodosNiveis, pesquisarInicio,
					idSecaoUltimoDeslocamento, siglaUsuarioDistribuicao, idGrupoProcessoSetor, dataDistribuicaoMinistroInicial, dataDistribuicaoMinistroFinal,
					dataDistribuicaoInicial, dataDistribuicaoFinal, dataFaseInicial, dataFaseFinal, dataRemessaInicial, dataRemessaFinal, dataRecebimentoInicial,
					dataRecebimentoFinal, dataEntradaInicial, dataEntradaFinal, dataSaidaInicial, dataSaidaFinal, idSetor, idTipoUltimaFaseSetor, idTipoUltimoStatusSetor,
					faseAtualProcesso, repercussaoGeralCheckbox, protocoloNaoAutuadoCheckbox, semLocalizacao, semFase, semDistribuicao, semVista, idCategoriaPartePesquisa,
					nomeParte, idTipoTarefa, localizadosNoSetor, emTramiteNoSetor, possuiLiminar, possuiPreferencia, sobrestado, julgado, listaIncluirAndamentos,
					dataInicialIncluirAndamentos, dataFinalIncluirAndamentos, listaNaoIncluirAndamentos, dataInicialNaoIncluirAndamentos, dataFinalNaoIncluirAndamentos,
					groupByFase, groupByFaseStatus, groupByDistribuicao, groupByDeslocamento, groupByAssunto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return listaProcessoSetor;
	}

	public List<RelatorioAnaliticoProcessoSetor> pesquisarRelatorioAnaliticoProcessoSetor(Short anoProtocolo, Long numeroProtocolo, String siglasClassesProcessuaisAgrupadas,
			String sigla, Long numeroProcesso, Short recurso, Boolean possuiRecurso, String siglaRecursoUnificada, String siglaTipoJulgamento, String codigoTipoMeioProcesso,
			Long numeroPeticao, String codigoAssunto, String descricaoAssunto, String complementoAssunto, Long codigoMinistroRelator, String numeroSala, String numeroArmario,
			String numeroEstante, String numeroPrateleira, String numeroColuna, String obsDeslocamento, Boolean pesquisarAssuntoEmTodosNiveis, Boolean pesquisarInicio,
			Long idSecaoUltimoDeslocamento, String siglaUsuarioDistribuicao, Long idGrupoProcessoSetor, Date dataDistribuicaoMinistroInicial, Date dataDistribuicaoMinistroFinal,
			Date dataDistribuicaoInicial, Date dataDistribuicaoFinal, Date dataFaseInicial, Date dataFaseFinal, Date dataRemessaInicial, Date dataRemessaFinal,
			Date dataRecebimentoInicial, Date dataRecebimentoFinal, Date dataEntradaInicial, Date dataEntradaFinal, Date dataSaidaInicial, Date dataSaidaFinal, Long idSetor,
			Long idTipoUltimaFaseSetor, Long idTipoUltimoStatusSetor, Boolean faseProcessualAtual, Boolean repercussaoGeralCheckbox, Boolean protocoloNaoAutuadoCheckbox,
			Boolean semLocalizacao, Boolean semFase, Boolean semDistribuicao, Boolean semVista, Long idCategoriaPartePesquisa, String nomeParte, Long idTipoTarefa,
			Boolean localizadosNoSetor, Boolean emTramiteNoSetor, Boolean possuiLiminar, Boolean possuiPreferencia, Boolean sobrestado, Boolean julgado, Boolean preFetchAssunto,
			Boolean readOnlyQuery, Boolean limitarPesquisa, String tipoRelatorio, Boolean groupByFase, Boolean groupByFaseStatus, Boolean groupByDistribuicao,
			Boolean groupByDeslocamento, Boolean groupByAssunto, Boolean orderByProcesso, Boolean orderByProtocolo, Boolean orderByValorGut, Boolean orderByDataEntrada,
			Boolean orderByAssunto, Boolean orderByCrescente, List<Andamento> listaIncluirAndamentos, Date dataInicialIncluirAndamentos, Date dataFinalIncluirAndamentos,
			List<Andamento> listaNaoIncluirAndamentos, Date dataInicialNaoIncluirAndamentos, Date dataFinalNaoIncluirAndamentos) throws ServiceException {

		List<RelatorioAnaliticoProcessoSetor> lista = null;

		try {

			lista = dao.pesquisarRelatorioAnaliticoProcessoSetor(anoProtocolo, numeroProtocolo, siglasClassesProcessuaisAgrupadas, sigla, numeroProcesso, recurso, possuiRecurso,
					siglaRecursoUnificada, siglaTipoJulgamento, codigoTipoMeioProcesso, numeroPeticao, codigoAssunto, descricaoAssunto, complementoAssunto, codigoMinistroRelator,
					numeroSala, numeroArmario, numeroEstante, numeroPrateleira, numeroColuna, obsDeslocamento, pesquisarAssuntoEmTodosNiveis, pesquisarInicio,
					idSecaoUltimoDeslocamento, siglaUsuarioDistribuicao, idGrupoProcessoSetor, dataDistribuicaoMinistroInicial, dataDistribuicaoMinistroFinal,
					dataDistribuicaoInicial, dataDistribuicaoFinal, dataFaseInicial, dataFaseFinal, dataRemessaInicial, dataRemessaFinal, dataRecebimentoInicial,
					dataRecebimentoFinal, dataEntradaInicial, dataEntradaFinal, dataSaidaInicial, dataSaidaFinal, idSetor, idTipoUltimaFaseSetor, idTipoUltimoStatusSetor,
					faseProcessualAtual, repercussaoGeralCheckbox, protocoloNaoAutuadoCheckbox, semLocalizacao, semFase, semDistribuicao, semVista, idCategoriaPartePesquisa,
					nomeParte, idTipoTarefa, localizadosNoSetor, emTramiteNoSetor, possuiLiminar, possuiPreferencia, sobrestado, julgado, preFetchAssunto, readOnlyQuery,
					limitarPesquisa, tipoRelatorio, groupByFase, groupByFaseStatus, groupByDistribuicao, groupByDeslocamento, groupByAssunto, orderByProcesso, orderByProtocolo,
					orderByValorGut, orderByDataEntrada, orderByAssunto, orderByCrescente, listaIncluirAndamentos, dataInicialIncluirAndamentos, dataFinalIncluirAndamentos,
					listaNaoIncluirAndamentos, dataInicialNaoIncluirAndamentos, dataFinalNaoIncluirAndamentos);

		} catch (DaoException ex) {
			throw new ServiceException(ex);
		}

		return lista;
	}

	public List<EstatisticaProcessoSetor> pesquisarRelatorioSinteticoProcessoSetor(Short anoProtocolo, Long numeroProtocolo, String siglasClassesProcessuaisAgrupadas,
			String sigla, Long numeroProcesso, Short recurso, Boolean possuiRecurso, String siglaRecursoUnificada, String siglaTipoJulgamento, String codigoTipoMeioProcesso,
			Long numeroPeticao, String codigoAssunto, String descricaoAssunto, String complementoAssunto, Long codigoMinistroRelator, String numeroSala, String numeroArmario,
			String numeroEstante, String numeroPrateleira, String numeroColuna, String obsDeslocamento, Boolean pesquisarAssuntoEmTodosNiveis, Boolean pesquisarInicio,
			Long idSecaoUltimoDeslocamento, String siglaUsuarioDistribuicao, Long idGrupoProcessoSetor, Date dataDistribuicaoMinistroInicial, Date dataDistribuicaoMinistroFinal,
			Date dataDistribuicaoInicial, Date dataDistribuicaoFinal, Date dataFaseInicial, Date dataFaseFinal, Date dataRemessaInicial, Date dataRemessaFinal,
			Date dataRecebimentoInicial, Date dataRecebimentoFinal, Date dataEntradaInicial, Date dataEntradaFinal, Date dataSaidaInicial, Date dataSaidaFinal, Long idSetor,
			Long idTipoUltimaFaseSetor, Long idTipoUltimoStatusSetor, Boolean faseProcessualAtual, Boolean repercussaoGeralCheckbox, Boolean protocoloNaoAutuadoCheckbox,
			Boolean semLocalizacao, Boolean semFase, Boolean semDistribuicao, Boolean semVista, Long idCategoriaPartePesquisa, String nomeParte, Long idTipoTarefa,
			Boolean localizadosNoSetor, Boolean emTramiteNoSetor, Boolean possuiLiminar, Boolean possuiPreferencia, Boolean sobrestado, Boolean julgado, Boolean preFetchAssunto,
			Boolean readOnlyQuery, Boolean limitarPesquisa, String tipoRelatorio, Boolean groupByFase, Boolean groupByFaseStatus, Boolean groupByDistribuicao,
			Boolean groupByDeslocamento, Boolean groupByAssunto, Boolean orderByProcesso, Boolean orderByProtocolo, Boolean orderByValorGut, Boolean orderByDataEntrada,
			Boolean orderByAssunto, Boolean orderByCrescente, List<Andamento> listaIncluirAndamentos, Date dataInicialIncluirAndamentos, Date dataFinalIncluirAndamentos,
			List<Andamento> listaNaoIncluirAndamentos, Date dataInicialNaoIncluirAndamentos, Date dataFinalNaoIncluirAndamentos) throws ServiceException {

		List<EstatisticaProcessoSetor> lista = null;

		try {

			lista = dao.pesquisarRelatorioSinteticoProcessoSetor(anoProtocolo, numeroProtocolo, siglasClassesProcessuaisAgrupadas, sigla, numeroProcesso, recurso, possuiRecurso,
					siglaRecursoUnificada, siglaTipoJulgamento, codigoTipoMeioProcesso, numeroPeticao, codigoAssunto, descricaoAssunto, complementoAssunto, codigoMinistroRelator,
					numeroSala, numeroArmario, numeroEstante, numeroPrateleira, numeroColuna, obsDeslocamento, pesquisarAssuntoEmTodosNiveis, pesquisarInicio,
					idSecaoUltimoDeslocamento, siglaUsuarioDistribuicao, idGrupoProcessoSetor, dataDistribuicaoMinistroInicial, dataDistribuicaoMinistroFinal,
					dataDistribuicaoInicial, dataDistribuicaoFinal, dataFaseInicial, dataFaseFinal, dataRemessaInicial, dataRemessaFinal, dataRecebimentoInicial,
					dataRecebimentoFinal, dataEntradaInicial, dataEntradaFinal, dataSaidaInicial, dataSaidaFinal, idSetor, idTipoUltimaFaseSetor, idTipoUltimoStatusSetor,
					faseProcessualAtual, repercussaoGeralCheckbox, protocoloNaoAutuadoCheckbox, semLocalizacao, semFase, semDistribuicao, semVista, idCategoriaPartePesquisa,
					nomeParte, idTipoTarefa, localizadosNoSetor, emTramiteNoSetor, possuiLiminar, possuiPreferencia, sobrestado, julgado, preFetchAssunto, readOnlyQuery,
					limitarPesquisa, tipoRelatorio, groupByFase, groupByFaseStatus, groupByDistribuicao, groupByDeslocamento, groupByAssunto, orderByProcesso, orderByProtocolo,
					orderByValorGut, orderByDataEntrada, orderByAssunto, orderByCrescente, listaIncluirAndamentos, dataInicialIncluirAndamentos, dataFinalIncluirAndamentos,
					listaNaoIncluirAndamentos, dataInicialNaoIncluirAndamentos, dataFinalNaoIncluirAndamentos);

		} catch (DaoException ex) {
			throw new ServiceException(ex);
		}

		return lista;
	}

	public Boolean alterarProcessoSetor(ProcessoSetor processoSetor) throws ServiceException {

		Boolean alterado = Boolean.FALSE;

		try {

			alterado = dao.alterarProcessoSetor(processoSetor);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return alterado;

	}

	public Boolean persistirProcessoSetor(ProcessoSetor processoSetor) throws ServiceException {

		Boolean alterado = Boolean.FALSE;

		try {

			alterado = dao.persistirProcessoSetor(processoSetor);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return alterado;

	}

	/**
	 * Método responsável por recuperar os protocolos associados a determinado usuário. A associação é dada pela distribuição "interna" que pode ser realizada
	 * de forma manual ou automática.
	 * 
	 * @param siglaUsuario
	 *            usuário
	 * @return List<ProcessoSetor>
	 * @throws ServiceException
	 * @since 1.0
	 * @athor Thiagom
	 */
	public List<ProcessoSetor> pesquisarProcessoSetor(String siglaUsuario) throws ServiceException {
		List<ProcessoSetor> processos = null;
		try {
			processos = dao.pesquisarProcessoSetor(siglaUsuario);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return processos;
	}

	public Boolean isProcessoProtocoloPosseUsuario(Long idProcessoSetor, String siglaUsuario, Long idSetorUsuario) throws ServiceException {

		if (idProcessoSetor == null)
			throw new NullPointerException("Identificação do processo setor nula");

		if (siglaUsuario == null)
			throw new NullPointerException("Sigla do usuário nula");

		if (idSetorUsuario == null)
			throw new NullPointerException("Identificação do setor de alocação do usuário nula");

		Boolean isPosseUsuario = null;

		try {
			isPosseUsuario = dao.isProcessoProtocoloPosseUsuario(idProcessoSetor, siglaUsuario, idSetorUsuario);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return isPosseUsuario;
	}

	public List<ProcessoSetor> pesquisarProcessoSetor(String sigla, Long idSetor) throws ServiceException {

		List<ProcessoSetor> lista = null;

		try {
			if (idSetor == null) {
				throw new ServiceException("O codigo do setor de pesquisa está nulo");
			}

			lista = dao.pesquisarProcessoSetor(sigla, idSetor);

		} catch (DaoException ex) {
			throw new ServiceException(ex);
		}
		return lista;
	}

	public List<EstatisticaProcessoSetor> pesquisarProcessoSetorEstatistica(String sigla, Long idSetor) throws ServiceException {

		List<EstatisticaProcessoSetor> lista = null;

		try {
			if (idSetor == null) {
				throw new ServiceException("O codigo do setor de pesquisa está nulo");
			}

			lista = dao.pesquisarProcessoSetorEstatistica(sigla, idSetor);

		} catch (DaoException ex) {
			throw new ServiceException(ex);
		}
		return lista;
	}

	public List<RelatorioAnaliticoProcessoSetor> pesquisarRelatorioAnaliticoProcessoSetor(ProcessoSetorSearchData pssd) throws ServiceException {

		List<RelatorioAnaliticoProcessoSetor> lista = null;

		try {

			lista = dao.pesquisarRelatorioAnaliticoProcessoSetor(pssd);

		} catch (DaoException ex) {
			throw new ServiceException(ex);
		}

		return lista;
	}

	public List<EstatisticaProcessoSetor> pesquisarRelatorioSinteticoProcessoSetor(ProcessoSetorSearchData pssd) throws ServiceException {

		List<EstatisticaProcessoSetor> lista = null;

		try {

			lista = dao.pesquisarRelatorioSinteticoProcessoSetor(pssd);

		} catch (DaoException ex) {
			throw new ServiceException(ex);
		}

		return lista;
	}

	public List<ProcessoSetor> pesquisarProcessoSetor(TipoFaseSetor faseSetor) throws ServiceException {
		List<ProcessoSetor> listaProcessos = new LinkedList<ProcessoSetor>();

		try {

			listaProcessos = dao.pesquisarProcessoSetor(faseSetor);

		} catch (DaoException ex) {
			throw new ServiceException(ex);
		}
		return listaProcessos;
	}

	public List<ProcessoSetor> pesquisarProcessoSetor(SecaoSetor secaoSetor, Long idSetor) throws ServiceException {
		List<ProcessoSetor> listaProcessos = new LinkedList<ProcessoSetor>();

		try {

			listaProcessos = dao.pesquisarProcessoSetor(secaoSetor, idSetor);

		} catch (DaoException ex) {
			throw new ServiceException(ex);
		}
		return listaProcessos;
	}

	@SuppressWarnings("unchecked")
	public SearchResult pesquisarProcessoSetor(ProcessoSetorSearchData sdProcessoSetor) throws ServiceException {
		try {

			return dao.pesquisarProcessoSetor(sdProcessoSetor);

		} catch (DaoException ex) {
			throw new ServiceException(ex);
		}

	}

	public ProcessoSetor recuperarProcessoSetor(Long seqObjetoIncidente, Long idSetor) throws ServiceException {
		try {
			return dao.recuperarProcessoSetor(seqObjetoIncidente, idSetor);
		} catch (DaoException ex) {
			throw new ServiceException(ex);
		}
	}
	
	public List<ProcessoSetor> recuperarListaProcessoSetor(Long seqObjetoIncidente, Long idSetor) throws ServiceException {
		try {
			return dao.recuperarListaProcessoSetor(seqObjetoIncidente, idSetor);
		} catch (DaoException ex) {
			throw new ServiceException(ex);
		}
	}

	public List<String> pesquisarComplementoAssunto(String complementoAssunto, Long idSetor, Short numeroMaximoDeResultados) throws ServiceException {
		try {
			return dao.pesquisarComplementoAssunto(complementoAssunto, idSetor, numeroMaximoDeResultados);
		} catch (DaoException ex) {
			throw new ServiceException(ex);
		}
	}

	public Integer pesquisarQuantidadeComplementoAssunto(String complementoAssunto, Long idSetor) throws ServiceException {
		try {
			return dao.pesquisarQuantidadeComplementoAssunto(complementoAssunto, idSetor);
		} catch (DaoException ex) {
			throw new ServiceException(ex);
		}
	}

	public ProcessoSetorSearchData converterParametros(Short anoProtocolo, Long numeroProtocolo, String siglasClassesProcessuaisAgrupadas, String sigla, Long numeroProcesso,
			Short recurso, Boolean possuiRecurso, String siglaRecursoUnificada, String siglaTipoJulgamento, String codigoTipoMeioProcesso, Long numeroPeticao,
			String codigoAssunto, String descricaoAssunto, String complementoAssunto, Long codigoMinistroRelator, String numeroSala, String numeroArmario, String numeroEstante,
			String numeroPrateleira, String numeroColuna, String obsDeslocamento, Boolean pesquisarAssuntoEmTodosNiveis, Boolean pesquisarInicio, Long idSecaoUltimoDeslocamento,
			String siglaUsuarioDistribuicao, Long idGrupoProcessoSetor, Date dataDistribuicaoMinistroInicial, Date dataDistribuicaoMinistroFinal, Date dataDistribuicaoInicial,
			Date dataDistribuicaoFinal, Date dataFaseInicial, Date dataFaseFinal, Date dataRemessaInicial, Date dataRemessaFinal, Date dataRecebimentoInicial,
			Date dataRecebimentoFinal, Date dataEntradaInicial, Date dataEntradaFinal, Date dataSaidaInicial, Date dataSaidaFinal, Long idSetor, Long idTipoUltimaFaseSetor,
			Long idTipoUltimoStatusSetor, Boolean faseAtualProcesso, Boolean repercussaoGeralCheckbox, Boolean protocoloNaoAutuadoCheckbox,
			Boolean mostraProcessoReautuadoRejeitadoCheckbox, Boolean semLocalizacao, Boolean semFase, Boolean semDistribuicao, Boolean semVista,
			Long idCategoriaPartePesquisa, String nomeParte, Long idTipoTarefa,	Boolean localizadosNoSetor,	Boolean emTramiteNoSetor, Boolean possuiLiminar,
			Boolean possuiPreferencia, Boolean sobrestado, Boolean julgado,	List<Andamento> listaIncluirAndamentos,	Date dataInicialIncluirAndamentos, Date dataFinalIncluirAndamentos,
			List<Andamento> listaNaoIncluirAndamentos, Date dataInicialNaoIncluirAndamentos, Date dataFinalNaoIncluirAndamentos)

	{
		ProcessoSetorSearchData pssd = new ProcessoSetorSearchData();
		pssd.anoProtocolo = anoProtocolo;
		pssd.numeroProtocolo = numeroProtocolo;
		pssd.siglasClassesProcessuaisAgrupadas = siglasClassesProcessuaisAgrupadas;
		pssd.sigla = sigla;
		pssd.numeroProcesso = numeroProcesso;
		pssd.recurso = recurso;
		pssd.possuiRecurso = possuiRecurso;
		pssd.siglaRecursoUnificada = siglaRecursoUnificada;
		pssd.siglaTipoJulgamento = siglaTipoJulgamento;
		pssd.codigoTipoMeioProcesso = codigoTipoMeioProcesso;
		pssd.numeroSala = numeroSala;
		pssd.numeroArmario = numeroArmario;
		pssd.numeroEstante = numeroEstante;
		pssd.numeroPrateleira = numeroPrateleira;
		pssd.numeroColuna = numeroColuna;
		pssd.obsDeslocamento = obsDeslocamento;
		pssd.idSecaoUltimoDeslocamento = idSecaoUltimoDeslocamento;
		pssd.siglaUsuarioDistribuicao = siglaUsuarioDistribuicao;
		pssd.idGrupoProcessoSetor = idGrupoProcessoSetor;
		pssd.dataDistribuicaoMinistro = new DateRange(dataDistribuicaoMinistroInicial, dataDistribuicaoMinistroFinal);
		pssd.dataDistribuicao = new DateRange(dataDistribuicaoInicial, dataDistribuicaoFinal);
		pssd.dataFase = new DateRange(dataFaseInicial, dataFaseFinal);
		pssd.dataRemessa = new DateRange(dataRemessaInicial, dataRemessaFinal);
		pssd.dataRecebimento = new DateRange(dataRecebimentoInicial, dataRecebimentoFinal);
		pssd.dataEntrada = new DateRange(dataEntradaInicial, dataEntradaFinal);
		pssd.dataSaida = new DateRange(dataSaidaInicial, dataSaidaFinal);
		pssd.idSetor = idSetor;
		pssd.idTipoUltimaFaseSetor = idTipoUltimaFaseSetor;
		pssd.idTipoUltimoStatusSetor = idTipoUltimoStatusSetor;
		pssd.faseProcessualAtual = faseAtualProcesso;
		pssd.repercussaoGeral = repercussaoGeralCheckbox;
		pssd.protocoloNaoAutuado = protocoloNaoAutuadoCheckbox;
		pssd.mostraProcessoReautuadoRejeitado = mostraProcessoReautuadoRejeitadoCheckbox;
		pssd.semLocalizacao = semLocalizacao;
		pssd.semFase = semFase;
		pssd.semDistribuicao = semDistribuicao;
		pssd.semVista = semVista;
		pssd.idCategoriaPartePesquisa = idCategoriaPartePesquisa;
		pssd.nomeParte = nomeParte;
		pssd.idTipoTarefa = idTipoTarefa;
		pssd.localizadosNoSetor = localizadosNoSetor;
		pssd.emTramiteNoSetor = emTramiteNoSetor;
		pssd.possuiLiminar = possuiLiminar;
		pssd.possuiPreferencia = possuiPreferencia;
		pssd.sobrestado = sobrestado;
		pssd.julgado = julgado;
		pssd.listaIncluirAndamentos = recuperaListaTipoAndamento(listaIncluirAndamentos);
		pssd.dataIncluirAndamentos = new DateRange(dataInicialIncluirAndamentos, dataFinalIncluirAndamentos);
		pssd.listaNaoIncluirAndamentos = recuperaListaTipoAndamento(listaNaoIncluirAndamentos);
		pssd.dataNaoIncluirAndamentos = new DateRange(dataInicialNaoIncluirAndamentos, dataFinalNaoIncluirAndamentos);

		return pssd;
	}

	@SuppressWarnings("unchecked")
	public SearchResult pesquisarProcessoSetorCanetaOtica(ProcessoSetorSearchData sdProcessoSetor, List<Classe> listaClasse, List<ClasseConversao> listaClasseAntiga)
			throws ServiceException {
		Set<String> arrayProcesso = new HashSet<String>();
		SearchResult sr = new SearchResult();
		SearchResult srTemp = null;
		
		for (String item : sdProcessoSetor.canetaOtica.split(";"))
			arrayProcesso.add(item);
		
		ProcessoSetorUtil psUtil = new ProcessoSetorUtil();

		try {
			for (String processoSetorTemp : arrayProcesso) {
				if (psUtil.possuiLetra(processoSetorTemp)) {
					sdProcessoSetor.sigla = (String) psUtil.recuperaIndentificacaoProcesso(processoSetorTemp, false, listaClasse, listaClasseAntiga);
					sdProcessoSetor.numeroProcesso = (Long) psUtil.recuperaIndentificacaoProcesso(processoSetorTemp, true, listaClasse, listaClasseAntiga);
				} else {
					sdProcessoSetor.numeroProtocolo = (Long) psUtil.recuperaIndentificacaoProtocolo(processoSetorTemp, true);
					sdProcessoSetor.anoProtocolo = (Short) psUtil.recuperaIndentificacaoProtocolo(processoSetorTemp, false);
				}

				srTemp = dao.pesquisarProcessoSetor(sdProcessoSetor);
				sr = adicionarDadosSearchResult(srTemp, sr);
			}
		} catch (DaoException ex) {
			throw new ServiceException(ex);
		}

		return sr;
	}

	@SuppressWarnings("unchecked")
	public SearchResult adicionarDadosSearchResult(SearchResult srNovo, SearchResult srAntigo) {
		SearchResult srTemp = new SearchResult();
		Collection lista = new LinkedList();

		if (srNovo == null)
			srNovo = new SearchResult();

		if (srAntigo == null)
			srAntigo = new SearchResult();

		if (srNovo != null && srNovo.getResultCollection() != null && srNovo.getResultCollection().size() > 0)
			lista.addAll(srNovo.getResultCollection());

		if (srAntigo != null && srAntigo.getResultCollection() != null && srAntigo.getResultCollection().size() > 0)
			lista.addAll(srAntigo.getResultCollection());

		srTemp.setTotalResult(lista.size());
		srTemp.setResultCollection(lista);
		srTemp.setSearchData(srNovo.getSearchData());
		srTemp.setLastResult(lista.size());

		return srTemp;
	}

	public List<Long> recuperaListaTipoAndamento(List<Andamento> lista) {
		if (lista != null && lista.size() > 0) {
			List<Long> listaId = new LinkedList<Long>();
			for (Andamento tipoAndamento : lista) {
				listaId.add(tipoAndamento.getId());
			}
			return listaId;
		}

		return null;
	}

	public SearchResult<ProcessoSetor> pesquisarProcessoSetorEletronico(ProcessoSetorEletronicoSearchData sd) throws ServiceException {
		try {
			return dao.pesquisarProcessoSetorEletronico(sd);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public Integer pesquisarQuantidadeProcessoSetorEletronico(ProcessoSetorEletronicoSearchData sd) throws ServiceException {
		try {
			return dao.pesquisarQuantidadeProcessoSetorEletronico(sd);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public void limparFaseAtual(List<ProcessoSetor> processosSelecionados) throws ServiceException {
		try {
		for (ProcessoSetor processoSetor: processosSelecionados) {
			processoSetor.setFaseAtual(null);
			dao.salvar(processoSetor);
		}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	
}
