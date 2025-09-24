/**
 * 
 */
package br.jus.stf.estf.decisao.texto.service;

import java.util.Collection;
import java.util.List;

import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.texto.support.TextoReport;

/**
 * @author Paulo.Estevao
 * @since 01.09.2010
 */
public interface RelatorioTextoService {
	List<TextoReport> recuperaTextoReport(Collection<TextoDto> listaTextos) throws ServiceException;
}
