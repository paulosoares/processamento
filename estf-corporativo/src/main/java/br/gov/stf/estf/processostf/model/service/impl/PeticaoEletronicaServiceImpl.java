package br.gov.stf.estf.processostf.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.PeticaoEletronica;
import br.gov.stf.estf.processostf.model.dataaccess.PeticaoEletronicaDao;
import br.gov.stf.estf.processostf.model.service.PeticaoEletronicaService;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("peticaoEletronicaService")
public class PeticaoEletronicaServiceImpl extends GenericServiceImpl<PeticaoEletronica, Long, PeticaoEletronicaDao> implements PeticaoEletronicaService {
	
	protected PeticaoEletronicaServiceImpl(PeticaoEletronicaDao dao) {
		super(dao);
	}

}
