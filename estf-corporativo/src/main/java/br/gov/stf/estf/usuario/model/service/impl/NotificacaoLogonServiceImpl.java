package br.gov.stf.estf.usuario.model.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.usuario.NotificacaoLogon;
import br.gov.stf.estf.usuario.model.dataaccess.NotificacaoLogonDao;
import br.gov.stf.estf.usuario.model.service.NotificacaoLogonService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("notificacaoLogonService")
public class NotificacaoLogonServiceImpl extends GenericServiceImpl<NotificacaoLogon, Long, NotificacaoLogonDao> implements NotificacaoLogonService{
		@Autowired
	    public NotificacaoLogonServiceImpl(NotificacaoLogonDao dao) { 
			super(dao); 
		}
		
		public List<NotificacaoLogon> pesquisaNotificacao(String grupoTopico, Date dataInicio, Date dataFim, Date dataNotificacaoFim, String usuarioInlusao, Integer prioridade) 
		throws ServiceException { 
			try {
				return dao.pesquisaNotificacao(grupoTopico, dataInicio, dataFim, dataNotificacaoFim, usuarioInlusao, prioridade);
			} catch (DaoException e) {
				throw new ServiceException(e);
			}
		}
}
