package br.gov.stf.estf.mag.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.mag.HistoricoDeslocamentoMag;
import br.gov.stf.estf.mag.model.dataaccess.HistoricoDeslocamentoMagDao;
import br.gov.stf.estf.mag.model.service.HistoricoDeslocamentoMagService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("historicoDeslocamentoMagService")
public class HistoricoDeslocamentoMagServiceImpl extends GenericServiceImpl<HistoricoDeslocamentoMag, Long, HistoricoDeslocamentoMagDao>  
implements HistoricoDeslocamentoMagService {

	public HistoricoDeslocamentoMagServiceImpl(HistoricoDeslocamentoMagDao dao) {
		super(dao);
	}
	
	public HistoricoDeslocamentoMag recuperarHistoricoDeslocamentoMag(Long id) throws ServiceException {
		
		HistoricoDeslocamentoMag historicoRecuperado = null;
		
		if(id == null)
			throw new NullPointerException("Identificação do histórico de deslocamento nula");
		
		try {
			
			historicoRecuperado = dao.recuperarHistoricoDeslocamentoMag(id);
		}
		catch(DaoException e) {
			throw new ServiceException(e);
		}
		
		return historicoRecuperado;
	}
	
	public List<HistoricoDeslocamentoMag> pesquisarHistoricoDeslocamentoMag(
			Long codigoSetor, String siglaClasseProcessual, Long numeroProcessual) 
	throws ServiceException {
		
		List<HistoricoDeslocamentoMag> historicosRecuperados = null;
		
		try {
			historicosRecuperados = dao.pesquisarHistoricoDeslocamentoMag(
					codigoSetor, siglaClasseProcessual, numeroProcessual);
		}
		catch(DaoException e) {
			throw new ServiceException(e);
		}
		
		return historicosRecuperados;
	}


}
