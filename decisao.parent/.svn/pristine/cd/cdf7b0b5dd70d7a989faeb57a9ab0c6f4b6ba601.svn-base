package br.jus.stf.estf.decisao.mocker;

import br.gov.stf.estf.entidade.localizacao.Setor;

public class SetorMocker extends Mocker<Setor> {

	public SetorMocker setSetor(final Long id, final String sigla,
			final String nome) {
		addExecutor(new ExecucaoMock<Setor>() {
			@Override
			public void executar(Setor s) {
				s.setId(id);
				s.setSigla(sigla);
				s.setNome(nome);
			}
		});
		return this;
	}

	@Override
	protected Setor instanciar() {
		Setor setor = new Setor();
		return setor;
	}

}
