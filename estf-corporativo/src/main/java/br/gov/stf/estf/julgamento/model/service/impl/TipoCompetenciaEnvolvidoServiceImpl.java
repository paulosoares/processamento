package br.gov.stf.estf.julgamento.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.julgamento.TipoCompetenciaEnvolvido;
import br.gov.stf.estf.julgamento.model.dataaccess.TipoCompetenciaEnvolvidoDao;
import br.gov.stf.estf.julgamento.model.service.TipoCompetenciaEnvolvidoService;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("tipoCompetenciaEnvolvidoService")
public class TipoCompetenciaEnvolvidoServiceImpl extends GenericServiceImpl<TipoCompetenciaEnvolvido, Long, TipoCompetenciaEnvolvidoDao> implements TipoCompetenciaEnvolvidoService {
    public TipoCompetenciaEnvolvidoServiceImpl(TipoCompetenciaEnvolvidoDao dao) { super(dao); } 

}
