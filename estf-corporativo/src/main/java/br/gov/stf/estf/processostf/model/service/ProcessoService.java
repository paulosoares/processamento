package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.SituacaoMinistroProcesso;
import br.gov.stf.estf.processostf.model.dataaccess.ProcessoDao;
import br.gov.stf.estf.processostf.model.util.ProcessoSearchData;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.util.SearchResult;


public interface ProcessoService extends GenericService<Processo, Long, ProcessoDao> {

	public Processo recuperarProcesso(String classeProcessual,
			Long numeroProcesso) throws ServiceException, ProcessoException;

	public void alterarDeslocamentoProcessoEletronico(Processo processo,
			Setor setor) throws ServiceException;

	void deslocarProcessoEletronico(Processo processoDoTexto,
			Setor setorDeOrigem, Setor setorDeDestino) throws ServiceException;
			
	public Processo recuperarProcessoSTF(String sigla, Long numero) throws ServiceException;
	public Processo recuperarProcessoSTF(Long numeroProcotolo,Short anoProtocolo) throws ServiceException;
	public Processo recuperarProcessoSTF(Long codigoOrigem, String siglaClasseProcedencia, String numeroProcessoProcedencia) throws ServiceException;
	
	/**
	 * 
	 * @param seqObjetoIncidente
	 * @param sigla - não está sendo utilizado
	 * @param numeroProcesso - não está sendo utilizado
	 * @param codigoMinistroRelator - não está sendo utilizado
	 * @return
	 * @throws ServiceException
	 */
    public SituacaoMinistroProcesso recuperarDataDistribuicaoSTF(Long seqObjetoIncidente, String sigla, Long numeroProcesso, 
    		Long codigoMinistroRelator) 
    		throws ServiceException;


	/**
	 * Recupera a lista de ministros que já foram relatores do processo informado.
	 * 
	 * @param processo:
	 *            Processo a ser pesquisado. A siglaClasseProcessual e o numeroProcessual não devem ser nulos, ou será
	 *            retornado uma ServiceException.
	 * @return
	 * @throws ServiceException
	 */
	public List<SituacaoMinistroProcesso> pesquisarSituacaoMinistroProcesso(Processo processo) throws ServiceException;
	
	public SearchResult<Processo> pesquisarProcesso (ProcessoSearchData processoSearchData) throws ServiceException;
	
	public Processo recuperar(Peticao peticao) throws ServiceException;
	
	public void alterarBaixaProcesso (Processo processo) throws ServiceException;

	public void alterarBaixaProcesso (Processo processo, Boolean flag) throws ServiceException;
	
	public boolean isProcessoFindo(Processo processo) throws ServiceException;

	public List<Processo> pesquisarProcesso(String identificacao) throws ServiceException;	

	public List<Processo> pesquisarProcessos(String identificacao, Boolean isFisico) throws ServiceException;
	
	public boolean isProcessoDistribuido(Processo processo) throws ServiceException;
	
	public boolean validarNumeroUnico(String numeroUnico);

	public boolean isBloqueadoBaixa(Processo processo) throws ServiceException;

	public Ministro pesquisarRelatorAtual(ObjetoIncidente objetoIncidente) throws ServiceException;

	public Boolean houveRemessa(ObjetoIncidente<?> objetoIncidente) throws ServiceException;
}
