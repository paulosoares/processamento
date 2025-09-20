package br.gov.stf.estf.documento.model.service;

import java.util.Date;

import br.gov.stf.framework.model.service.ServiceException;

public interface ExtratoAtaService {
	public void salvarExtratoAta ( Long numeroProcessual, String siglaClasse, Long tipoJulgamento, Long codRecurso, Date dataAta, byte[] pdf  ) throws ServiceException;
}
