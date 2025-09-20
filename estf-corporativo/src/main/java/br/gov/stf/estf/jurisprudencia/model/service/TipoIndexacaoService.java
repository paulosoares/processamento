/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service;

import br.gov.stf.estf.entidade.jurisprudencia.TipoIndexacao;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.TipoIndexacaoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * @author Paulo.Estevao
 * @since 10.07.2012
 */
public interface TipoIndexacaoService extends GenericService<TipoIndexacao, Long, TipoIndexacaoDao> {

	TipoIndexacao recuperarPorSigla(String sigla) throws ServiceException;

}
