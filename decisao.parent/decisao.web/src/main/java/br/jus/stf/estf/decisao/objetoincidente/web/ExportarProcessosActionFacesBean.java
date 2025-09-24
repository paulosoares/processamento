package br.jus.stf.estf.decisao.objetoincidente.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.jus.stf.estf.decisao.objetoincidente.service.ObjetoIncidenteService;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;


@Action(id = "exportarProcessosParaProcessosActionFacesBean", name = "Exportar Processos", view = "/acoes/objetoincidente/exportarProcessos.xhtml", width=750)
@Restrict({ ActionIdentification.EXPORTAR_PROCESSOS })
@RequiresResources(Mode.Many)
public class ExportarProcessosActionFacesBean extends  AbstractExportarProcessosActionFacesBean<ObjetoIncidenteDto> {

	@Qualifier("objetoIncidenteServiceLocal") @Autowired
	private ObjetoIncidenteService objetoIncidenteService;
	
	@Override
	protected List<ObjetoIncidente<?>> getListaObjetosIncidentes() {
		List<ObjetoIncidente<?>> objetosIncidentes = new ArrayList<ObjetoIncidente<?>>();
		for (ObjetoIncidenteDto oiDto : getResources()) {
			ObjetoIncidente<?> objetoIncidente = objetoIncidenteService.recuperarObjetoIncidentePorId(oiDto.getId());
			objetosIncidentes.add(objetoIncidente);
		}
		return objetosIncidentes;
	}	
}
