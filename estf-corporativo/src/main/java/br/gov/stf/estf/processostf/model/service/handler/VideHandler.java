package br.gov.stf.estf.processostf.model.service.handler;

import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.framework.model.service.ServiceException;

public class VideHandler extends AndamentoProcessoHandler {

	@Override
	public Long getCodigoRecurso(AndamentoProcesso andamentoProcessoAnterior, boolean isAndamentoVide) throws ServiceException {
		return super.getCodigoRecurso(andamentoProcessoAnterior, true);
	}

}