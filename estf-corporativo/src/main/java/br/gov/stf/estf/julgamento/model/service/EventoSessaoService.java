package br.gov.stf.estf.julgamento.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.julgamento.EventoSessao;
import br.gov.stf.estf.julgamento.model.dataaccess.EventoSessaoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface EventoSessaoService extends GenericService<EventoSessao, Long, EventoSessaoDao> {
	
	public List<EventoSessao> pesquisar( Long idSessao ) throws ServiceException;

}
