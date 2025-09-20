package br.gov.stf.estf.julgamento.model.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.julgamento.ProcessoTema;
import br.gov.stf.estf.julgamento.model.dataaccess.ProcessoTemaDao;
import br.gov.stf.estf.julgamento.model.service.ProcessoTemaService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("processoTemaService")
public class ProcessoTemaServiceImpl
extends GenericServiceImpl<ProcessoTema, Long, ProcessoTemaDao> 
implements ProcessoTemaService {

    public ProcessoTemaServiceImpl(ProcessoTemaDao dao) {
        super(dao);
    }

	public List<ProcessoTema> pesquisarProcessoTema(Long idTema,String sigClassePrecesso, Long numeroProcessual,  
			String tipoJulgamento,Long codTipoOcorrencia, Date dataOcorrencia)throws ServiceException {
		try{ 
			return dao.pesquisarProcessoTema(idTema, sigClassePrecesso, numeroProcessual, tipoJulgamento, codTipoOcorrencia, dataOcorrencia);
		}catch( DaoException  e){
			throw new ServiceException(e);
		}
	}

	public List<ProcessoTema> pesquisarProcessoTema(Long idTema,Long idIncidenteJulgamento,
			Long idObjetoIncidentePrincipal,String siglaTipoRecurso) 
			throws ServiceException {
		
		try {
			return dao.pesquisarProcessoTema(idTema, idIncidenteJulgamento, idObjetoIncidentePrincipal, siglaTipoRecurso);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public List<ProcessoTema> pesquisarProcessoTemaLCase(Long numTema, String sigClassePrecesso, Long numeroProcessual, String tipoJulgamento, Long codTipoOcorrencia) throws ServiceException{
		try{ 
			return dao.pesquisarProcessoTemaLCase(numTema, sigClassePrecesso, numeroProcessual, tipoJulgamento, codTipoOcorrencia);
		}catch( DaoException  e){
			throw new ServiceException(e);
		}		
	}
	
	@Override
	public void removerProcessoTema(Long idObjetoIncidente, Long numeroTema) throws ServiceException{
		try{ 
			dao.removerProcessoTema(idObjetoIncidente, numeroTema);
		}catch( DaoException  e){
			throw new ServiceException(e);
		}	
	}
	
}