package br.gov.stf.estf.processostf.model.service;

import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.ProcessoPlenario;
import br.gov.stf.estf.processostf.model.dataaccess.ProcessoPlenarioDao;
import br.gov.stf.framework.model.service.GenericService;

public interface ProcessoPlenarioService extends GenericService<ProcessoPlenario, Long, ProcessoPlenarioDao> {

	public ProcessoPlenario recuperarPorObjetoIncidente(ObjetoIncidente<?> objetoIncidente);

}
