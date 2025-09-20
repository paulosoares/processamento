package br.gov.stf.estf.julgamento.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.processostf.ProtocoloTaquigrafia;
import br.gov.stf.estf.julgamento.model.dataaccess.ProtocoloTaquigrafiaDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;


/**
 * @author Almir.Oliveira
 * @since 14.12.2011 */
public interface ProtocoloTaquigrafiaService extends GenericService<ProtocoloTaquigrafia, Long, ProtocoloTaquigrafiaDao>{

	List<ProtocoloTaquigrafia> pesquisar(ListaJulgamento listaJulgamento) throws ServiceException;

}
