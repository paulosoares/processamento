package br.gov.stf.estf.julgamento.model.dataaccess;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.julgamento.ProcessoTema;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ProcessoTemaDao extends GenericDao<ProcessoTema, Long> {
	
	public List<ProcessoTema> pesquisarProcessoTema(Long idTema,String sigClassePrecesso, Long numeroProcessual,  
			String tipoJulgamento,Long codTipoOcorrencia, Date dataOcorrencia)throws DaoException;
	
	public List<ProcessoTema> pesquisarProcessoTema(Long idTema,Long idIncidenteJulgamento,
			Long idObjetoIncidentePrincipal,String siglaTipoRecurso)  throws DaoException;

	List<ProcessoTema> pesquisarProcessoTemaLCase(Long numTema,
			String sigClassePrecesso, Long numeroProcessual,
			String tipoJulgamento, Long codTipoOcorrencia) throws DaoException;

	void removerProcessoTema(Long idObjetoIncidente, Long numeroTema) throws DaoException;
	


}
