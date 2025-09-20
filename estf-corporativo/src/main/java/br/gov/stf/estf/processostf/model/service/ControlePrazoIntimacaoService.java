package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.ControlePrazoIntimacao;
import br.gov.stf.estf.processostf.model.dataaccess.ControlePrazoIntimacaoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ControlePrazoIntimacaoService extends GenericService<ControlePrazoIntimacao, Long, ControlePrazoIntimacaoDao> {
	public void persistirControlePrazoIntimcao(ControlePrazoIntimacao controlePrazoIntimacao)throws ServiceException;
	public List<ControlePrazoIntimacao> recuperarProcessoIntimadoPendente()throws ServiceException;
	public void atualizaControlePrazoIntimacao(ControlePrazoIntimacao controlePrazoIntimacao) throws ServiceException;
	public ControlePrazoIntimacao recuperarControlePrazoIntimacao(Long seqAndamentoProcesso) throws ServiceException;
}
