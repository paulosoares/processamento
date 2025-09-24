package br.jus.stf.estf.decisao.documento.service;

import java.util.List;

import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.documento.support.Documento;
import br.jus.stf.estf.decisao.documento.support.ValidacaoPermissaoAssinaturaDocumento;

public interface PermissaoAssinaturaDocumentoService<T extends Documento> {

	ValidacaoPermissaoAssinaturaDocumento<T> documentoPodeSerAssinado(T documento);
	
	List<ValidacaoPermissaoAssinaturaDocumento<T>> documentosPodemSerAssinados(List<T> documentos) throws ServiceException;

}
