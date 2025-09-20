package br.gov.stf.estf.processostf.model.service;


import java.util.List;

import br.gov.stf.estf.entidade.processostf.ClasseUnificada;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.ProcessoImagem;
import br.gov.stf.estf.entidade.processostf.ProcessoImagem.ProcessoImagemId;
import br.gov.stf.estf.processostf.model.dataaccess.ProcessoImagemDao;
import br.gov.stf.estf.processostf.model.util.ProcessoImagemSearchData;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.util.SearchResult;


public interface ProcessoImagemService extends GenericService<ProcessoImagem, Long, ProcessoImagemDao> {

	public ProcessoImagem recuperarProcessoImagem(ObjetoIncidente<?> objetoIncidente) throws ServiceException, ProcessoException;

	public boolean salvarProcessoImagem (ProcessoImagem processoImagem) throws ServiceException;
	
	public SearchResult<ProcessoImagem> pesquisarProcessoImagem (ProcessoImagemSearchData processoImagemSearchData) throws ServiceException;
	
	public ClasseUnificada recuperarClasseUnificada(ProcessoImagem processoImagem) throws ServiceException;

	Long recuperarQuantidadePaginasProcessoImagem(ProcessoImagem processoImagem) throws ServiceException;
	
	public List<ProcessoImagem> pesquisarProcessoImagemPorClasseNumero(String siglaClasse, Long numeroProcesso) throws ServiceException;
	
	public ProcessoImagem recuperarProcessoImagemLiberadoPorId(ProcessoImagemId id) throws ServiceException;
}
