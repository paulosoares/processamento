package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.Classe;
import br.gov.stf.estf.entidade.processostf.TipoIncidenteJulgamento;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;
import br.gov.stf.framework.model.dataaccess.hibernate.criterion.OrderCriterion;

public interface TipoIncidenteJulgamentoDao extends GenericDao<TipoIncidenteJulgamento, Long>{
	
	public TipoIncidenteJulgamento recuperar(String sigla) throws DaoException;
	
	public List<TipoIncidenteJulgamento> pesquisar(Boolean ativo) throws DaoException;
	
	public List<TipoIncidenteJulgamento> pesquisarTipoJulgamento(String sigla, String descricao, Boolean ativo)	throws DaoException;
	
	public List<TipoIncidenteJulgamento> pesquisarTodosTiposJulgamento(OrderCriterion... ordenacoes) throws DaoException;

	public List<TipoIncidenteJulgamento> pesquisarTiposJulgamento(Classe classeProcessual) throws DaoException;
	
}
