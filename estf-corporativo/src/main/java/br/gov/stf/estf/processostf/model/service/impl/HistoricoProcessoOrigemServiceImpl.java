package br.gov.stf.estf.processostf.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.HistoricoProcessoOrigem;
import br.gov.stf.estf.processostf.model.dataaccess.HistoricoProcessoOrigemDao;
import br.gov.stf.estf.processostf.model.service.HistoricoProcessoOrigemService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("historicoProcessoOrigemService")
public class HistoricoProcessoOrigemServiceImpl extends GenericServiceImpl<HistoricoProcessoOrigem, Long, HistoricoProcessoOrigemDao> 
	implements HistoricoProcessoOrigemService {
    public HistoricoProcessoOrigemServiceImpl(HistoricoProcessoOrigemDao dao) { super(dao); }
	 
    public List<HistoricoProcessoOrigem> recuperarPorObjetoIncidente(Long objetoIncidente) throws ServiceException{
    	
    	List<HistoricoProcessoOrigem> listaHistorico = null;
    	try {
        	
    		listaHistorico = dao.recuperarPorObjetoIncidente(objetoIncidente);

        }
        catch( DaoException e ) {
            throw new ServiceException(e);
        }
        
        return listaHistorico;
    }
    
    public List<HistoricoProcessoOrigem> recuperarTodosPorObjetoIncidente(Long objetoIncidente) throws ServiceException{
    	
    	List<HistoricoProcessoOrigem> listaHistorico = null;
    	try {
        	
    		listaHistorico = dao.recuperarTodosPorObjetoIncidente(objetoIncidente);

        }
        catch( DaoException e ) {
            throw new ServiceException(e);
        }
        
        return listaHistorico;
    }
    
    
    public HistoricoProcessoOrigem recuperarOrigemInicialSTJ(Long idObjetoIncidente) throws ServiceException{
    	try {
    		return dao.recuperarOrigemInicialSTJ(idObjetoIncidente);
    	}catch(DaoException e){
    		throw new ServiceException(e);
    	}
    }


}
