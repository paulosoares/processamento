package br.gov.stf.estf.processostf.model.dataaccess;


import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.processostf.IncidenteDistribuicao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
* DAO interface para a entidade Distribuicao.
* @see Distribuicao
*/
public interface IncidenteDistribuicaoDao extends GenericDao <IncidenteDistribuicao, Long> {
	public List<IncidenteDistribuicao> pesquisar (Integer numAta, Date dataAta) throws DaoException;
	
}

