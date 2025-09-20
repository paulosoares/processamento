package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.ClasseConversao;
import br.gov.stf.estf.processostf.model.dataaccess.ClasseConversaoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ClasseConversaoService extends GenericService<ClasseConversao, String, ClasseConversaoDao>{
	public List pesquisarClasseAntiga() throws ServiceException;
}
