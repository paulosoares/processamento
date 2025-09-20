package br.gov.stf.estf.processosetor.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.TipoFaseSetor;
import br.gov.stf.estf.entidade.processosetor.HistoricoFase;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface FaseDao extends GenericDao<HistoricoFase, Long> {

	public Boolean excluirHistoricoFase(HistoricoFase fase) 
	throws DaoException;
	
	public List<HistoricoFase> pesquisarFases(TipoFaseSetor faseSetor)
	throws DaoException;
	
}
