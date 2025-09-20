package br.gov.stf.estf.julgamento.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.julgamento.TipoEvento;
import br.gov.stf.estf.julgamento.model.dataaccess.TipoEventoDao;
import br.gov.stf.estf.julgamento.model.service.TipoEventoService;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("tipoEventoService")
public class TipoEventoServiceImpl extends GenericServiceImpl<TipoEvento, String, TipoEventoDao> implements TipoEventoService {
    public TipoEventoServiceImpl(TipoEventoDao dao) { super(dao); }

}
