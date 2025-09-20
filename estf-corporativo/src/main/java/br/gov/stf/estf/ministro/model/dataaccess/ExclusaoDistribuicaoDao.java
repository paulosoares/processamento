package br.gov.stf.estf.ministro.model.dataaccess;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.ministro.ExclusaoDistribuicao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ExclusaoDistribuicaoDao extends GenericDao<ExclusaoDistribuicao, Long> {
	
	public void inserirExclusao(ExclusaoDistribuicao exclusao) throws DaoException;
	public void alterarExclusao(ExclusaoDistribuicao exclusao) throws DaoException;
	public void removerExclusao(ExclusaoDistribuicao exclusao) throws DaoException;
	public List<ExclusaoDistribuicao> recuperarExclusao(ExclusaoDistribuicao exclusao) throws DaoException;
	public Boolean existeAusenciaPorPresidenteNoPeriodo(Date dtInicio, Date dtFim) throws DaoException;
	public Boolean existePeriodoParaMinistro(Date dtInicio, Date dtFim, Long codMinistro) throws DaoException;


}
