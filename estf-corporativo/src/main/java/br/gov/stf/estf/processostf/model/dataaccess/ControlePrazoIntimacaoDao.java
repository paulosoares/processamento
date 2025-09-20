package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.ControlePrazoIntimacao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ControlePrazoIntimacaoDao extends GenericDao<ControlePrazoIntimacao, Long>{
	public void persistirControlePrazoIntimacao(ControlePrazoIntimacao controlePrazoIntimacao)throws DaoException ;	
	public List<ControlePrazoIntimacao> recuperarProcessoIntimadoPendente() throws DaoException ;
	public void atualizaControlePrazoIntimacao(ControlePrazoIntimacao controlePrazoIntimacao)throws DaoException ;	
	public ControlePrazoIntimacao recuperarControlePrazoIntimacao(Long seqAndamentoP)throws DaoException ;
}
