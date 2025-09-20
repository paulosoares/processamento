package br.gov.stf.estf.mag.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.mag.HistoricoDeslocamentoMag;
import br.gov.stf.estf.mag.model.dataaccess.HistoricoDeslocamentoMagDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface HistoricoDeslocamentoMagService extends GenericService<HistoricoDeslocamentoMag, Long, HistoricoDeslocamentoMagDao> {

	public HistoricoDeslocamentoMag recuperarHistoricoDeslocamentoMag(Long id) throws ServiceException;
	
	public List<HistoricoDeslocamentoMag> pesquisarHistoricoDeslocamentoMag(
			Long codigoSetor, String siglaClasseProcessual, Long numeroProcessual) 
	throws ServiceException;
}
