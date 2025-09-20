package br.gov.stf.estf.processosetor.model.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processosetor.PeticaoSetor;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.processosetor.model.dataaccess.PeticaoSetorDao;
import br.gov.stf.estf.processosetor.model.service.PeticaoSetorService;
import br.gov.stf.estf.processosetor.model.util.PeticaoSetorSearchData;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("peticaoSetorService")
public class PeticaoSetorServiceImpl extends
		GenericServiceImpl<PeticaoSetor, Long, PeticaoSetorDao> implements
		PeticaoSetorService {

	public PeticaoSetorServiceImpl(PeticaoSetorDao dao) {
		super(dao);
	}

	public PeticaoSetor recuperarPeticaoSetor(Long id) throws ServiceException {

		if (id == null)
			throw new NullPointerException("Identificação da Petição nula");

		PeticaoSetor peticaoSetorRecuperada = null;

		try {

			peticaoSetorRecuperada = dao.recuperarPeticaoSetor(id);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return peticaoSetorRecuperada;
	}

	public List<PeticaoSetor> pesquisarPeticaoSetor(Long numeroPeticao,
			Short anoPeticao, Long idSetor, String siglaClasseProcessual,
			Long numeroProcesso, Short codigoRecurso, Date dataEntradaInicial,
			Date dataEntradaFinal, Date dataRemessaInicial,
			Date dataRemessaFinal, Date dataRecebimentoInicial,
			Date dataRecebimentoFinal, Boolean juntado, Boolean tratado,
			Boolean vinculadoProcesso, Boolean semLocalizacao,
			String numeroSala, String numeroArmario, String numeroEstante,
			String numeroPrateleira, String numeroColuna,
			Boolean deslocamentoPeticao, Long idSecaoUltimoDeslocamento,
			Boolean localizadoNoSetor, String tipoMeioProcesso,
			Boolean OderByPeticao, Boolean OderByProcesso,
			Boolean OderByDataEntrada, Boolean orderByCrescente,
			Boolean readOnlyQuery, Boolean limitarPesquisa, Boolean peticoesSemDeslocamento)
			throws ServiceException {

		try {

			return dao.pesquisarPeticaoSetor(numeroPeticao, anoPeticao,
					idSetor, siglaClasseProcessual, numeroProcesso,
					codigoRecurso, dataEntradaInicial, dataEntradaFinal,
					dataRemessaInicial, dataRemessaFinal,
					dataRecebimentoInicial, dataRecebimentoFinal, juntado,
					tratado, vinculadoProcesso, semLocalizacao, numeroSala,
					numeroArmario, numeroEstante, numeroPrateleira,
					numeroColuna, deslocamentoPeticao,
					idSecaoUltimoDeslocamento, localizadoNoSetor,
					tipoMeioProcesso, OderByPeticao, OderByProcesso,
					OderByDataEntrada, orderByCrescente, readOnlyQuery,
					limitarPesquisa, peticoesSemDeslocamento);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<PeticaoSetor> pesquisarPeticaoSetor(PeticaoSetorSearchData peticaoSetorSearchData) throws ServiceException {
		try {
			return dao.pesquisarPeticaoSetor(peticaoSetorSearchData);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public Long pesquisarQuantidadePeticaoSetor(Long numeroPeticao,
			Short anoPeticao, Long idSetor, String siglaClasseProcessual,
			Long numeroProcesso, Short codigoRecurso, Date dataEntradaInicial,
			Date dataEntradaFinal, Date dataRemessaInicial,
			Date dataRemessaFinal, Date dataRecebimentoInicial,
			Date dataRecebimentoFinal, Boolean juntado, Boolean tratado,
			Boolean vinculadoProcesso, Boolean semLocalizacao,
			String numeroSala, String numeroArmario, String numeroEstante,
			String numeroPrateleira, String numeroColuna,
			Boolean deslocamentoPeticao, Long idSecaoUltimoDeslocamento,
			Boolean localizadoNoSetor, String tipoMeioProcesso, Boolean peticoesSemDeslocamento)
			throws ServiceException {
		try {

			return dao.pesquisarQuantidadePeticaoSetor(numeroPeticao,
					anoPeticao, idSetor, siglaClasseProcessual, numeroProcesso,
					codigoRecurso, dataEntradaInicial, dataEntradaFinal,
					dataRemessaInicial, dataRemessaFinal,
					dataRecebimentoInicial, dataRecebimentoFinal, juntado,
					tratado, vinculadoProcesso, semLocalizacao, numeroSala,
					numeroArmario, numeroEstante, numeroPrateleira,
					numeroColuna, deslocamentoPeticao,
					idSecaoUltimoDeslocamento, localizadoNoSetor,
					tipoMeioProcesso,peticoesSemDeslocamento);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Long pesquisarQuantidadePeticaoSetor(PeticaoSetorSearchData peticaoSetorSearchData) throws ServiceException {
		try {
			return dao.pesquisarQuantidadePeticaoSetor(peticaoSetorSearchData);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public Boolean persistirPeticaoSetor(PeticaoSetor peticaoSetor)
			throws ServiceException {

		Boolean alterado = Boolean.FALSE;

		try {

			alterado = dao.persistirPeticaoSetor(peticaoSetor);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return alterado;
	}
	
	@Override
	public Boolean registrarSituacaoTratada(PeticaoSetor peticaoSetor)
			throws ServiceException {
		Boolean alterado = Boolean.FALSE;
		
		try {
			alterado = dao.registrarSituacaoTratada(peticaoSetor);
		} catch (DaoException exception) {
			throw new ServiceException(exception);
		}
		
		return alterado;
	}

	public Long estatisticaPeticaoSetor(Long idSetor, Date dataEntradaInicial,
			Date dataEntradaFinal, Boolean localizadoNoSetor,
			String tipoMeioProcesso) throws ServiceException {
		try {
			return dao.estatisticaPeticaoSetor(idSetor, dataEntradaInicial,
					dataEntradaFinal, localizadoNoSetor, tipoMeioProcesso);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public Boolean isPeticaoPendenteTratamento(Processo processo) throws ServiceException {
		try {
			return dao.isPeticaoPendenteTratamento(processo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}


}
