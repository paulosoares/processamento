package br.gov.stf.estf.usuario.model.service;

import br.gov.stf.estf.entidade.usuario.Transacao;
import br.gov.stf.estf.usuario.model.dataaccess.TransacaoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface TransacaoService extends GenericService<Transacao, Long, TransacaoDao> {
	Transacao pesquisarTransacao(String sistema, int numOrdem) throws ServiceException;
	boolean usuarioPossuiTransacao(String sigUsuario, String sistema, String transacao) throws ServiceException;
}
