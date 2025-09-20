package br.gov.stf.estf.documento.model.service;

import java.util.List;

import br.gov.stf.estf.documento.model.dataaccess.MotivoInaptidaoDao;
import br.gov.stf.estf.entidade.documento.MotivoInaptidao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface MotivoInaptidaoService extends GenericService<MotivoInaptidao, Long, MotivoInaptidaoDao> {

	List<MotivoInaptidao> pesquisarTodos() throws ServiceException;
}
