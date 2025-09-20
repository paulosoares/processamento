package br.gov.stf.estf.processosetor.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.TipoFaseSetor;
import br.gov.stf.estf.entidade.processosetor.HistoricoFase;
import br.gov.stf.estf.entidade.processosetor.ProcessoSetor;
import br.gov.stf.estf.processosetor.model.dataaccess.FaseDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface FaseService extends GenericService<HistoricoFase, Long, FaseDao> {
    
	public Boolean registrarFases(List<ProcessoSetor> listaProcessoSetor, HistoricoFase historicoFase, Boolean registrarFaseNaoLocazizadoSetor) 
	throws ServiceException;
	
	public Boolean registrarFase(HistoricoFase historicoFase, Boolean registrarFaseNaoLocazizadoSetor) 
	throws ServiceException;
	
	public Boolean excluirHistoricoFase(HistoricoFase fase)
	throws ServiceException;
	
	public List<HistoricoFase> pesquisarFases(TipoFaseSetor fase)
	throws ServiceException;
	
}
