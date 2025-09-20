package br.gov.stf.estf.julgamento.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.julgamento.TipoListaJulgamento;
import br.gov.stf.estf.julgamento.model.dataaccess.TipoListaJulgamentoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface TipoListaJulgamentoService extends GenericService<TipoListaJulgamento, Long, TipoListaJulgamentoDao> {

	List<TipoListaJulgamento> recuperarTipoListaJulgamentoAtivas() throws ServiceException;

}
