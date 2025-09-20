package br.gov.stf.estf.processosetor.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.processosetor.EstatisticaProcessoSetorSecao;
import br.gov.stf.framework.model.service.ServiceException;

public interface EstatisticaProcessoSetorService {

	public List<EstatisticaProcessoSetorSecao> gerarEstatisticaProcessoSetorSecao(
			Long idSetor) throws ServiceException;
	
	public List<EstatisticaProcessoSetorSecao> gerarEstatisticaProcessoFaseSetor(
			Long idSetor , Boolean status ) throws ServiceException;
}
