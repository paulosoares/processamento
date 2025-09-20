package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.Classe;
import br.gov.stf.estf.processostf.model.dataaccess.ClasseDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ClasseService extends GenericService<Classe, String, ClasseDao> {
	public List pesquisarClasseProcessual() throws ServiceException;

	public List pesquisarClasseAntiga() throws ServiceException;
	
	public Classe pesquisarClassePorSigla(String siglaClasse) throws ServiceException;
	
	public List<Classe> pesquisarClassesAtivas() throws ServiceException;
}
