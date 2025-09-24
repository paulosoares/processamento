/**
 * 
 */
package br.jus.stf.estf.decisao.texto.web;

import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.action.handlers.CheckMinisterId;
import br.jus.stf.estf.decisao.support.action.handlers.CheckRestrictions;
import br.jus.stf.estf.decisao.support.action.handlers.CheckTextoDigital;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.handlers.States;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;

/**
 * @author Paulo.Estevao
 * @since 17.05.2011
 */
@Action(id="excluirTextoEmElaboracaoActionFacesBean", 
		name="Excluir Textos", 
		view="/acoes/texto/excluirTexto.xhtml")
@Restrict({ ActionIdentification.EXCLUIR_TEXTO, ActionIdentification.EDITAR_TEXTO_EM_ELABORACAO})
@RequiresResources(Mode.Many)
@States({ FaseTexto.EM_ELABORACAO})
@CheckMinisterId
@CheckTextoDigital
@CheckRestrictions
public class ExcluirTextoEmElaboracaoActionFacesBean extends
		AbstractExcluirTextoActionFacesBean<TextoDto> {

}
