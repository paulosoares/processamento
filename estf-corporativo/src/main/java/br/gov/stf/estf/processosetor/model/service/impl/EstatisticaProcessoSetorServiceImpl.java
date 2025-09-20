package br.gov.stf.estf.processosetor.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processosetor.EstatisticaProcessoSetorSecao;
import br.gov.stf.estf.processosetor.model.dataaccess.EstatisticaProcessoSetorDao;
import br.gov.stf.estf.processosetor.model.service.EstatisticaProcessoSetorService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;

@Service("estatisticaProcessoSetorService")
public class EstatisticaProcessoSetorServiceImpl implements EstatisticaProcessoSetorService {

	private final EstatisticaProcessoSetorDao estatisticaProcessoSetorDao;

	public EstatisticaProcessoSetorServiceImpl(EstatisticaProcessoSetorDao estatisticaProcessoSetorDao) {
		this.estatisticaProcessoSetorDao = estatisticaProcessoSetorDao;
	}
	
	public List<EstatisticaProcessoSetorSecao> gerarEstatisticaProcessoSetorSecao(
			Long idSetor) throws ServiceException {
			
		List<EstatisticaProcessoSetorSecao> result;
		try{
		
			result = estatisticaProcessoSetorDao.gerarEstatisticaProcessoSetorSecao( idSetor );
		
		}catch(DaoException ex){
			
			throw new ServiceException(ex);
		}
		
		return result;
	}
	
	public List<EstatisticaProcessoSetorSecao> gerarEstatisticaProcessoFaseSetor(
			Long idSetor , Boolean status ) throws ServiceException{
		
		List<EstatisticaProcessoSetorSecao> result;
		
		try{
			result = estatisticaProcessoSetorDao.gerarEstatisticaProcessoFaseSetor(idSetor, status);
		
		}catch(DaoException ex ){
			
			throw new ServiceException(ex);
		}
		
		
		return result;
	}


}
