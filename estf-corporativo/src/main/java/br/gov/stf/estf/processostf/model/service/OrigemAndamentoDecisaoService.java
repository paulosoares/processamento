package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.OrigemAndamentoDecisao;
import br.gov.stf.estf.processostf.model.dataaccess.OrigemAndamentoDecisaoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface OrigemAndamentoDecisaoService extends GenericService<OrigemAndamentoDecisao, Long, OrigemAndamentoDecisaoDao> {
	
	public OrigemAndamentoDecisao pesquisarOrigemDecisao(Setor setor) throws ServiceException;

	public List<OrigemAndamentoDecisao> pesquisarOrigensComMinistroAtivo() throws ServiceException;
	
	public List<OrigemAndamentoDecisao> pesquisarOrigensDecisao(List<Long> idsOrigem) throws ServiceException;

}
