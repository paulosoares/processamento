package br.gov.stf.estf.processostf.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.TipoImpressao;
import br.gov.stf.estf.processostf.model.dataaccess.TipoImpressaoDao;
import br.gov.stf.estf.processostf.model.service.TipoImpressaoService;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("tipoImpressaoService")
public class TipoImpressaoServiceImpl extends GenericServiceImpl<TipoImpressao, Short, TipoImpressaoDao>
	implements TipoImpressaoService {
    public TipoImpressaoServiceImpl(TipoImpressaoDao dao) { super(dao); }

}
