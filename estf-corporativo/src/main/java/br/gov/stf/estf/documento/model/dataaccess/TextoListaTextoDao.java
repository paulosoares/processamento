/**
 * 
 */
package br.gov.stf.estf.documento.model.dataaccess;

import br.gov.stf.estf.entidade.documento.ListaTextos;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TextoListaTexto;
import br.gov.stf.estf.entidade.documento.TextoListaTexto.TextoListaTextoId;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * @author Paulo.Estevao
 * @since 24.05.2011
 */
public interface TextoListaTextoDao extends
		GenericDao<TextoListaTexto, TextoListaTextoId> {

	TextoListaTexto recuperar(ListaTextos listaTextos, Texto texto) throws DaoException;
}
