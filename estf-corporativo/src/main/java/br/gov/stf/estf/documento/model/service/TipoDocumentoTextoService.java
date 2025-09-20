package br.gov.stf.estf.documento.model.service;

import java.util.List;

import br.gov.stf.estf.documento.model.dataaccess.TipoDocumentoTextoDao;
import br.gov.stf.estf.entidade.documento.TipoDocumentoTexto;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface TipoDocumentoTextoService extends GenericService<TipoDocumentoTexto, Long, TipoDocumentoTextoDao> {
	
	public List<TipoDocumentoTexto> pesquisarTiposDocumentoTextoPorSetor(Long codSetor)	throws ServiceException;
	
	public TipoDocumentoTexto recuperar (Long codigoTipoTexto) throws ServiceException;
	
}
