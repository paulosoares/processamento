package br.gov.stf.estf.assinatura.security;

import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.usuario.model.service.UsuarioService;
import br.gov.stf.framework.event.IUserEventListener;
import br.gov.stf.framework.event.UserEvent;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.util.ApplicationFactory;

public class UsuarioAssinaturaEventListener implements IUserEventListener {

	public void userCreated(UserEvent event) {
		loadUser((UsuarioAssinatura) event.getUser());
	}

	public void userChanged(UserEvent event) {
		loadUser((UsuarioAssinatura) event.getUser());
	}

	private void loadUser(UsuarioAssinatura usuario) {
		try {
			UsuarioService usuarioService = (UsuarioService) ApplicationFactory.getInstance().getServiceLocator().getService("usuarioService");
			Usuario usuarioRecuperado = usuarioService.recuperarPorId(usuario.getUsername().toUpperCase());

			if (usuarioRecuperado != null) {

				usuario.setAtivo(usuarioRecuperado.getAtivo());
				usuario.setSetor(usuarioRecuperado.getSetor());
				usuario.setName(usuarioRecuperado.getNome());

			} else {
				usuario.setAtivo(false);
			}
		} catch (ServiceException e) {
			e.printStackTrace();
			usuario.setAtivo(false);
		}
	}
}
