package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.Classe;
import br.gov.stf.estf.entidade.processostf.TipoIncidenteJulgamento;
import br.gov.stf.estf.processostf.model.dataaccess.TipoIncidenteJulgamentoDao;
import br.gov.stf.framework.model.dataaccess.hibernate.criterion.OrderCriterion;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface TipoIncidenteJulgamentoService extends GenericService<TipoIncidenteJulgamento, Long, TipoIncidenteJulgamentoDao> {

	public TipoIncidenteJulgamento recuperar(String sigla) throws ServiceException;
	
	public List<TipoIncidenteJulgamento> pesquisar(Boolean ativo) throws ServiceException;
	
	public List<TipoIncidenteJulgamento> pesquisarTipoJulgamento(String sigla, String descricao, Boolean ativo)	throws ServiceException;
	
	public List<TipoIncidenteJulgamento> pesquisarTodosTiposJulgamento(OrderCriterion... ordenacoes) throws ServiceException;

	public List<TipoIncidenteJulgamento> pesquisarTiposJulgamento(Classe classeProcessual) throws ServiceException;
	
}
