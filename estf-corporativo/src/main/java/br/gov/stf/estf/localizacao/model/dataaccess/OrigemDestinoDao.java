package br.gov.stf.estf.localizacao.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.OrigemDestino;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * DAO interface for domain model class OrigemDestino.
 * 
 * @see .OrigemDestino
 * @author Hibernate Tools
 */

public interface OrigemDestinoDao extends GenericDao<OrigemDestino, Long> {

	List<OrigemDestino> recuperarPorIdOuDescricao(String id, Boolean ativo, Boolean deslocaProcesso) throws DaoException;
	
	
	List<OrigemDestino> recuperarPorId(String id, Boolean ativo, Boolean deslocaProcesso) throws DaoException;
	
	List<OrigemDestino> recuperarPorDescricao(String descricao, Boolean ativo, Boolean deslocaProcesso) throws DaoException;

	public List<OrigemDestino> recuperarPorIdOuDescricaoSetoresEOrgaosExternos(String value) throws DaoException;
	
	public OrigemDestino recuperarPorId(Long id, int tipoOrigemDestino) throws DaoException;
	
	public List<OrigemDestino> recuperarPorIdOuDescricao(String value, Integer tipoOrigemDestino) throws DaoException;

	public List<OrigemDestino> recuperarPorIdOuDescricao(String value) throws DaoException;
}
