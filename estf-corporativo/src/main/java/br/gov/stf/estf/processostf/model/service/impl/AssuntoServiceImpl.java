package br.gov.stf.estf.processostf.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.Assunto;
import br.gov.stf.estf.processostf.model.dataaccess.AssuntoDao;
import br.gov.stf.estf.processostf.model.service.AssuntoService;
import br.gov.stf.estf.processostf.model.util.AssuntoSearchData;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;
import br.gov.stf.framework.util.SearchResult;

@Service("assuntoService")
public class AssuntoServiceImpl extends GenericServiceImpl<Assunto, String, AssuntoDao> 
	implements AssuntoService{
    public AssuntoServiceImpl(AssuntoDao dao) { super(dao); }

	public SearchResult<Assunto> pesquisarPorDescricao(AssuntoSearchData sd) {
		return dao.pesquisarPorDescricao(sd);
	}
	
	public SearchResult<Assunto> pesquisar(AssuntoSearchData sd)
			throws ServiceException {
		try {
			return dao.pesquisar(sd);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public List<Assunto> pesquisarAssuntoHierarquicoProcesso(
			String codigoAssuntoIncompleto, String codigoAssunto, String siglaClasseProcessual,
			Long numeroProcessual) throws ServiceException {

		List<Assunto> assuntos = null;
		
		try {                
			assuntos = dao.pesquisarAssuntoHierarquicoProcesso(codigoAssuntoIncompleto, codigoAssunto, siglaClasseProcessual, numeroProcessual);            
		} catch( DaoException e ) {
			throw new ServiceException(e);
		}        
		return assuntos;		
	}

	public List<Assunto> pesquisarAssuntoHierarquico(
			String codigoAssunto, String descricao, Boolean ativo,Boolean pesquisarInicio, 
			Short numeroMaximoDeResultados, Boolean pesquisarTodosNiveis) throws ServiceException {
		
		List<Assunto> assuntos = null;
		
		try {                
			assuntos = dao.pesquisarAssuntoHierarquico(codigoAssunto, descricao, ativo,pesquisarInicio, numeroMaximoDeResultados, pesquisarTodosNiveis);            
		} catch( DaoException e ) {
			throw new ServiceException(e);
		}        
		return assuntos;		
		
	}
	
	public Integer pesquisarQuantidadeAssuntoHierarquico(
			String codigoAssunto, String descricao, Boolean ativo,Boolean pesquisarInicio, 
			Boolean pesquisarTodosNiveis) throws ServiceException {

		Integer quantidade = null;
		
		try {                
			quantidade = dao.pesquisarQuantidadeAssuntoHierarquico(
					codigoAssunto, descricao, ativo,pesquisarInicio, pesquisarTodosNiveis);            
		} catch( DaoException e ) {
			throw new ServiceException(e);
		}        
		return quantidade;		
	}
	
	public List<Assunto> pesquisarAssunto(
			String codigoAssunto, String descricao, Boolean ativo, Short numeroMaximoDeResultados) throws ServiceException {
		
		List<Assunto> assuntos = null;
		
		try {                
			assuntos = dao.pesquisarAssunto(codigoAssunto, descricao, ativo, numeroMaximoDeResultados);            
		} catch( DaoException e ) {
			throw new ServiceException(e);
		}        
		return assuntos;		
		
	}
	
	public Assunto recuperarAssunto(String codigoAssunto) throws ServiceException {
		
		if(codigoAssunto == null || codigoAssunto.length() == 0)
			throw new ServiceException("Código do assunto não informado");
		
		Assunto assunto = null;
		
		try {                
			assunto = dao.recuperarAssunto(codigoAssunto);            
		} 
		catch(DaoException e) {
			throw new ServiceException(e);
		}        
		return assunto;		
		
	}

	
}
