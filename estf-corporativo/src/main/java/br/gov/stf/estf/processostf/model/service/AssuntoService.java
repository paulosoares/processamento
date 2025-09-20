package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.Assunto;
import br.gov.stf.estf.processostf.model.dataaccess.AssuntoDao;
import br.gov.stf.estf.processostf.model.util.AssuntoSearchData;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.util.SearchResult;

public interface AssuntoService extends GenericService<Assunto, String, AssuntoDao>{

	public SearchResult<Assunto> pesquisarPorDescricao(AssuntoSearchData sd);
	
	public SearchResult<Assunto> pesquisar(AssuntoSearchData sd) throws ServiceException;
	
	public List<Assunto> pesquisarAssuntoHierarquicoProcesso(
			String codigoAssuntoIncompleto, String codigoAssunto, String siglaClasseProcessual,
			Long numeroProcessual) throws ServiceException;
	
	public List<Assunto> pesquisarAssuntoHierarquico(
			String codigoAssunto, String descricao, Boolean ativo,Boolean pesquisarInicio, 
			Short numeroMaximoDeResultados, Boolean pesquisarTodosNiveis) throws ServiceException;
	
	public Integer pesquisarQuantidadeAssuntoHierarquico(
			String codigoAssunto, String descricao, Boolean ativo,Boolean pesquisarInicio, 
			Boolean pesquisarTodosNiveis) throws ServiceException;
	
	public List<Assunto> pesquisarAssunto(
			String codigoAssunto, String descricao, Boolean ativo, Short numeroMaximoDeResultados) throws ServiceException;
	
	public Assunto recuperarAssunto(String codigoAssunto) throws ServiceException;	
	
	
}
