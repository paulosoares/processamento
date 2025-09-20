package br.gov.stf.estf.processostf.model.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.Classe;
import br.gov.stf.estf.processostf.model.dataaccess.ClasseDao;
import br.gov.stf.estf.processostf.model.service.ClasseService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("classeService")
public class ClasseServiceImpl extends GenericServiceImpl<Classe, String, ClasseDao> implements ClasseService {
	public ClasseServiceImpl(ClasseDao dao) {
		super(dao);
	}

	public List pesquisarClasseProcessual() throws ServiceException {

		List classes = null;

		try {

			classes = dao.pesquisarClasseProcessual();

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return classes;
	}

	public List pesquisarClasseAntiga() throws ServiceException {

		List classes = null;

		try {

			classes = dao.pesquisarClasseAntiga();

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return classes;
	}

	@Override
	public Classe pesquisarClassePorSigla(String siglaClasse) throws ServiceException {
		Classe classe;
		
		try {
			classe = dao.pesquisarClassePorSigla(siglaClasse);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return classe;
	}

	@Override
	public List<Classe> pesquisarClassesAtivas() throws ServiceException {
		List<Classe> classes = new ArrayList<Classe>();
		try {
			classes = dao.pesquisarClassesAtivas();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return classes;
	}

}
