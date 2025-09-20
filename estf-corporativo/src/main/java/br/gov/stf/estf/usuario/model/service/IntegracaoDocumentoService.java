package br.gov.stf.estf.usuario.model.service;

import java.util.Date;

import br.gov.stf.estf.entidade.usuario.IntegracaoDocumento;
import br.gov.stf.estf.usuario.model.dataaccess.IntegracaoDocumentoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface IntegracaoDocumentoService extends GenericService<IntegracaoDocumento, Long, IntegracaoDocumentoDao> {
	
	public Date findMaxDataById(Long seqUsuarioExterno) throws ServiceException;

}

