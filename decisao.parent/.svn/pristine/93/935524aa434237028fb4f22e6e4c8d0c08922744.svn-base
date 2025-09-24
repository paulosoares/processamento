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
 * @see 27.05.2010
 */
@Action(id = "liberarParaRevisaoActionFacesBean", name = "Liberar para Revisão", view = "/acoes/texto/transicao/executar.xhtml", height = 215, width = 500)
@Restrict({ActionIdentification.LIBERAR_PARA_REVISAO})
@States({ FaseTexto.EM_ELABORACAO })
@RequiresResources(Mode.Many)
@CheckMinisterId
@CheckRestrictions
@CheckNotForIdTipoTexto({TipoTexto.CODIGO_MINUTA, TipoTexto.CODIGO_MEMORIA_DE_CASO})
public class LiberarParaRevisaoActionFacesBean extends AbstractAlterarFaseDoTextoActionFacesBean<TextoDto> {

	/**
	 * @see br.jus.stf.estf.decisao.texto.web.AbstractAlterarFaseDoTextoActionFacesBean#getDestino()
	 */
	@Override
	protected TipoTransicaoFaseTexto getDestino() {
		return TipoTransicaoFaseTexto.LIBERAR_PARA_REVISAO;
	}

	@Override
	protected String getErrorTitle() {
		return "Não foi possível liberar os seguintes textos para revisão:";
	}

}
