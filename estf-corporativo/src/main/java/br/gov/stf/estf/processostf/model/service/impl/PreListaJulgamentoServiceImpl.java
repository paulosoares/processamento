package br.gov.stf.estf.processostf.model.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.star.uno.RuntimeException;

import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.Agrupador;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamento;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamentoMotivoAlteracao;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamentoObjetoIncidente;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.julgamento.model.dataaccess.hibernate.PreListaJulgamentoDaoHibernate;
import br.gov.stf.estf.processostf.model.dataaccess.PreListaJulgamentoDao;
import br.gov.stf.estf.processostf.model.service.AgrupadorService;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.PreListaJulgamentoObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.PreListaJulgamentoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("preListaJulgamentoService")
public class PreListaJulgamentoServiceImpl extends GenericServiceImpl<PreListaJulgamento, Long, PreListaJulgamentoDao> implements PreListaJulgamentoService {

	@Autowired
	PreListaJulgamentoObjetoIncidenteService preListaJulgamentoObjetoIncidenteService;
	
	@Autowired
	private ObjetoIncidenteService objetoIncidenteService;
	
	@Autowired
	AgrupadorService agrupadorService;
	
	public PreListaJulgamentoServiceImpl(PreListaJulgamentoDao dao) {
		super(dao);
	}

	@Override
	public PreListaJulgamentoObjetoIncidente alterarProcessoParaRevisado(ObjetoIncidente<?> objetoIncidente,
			PreListaJulgamento preListaJulgamento, Boolean revisado, Usuario revisor)
			throws ServiceException {
		
		List<PreListaJulgamentoObjetoIncidente> listaRelacionamentos = preListaJulgamentoObjetoIncidenteService.pesquisarProcessoEmLista(objetoIncidente, preListaJulgamento);
		
		if (listaRelacionamentos == null || listaRelacionamentos.isEmpty()){
			throw new RuntimeException("Processo não encontrado na lista informada.");
		}
		
		PreListaJulgamentoObjetoIncidente relacionamento = null;
		
		for(PreListaJulgamentoObjetoIncidente rel : listaRelacionamentos) {
			rel.setRevisado(revisado);
			
			if (Boolean.TRUE.equals(revisado)) {
				rel.setUsuarioRevisor(revisor);
				rel.setDataRevisao(new Date());
			} else {
				rel.setUsuarioRevisor(null);
				rel.setDataRevisao(null);
			}
				preListaJulgamentoObjetoIncidenteService.alterar(rel);
				preListaJulgamentoObjetoIncidenteService.flushSession();
				relacionamento = rel;
		}
		return relacionamento;
	}

	public List<PreListaJulgamento> listarPreListasJulgamentoDoSetor(Setor setor) throws ServiceException {
		return listarPreListasJulgamentoDoSetor(setor, false);
	}
	
	@Override
	public List<PreListaJulgamento> listarPreListasJulgamentoDoSetor(Setor setor, Boolean ordenarListasPorData) throws ServiceException {
    	try {
    		return dao.listarPreListasJulgamentoDoSetor(setor, ordenarListasPorData);
    	} catch (DaoException e) {
    		throw new ServiceException(e);
    	}
	}

	@Override
	public PreListaJulgamento recuperarPreListaJulgamentoAtiva  (
			ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		
		PreListaJulgamentoObjetoIncidente relacionamento = recuperarPreListaJulgamentoObjetoIncidente(objetoIncidente);
		
		if (relacionamento != null){
			return relacionamento.getPreListaJulgamento();
		}
		return null;
	}
	
	@Override
	public PreListaJulgamentoObjetoIncidente recuperarPreListaJulgamentoObjetoIncidente (ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		return preListaJulgamentoObjetoIncidenteService.pesquisarPorObjetoIncidente(objetoIncidente);
	}

