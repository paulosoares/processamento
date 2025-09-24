package br.jus.stf.estf.decisao.mocker;

import br.gov.stf.estf.entidade.usuario.Usuario;

public class UsuarioMocker extends Mocker<Usuario> {

	public UsuarioMocker setUsuario(final String id, final String matricula,
			final String nome) {
		addExecutor(new ExecucaoMock<Usuario>() {
			@Override
			public void executar(Usuario u) {
				u.setId(id);
				u.setMatricula(matricula);
				u.setNome(nome);
			}
		});
		return this;
	}

	@Override
	protected Usuario instanciar() {
		Usuario usuario = new Usuario();
		return usuario;
	}

}
