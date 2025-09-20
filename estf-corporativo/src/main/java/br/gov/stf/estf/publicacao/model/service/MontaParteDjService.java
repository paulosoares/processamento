package br.gov.stf.estf.publicacao.model.service;

import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.framework.model.service.ServiceException;

public interface MontaParteDjService {
	
	@Deprecated
	public byte[] recuperarCabecalhoPartesDj (ObjetoIncidente<?> objetoIncidente) throws ServiceException;
	
	public byte[] recuperarCabecalhoPartesDj (ObjetoIncidente<?> objetoIncidente, boolean inserirOrigem) throws ServiceException;

	public byte[] recuperarCabecalhoPartesDj(Texto texto, boolean inserirOrigem) throws ServiceException;	
	
}
