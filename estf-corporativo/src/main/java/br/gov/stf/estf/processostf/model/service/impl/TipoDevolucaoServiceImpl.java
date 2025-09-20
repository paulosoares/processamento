package br.gov.stf.estf.processostf.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.TipoDevolucao;
import br.gov.stf.estf.processostf.model.dataaccess.TipoDevolucaoDao;
import br.gov.stf.estf.processostf.model.service.TipoDevolucaoService;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("tipoDevolucaoService")
public class TipoDevolucaoServiceImpl extends GenericServiceImpl<TipoDevolucao, Long, TipoDevolucaoDao> implements TipoDevolucaoService {

	public TipoDevolucaoServiceImpl(TipoDevolucaoDao dao) {
		super(dao);
	}
}
