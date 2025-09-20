package br.gov.stf.estf.localizacao.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.TipoStatusSetor;
import br.gov.stf.estf.localizacao.model.dataaccess.TipoStatusSetorDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface TipoStatusSetorService extends GenericService<TipoStatusSetor, Long, TipoStatusSetorDao> {
	
	/**
	 * Recupera todos os StatusProcessoSetor que se encaixam nos parametros informado
	 * 
	 * @param idStatus id do StatusProcessoSetor
	 * @param ativo se o StatusProcessoSetor está ativo ou não
	 * @return Lista de StatusProcessoSetor
	 * @throws ServiceException
	 */
	
	public List <TipoStatusSetor> pesquisarTipoStatusSetor(String descricao, Long idSetor, 
			Boolean comumEntreSetores, Boolean ativo)throws ServiceException;
	
	public TipoStatusSetor recuperarTipoStatusSetor(Long idStatus) throws ServiceException;
	
	public Boolean persistirTipoStatusSetor(TipoStatusSetor status) throws ServiceException;
	
	public Boolean excluirTipoStatusSetor(TipoStatusSetor status)throws ServiceException;       

}
