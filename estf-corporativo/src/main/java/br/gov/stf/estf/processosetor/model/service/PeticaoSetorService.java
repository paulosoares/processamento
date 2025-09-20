package br.gov.stf.estf.processosetor.model.service;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.processosetor.PeticaoSetor;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.processosetor.model.dataaccess.PeticaoSetorDao;
import br.gov.stf.estf.processosetor.model.util.PeticaoSetorSearchData;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface PeticaoSetorService extends
		GenericService<PeticaoSetor, Long, PeticaoSetorDao> {

	public PeticaoSetor recuperarPeticaoSetor(Long id) throws ServiceException;

	public Boolean persistirPeticaoSetor(PeticaoSetor peticaoSetor)
			throws ServiceException;

	public Boolean registrarSituacaoTratada(PeticaoSetor peticaoSetor)
			throws ServiceException;

	/**
	 * @deprecated Utilizar {@link #pesquisarPeticaoSetor(PeticaoSetorSearchData)} ao invés deste.
	 */
	@Deprecated
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
			throws ServiceException;
	
	public List<PeticaoSetor> pesquisarPeticaoSetor(PeticaoSetorSearchData peticaoSetorSearchData)
			throws ServiceException;

	/**
	 * @deprecated Utilizar {@link #pesquisarQuantidadePeticaoSetor(PeticaoSetorSearchData)} ao invés deste.
	 */
	@Deprecated
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
			throws ServiceException;
	
	public Long pesquisarQuantidadePeticaoSetor(PeticaoSetorSearchData peticaoSetorSearchData)
			throws ServiceException;

	public Long estatisticaPeticaoSetor(Long idSetor, Date dataEntradaInicial,
			Date dataEntradaFinal, Boolean localizadoNoSetor,
			String tipoMeioProcesso) throws ServiceException;
	public Boolean isPeticaoPendenteTratamento(Processo processo) throws ServiceException;
}
