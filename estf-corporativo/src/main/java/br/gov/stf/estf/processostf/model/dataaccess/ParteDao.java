package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.Parte;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ParteDao extends GenericDao<Parte, Long> {
	
	public List<Parte> pesquisarPartes(Long idObjetoIncidente) throws DaoException;
	
	public List<Parte> pesquisarPartes(Long idObjetoIncidente, List<Long> codigosCategoria) throws DaoException;

}
