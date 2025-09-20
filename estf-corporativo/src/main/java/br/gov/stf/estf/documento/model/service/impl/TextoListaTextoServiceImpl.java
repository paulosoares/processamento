/**
 * 
 */
package br.gov.stf.estf.documento.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.TextoListaTextoDao;
import br.gov.stf.estf.documento.model.service.TextoListaTextoService;
import br.gov.stf.estf.entidade.documento.ListaTextos;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TextoListaTexto;
import br.gov.stf.estf.entidade.documento.TextoListaTexto.TextoListaTextoId;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Paulo.Estevao
 * @since 24.05.2011
 */
@Service("textoListaTextoService")
public class TextoListaTextoServiceImpl extends
		GenericServiceImpl<TextoListaTexto, TextoListaTextoId, TextoListaTextoDao> implements TextoListaTextoService {

	protected TextoListaTextoServiceImpl(TextoListaTextoDao dao) {
		super(dao);
	}

	@Override
	public TextoListaTexto recuperar(ListaTextos listaTextos, Texto texto)
			throws ServiceException {
		try {
			return dao.recuperar(listaTextos, texto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}


}
