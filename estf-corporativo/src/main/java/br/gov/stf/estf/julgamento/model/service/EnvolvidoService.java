package br.gov.stf.estf.julgamento.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.julgamento.Envolvido;
import br.gov.stf.estf.julgamento.model.dataaccess.EnvolvidoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface EnvolvidoService extends GenericService<Envolvido, Long, EnvolvidoDao> {

	List<Envolvido> pesquisar(String sugestaoNome) throws ServiceException;

}
