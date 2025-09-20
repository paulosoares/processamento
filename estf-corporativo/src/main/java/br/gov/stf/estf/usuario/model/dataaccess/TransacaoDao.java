package br.gov.stf.estf.usuario.model.dataaccess;

import br.gov.stf.estf.entidade.usuario.Transacao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TransacaoDao extends GenericDao<Transacao,Long>{
	Transacao recuperarPorSistemaOrdem(String sistema, int numOrdem) throws DaoException;
	boolean usuarioPossuiTransacao(String sigUsuario, String sistema, String transacao) throws DaoException;
	
}
