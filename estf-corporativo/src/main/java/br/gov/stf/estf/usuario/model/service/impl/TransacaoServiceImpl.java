package br.gov.stf.estf.usuario.model.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.usuario.Transacao;
import br.gov.stf.estf.usuario.model.dataaccess.TransacaoDao;
import br.gov.stf.estf.usuario.model.service.TransacaoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("transacaoService")
public class TransacaoServiceImpl extends GenericServiceImpl<Transacao, Long, TransacaoDao> implements TransacaoService {
		@Autowired
	    public TransacaoServiceImpl(TransacaoDao dao) { 
			super(dao); 
		}

		public Transacao pesquisarTransacao(String sistema, int numOrdem)
				throws ServiceException {
			try {
				return dao.recuperarPorSistemaOrdem(sistema, numOrdem);
			} catch (DaoException e) {
				throw new ServiceException("Erro ao pesquisar transacao", e);
			}
		}

		@Override
		public boolean usuarioPossuiTransacao(String sigUsuario, String sistema, String transacao) throws ServiceException {
			try {
				return dao.usuarioPossuiTransacao(sigUsuario, sistema, transacao);
			} catch (DaoException e) {
				throw new ServiceException("Erro ao pesquisar transacao", e);
			}
		}
}
