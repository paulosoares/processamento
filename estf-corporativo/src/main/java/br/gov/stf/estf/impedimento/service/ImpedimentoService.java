package br.gov.stf.estf.impedimento.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.impedimento.dataaccess.ImpedimentoDao;
import br.gov.stf.framework.model.service.ServiceException;

@Service
public class ImpedimentoService {

	@Autowired
	ImpedimentoDao dao;
	
	public boolean temImpedimento(Long codMinistro, Long objetoIncidente) throws ServiceException {
		if (codMinistro != null && objetoIncidente != null && objetoIncidente != null)
			return dao.temImpedimento(codMinistro, objetoIncidente);
		
		return false;
	}
}
