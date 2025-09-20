package br.gov.stf.estf.processosetor.model.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.localizacao.TipoFaseSetor;
import br.gov.stf.estf.entidade.processosetor.HistoricoFase;
import br.gov.stf.estf.entidade.processosetor.ProcessoSetor;
import br.gov.stf.estf.processosetor.model.dataaccess.FaseDao;
import br.gov.stf.estf.processosetor.model.service.FaseService;
import br.gov.stf.estf.processosetor.model.service.ProcessoSetorService;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("faseService")
public class FaseServiceImpl extends GenericServiceImpl<HistoricoFase, Long, FaseDao>  
implements FaseService {

	
	protected ObjetoIncidenteService getObjetoIncidenteService() throws ServiceException {
		return (ObjetoIncidenteService) getService("objetoIncidenteService");
	}
	
	private final ProcessoSetorService processoSetorService;

	public FaseServiceImpl(FaseDao dao, ProcessoSetorService processoSetorService) {
		super(dao);
		this.processoSetorService = processoSetorService;
	}

	public Boolean registrarFases(List<ProcessoSetor> listaProcessoSetor, HistoricoFase historicoFase, Boolean registrarFaseNaoLocazizadoSetor) 
	throws ServiceException {

		Boolean result = Boolean.FALSE; 
		
		if( listaProcessoSetor == null || listaProcessoSetor.size() == 0 ){
			throw new ServiceException("Os processos/protocolos devem ser informados.");
		} 
		else if( historicoFase == null ){
			throw new ServiceException("A fase deve ser informada.");
		}

		for(ProcessoSetor processoSetor: listaProcessoSetor ) {
			if( !registrarFase(instanciarHistoricoFase(historicoFase, processoSetor), registrarFaseNaoLocazizadoSetor ) ) {
				
				StringBuffer error = new StringBuffer("Não foi possível registrar a fase para o ");
				
				if( processoSetor.getPossuiIndentificacaoProcessual() )
					error.append("processo: "+processoSetor.getIdentificacaoProcessual());
				else
					error.append("protocolo: "+processoSetor.getIdentificacaoProtocolo());
				
				throw new ServiceException(error.toString());
			}			
		}
		
		result = Boolean.TRUE;

		return result;
	} 
	
	private HistoricoFase instanciarHistoricoFase(HistoricoFase fase, ProcessoSetor processoSetor) {
		HistoricoFase novaFase = new HistoricoFase();
		
		novaFase.setProcessoSetor(processoSetor);
		novaFase.setTipoFaseSetor(fase.getTipoFaseSetor());
		novaFase.setTipoStatusSetor(fase.getTipoStatusSetor());
		novaFase.setUsuario(fase.getUsuario());
		novaFase.setObservacao(fase.getObservacao());
		novaFase.setDataFase(fase.getDataFase());
		novaFase.setObjetoIncidente(processoSetor.getObjetoIncidente());
		
		return novaFase;
	}


	public Boolean registrarFase(HistoricoFase historicoFase, Boolean registrarFaseNaoLocazizadoSetor) 
	throws ServiceException {

		Boolean result = Boolean.FALSE;

		try {
			
			validarFase(historicoFase, registrarFaseNaoLocazizadoSetor);			

			if( historicoFase.getProcessoSetor().adicionarHistoricoFase(historicoFase) )
				result = processoSetorService.persistirProcessoSetor(historicoFase.getProcessoSetor());
		}
		catch( ServiceException e ) {
			throw e;
		}

		return result;
	}	
	
	private void validarFase(HistoricoFase fase, Boolean registrarFaseNaoLocazizadoSetor) 
	throws ServiceException {
		if( fase == null )
			throw new NullPointerException("Objeto que representa a fase é nulo.");
		
		if( fase.getProcessoSetor() == null )
			throw new ServiceException("A fase precisa estar associada a um processo/protocolo.");
		
		if( registrarFaseNaoLocazizadoSetor == null || !registrarFaseNaoLocazizadoSetor.booleanValue() ) {
			if( fase.getProcessoSetor().getDataSaida() != null )
				throw new ServiceException("O processo/protocolo ao qual a fase está associada não mais se encontra no setor.");
		}
		
		if( fase.getTipoFaseSetor() == null )
			throw new ServiceException("O tipo da fase não foi informado.");		

		if( fase.getDataFase() == null )
			throw new ServiceException("A data da fase não foi informada.");		
	}

	public Boolean excluirHistoricoFase(HistoricoFase fase)
	throws ServiceException{
		try{	
			return dao.excluirHistoricoFase(fase);
		}
		catch(DaoException e) {
			throw new ServiceException(e);
		}				
	}
	
	public List<HistoricoFase> pesquisarFases(TipoFaseSetor faseSetor)
	throws ServiceException{
		List<HistoricoFase> fases = new LinkedList<HistoricoFase>();
		
		try{
			
			fases = dao.pesquisarFases(faseSetor);
			
		} 
		catch(DaoException e) {
			throw new ServiceException(e);
		}

		return fases;    	
	}

}
