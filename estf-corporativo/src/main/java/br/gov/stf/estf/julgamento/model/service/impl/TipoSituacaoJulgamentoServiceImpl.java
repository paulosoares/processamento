package br.gov.stf.estf.julgamento.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.julgamento.TipoSituacaoJulgamento;
import br.gov.stf.estf.julgamento.model.dataaccess.TipoSituacaoJulgamentoDao;
import br.gov.stf.estf.julgamento.model.service.TipoSituacaoJulgamentoService;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("tipoSituacaoJulgamentoService")
public class TipoSituacaoJulgamentoServiceImpl extends GenericServiceImpl<TipoSituacaoJulgamento, String, TipoSituacaoJulgamentoDao> implements TipoSituacaoJulgamentoService {
    public TipoSituacaoJulgamentoServiceImpl(TipoSituacaoJulgamentoDao dao) { super(dao); } 

	
}
