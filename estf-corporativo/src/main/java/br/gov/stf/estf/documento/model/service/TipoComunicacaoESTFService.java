package br.gov.stf.estf.documento.model.service;

import java.util.List;

import br.gov.stf.estf.documento.model.dataaccess.TipoComunicacaoESTFDao;
import br.gov.stf.estf.entidade.documento.TipoComunicacaoESTF;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface TipoComunicacaoESTFService extends
		GenericService<TipoComunicacaoESTF, Integer, TipoComunicacaoESTFDao> {
	
	
	public List<TipoComunicacaoESTF> pesquisarTodos() throws ServiceException;
	
	public List<TipoComunicacaoESTF> pesquisarPorTipo(Integer[] tipos) throws ServiceException;

}
