package br.gov.stf.estf.usuario.model.service;

import br.gov.stf.estf.entidade.usuario.CustomizacaoUsuario;
import br.gov.stf.estf.entidade.usuario.TipoCustomizacao;
import br.gov.stf.estf.usuario.model.dataaccess.CustomizacaoUsuarioDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface CustomizacaoUsuarioService extends GenericService<CustomizacaoUsuario, Long, CustomizacaoUsuarioDao> {
	
	public CustomizacaoUsuario retornaCustomizacaoSetor(TipoCustomizacao tipoCustomizacao, Long codSetor) throws ServiceException;

}
