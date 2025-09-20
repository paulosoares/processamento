package br.gov.stf.estf.usuario.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.usuario.ConfiguracaoUsuario;
import br.gov.stf.estf.usuario.model.dataaccess.ConfiguracaoUsuarioDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ConfiguracaoUsuarioService extends GenericService<ConfiguracaoUsuario, Long, ConfiguracaoUsuarioDao> {

	public List<ConfiguracaoUsuario> pesquisar(String siglaUsuario, Long idSetor, Long idTipoConfUsuario, String codChave) throws ServiceException;
}
