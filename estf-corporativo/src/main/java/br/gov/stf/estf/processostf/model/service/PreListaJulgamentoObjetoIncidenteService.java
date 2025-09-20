package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamento;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamentoMotivoAlteracao;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamentoObjetoIncidente;
import br.gov.stf.estf.processostf.model.dataaccess.PreListaJulgamentoObjetoIncidenteDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface PreListaJulgamentoObjetoIncidenteService extends GenericService<PreListaJulgamentoObjetoIncidente, Long, PreListaJulgamentoObjetoIncidenteDao>{

	
	public final static Long SEM_LISTA_ID = -15L;
	
	List<PreListaJulgamentoObjetoIncidente> pesquisarProcessoEmLista(ObjetoIncidente<?> objetoIncidente,
			PreListaJulgamento preListaJulgamento) throws ServiceException;

	PreListaJulgamentoObjetoIncidente pesquisarPorObjetoIncidente(ObjetoIncidente<?> objetoIncidente);
	
	public void inserirObjetoIncidenteemPreListaJulgamento(Long idColuna, ObjetoIncidente<?> objetoIncidente, 
			PreListaJulgamentoMotivoAlteracao motivo) throws ServiceException;
}
