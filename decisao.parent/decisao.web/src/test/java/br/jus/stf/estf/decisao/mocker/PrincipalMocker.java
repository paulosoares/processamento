package br.jus.stf.estf.decisao.mocker;

import br.jus.stf.estf.decisao.support.security.Principal;

public class PrincipalMocker extends Mocker<Principal> {

	public PrincipalMocker setIdSetor(final Long idSetor) {
		addExecutor(new ExecucaoMock<Principal>() {
			@Override
			public void executar(Principal p) {
				p.setIdSetor(idSetor);
			}
		});
		return this;
	}

	private UsuarioMocker usuarioMocker = new UsuarioMocker();

	public UsuarioMocker mockUsuario() {
		return usuarioMocker;
	}

	private MinistroMocker ministroMocker = new MinistroMocker();

	public MinistroMocker mockMinistro() {
		return ministroMocker;
	}

	@Override
	protected Principal instanciar() {
		Principal principal = new Principal();
		principal.setMinistro(ministroMocker.preparar());
		principal.setUsuario(usuarioMocker.preparar());
		return principal;
	}

}
