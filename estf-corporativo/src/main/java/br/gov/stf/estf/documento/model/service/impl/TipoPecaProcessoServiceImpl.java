package br.gov.stf.estf.documento.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.TipoPecaProcessoDao;
import br.gov.stf.estf.documento.model.service.TipoPecaProcessoService;
import br.gov.stf.estf.entidade.documento.TipoPecaProcesso;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("tipoPecaProcessoService")
public class TipoPecaProcessoServiceImpl extends GenericServiceImpl<TipoPecaProcesso, Long, TipoPecaProcessoDao> 
	implements TipoPecaProcessoService {
    public TipoPecaProcessoServiceImpl(TipoPecaProcessoDao dao) { super(dao); }

	public TipoPecaProcesso recuperar(String sigla) throws ServiceException {
		TipoPecaProcesso tipoPecaProcesso = null;
		try {
			tipoPecaProcesso = dao.recuperar(sigla);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		return tipoPecaProcesso;
	}
	
    public List pesquisarTipoPecaProcessoEletronico () throws ServiceException {    	
    	List lista = null;    	
        try {        	
            lista = dao.pesquisarTipoPecaProcessoEletronico();
        } catch( DaoException e ) {
            throw new ServiceException(e);
        }        
        return lista;
    }
    
    
    // WARN: Método utilizado no digitalizador de peças do e-STF
    public List<TipoPecaProcesso> pesquisarTipoPecaProcessoEletronicoOrdenadoDescricao()
			throws ServiceException {
		List<TipoPecaProcesso> lista = null;    	
		try {        	
		    lista = dao.pesquisarTipoPecaProcessoEletronicoOrdenadoDescricao();
		} catch( DaoException e ) {
		    throw new ServiceException(e);
		}        
		return lista;
	}
    
    
    // WARN: Método utilizado no digitalizador de peças do e-STF
    public TipoPecaProcesso recuperarTipoPecaProcessoEletronico(Long id)
			throws ServiceException {
    	TipoPecaProcesso tipoPeca = null;     
        try {           
            tipoPeca = dao.recuperarTipoPecaProcessoEletronico(id);
        }
        catch( DaoException e ) {
            throw new ServiceException(e);
        }       
        return tipoPeca;
	}

}
