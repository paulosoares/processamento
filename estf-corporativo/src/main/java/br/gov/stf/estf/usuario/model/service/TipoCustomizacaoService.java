package br.gov.stf.estf.usuario.model.service;

import br.gov.stf.estf.entidade.usuario.TipoCustomizacao;
import br.gov.stf.estf.usuario.model.dataaccess.TipoCustomizacaoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface TipoCustomizacaoService extends GenericService<TipoCustomizacao, Long, TipoCustomizacaoDao> {
	
	public TipoCustomizacao buscaPorDscParametro(String dscParametro) throws ServiceException;

}
