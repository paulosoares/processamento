package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.processostf.Sumula;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface SumulaDao extends GenericDao<Sumula, Long> {
	
	public Long recuperarNumeroUltimaSumula () throws DaoException;
	
	public List<Sumula>pesquisarSumula(Long numeroSumula, Long processoPrecedente, String descricaoVerbete,
			Date dataAprovacao, String tipoSumula) throws DaoException;

	public Long recuperarNumeroUltimaSeqSumula() throws DaoException;
	
}

