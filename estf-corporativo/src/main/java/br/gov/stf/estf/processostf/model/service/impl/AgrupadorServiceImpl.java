package br.gov.stf.estf.processostf.model.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.Agrupador;
import br.gov.stf.estf.entidade.processostf.AgrupadorObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamento;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamentoMotivoAlteracao;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamentoObjetoIncidente;
import br.gov.stf.estf.processostf.model.dataaccess.AgrupadorDao;
import br.gov.stf.estf.processostf.model.service.AgrupadorObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.AgrupadorService;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.PreListaJulgamentoObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.PreListaJulgamentoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("agrupadorService")
public class AgrupadorServiceImpl extends GenericServiceImpl<Agrupador, Long, AgrupadorDao>
		implements AgrupadorService {
	
	@Autowired
	PreListaJulgamentoService preListaJulgamentoService;
	
	@Autowired
	PreListaJulgamentoObjetoIncidenteService preListaJulgamentoObjetoIncidenteService;
	
	@Autowired
	ObjetoIncidenteService objetoIncidenteService;
	
	@Autowired
	AgrupadorObjetoIncidenteService agrupadorObjetoIncidenteService;
	
	public AgrupadorServiceImpl(AgrupadorDao dao) {
		super(dao);
	}

	@Override
	public List<Agrupador> recuperarCategoriasDoSetor(Long idSetor) throws ServiceException {
		try {
			return dao.pesquisarPorSetor(idSetor);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public List<Agrupador> recuperarCategoriasDoSetor(Long idSetor, String texto) throws ServiceException {
		try {
			return dao.pesquisarPorSetor(idSetor, texto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public String getCategoriaDoIncidente(Long idObjetoIncidente, Long idSetor) throws ServiceException{
		try {
			return dao.getCategoriaDoIncidente(idObjetoIncidente, idSetor);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void removerCategoriaDoObjetoIncidente(Long idObjetoIncidente, Long idCategoria) throws ServiceException {
		try {
			dao.removerObjetoIncidenteDoAgrupador(idObjetoIncidente, idCategoria);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public void alterarCategoriaDoObjetoIncidente(Long idObjetoIncidente, Long idCategoriaAnterior, Long idCategoriaNova) 
			throws ServiceException {
		try {
			dao.removerObjetoIncidenteDoAgrupador(idObjetoIncidente, idCategoriaAnterior);
			dao.inserirObjetoIncidenteNoAgrupador(idObjetoIncidente, idCategoriaNova);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		
	}
	
	@Override
	public boolean objetoIncidentePossuiCategorias(Long idObjetoIncidente) throws ServiceException {
		try {
			return (dao.recuperarCategoriasDoObjetoIncidente(idObjetoIncidente).size() > 0);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public boolean objetoIncidentePossuiCategoriasNoSetor(Long idObjetoIncidente, Long idSetor) throws ServiceException {
		try {
			return (dao.recuperarCategoriasDoObjetoIncidenteNoSetor(idObjetoIncidente, idSetor).size() > 0);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public List<Agrupador> recuperarCategoriasDoObjetoIncidente(Long idObjetoIncidente) throws ServiceException {
		try {
			return dao.recuperarCategoriasDoObjetoIncidente(idObjetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public void categorizarIncidente(ObjetoIncidente<?> objetoIncidente, Agrupador agrupador, Setor setor)
			throws ServiceException, DaoException {
		
		if (agrupador == null || agrupador.getId() == -1L) {
			List<AgrupadorObjetoIncidente> entidade = agrupadorObjetoIncidenteService.pesquisarAgrupadorObjetoIncidente(null, objetoIncidente);
			agrupadorObjetoIncidenteService.excluirTodos(entidade);
			
			PreListaJulgamentoObjetoIncidente relacionamentoAtivo = preListaJulgamentoService.recuperarPreListaJulgamentoObjetoIncidente(objetoIncidente);
			
			if (relacionamentoAtivo != null) {
				if (relacionamentoAtivo.getRevisado().equals(Boolean.FALSE) && relacionamentoAtivo.getMotivo().equals(PreListaJulgamentoMotivoAlteracao.AUTOMATICA)) {
					preListaJulgamentoObjetoIncidenteService.excluir(relacionamentoAtivo);
				}
			}
			
		} else {
			List<AgrupadorObjetoIncidente> entidade = agrupadorObjetoIncidenteService.pesquisarAgrupadorObjetoIncidente(agrupador, objetoIncidente);
			agrupadorObjetoIncidenteService.excluirTodos(entidade);
			inserirObjetoIncidenteNoAgrupador(objetoIncidente.getId(), agrupador.getId());
			boolean julgado = objetoIncidenteService.isObjetoIncidenteJulgado(objetoIncidente);
			
			PreListaJulgamento preListaJulgamento = preListaJulgamentoService.recuperarPreListaPorCategoria(agrupador);
			PreListaJulgamentoObjetoIncidente relacionamentoAtivo = preListaJulgamentoService.recuperarPreListaJulgamentoObjetoIncidente(objetoIncidente);
			
			if (!julgado && preListaJulgamento != null){
				if (relacionamentoAtivo == null) {
					relacionamentoAtivo = new PreListaJulgamentoObjetoIncidente();
					relacionamentoAtivo.setMotivo(PreListaJulgamentoMotivoAlteracao.AUTOMATICA);
					relacionamentoAtivo.setObjetoIncidente(objetoIncidente);
					relacionamentoAtivo.setPreListaJulgamento(preListaJulgamento);
					relacionamentoAtivo.setRevisado(false);
					preListaJulgamentoObjetoIncidenteService.salvar(relacionamentoAtivo);
				} else { // Se já existir, então
					if (relacionamentoAtivo.getRevisado().equals(Boolean.FALSE)  // verifica se o processo consta como não revisado
							&& relacionamentoAtivo.getMotivo().equals(PreListaJulgamentoMotivoAlteracao.AUTOMATICA)) { // verifica se o processo foi incluído de forma automática
						relacionamentoAtivo.setPreListaJulgamento(preListaJulgamento); // altera a lista de julgamento do relacionamento
						preListaJulgamentoObjetoIncidenteService.salvar(relacionamentoAtivo);
					}
				}
			}
		}
	}
	
	@Override
	public void inserirObjetoIncidenteNoAgrupador(Long idObjetoIncidente, Long idCategoriaNova) throws ServiceException {
		try {
			dao.inserirObjetoIncidenteNoAgrupador(idObjetoIncidente, idCategoriaNova);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}
