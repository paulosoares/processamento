package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.SituacaoMinistroProcesso;
import br.gov.stf.estf.processostf.model.util.ProcessoSearchData;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.util.SearchResult;

public interface ProcessoDao extends GenericDao<Processo, Long> {

	public Processo recuperarProcesso(String classeProcessual, Long numeroProcesso) throws DaoException;

	public Processo recuperarProcessoSTF(String sigla, Long numero) throws DaoException;

	public Processo recuperarProcessoSTF(Long numeroProcotolo, Short anoProtocolo) throws DaoException;

	public Processo recuperarProcessoSTF(Long codigoOrigem, String siglaClasseProcedencia, String numeroProcessoProcedencia)
			throws DaoException;

	public SituacaoMinistroProcesso recuperarDataDistribuicaoSTF(Long seqObjetoIncidente, String sigla, Long numeroProcesso, Long codigoMinistroRelator)
			throws DaoException;

	/**
	 * Recupera a lista de ministros que já foram relatores do processo
	 * informado.
	 * 
	 * @param processo
	 *            : Processo a ser pesquisado. A id do processo não deve ser
	 *            nula, ou será retornado uma ServiceException.
	 * @return
	 * @throws ServiceException
	 */
	public List<SituacaoMinistroProcesso> pesquisarSituacaoMinistroProcesso(Processo processo) throws DaoException;
	
	public SearchResult<Processo> pesquisarProcesso(ProcessoSearchData processoSearchData)throws DaoException;
	
	public Processo recuperar(Peticao peticao) throws DaoException;
	
	public void alterarBaixaProcesso (Processo processo) throws DaoException;

	public void alterarBaixaProcesso (Processo processo, Boolean flag) throws DaoException;
	
	public boolean isProcessoFindo(Processo processo) throws DaoException;

	public List<Processo> pesquisarProcessos(Long numero) throws DaoException;
	public List<Processo> pesquisarProcessos(Long numero, Boolean isFisico) throws DaoException;
	
	public boolean isProcessoDistribuido(Processo processo) throws DaoException;

	public boolean isBloqueadoBaixa(Processo processo) throws DaoException;

	public Ministro pesquisarRelatorAtual(ObjetoIncidente objetoIncidente) throws DaoException;
}
