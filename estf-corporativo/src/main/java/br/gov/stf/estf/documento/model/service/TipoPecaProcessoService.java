package br.gov.stf.estf.documento.model.service;

import java.util.List;

import br.gov.stf.estf.documento.model.dataaccess.TipoPecaProcessoDao;
import br.gov.stf.estf.entidade.documento.TipoPecaProcesso;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface TipoPecaProcessoService extends GenericService<TipoPecaProcesso, Long, TipoPecaProcessoDao>{
	public TipoPecaProcesso recuperar (String sigla) throws ServiceException;
			
    public List pesquisarTipoPecaProcessoEletronico () throws ServiceException;       
    
    // WARN: Método utilizado no digitalizador de peças do e-STF
    public List<TipoPecaProcesso> pesquisarTipoPecaProcessoEletronicoOrdenadoDescricao()
			throws ServiceException;
    
    // WARN: Método utilizado no digitalizador de peças do e-STF
    public TipoPecaProcesso recuperarTipoPecaProcessoEletronico(Long id)
    		throws ServiceException;
}
