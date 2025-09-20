package br.gov.stf.estf.processostf.model.dataaccess;

import br.gov.stf.estf.entidade.processostf.ClasseUnificada;
import br.gov.stf.estf.entidade.processostf.ClasseUnificada.ClasseUnificadaId;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;


/**
* Interface DAO para a entidade ClasseUnificada
* @see .ClasseUnificada
* @author SSGJ
*/

public interface ClasseUnificadaDao extends GenericDao <ClasseUnificada, ClasseUnificadaId> {
	public Short recuperarCodigoClasseUnificada (String sigClasse, String tipJulgamento, Long codRecurso) 
	throws DaoException;
}


