package br.jus.stf.estf.decisao.texto.web;

import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.gov.stf.estf.entidade.documento.tipofase.TipoTransicaoFaseTexto;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.action.handlers.CheckMinisterId;
import br.jus.stf.estf.decisao.support.action.handlers.CheckNotForIdTipoTexto;
import br.jus.stf.estf.decisao.support.action.handlers.CheckRestrictions;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.handlers.States;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;

/**
 * @author Rodrigo Barreiros
 */
@Action(id = "concluirRevisaoActionFacesBean", name = "Concluir Revisão", view = "/acoes/texto/transicao/executar.xhtml", height = 215, width = 500)
@Restrict({ActionIdentification.CONCLUIR_REVISAO})
@States({ FaseTexto.EM_ELABORACAO, FaseTexto.EM_REVISAO })
@RequiresResources(Mode.Many)
@CheckMinisterId
@CheckRestrictions
@CheckNotForIdTipoTexto({TipoTexto.CODIGO_MINUTA, TipoTexto.CODIGO_MEMORIA_DE_CASO})

public class ConcluirRevisaoActionFacesBean extends AbstractAlterarFaseDoTextoActionFacesBean<TextoDto> {

	/**
	 * @see br.jus.stf.estf.decisao.texto.web.AbstractAlterarFaseDoTextoActionFacesBean#getDestino()
	 */
	@Override
	protected TipoTransicaoFaseTexto getDestino() {
		return TipoTransicaoFaseTexto.CONCLUIR_REVISAO;
	}

	@Override
	protected String getErrorTitle() {
		return "Não foi possível concluir a revisão para os seguintes textos:";
	}
}
