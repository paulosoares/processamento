package br.gov.stf.estf.usuario.model.service;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.usuario.NotificacaoLogon;
import br.gov.stf.estf.usuario.model.dataaccess.NotificacaoLogonDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface NotificacaoLogonService extends GenericService<NotificacaoLogon, Long, NotificacaoLogonDao> {
	
	public List<NotificacaoLogon> pesquisaNotificacao(
			String grupoTopico, Date dataInicio, Date dataFim, Date dataNotificacaoFim, String usuarioInlusao, Integer prioridade)
	throws  ServiceException;
	
}
