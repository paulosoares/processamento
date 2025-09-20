package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.processostf.AcordaoAgendado;
import br.gov.stf.estf.entidade.processostf.AcordaoAgendado.AcordaoAgendadoId;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface AcordaoAgendadoDao extends GenericDao<AcordaoAgendado, AcordaoAgendadoId> {
	public List<AcordaoAgendado> pesquisarSessaoEspecial (
			Boolean recuperarOcultos, String ...siglaClasseProcessual) 
	throws DaoException;
	
	public List<AcordaoAgendado> pesquisarComposto (Date dataComposicaoDj) throws DaoException;
}
