package br.gov.stf.estf.processostf.model.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamento;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamentoMotivoAlteracao;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamentoObjetoIncidente;
import br.gov.stf.estf.processostf.model.dataaccess.PreListaJulgamentoObjetoIncidenteDao;
import br.gov.stf.estf.processostf.model.service.PreListaJulgamentoObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.PreListaJulgamentoService;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;


@Service("preListaJulgamentoObjetoIncidenteService")
public class PreListaJulgamentoObjetoIncidenteServiceImpl extends GenericServiceImpl<PreListaJulgamentoObjetoIncidente, Long, PreListaJulgamentoObjetoIncidenteDao> implements PreListaJulgamentoObjetoIncidenteService  {
	
	@Autowired
	private PreListaJulgamentoService preListaJulgamentoService;
	
	
	public PreListaJulgamentoObjetoIncidenteServiceImpl(
			PreListaJulgamentoObjetoIncidenteDao dao) {
		super(dao);
	}

	@Override
	public List<PreListaJulgamentoObjetoIncidente> pesquisarProcessoEmLista(ObjetoIncidente<?> objetoIncidente,
			PreListaJulgamento preListaJulgamento) throws ServiceException {
		return dao.pesquisarProcessoEmLista(objetoIncidente, preListaJulgamento);
	}

	@Override
	public PreListaJulgamentoObjetoIncidente pesquisarPorObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		return dao.pesquisarPorObjetoIncidente(objetoIncidente);
	}

	@Override
	public void inserirObjetoIncidenteemPreListaJulgamento(Long idColuna,
			ObjetoIncidente<?> objetoIncidente,
			PreListaJulgamentoMotivoAlteracao motivo) throws ServiceException {
		
		PreListaJulgamentoObjetoIncidente relacionamentoVelho = preListaJulgamentoService.recuperarPreListaJulgamentoObjetoIncidente(objetoIncidente);
		
		if (relacionamentoVelho != null){
			this.excluir(relacionamentoVelho);
		}
		
		if (idColuna != SEM_LISTA_ID && idColuna != null) {
			PreListaJulgamentoObjetoIncidente relacionamentoNovo = new PreListaJulgamentoObjetoIncidente();
			
			PreListaJulgamento novaPreLista = preListaJulgamentoService.recuperarPorId(idColuna);
			relacionamentoNovo.setPreListaJulgamento(novaPreLista);
			relacionamentoNovo.setObjetoIncidente(objetoIncidente);
			
			if (motivo == null)
				relacionamentoNovo.setMotivo(PreListaJulgamentoMotivoAlteracao.MANUAL);
			else
				relacionamentoNovo.setMotivo(motivo);
			
			relacionamentoNovo.setRevisado(false);
			this.salvar(relacionamentoNovo);
		}
		
	}
	
	

}
