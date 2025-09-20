package br.gov.stf.estf.processosetor.model.dataaccess;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.processosetor.PeticaoSetor;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.processosetor.model.util.PeticaoSetorSearchData;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface PeticaoSetorDao extends GenericDao<PeticaoSetor, Long> {

	public PeticaoSetor recuperarPeticaoSetor(Long id) throws DaoException;

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
			throws DaoException;
	
	public List<PeticaoSetor> pesquisarPeticaoSetor(PeticaoSetorSearchData peticaoSetorSearchData)
			throws DaoException;

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
			throws DaoException;
	
	public Long pesquisarQuantidadePeticaoSetor(PeticaoSetorSearchData peticaoSetorSearchData)
			throws DaoException;

	public Boolean persistirPeticaoSetor(PeticaoSetor peticaoSetor)
			throws DaoException;
	
	/**
	 * Funciona do mesmo modo que {@link #alterar(PeticaoSetor)}, porém sem 
	 * validar outros objetos que se associem à PeticaoSetor (ou seja, realizar 
	 * insert, update ou delete em outras tabelas) ou utilizar outras colunas - 
	 * apenas a referente à situação de "tratada".
	 */
	public Boolean registrarSituacaoTratada(PeticaoSetor peticaoSetor) throws DaoException;

	public Long estatisticaPeticaoSetor(Long idSetor, Date dataEntradaInicial,
			Date dataEntradaFinal, Boolean localizadoNoSetor,
			String tipoMeioProcesso) throws DaoException;
	public Boolean isPeticaoPendenteTratamento(Processo processo) throws DaoException;
}