/**
 * 
 */
package br.jus.stf.estf.decisao.objetoincidente.web;

import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;

/**
 * @author Paulo.Estevao
 * @since 09.08.2010
 */
//@Action(id="imprimirRelatorioVotoActionFacesBean", 
//		name="Impressão combinada de relatório e voto", 
//		view="/acoes/objetoincidente/impressaoCombinada.xhtml",
//		height=210)
//@Restrict(ActionIdentification.IMPRIMIR_LISTA_DE_PROCESSOS)
//@RequiresResources(Mode.Many)
// Issue 884 para remoção da ação
public class ImprimirRelatorioVotoActionFacesBean extends AbstractImpressaoCombinadaActionFacesBean<ObjetoIncidenteDto> {
	
	@Override
	protected TipoTexto getTipoPrimeiroTexto() {
		return TipoTexto.RELATORIO;
	}

	@Override
	protected TipoTexto getTipoSegundoTexto() {
		return TipoTexto.VOTO;
	}

	@Override
	protected boolean quebrarPagina() {
		return true;
	}

}
