package br.jus.stf.estf.decisao.objetoincidente.web;

import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;


@Action(id="imprimirEmentaAcordaoActionFacesBean", name="Impressão combinada de ementa e acórdão", 
		view="/acoes/objetoincidente/impressaoCombinada.xhtml", 
		height=210)
@Restrict({ActionIdentification.IMPRIMIR_LISTA_DE_PROCESSOS})
@RequiresResources(Mode.Many)
public class ImprimirEmentaAcordaoActionFacesBean extends AbstractImpressaoCombinadaActionFacesBean<ObjetoIncidenteDto> {

	@Override
	protected TipoTexto getTipoPrimeiroTexto() {
		return TipoTexto.EMENTA;
	}

	@Override
	protected TipoTexto getTipoSegundoTexto() {
		return TipoTexto.ACORDAO;
	}

	@Override
	protected boolean quebrarPagina() {
		return false;
	}
	

}
