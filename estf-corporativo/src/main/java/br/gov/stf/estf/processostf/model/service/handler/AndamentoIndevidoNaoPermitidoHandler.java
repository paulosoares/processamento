package br.gov.stf.estf.processostf.model.service.handler;

import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.processostf.model.service.exception.LancamentoIndevidoException;
import br.gov.stf.framework.model.service.ServiceException;

public class AndamentoIndevidoNaoPermitidoHandler extends AndamentoProcessoHandler {

	@Override
	public void verificarLancamentoIndevido(AndamentoProcesso andamentoProcessoAnulado) throws ServiceException {
		throw new LancamentoIndevidoException("Não é possível indevidar o andamento \"" + andamentoProcessoAnulado.getCodigoAndamento() + " - " + 
				andamentoProcessoAnulado.getTipoAndamento().getDescricao() + "\".");
	}
}