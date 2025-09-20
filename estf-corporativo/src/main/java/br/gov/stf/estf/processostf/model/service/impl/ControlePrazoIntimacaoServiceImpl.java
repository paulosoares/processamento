package br.gov.stf.estf.processostf.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.ControlePrazoIntimacao;
import br.gov.stf.estf.processostf.model.dataaccess.ControlePrazoIntimacaoDao;
import br.gov.stf.estf.processostf.model.service.ControlePrazoIntimacaoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("controlePrazoIntimacaoService")
public class ControlePrazoIntimacaoServiceImpl extends GenericServiceImpl<ControlePrazoIntimacao, Long, ControlePrazoIntimacaoDao> 
	implements ControlePrazoIntimacaoService {
    public ControlePrazoIntimacaoServiceImpl(ControlePrazoIntimacaoDao dao) { super(dao); }
    
    public void persistirControlePrazoIntimcao(
			ControlePrazoIntimacao controlePrazoIntimacao)
			throws ServiceException {
		try {
			dao.persistirControlePrazoIntimacao(controlePrazoIntimacao);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}	
		
	}

	
	public List<ControlePrazoIntimacao> recuperarProcessoIntimadoPendente()throws ServiceException {
		
		try {
			return dao.recuperarProcessoIntimadoPendente();
			
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}	
		
	}
	
	public void atualizaControlePrazoIntimacao(
			ControlePrazoIntimacao controlePrazoIntimacao)
			throws ServiceException {
		try {
			dao.atualizaControlePrazoIntimacao(controlePrazoIntimacao);
			
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}	
		
	}

	
	public ControlePrazoIntimacao recuperarControlePrazoIntimacao(
			Long seqAndamentoProcesso) throws ServiceException {
	
		try {
			return dao.recuperarControlePrazoIntimacao(seqAndamentoProcesso);
			
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		
	}


}
