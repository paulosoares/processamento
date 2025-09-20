package br.gov.stf.estf.julgamento.model.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.julgamento.ProcessoTema;
import br.gov.stf.estf.entidade.julgamento.SuspensaoNacionalTema;
import br.gov.stf.estf.entidade.julgamento.Tema;
import br.gov.stf.estf.entidade.julgamento.TipoOcorrencia;
import br.gov.stf.estf.entidade.julgamento.TipoOcorrencia.TipoOcorrenciaConstante;
import br.gov.stf.estf.entidade.julgamento.TipoTema;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.julgamento.model.dataaccess.TemaDao;
import br.gov.stf.estf.julgamento.model.exception.TemaException;
import br.gov.stf.estf.julgamento.model.service.ProcessoTemaService;
import br.gov.stf.estf.julgamento.model.service.TemaService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("temaService")
public class TemaServiceImpl
extends GenericServiceImpl<Tema, Long, TemaDao> 
implements TemaService {

    private final ProcessoTemaService processoTemaService;
    

    public TemaServiceImpl(TemaDao dao,ProcessoTemaService processoTemaService) {
        super(dao);
        this.processoTemaService = processoTemaService;
    }

	public Boolean persistirTema(Tema tema) throws ServiceException,TemaException {
		try{
			
			if( tema == null ){
				throw new TemaException("O tema deve ser informado");
			}
			if( tema.getId() != null ){
				List<ProcessoTema> listaProcTema = processoTemaService.pesquisarProcessoTema(tema.getId(), null, null, null, null, null);
				if( listaProcTema != null && tema.getProcessosTema() != null ){
					for( ProcessoTema procTema : listaProcTema ){
						boolean contem = false;
						for( ProcessoTema proc : tema.getProcessosTema() ){
							if( proc.getId()!= null && proc.getId().equals(procTema.getId()) ){
								contem = true;
							}
						}
						if( !contem ){
							dao.excluirProcessoTema(procTema);
						}
					}
					
				}
			}
			
			return dao.persistirTema(tema);
		}catch(TemaException e){
			throw e;
		}catch( DaoException  e){
			throw new ServiceException(e);
		}
	}
	
	public Boolean excluirTema(Tema tema) throws ServiceException {
		try{
			return dao.excluirTema(tema);
		}catch( DaoException  e){
			throw new ServiceException(e);
		}
	}
    


	public List<TipoOcorrencia> pesquisarTipoOcorrencia(Long codigo,String descricao) throws ServiceException {
		try{
			return dao.pesquisarTipoOcorrencia(codigo, descricao);
		}catch( DaoException  e){
			throw new ServiceException(e);
		}
	}

	public List<TipoTema> pesquisarTipoTema(Long codigo, String descricao)throws ServiceException {
		try{
			return dao.pesquisarTipoTema(codigo, descricao);
		}catch( DaoException  e){
			throw new ServiceException(e);
		}
	}
	
	public Tema recuperarTemas(Long idObjetoIncidente) throws ServiceException{
		try {
			return dao.recuperarTemas(idObjetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public void registarSuspensaoNacional(Tema tema, Processo processo,
			Date dataInicial, AndamentoProcesso andamento) throws ServiceException {
		List<ProcessoTema> lista = processoTemaService.pesquisarProcessoTema(tema.getId(), processo.getSiglaClasseProcessual(), processo.getNumeroProcessual()
				,null,TipoOcorrenciaConstante.JULGAMENTO_LEADING_CASE.getCodigo(), null);		
		if (lista == null || lista.isEmpty())
			throw new ServiceException("O processo não está vinculado ao tema a ser suspenso como LEADING CASE.");
		if (tema.getSuspensaoNacionalAtual() != null)
			throw new ServiceException("Já há uma suspensão nacional registrada para o tema.");
		SuspensaoNacionalTema suspensao = new SuspensaoNacionalTema();
		ProcessoTema processoTema = lista.get(0);
		
		suspensao.setTema(processoTema.getTema());
		suspensao.setDataInicial(dataInicial);
		suspensao.setObjetoIncidente(processo);
		suspensao.setAndamentoProcesso(andamento);
		processoTema.getTema().getHistoricoSuspensaoNacional().add(suspensao);
		
		processoTemaService.alterar(processoTema);
	}

	@Override
	public void encerrarSuspensaoNacional(Tema tema, Processo processo,
			Date dataFinal) throws ServiceException {
		List<ProcessoTema> lista = processoTemaService.pesquisarProcessoTema(tema.getId(), processo.getSiglaClasseProcessual(), processo.getNumeroProcessual()
				,null,TipoOcorrenciaConstante.JULGAMENTO_LEADING_CASE.getCodigo(), null);	
		if (lista == null || lista.isEmpty())
			throw new ServiceException("O processo não está vinculado ao tema a ser suspenso como LEADING CASE.");
		if (tema.getSuspensaoNacionalAtual() == null || tema.getSuspensaoNacionalAtual().getDataFinal() != null){
			throw new ServiceException("Não há suspensão nacional em aberto para o tema.");
		}
		ProcessoTema processoTema = lista.get(0);
		processoTema.getTema().getSuspensaoNacionalAtual().setDataFinal(dataFinal);
		processoTemaService.alterar(processoTema);				
	}

	
}