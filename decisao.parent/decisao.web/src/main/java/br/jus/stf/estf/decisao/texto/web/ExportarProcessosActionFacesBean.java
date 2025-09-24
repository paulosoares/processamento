package br.jus.stf.estf.decisao.texto.web;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.jus.stf.estf.decisao.objetoincidente.service.ObjetoIncidenteService;
import br.jus.stf.estf.decisao.objetoincidente.web.AbstractExportarProcessosActionFacesBean;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;


@Action(id = "exportarProcessosParaTextoActionFacesBean", name = "Exportar Processos", view = "/acoes/objetoincidente/exportarProcessos.xhtml", width=750)
@Restrict({ ActionIdentification.EXPORTAR_PROCESSOS })
@RequiresResources(Mode.Many)
public class ExportarProcessosActionFacesBean extends AbstractExportarProcessosActionFacesBean<TextoDto> {

	@Qualifier("objetoIncidenteServiceLocal") @Autowired
	private ObjetoIncidenteService objetoIncidenteService;
	
	@Override
	protected List<ObjetoIncidente<?>> getListaObjetosIncidentes() {
		Set<TextoDto> conjuntoTextos = getResources();
		Map<Long, ObjetoIncidente<?>> mapaObjetos = new HashMap<Long, ObjetoIncidente<?>>();
		for (TextoDto dto : conjuntoTextos){
			if (mapaObjetos.get(dto.getIdObjetoIncidente()) != null)
				continue;
			mapaObjetos.put(dto.getIdObjetoIncidente(), objetoIncidenteService.recuperarObjetoIncidentePorId(dto.getIdObjetoIncidente()));
		}
		return new LinkedList<ObjetoIncidente<?>>(mapaObjetos.values());
	}

	
	
}
