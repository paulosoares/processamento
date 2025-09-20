package br.gov.stf.estf.processostf.model.service;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.RecursoProcesso;
import br.gov.stf.estf.processostf.model.dataaccess.RecursoProcessoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface RecursoProcessoService extends GenericService<RecursoProcesso, Long, RecursoProcessoDao> {
	public List<RecursoProcesso> pesquisar(String siglaProcessual, Long numeroProcessual) throws ServiceException;
	
	/**
	 * Pesquisa pelo id do primeiro recurso do processo anterior a data especificada. 
	 */
	public Long pesquisar(Processo processo, Date data) throws ServiceException;
}
