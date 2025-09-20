package br.gov.stf.estf.ministro.model.service;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.ministro.ExclusaoDistribuicao;
import br.gov.stf.estf.ministro.model.dataaccess.ExclusaoDistribuicaoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ExclusaoDistribuicaoService 
				extends GenericService<ExclusaoDistribuicao, Long, ExclusaoDistribuicaoDao> {
	
	public void inserirExclusao(ExclusaoDistribuicao exclusao) throws ServiceException;
	public void alterarExclusao(ExclusaoDistribuicao exclusao) throws ServiceException;
	public void removerExclusao(ExclusaoDistribuicao exclusao) throws ServiceException;
	public List<ExclusaoDistribuicao> recuperarExclusao(ExclusaoDistribuicao exclusao) throws ServiceException;
	public Boolean existeAusenciaPorPresidenteNoPeriodo(Date dtInicio, Date dtFim) throws ServiceException;
	public Boolean existePeriodoParaMinistro(Date dtInicio, Date dtFim, Long codMinistro) throws ServiceException;


}
