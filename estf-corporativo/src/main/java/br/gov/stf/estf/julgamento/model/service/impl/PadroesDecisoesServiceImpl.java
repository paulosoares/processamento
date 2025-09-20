package br.gov.stf.estf.julgamento.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.julgamento.TextoModelo;
import br.gov.stf.estf.julgamento.model.dataaccess.PadroesDecisoesDao;
import br.gov.stf.estf.julgamento.model.service.PadroesDecisoesService;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("padroesDecisoesService")
public class PadroesDecisoesServiceImpl extends GenericServiceImpl<TextoModelo, Long, PadroesDecisoesDao>
		implements PadroesDecisoesService {

	public PadroesDecisoesServiceImpl(PadroesDecisoesDao dao) {
		super(dao);
	}

}
