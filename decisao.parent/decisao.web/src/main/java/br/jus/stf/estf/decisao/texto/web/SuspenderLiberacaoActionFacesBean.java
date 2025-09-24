package br.jus.stf.estf.decisao.texto.web;

import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.gov.stf.estf.entidade.documento.tipofase.TipoTransicaoFaseTexto;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.action.handlers.CheckMinisterId;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.handlers.States;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;

@Action(id = "suspenderLiberacaoActionFacesBean", name = "Suspender Liberação", view = "/acoes/texto/transicao/executar.xhtml", height = 250, width = 500)
@Restrict({ActionIdentification.SUSPENDER_LIBERACAO})
@States({ FaseTexto.LIBERADO_ASSINATURA})
@RequiresResources(Mode.Many)
@CheckMinisterId
public class SuspenderLiberacaoActionFacesBean extends AbstractAlterarFaseDoTextoActionFacesBean<TextoDto> {

	@Override
	protected TipoTransicaoFaseTexto getDestino() {
		return TipoTransicaoFaseTexto.SUSPENDER_LIBERACAO;
	}
	

	@Override
	protected String getErrorTitle() {
		return "Não foi possível suspender a liberação para revisão dos seguintes textos:";
	}

}
