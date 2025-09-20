package br.gov.stf.estf.julgamento.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.julgamento.EventoSessao;
import br.gov.stf.estf.julgamento.model.dataaccess.EventoSessaoDao;
import br.gov.stf.estf.julgamento.model.service.EventoSessaoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("eventoSessaoService")
public class EventoSessaoServiceImpl extends GenericServiceImpl<EventoSessao, Long, EventoSessaoDao> implements EventoSessaoService {
    public EventoSessaoServiceImpl(EventoSessaoDao dao) { super(dao); } 

	public List<EventoSessao> pesquisar( Long idSessao ) throws ServiceException {
		try {
			return dao.pesquisar( idSessao );
		} catch ( DaoException e ) {
			throw new ServiceException( e );
		}
	}
	
}
