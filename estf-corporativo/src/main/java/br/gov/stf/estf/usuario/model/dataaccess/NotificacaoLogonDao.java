package br.gov.stf.estf.usuario.model.dataaccess;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.usuario.NotificacaoLogon;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface NotificacaoLogonDao extends GenericDao<NotificacaoLogon,Long>{
	public List<NotificacaoLogon> pesquisaNotificacao(
			String grupoTopico, Date dataInicio, Date dataFim, Date dataNotificacaoFim, String usuarioInlusao, Integer prioridade)
	throws DaoException;
}
