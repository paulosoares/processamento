package br.gov.stf.estf.usuario.model.dataaccess;

import br.gov.stf.estf.entidade.usuario.TipoCustomizacao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TipoCustomizacaoDao extends GenericDao<TipoCustomizacao, Long>{
	
	public TipoCustomizacao buscaPorDscParametro(String dscParametro) throws DaoException;

}
