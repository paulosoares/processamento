package br.jus.stf.estf.decisao.texto.web;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.action.handlers.CheckFavoritosSelecionados;
import br.jus.stf.estf.decisao.support.action.handlers.CheckFavoritosSelecionados.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.CheckMinisterId;
import br.jus.stf.estf.decisao.support.action.handlers.CheckRestrictions;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;
import br.jus.stf.estf.decisao.support.security.PermissionChecker;
import br.jus.stf.estf.decisao.texto.service.TextoService;

@Action(id = "desfavoritarTextoActionFacesBean", name = "Desmarcar como Favoritos", view = "/acoes/texto/desfavoritarTexto.xhtml")
@Restrict({ ActionIdentification.DEFINIR_TEXTO_COMO_FAVORITO })
@CheckRestrictions
@CheckMinisterId
@CheckFavoritosSelecionados(Mode.Favoritados)
public class DesfavoritarTextoActionFacesBean extends ActionSupport<TextoDto> {

	@Autowired
	private TextoService textoService;
	
	@Autowired
	private PermissionChecker permissionChecker;

	public void desmarcarComoFavoritos() {
		try {
			checarPermissao();
			textoService.desmarcarComoFavoritos(getResources());
		} catch (ServiceException e) {
			logger.error("Erro ao desmarcar como favoritos!", e);
			addError(String.format("Erro ao desmarcar como favoritos: %s ", getMensagemDeErroPadrao(e)));
		}
		setRefresh(true);
		sendToConfirmation();
		if (!hasMessages()) {
			sendToConfirmation();
		} else {
			sendToErrors();
		}
	}

	public void desmarcarComoFavorito(TextoDto texto) {
		try {
			checarPermissao();
			textoService.desmarcarComoFavoritos(Arrays.asList(texto));
			texto.setFavoritoNoGabinete(false);
		} catch (ServiceException e) {
			logger.error("Erro ao desmarcar como favoritos!", e);
			addError(String.format("Erro ao desmarcar como favoritos: %s ", getMensagemDeErroPadrao(e)));
		}
	}

	private void checarPermissao() throws ServiceException {
		if (!permissionChecker.hasPermission(getPrincipal(), ActionIdentification.DEFINIR_TEXTO_COMO_FAVORITO)) {
			throw new ServiceException("Usuário não possui permissão para Desmarcar como Favoritos!");
		}
	}
	
}
