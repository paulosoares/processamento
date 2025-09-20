package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.Assunto;
import br.gov.stf.estf.entidade.processostf.AssuntoProcesso;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.processostf.model.dataaccess.AssuntoProcessoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface AssuntoProcessoService extends GenericService<AssuntoProcesso, Long, AssuntoProcessoDao> {
	
	public List<AssuntoProcesso> pesquisar(String siglaClasseProcessual, Long numeroProcesso) throws ServiceException;
	
	public void persistirAssuntoProcesso(AssuntoProcesso assuntoProcesso) throws ServiceException;
	
	public Assunto recuperarAssuntoProcesso(Processo processo) throws ServiceException;
	
	public List<Assunto> recuperarListaAssuntosDoProcesso(Processo processo)throws ServiceException;
	
}
