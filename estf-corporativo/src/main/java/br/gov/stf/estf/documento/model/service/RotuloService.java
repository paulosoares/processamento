/**
 * 
 */
package br.gov.stf.estf.documento.model.service;

import java.util.List;

import br.gov.stf.estf.documento.model.dataaccess.RotuloDao;
import br.gov.stf.estf.entidade.documento.Rotulo;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * @author Paulo.Estevao
 * @since 28.11.2013
 */
public interface RotuloService extends GenericService<Rotulo, Long, RotuloDao> {

	List<Rotulo> pesquisarRotulos(ObjetoIncidente<?> objetoIncidente, Setor setor) throws ServiceException;
}
