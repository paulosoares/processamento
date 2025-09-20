package br.gov.stf.estf.processostf.model.service;

import br.gov.stf.estf.entidade.processostf.ClasseUnificada;
import br.gov.stf.estf.entidade.processostf.ClasseUnificada.ClasseUnificadaId;
import br.gov.stf.estf.processostf.model.dataaccess.ClasseUnificadaDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;


/**
* Serice interface para a Entidade ClasseUnificada
* @see .ClasseUnificada
* @author SSGJ
*/

public interface ClasseUnificadaService extends GenericService <ClasseUnificada, ClasseUnificadaId, ClasseUnificadaDao> {
	public Short recuperarCodigoClasseUnificada (String sigClasse, String tipJulgamento, Long codRecurso) throws ServiceException; 
}



