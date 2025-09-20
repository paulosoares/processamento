package br.gov.stf.estf.configuracao.model.service;

import java.util.List;

import br.gov.stf.estf.configuracao.model.dataaccess.AlertaSistemaDao;
import br.gov.stf.estf.entidade.configuracao.AlertaSistema;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface AlertaSistemaService extends GenericService<AlertaSistema, Long, AlertaSistemaDao> {

	public List<AlertaSistema> recuperarValor(String siglaSistema, String chave) throws ServiceException;

}
