package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.jurisdicionado.Jurisdicionado;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProcessoInteresse;
import br.gov.stf.estf.processostf.model.dataaccess.ProcessoInteresseDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ProcessoInteresseService extends GenericService<ProcessoInteresse, Long, ProcessoInteresseDao> {
	public List<ProcessoInteresse> recuperarProcessosInteresse(Jurisdicionado advogado) throws ServiceException;
	public ProcessoInteresse recuperarProcessosInteresse(Jurisdicionado advogado, Processo processo) throws ServiceException;
	public void salvarVariosProcessosInteresse(List<Processo> processos, Jurisdicionado advogado) throws ServiceException;
	public Boolean existeMovimentada(Jurisdicionado advogado) throws ServiceException;

}
