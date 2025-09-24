package br.jus.stf.estf.decisao.objetoincidente.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.usuario.UsuarioIncidentePesquisa;
import br.gov.stf.estf.entidade.usuario.UsuarioIncidentePesquisa.UsuarioIncidentePesquisaId;
import br.gov.stf.estf.usuario.model.service.UsuarioIncidentePesquisaService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;

public abstract class AbstractExportarProcessosActionFacesBean<T> extends ActionSupport<T> {
		
	
	@Autowired
	private UsuarioIncidentePesquisaService usuarioIncidentePesquisaService;
	
private String message;
	
	
	public void load(){
		List<ObjetoIncidente<?>> objetosIncidentes = getListaObjetosIncidentes();
		if (objetosIncidentes != null && !objetosIncidentes.isEmpty()){
			List<UsuarioIncidentePesquisa> listaExportacao = new ArrayList<UsuarioIncidentePesquisa>();
			
			usuarioIncidentePesquisaService.apagarListaExportacao(getUsuario().getId().toUpperCase());
						
			for (ObjetoIncidente<?> objetoIncidente : objetosIncidentes){
				UsuarioIncidentePesquisaId uipId = new UsuarioIncidentePesquisaId();
				UsuarioIncidentePesquisa uip = new UsuarioIncidentePesquisa();
				
				uipId.setSeqObjetoIncidente(objetoIncidente.getId());
				uipId.setSigUsuario(getUsuario().getId().toUpperCase());				
				uip.setId(uipId);
				listaExportacao.add(uip);
			}
			if (!listaExportacao.isEmpty()){
				try {
					usuarioIncidentePesquisaService.incluirTodos(listaExportacao);
					sendToConfirmation();
				} catch (ServiceException e) {
					addError("Ocorreu um erro ao exportar a lista de processos: " + e.getMessage());
				}
			}	
		}
	}
		

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	protected abstract List<ObjetoIncidente<?>> getListaObjetosIncidentes();	


	public UsuarioIncidentePesquisaService getUsuarioIncidentePesquisaService() {
		return usuarioIncidentePesquisaService;
	}


	public void setUsuarioIncidentePesquisaService(
			UsuarioIncidentePesquisaService usuarioIncidentePesquisaService) {
		this.usuarioIncidentePesquisaService = usuarioIncidentePesquisaService;
	}
	
	
}
