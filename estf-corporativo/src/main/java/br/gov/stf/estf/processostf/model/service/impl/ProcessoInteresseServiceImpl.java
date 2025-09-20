package br.gov.stf.estf.processostf.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.jurisdicionado.Jurisdicionado;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProcessoInteresse;
import br.gov.stf.estf.processostf.model.dataaccess.ProcessoInteresseDao;
import br.gov.stf.estf.processostf.model.service.ProcessoInteresseService;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("processoInteresseService")
public class ProcessoInteresseServiceImpl extends GenericServiceImpl<ProcessoInteresse, Long, ProcessoInteresseDao>
 implements ProcessoInteresseService {
	
	public ProcessoInteresseServiceImpl(ProcessoInteresseDao dao){
		super(dao);
	}
	
	@Override
	public List<ProcessoInteresse> recuperarProcessosInteresse(Jurisdicionado advogado) throws ServiceException {
		try {
			return dao.recuperarProcessosInteresse(advogado);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public ProcessoInteresse recuperarProcessosInteresse(Jurisdicionado advogado, Processo processo) throws ServiceException {
		try {
			return dao.recuperarProcessosInteresse(advogado, processo);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public void salvarVariosProcessosInteresse(List<Processo> processos, Jurisdicionado advogado) throws ServiceException {
		try {
			for (Processo processo: processos) {
				ProcessoInteresse processoInteresse = new ProcessoInteresse();
				processoInteresse.setAdvogado(advogado);
				processoInteresse.setProcesso(processo);
				dao.salvar(processoInteresse);
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
	}
	
	@Override
	public Boolean existeMovimentada(Jurisdicionado advogado) throws ServiceException {
		try {
			return dao.existeMovimentada(advogado);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}



}