	@Override
	public void removerProcessosRevisados(PreListaJulgamento preLista) throws ServiceException {
		List<PreListaJulgamentoObjetoIncidente> listaARemover = new ArrayList<PreListaJulgamentoObjetoIncidente>();
		if (preLista.getObjetosIncidentes() != null && preLista.getObjetosIncidentes().size() >0){
			for(PreListaJulgamentoObjetoIncidente relacionamento : preLista.getObjetosIncidentes()){
				if (relacionamento.getRevisado()){
					preListaJulgamentoObjetoIncidenteService.excluir(relacionamento);
					listaARemover.add(relacionamento);
				}
			}
		}
		preLista.getObjetosIncidentes().removeAll(listaARemover);
	}

	@Override
	public PreListaJulgamento recuperarPorListaJulgamento(ListaJulgamento listaJulgamento) throws ServiceException {
		try {
			return dao.recuperarPorListaJulgamento(listaJulgamento);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public PreListaJulgamento recuperarPreListaPorCategoria(Agrupador agrupador) throws ServiceException {
		try {
			return dao.recuperarPreListaPorCategoria(agrupador);
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public PreListaJulgamento salvar(PreListaJulgamento entidade) throws ServiceException {
		if (entidade.getId() == null || entidade.getId() > PreListaJulgamentoDaoHibernate.LIMITE_PRE_LISTA_DESTAQUES_CANCELADOS) {
			try {
				entidade = dao.salvar(entidade);
				List<Agrupador> categoriasNovas = (List<Agrupador>) agrupadorService.recuperarPorId(entidade.getId());
				entidade.setCategorias(categoriasNovas);

				// Caso o usuário altere as categorias vinculadas à pré-lista, 
				// o sistema deve remover os processos vinculados a categorias removidas
				List<ObjetoIncidente<?>> incidentesCategorias = new ArrayList<ObjetoIncidente<?>>();
				
				if (entidade.getCategorias() != null && entidade.getCategorias().size()>0) {
					for (Agrupador agrupador : entidade.getCategorias()) {
						agrupador = agrupadorService.recuperarPorId(agrupador.getId());
						Hibernate.initialize(agrupador.getObjetosIncidentes());
						incidentesCategorias.addAll(agrupador.getObjetosIncidentes());
					}
				}
				
				List<PreListaJulgamentoObjetoIncidente> listaARemover = new ArrayList<PreListaJulgamentoObjetoIncidente>();
				if (entidade.getObjetosIncidentes() != null) {
					for (PreListaJulgamentoObjetoIncidente relacionamento : entidade.getObjetosIncidentes()) {
						if (!incidentesCategorias.contains(relacionamento.getObjetoIncidente())) {
							if (relacionamento.getRevisado().equals(Boolean.FALSE) && relacionamento.getMotivo().equals(PreListaJulgamentoMotivoAlteracao.AUTOMATICA))
								listaARemover.add(relacionamento); // remove os processos que pertencem a categorias que não estão associadas à pré-lista
						} else {
							incidentesCategorias.remove(relacionamento.getObjetoIncidente());
						}
					}	
				}
				
				preListaJulgamentoObjetoIncidenteService.excluirTodos(listaARemover);
				
				if (entidade.getObjetosIncidentes() != null)
					entidade.getObjetosIncidentes().removeAll(listaARemover);
				
				// e adicionar os processos NÃO JULGADOS de categorias adicionadas
				for (ObjetoIncidente<?> oi : incidentesCategorias) {
					if (!objetoIncidenteService.isObjetoIncidenteJulgado(oi)) {
						// Remove o objeto incidente de outras pré-listas
						PreListaJulgamentoObjetoIncidente pesquisaPorObjetoIncidente = preListaJulgamentoObjetoIncidenteService.pesquisarPorObjetoIncidente(oi);
	
						if (pesquisaPorObjetoIncidente == null) {
							incluirObjetoIncidenteNaPreLista(entidade, oi);
						} else {
								if (pesquisaPorObjetoIncidente.getRevisado().equals(Boolean.FALSE) && pesquisaPorObjetoIncidente.getMotivo().equals(PreListaJulgamentoMotivoAlteracao.AUTOMATICA)) {
									preListaJulgamentoObjetoIncidenteService.excluir(pesquisaPorObjetoIncidente);
									entidade.getObjetosIncidentes().remove(pesquisaPorObjetoIncidente);
									incluirObjetoIncidenteNaPreLista(entidade, oi);
								}
						}
					}
				}
			} catch (DaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new ServiceException(e.getMessage());
			}
		}
		return recuperarPorId(entidade.getId());
		
	}

	/**
	 * Adiciona o objeto incidente nesta pré-lista
	 * @param entidade
	 * @param oi
	 * @throws ServiceException
	 */
	@Override
	public PreListaJulgamentoObjetoIncidente incluirObjetoIncidenteNaPreLista(PreListaJulgamento entidade,ObjetoIncidente<?> oi, ListaJulgamento listaJulgamento) throws ServiceException {
		PreListaJulgamentoObjetoIncidente relacionamento = new PreListaJulgamentoObjetoIncidente();
		relacionamento.setMotivo(PreListaJulgamentoMotivoAlteracao.AUTOMATICA);
		relacionamento.setObjetoIncidente(oi);
		relacionamento.setPreListaJulgamento(entidade);
		relacionamento.setRevisado(false);
		relacionamento.setListaJulgamento(listaJulgamento);
		relacionamento = preListaJulgamentoObjetoIncidenteService.salvar(relacionamento);
		
		if (entidade.getObjetosIncidentes() == null)
			entidade.setObjetosIncidentes(new ArrayList<PreListaJulgamentoObjetoIncidente>());
		
		entidade.getObjetosIncidentes().add(relacionamento);
		
		return relacionamento;
	}
	
	public Long recuperarProximoSequencialParaNomeLista(Integer ano, Long codMinistro, Boolean avulso) throws ServiceException{
		Long proximoSequencial = 0L;
		try{
			proximoSequencial = dao.getProximoSequencialParaNomeLista(ano, codMinistro, avulso);
			
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		return proximoSequencial;
	}

	@Override
	public PreListaJulgamento recuperarPreListaDeCancelamentoDePedidoDeDestaque(Setor setorDoUsuario) throws ServiceException {

		List<PreListaJulgamento> listarPreListasJulgamentoDoSetor = listarPreListasJulgamentoDoSetor(setorDoUsuario);

		// Pré-listas têm id <= 40
		for (PreListaJulgamento preListaJulgamento : listarPreListasJulgamentoDoSetor)
			if (preListaJulgamento.getId() <= PreListaJulgamentoDaoHibernate.LIMITE_PRE_LISTA_DESTAQUES_CANCELADOS)
				return preListaJulgamento;

		throw new ServiceException("Não foi encontrada a pré-lista de Destaques Cancelados.");
	}

	@Override
	public PreListaJulgamentoObjetoIncidente incluirObjetoIncidenteNaPreLista(PreListaJulgamento preLista, ObjetoIncidente<?>oi) throws ServiceException {
		return incluirObjetoIncidenteNaPreLista(preLista, oi, null);
	}
	
	@Override
	public PreListaJulgamento recuperarPorIdComObjetoIncidente(Long id) throws ServiceException {
		try {
			PreListaJulgamento prelista = recuperarPorId(id);
			
			for (PreListaJulgamentoObjetoIncidente ploi : prelista.getObjetosIncidentes()) {
				Hibernate.initialize(ploi.getObjetoIncidente());
				Hibernate.initialize(ploi.getObjetoIncidente().getIdentificacao());
				Hibernate.initialize(ploi.getListaJulgamento());
				Hibernate.initialize(ploi.getListaJulgamento().getSessao());
				Hibernate.initialize(ploi.getListaJulgamento().getSessao().getColegiado());
				Hibernate.initialize(ploi.getListaJulgamento().getListaProcessoListaJulgamento());
			}
			
			return prelista;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

}