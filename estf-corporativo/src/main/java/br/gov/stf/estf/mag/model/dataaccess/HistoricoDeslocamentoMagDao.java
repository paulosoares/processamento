package br.gov.stf.estf.mag.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.mag.HistoricoDeslocamentoMag;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface HistoricoDeslocamentoMagDao extends GenericDao<HistoricoDeslocamentoMag, Long> {

	public HistoricoDeslocamentoMag recuperarHistoricoDeslocamentoMag(Long id) 
	throws DaoException;
	
	public List<HistoricoDeslocamentoMag> pesquisarHistoricoDeslocamentoMag(
			Long codigoSetor, String siglaClasseProcessual, Long numeroProcessual) 
	throws DaoException;
}
