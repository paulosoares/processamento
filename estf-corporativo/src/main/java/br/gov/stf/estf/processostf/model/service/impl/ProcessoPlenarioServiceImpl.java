package br.gov.stf.estf.processostf.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.ProcessoPlenario;
import br.gov.stf.estf.processostf.model.dataaccess.ProcessoPlenarioDao;
import br.gov.stf.estf.processostf.model.service.ProcessoPlenarioService;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("processoPlenarioService")
public class ProcessoPlenarioServiceImpl extends GenericServiceImpl<ProcessoPlenario, Long, ProcessoPlenarioDao> implements ProcessoPlenarioService {

	public ProcessoPlenarioServiceImpl(ProcessoPlenarioDao dao) {
		super(dao);
	}

	@Override
	public ProcessoPlenario recuperarPorObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		return dao.recuperarPorObjetoIncidente(objetoIncidente);
	}

}
