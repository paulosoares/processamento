package br.gov.stf.estf.processostf.model.service;


import br.gov.stf.estf.entidade.processostf.IncidenteDistribuicao;
import br.gov.stf.estf.processostf.model.dataaccess.IncidenteDistribuicaoDao;
import br.gov.stf.framework.model.service.GenericService;

/**
* Interface service para a entidade Distribuicao
* @see .Distribuicao
* @author SSGJ
*/
public interface IncidenteDistribuicaoService extends GenericService <IncidenteDistribuicao, Long, IncidenteDistribuicaoDao> {
//	public List<IncidenteDistribuicao> pesquisar (Integer numAta, Date dataAta) throws ServiceException;
}