package br.gov.stf.estf.documento.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.InteiroTeorAcordaoDao;
import br.gov.stf.estf.documento.model.service.InteiroTeorAcordaoService;
import br.gov.stf.estf.entidade.documento.InteiroTeorAcordao;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("inteiroTeorAcordaoService")
public class InteiroTeorAcordaoServiceImpl extends
		GenericServiceImpl<InteiroTeorAcordao, Long, InteiroTeorAcordaoDao>
		implements InteiroTeorAcordaoService {

	protected InteiroTeorAcordaoServiceImpl(InteiroTeorAcordaoDao dao) {
		super(dao);
		
	}
	
}