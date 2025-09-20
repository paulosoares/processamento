package br.gov.stf.estf.ministro.model.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.ministro.ExclusaoDistribuicao;
import br.gov.stf.estf.ministro.model.dataaccess.ExclusaoDistribuicaoDao;
import br.gov.stf.estf.ministro.model.service.ExclusaoDistribuicaoService;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("exclusaoDistribuicaoService")
public class ExclusaoDistribuicaoServiceImpl extends GenericServiceImpl<ExclusaoDistribuicao, Long, ExclusaoDistribuicaoDao> implements ExclusaoDistribuicaoService {
	public ExclusaoDistribuicaoServiceImpl(ExclusaoDistribuicaoDao dao)	{
		super(dao);
	}
	
	@Override
	public void inserirExclusao(ExclusaoDistribuicao exclusao) throws ServiceException {
		try {
			dao.salvar(exclusao);
			dao.limparSessao();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public void alterarExclusao(ExclusaoDistribuicao exclusao) throws ServiceException {
		try {
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public void removerExclusao(ExclusaoDistribuicao exclusao) throws ServiceException {
		try {
			dao.excluir(exclusao);
			dao.limparSessao();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public List<ExclusaoDistribuicao> recuperarExclusao(ExclusaoDistribuicao exclusao) throws ServiceException {
		try {
			return dao.recuperarExclusao(exclusao);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public Boolean existeAusenciaPorPresidenteNoPeriodo(Date dtInicio, Date dtFim) throws ServiceException {
		try {
			return dao.existeAusenciaPorPresidenteNoPeriodo(dtInicio, dtFim);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Boolean existePeriodoParaMinistro(Date dtInicio, Date dtFim, Long codMinistro) throws ServiceException {
		try {
			return dao.existePeriodoParaMinistro(dtInicio, dtFim, codMinistro);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}


}
