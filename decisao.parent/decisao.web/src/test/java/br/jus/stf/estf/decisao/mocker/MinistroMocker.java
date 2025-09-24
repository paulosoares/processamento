package br.jus.stf.estf.decisao.mocker;

import br.gov.stf.estf.entidade.ministro.Ministro;

public class MinistroMocker extends Mocker<Ministro> {

	private SetorMocker setorMocker = new SetorMocker();

	public SetorMocker mockSetor() {
		return setorMocker;
	}

	@Override
	protected Ministro instanciar() {
		Ministro m = new Ministro();
		m.setSetor(setorMocker.preparar());
		return m;
	}

}
