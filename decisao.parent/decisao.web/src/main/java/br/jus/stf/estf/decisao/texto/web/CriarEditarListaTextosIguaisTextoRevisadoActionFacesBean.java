package br.jus.stf.estf.decisao.texto.web;

import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.action.handlers.CheckMinisterId;
import br.jus.stf.estf.decisao.support.action.handlers.CheckRestrictions;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.handlers.States;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;

@Action(id="criarEditarListaTextosIguaisTextoRevisadoActionFacesBean", 
		name="Criar/Editar Lista de Textos Iguais", 
		view="/acoes/texto/listaTextosIguais.xhtml",
		height=360)
@Restrict({ActionIdentification.CRIAR_EDITAR_LISTA_DE_TEXTOS_IGUAIS, ActionIdentification.EDITAR_TEXTO_REVISADO})
@States({FaseTexto.REVISADO})
@RequiresResources(Mode.One)
@CheckMinisterId
@CheckRestrictions
public class CriarEditarListaTextosIguaisTextoRevisadoActionFacesBean extends
		AbstractCriarEditarListaTextosIguaisActionFacesBean<TextoDto> {

}
