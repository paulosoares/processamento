package br.gov.stf.estf.documento.model.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.TextoAndamentoProcessoDao;
import br.gov.stf.estf.documento.model.service.TextoAndamentoProcessoService;
import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TextoAndamentoProcesso;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.criterion.InCriterion;
import br.gov.stf.framework.model.dataaccess.hibernate.criterion.SearchCriterion;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("textoAndamentoProcessoService")
public class TextoAndamentoProcessoServiceImpl extends GenericServiceImpl<TextoAndamentoProcesso, Long, TextoAndamentoProcessoDao> 
	implements TextoAndamentoProcessoService {
    public TextoAndamentoProcessoServiceImpl(TextoAndamentoProcessoDao dao) { super(dao); }
    
    public List<TextoAndamentoProcesso> recuperarTextoAndamentoProcesso(Long codigoAndamentoProcesso, Long codigoDocumento) 
    throws ServiceException {
    	
    	List<TextoAndamentoProcesso> listTextoAndamentoProcesso = null;
    	
        try {
        	
        	listTextoAndamentoProcesso = dao.recuperarTextoAndamentoProcesso(codigoAndamentoProcesso,codigoDocumento );
        }
        catch( DaoException e ) {
            throw new ServiceException(e);
        }
        
        return listTextoAndamentoProcesso;
    }
    
    public TextoAndamentoProcesso recuperarTextoAndamentoProcesso(Long numero) 
    throws ServiceException {
    	
    	TextoAndamentoProcesso textoAndamentoProcesso = null;
    	
        try {
        	
        	textoAndamentoProcesso = dao.recuperarTextoAndamentoProcesso(numero);

        }
        catch( DaoException e ) {
            throw new ServiceException(e);
        }
        
        return textoAndamentoProcesso;
    }
    
    public Texto recuperarTexto(Long codigoTexto) 
    throws ServiceException {    	
    	Texto texto = null;    	
        try {        	
        	texto = dao.recuperarTexto(codigoTexto);
        }
        catch( DaoException e ) {
            throw new ServiceException(e);
        }       
        return texto;
    }
    
    
	public void persistirTextoAndamentoProcesso(TextoAndamentoProcesso textoAndamentoProcesso) throws ServiceException {
		try {
			dao.persistirTextoAndamentoProcesso(textoAndamentoProcesso);

			if (textoAndamentoProcesso.getAndamentoProcesso() != null) {
				List<TextoAndamentoProcesso> listaTextoAndamentoProcessos = textoAndamentoProcesso.getAndamentoProcesso().getListaTextoAndamentoProcessos();

				if (listaTextoAndamentoProcessos == null)
					listaTextoAndamentoProcessos = new ArrayList<TextoAndamentoProcesso>();

				listaTextoAndamentoProcessos.add(textoAndamentoProcesso);
				textoAndamentoProcesso.getAndamentoProcesso().setListaTextoAndamentoProcessos(listaTextoAndamentoProcessos);
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
    
    public void persistirTexto(Texto texto) throws ServiceException {
        try {            
        	dao.persistirTexto(texto);
        }
        catch( DaoException e ) {
            throw new ServiceException(e);
        }
    }
    
    public void persistirDocumentoTexto(DocumentoTexto documentoTexto) throws ServiceException {
        try {            
        	dao.persistirDocumentoTexto(documentoTexto);
        }
        catch( DaoException e ) {
            throw new ServiceException(e);
        }
    }

	@Override
	public List<TextoAndamentoProcesso> pesquisar(List<Texto> listaTextos) throws ServiceException {
		List<SearchCriterion> criterioTexto = new ArrayList<SearchCriterion>();
		criterioTexto.add(new InCriterion<Texto>("texto", listaTextos));
		return pesquisarPorExemplo(new TextoAndamentoProcesso(), criterioTexto);
	}

}
