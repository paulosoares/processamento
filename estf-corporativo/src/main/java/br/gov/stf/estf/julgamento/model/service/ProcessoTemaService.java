package br.gov.stf.estf.julgamento.model.service;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.julgamento.ProcessoTema;
import br.gov.stf.estf.julgamento.model.dataaccess.ProcessoTemaDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ProcessoTemaService extends GenericService<ProcessoTema, Long, ProcessoTemaDao> {

	public List<ProcessoTema> pesquisarProcessoTema(Long idTema,String sigClassePrecesso, Long numeroProcessual,  
			String tipoJulgamento,Long codTipoOcorrencia, Date dataOcorrencia) throws ServiceException;
	
	public List<ProcessoTema> pesquisarProcessoTema(Long idTema,Long idIncidenteJulgamento,
			Long idObjetoIncidentePrincipal,String siglaTipoRecurso) throws ServiceException;

	List<ProcessoTema> pesquisarProcessoTemaLCase(Long numTema,
			String sigClassePrecesso, Long numeroProcessual,
			String tipoJulgamento, Long codTipoOcorrencia)
			throws ServiceException;

	void removerProcessoTema(Long idObjetoIncidente, Long numeroTema) throws ServiceException;
	

	
}
