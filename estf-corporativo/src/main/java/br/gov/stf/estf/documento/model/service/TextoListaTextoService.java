/**
 * 
 */
package br.gov.stf.estf.documento.model.service;

import br.gov.stf.estf.documento.model.dataaccess.TextoListaTextoDao;
import br.gov.stf.estf.entidade.documento.ListaTextos;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TextoListaTexto;
import br.gov.stf.estf.entidade.documento.TextoListaTexto.TextoListaTextoId;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * @author Paulo.Estevao
 *
 */
public interface TextoListaTextoService extends GenericService<TextoListaTexto, TextoListaTextoId, TextoListaTextoDao> {
	
	TextoListaTexto recuperar(ListaTextos listaTextos, Texto texto) throws ServiceException;

}
