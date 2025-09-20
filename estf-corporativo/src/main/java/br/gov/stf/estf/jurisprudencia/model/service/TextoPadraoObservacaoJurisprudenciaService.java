/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.jurisprudencia.TextoPadraoObservacaoJurisprudencia;
import br.gov.stf.estf.entidade.jurisprudencia.TipoOrdenacaoObservacaoJurisprudencia;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.TextoPadraoObservacaoJurisprudenciaDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * @author Paulo.Estevao
 * @since 08.10.2012
 */
public interface TextoPadraoObservacaoJurisprudenciaService extends GenericService<TextoPadraoObservacaoJurisprudencia, Long, TextoPadraoObservacaoJurisprudenciaDao> {

	List<TextoPadraoObservacaoJurisprudencia> pesquisarTodos() throws ServiceException;

	List<TextoPadraoObservacaoJurisprudencia> pesquisar(TipoOrdenacaoObservacaoJurisprudencia tipoOrdenacao) throws ServiceException;

}
