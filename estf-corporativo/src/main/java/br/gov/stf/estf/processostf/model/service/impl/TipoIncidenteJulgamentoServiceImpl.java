package br.gov.stf.estf.processostf.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.Classe;
import br.gov.stf.estf.entidade.processostf.TipoIncidenteJulgamento;
import br.gov.stf.estf.processostf.model.dataaccess.TipoIncidenteJulgamentoDao;
import br.gov.stf.estf.processostf.model.service.TipoIncidenteJulgamentoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.criterion.OrderCriterion;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("tipoIncidenteJulgamentoService")
public class TipoIncidenteJulgamentoServiceImpl extends GenericServiceImpl<TipoIncidenteJulgamento, Long, TipoIncidenteJulgamentoDao> 
	implements TipoIncidenteJulgamentoService {

	protected TipoIncidenteJulgamentoServiceImpl(TipoIncidenteJulgamentoDao dao) {
		super(dao);
	}

	public TipoIncidenteJulgamento recuperar(String sigla)
			throws ServiceException {
		TipoIncidenteJulgamento tij = null;
		try {
			tij = dao.recuperar(sigla);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		return tij;
	}
	
	public List<TipoIncidenteJulgamento> pesquisar(Boolean ativo) throws ServiceException {
		try {
			return dao.pesquisar(ativo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public List<TipoIncidenteJulgamento> pesquisarTipoJulgamento(String sigla, String descricao, Boolean ativo)	throws ServiceException {
		try {
			return dao.pesquisarTipoJulgamento(sigla, descricao, ativo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public List<TipoIncidenteJulgamento> pesquisarTodosTiposJulgamento(OrderCriterion... ordenacoes) throws ServiceException {
		try {
			return dao.pesquisarTodosTiposJulgamento(ordenacoes);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<TipoIncidenteJulgamento> pesquisarTiposJulgamento(Classe classeProcessual) throws ServiceException {
		try {
			return dao.pesquisarTiposJulgamento(classeProcessual);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}
