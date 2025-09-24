package br.jus.stf.estf.decisao.texto.web;

import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.gov.stf.estf.entidade.documento.tipofase.TipoTransicaoFaseTexto;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.action.handlers.CheckMinisterId;
import br.jus.stf.estf.decisao.support.action.handlers.CheckRestrictions;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.handlers.States;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;

@Action(id = "voltarParaRevisaoActionFacesBean", name = "Voltar para Revisão", view = "/acoes/texto/transicao/executar.xhtml", height = 215, width = 500)
@Restrict({ActionIdentification.VOLTAR_PARA_REVISAO})
@States({ FaseTexto.REVISADO })
@RequiresResources(Mode.Many)
@CheckMinisterId
@CheckRestrictions
public class VoltarParaRevisaoActionFacesBean extends AbstractAlterarFaseDoTextoActionFacesBean<TextoDto> {

	@Override
	protected TipoTransicaoFaseTexto getDestino() {
		return TipoTransicaoFaseTexto.VOLTAR_PARA_REVISAO;
	}

	@Override
	protected String getErrorTitle() {
		return "Não foi possível voltar os seguintes textos para revisão:";
	}

}
