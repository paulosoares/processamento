package br.gov.stf.estf.usuario.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.usuario.AdvogadoPeticionamento;
import br.gov.stf.estf.usuario.model.dataaccess.AdvogadoPeticionamentoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface AdvogadoPeticionamentoService extends GenericService<AdvogadoPeticionamento, Long, AdvogadoPeticionamentoDao> {
	public void persistirAdvogado(AdvogadoPeticionamento advogado) throws ServiceException;
	public List<AdvogadoPeticionamento> pesquisarAdvogado (Long cpf)throws ServiceException;
}
