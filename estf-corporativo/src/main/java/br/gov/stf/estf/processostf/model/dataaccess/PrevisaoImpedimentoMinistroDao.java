package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.julgamento.PrevisaoImpedimentoMinistro;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface PrevisaoImpedimentoMinistroDao extends GenericDao<PrevisaoImpedimentoMinistro, Long> {
	
	public List<PrevisaoImpedimentoMinistro> recuperar(ObjetoIncidente<?> objetoIncidente) throws DaoException;
	
	public PrevisaoImpedimentoMinistro recuperar(Ministro ministro, ObjetoIncidente<?> objetoIncidente) throws DaoException;
	
}
