package br.gov.stf.estf.alerta.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.alerta.model.dataaccess.ValorFiltroAlertaDao;
import br.gov.stf.estf.alerta.model.service.ValorFiltroAlertaService;
import br.gov.stf.estf.entidade.alerta.ValorFiltroAlerta;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("valorFiltroAlertaService")
public class ValorFiltroAlertaServiceImpl extends GenericServiceImpl<ValorFiltroAlerta, Long, ValorFiltroAlertaDao>
		implements ValorFiltroAlertaService {

	protected ValorFiltroAlertaServiceImpl(ValorFiltroAlertaDao dao) {
		super(dao);
	}

}