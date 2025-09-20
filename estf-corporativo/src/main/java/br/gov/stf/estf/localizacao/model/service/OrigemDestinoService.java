package br.gov.stf.estf.localizacao.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.OrigemDestino;
import br.gov.stf.estf.localizacao.model.dataaccess.OrigemDestinoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * Service interface for domain model class OrigemDestino.
 * 
 * @see .OrigemDestino
 * @author Hibernate Tools
 */
public interface OrigemDestinoService extends GenericService<OrigemDestino, Long, OrigemDestinoDao> {

	public List<OrigemDestino> recuperarPorIdOuDescricao(String id, Boolean ativo, Boolean deslocaProcesso) throws ServiceException;

	List<OrigemDestino> recuperarPorId(String id, Boolean ativo, Boolean deslocaProcesso) throws ServiceException;
	
	List<OrigemDestino> recuperarPorDescricao(String descricao, Boolean ativo, Boolean deslocaProcesso) throws ServiceException;
	
	public List<OrigemDestino> recuperarPorIdOuDescricaoSetoresEOrgaosExternos(String value) throws ServiceException;
	
	public OrigemDestino recuperarPorId(Long id, int tipoOrigemDestino) throws ServiceException;

	public List<OrigemDestino> recuperarPorIdOuDescricao(String value, Integer tipoOrigemDestino) throws ServiceException;

	public List<OrigemDestino> recuperarPorIdOuDescricao(String value) throws ServiceException;
}