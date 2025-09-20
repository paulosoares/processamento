package br.gov.stf.estf.processostf.model.service;



import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.processostf.Sumula;
import br.gov.stf.estf.processostf.model.dataaccess.SumulaDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface SumulaService extends GenericService<Sumula, Long, SumulaDao>{
	
	public Long recuperarNumeroUltimaSumula () throws ServiceException;
	
	public List<Sumula> pesquisarSumula(Long numeroSumula, Long processoPrecedente, String descricaoVerbete,
			Date dataAprovacao, String tipoSumula) throws ServiceException;

	public Long recuperarNumeroUltimaSeqSumula() throws ServiceException;
}
