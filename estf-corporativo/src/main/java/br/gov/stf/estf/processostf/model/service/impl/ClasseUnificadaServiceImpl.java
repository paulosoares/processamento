package br.gov.stf.estf.processostf.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.ClasseUnificada;
import br.gov.stf.estf.processostf.model.dataaccess.ClasseUnificadaDao;
import br.gov.stf.estf.processostf.model.service.ClasseUnificadaService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("classeUnificadaService")
public class ClasseUnificadaServiceImpl extends 
	GenericServiceImpl<ClasseUnificada, ClasseUnificada.ClasseUnificadaId, ClasseUnificadaDao> 
	implements ClasseUnificadaService {

	protected ClasseUnificadaServiceImpl(ClasseUnificadaDao dao) {
		super(dao);
	}

	public Short recuperarCodigoClasseUnificada(String sigClasse,
			String tipJulgamento, Long codRecurso) throws ServiceException {
		Short codClasse = null;

		try {
			codClasse = dao.recuperarCodigoClasseUnificada(sigClasse, tipJulgamento, codRecurso);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}

		return codClasse;
	}   

}