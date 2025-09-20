package br.gov.stf.estf.processostf.model.service.handler;

public class CartaOrdemDevidaHandler extends AndamentoProcessoHandler {
	
	@Override
	public boolean precisaTipoDevolucao() {
		return true;
	}
}