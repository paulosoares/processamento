package br.jus.stf.estf.decisao.objetoincidente.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.jus.stf.estf.decisao.objetoincidente.service.ObjetoIncidenteService;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;

/**
 * Estende {@link AbstractNovoTextoActionFacesBean} para disponibilizar a ação Novo Texto
 * no contexto de pesquisa ou visualização de objetos incidentes.
 * 
 * @author Rodrigo Barreiros
 * @since 26.05.2010
 */
@Action(id="novoTextoParaObjetoIncidenteActionFacesBean", name="Novo Texto", view="/acoes/texto/criar.xhtml", height=305, width=550)
@Restrict({ActionIdentification.NOVO_TEXTO})
public class NovoTextoActionFacesBean extends AbstractNovoTextoActionFacesBean<ObjetoIncidenteDto> {
	
	@Qualifier("objetoIncidenteServiceLocal") @Autowired
	private ObjetoIncidenteService objetoIncidenteService;

	/**
	 * @see br.jus.stf.estf.decisao.objetoincidente.web.AbstractNovoTextoActionFacesBean#getObjetoIncidente(java.lang.Object)
	 */
	@Override
	protected ObjetoIncidente<?> getObjetoIncidente(ObjetoIncidenteDto resource) {
		return objetoIncidenteService.recuperarObjetoIncidentePorId(resource.getId());
	}
	
}
