package br.jus.stf.estf.decisao.mocker;

import java.util.ArrayList;
import java.util.List;

public abstract class Mocker<T> {

	private List<ExecucaoMock<T>> executores = new ArrayList<ExecucaoMock<T>>();

	protected void addExecutor(ExecucaoMock<T> ep) {
		executores.add(ep);
	}

	protected abstract T instanciar();

	public T preparar() {
		T instance = instanciar();
		for (ExecucaoMock<T> ep : executores) {
			ep.executar(instance);
		}
		return instance;
	}

}
