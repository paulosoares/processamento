package br.gov.stf.estf.usuario.model.dataaccess;

import br.gov.stf.estf.entidade.usuario.CustomizacaoUsuario;
import br.gov.stf.estf.entidade.usuario.TipoCustomizacao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface CustomizacaoUsuarioDao extends GenericDao<CustomizacaoUsuario, Long>{
	
	public CustomizacaoUsuario retornaCustomizacaoSetor(TipoCustomizacao tipoCustomizacao, Long codSetor) throws DaoException;
}
