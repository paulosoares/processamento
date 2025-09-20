package br.gov.stf.estf.julgamento.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.julgamento.SituacaoJulgamento;
import br.gov.stf.estf.julgamento.model.dataaccess.SituacaoJulgamentoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface SituacaoJulgamentoService extends GenericService<SituacaoJulgamento, Long, SituacaoJulgamentoDao>{
	
	List<SituacaoJulgamento> pesquisar(Long idJulgamentoProcesso)throws ServiceException;
	

}
