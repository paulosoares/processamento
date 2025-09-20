package br.gov.stf.estf.processostf.model.dataaccess;


import java.util.List;

import br.gov.stf.estf.entidade.processostf.Assunto;
import br.gov.stf.estf.processostf.model.util.AssuntoSearchData;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;
import br.gov.stf.framework.util.SearchResult;


/**
 * DAO interface for domain model class Distribuicao.
 * @see .Distribuicao
 * @author Hibernate Tools
 */

public interface AssuntoDao 
extends GenericDao <Assunto, String> {
	
	public SearchResult<Assunto> pesquisarPorDescricao(AssuntoSearchData sd);
	
	public SearchResult<Assunto> pesquisar (AssuntoSearchData sd) throws DaoException;
	
	public List<Assunto> pesquisarAssuntoHierarquicoProcesso(
			String codigoAssuntoIncompleto, String codigoAssunto, String siglaClasseProcessual,
			Long numeroProcessual) throws DaoException;
	
	public List<Assunto> pesquisarAssuntoHierarquico(
			String codigoAssunto, String descricao, Boolean ativo,Boolean pesquisarInicio, 
			Short numeroMaximoDeResultados, Boolean pesquisarTodosNiveis) throws DaoException;	

	public Integer pesquisarQuantidadeAssuntoHierarquico(
			String codigoAssunto, String descricao, Boolean ativo,Boolean pesquisarInicio, Boolean pesquisarTodosNiveis) throws DaoException;	
	
	public List<Assunto> pesquisarAssunto(
			String codigoAssunto, String descricao, Boolean ativo, Short numeroMaximoDeResultados) throws DaoException;	

	public Assunto recuperarAssunto(String codigoAssunto) throws DaoException;
	
	
}

