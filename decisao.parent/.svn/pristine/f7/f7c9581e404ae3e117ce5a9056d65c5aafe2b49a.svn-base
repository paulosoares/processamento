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

@Action(id = "voltarParaElaboracaoDeLiberadoParaAssinaturaActionFacesBean", name = "Voltar para Elaboração", view = "/acoes/texto/transicao/executar.xhtml", height = 215, width = 500)
@Restrict({ActionIdentification.VOLTAR_PARA_ELABORACAO_DE_LIBERADO_PARA_ASSINATURA})
@States({ FaseTexto.LIBERADO_ASSINATURA})
@RequiresResources(Mode.Many)
@CheckMinisterId
@CheckRestrictions
public class VoltarParaElaboracaoDeLiberadoParaAssinaturaActionFacesBean extends AbstractAlterarFaseDoTextoActionFacesBean<TextoDto> {

	@Override
	protected TipoTransicaoFaseTexto getDestino() {
		return TipoTransicaoFaseTexto.VOLTAR_PARA_ELABORACAO;
	}

	/**
	 * Quando um texto volta para elaboração, as mesmas ações de cancelamento de assinatura 
	 * são realizadas, razão pela qual esse método é chamado aqui.
	 */
	@Override
	protected void doExecute(TextoDto texto) throws Exception {
		textoService.cancelarAssinatura(texto, getDestino(), textosProcessados, getObservacao(), getResponsavel());
	}

	@Override
	protected String getErrorTitle() {
		return "Não foi possível voltar os seguintes textos para elaboração:";
	}
}
