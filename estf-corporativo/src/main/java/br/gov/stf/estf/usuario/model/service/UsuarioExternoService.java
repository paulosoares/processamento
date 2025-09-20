package br.gov.stf.estf.usuario.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.usuario.UsuarioExterno;
import br.gov.stf.estf.usuario.model.dataaccess.UsuarioExternoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface UsuarioExternoService extends GenericService<UsuarioExterno, Integer, UsuarioExternoDao>{
	
	public List<UsuarioExterno> pesquisarOrgaosIntegracao() throws ServiceException;

}
