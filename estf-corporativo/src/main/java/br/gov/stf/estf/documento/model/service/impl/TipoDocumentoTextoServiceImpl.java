package br.gov.stf.estf.documento.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.TipoDocumentoTextoDao;
import br.gov.stf.estf.documento.model.service.TipoDocumentoTextoService;
import br.gov.stf.estf.entidade.documento.TipoDocumentoTexto;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("tipoDocumentoTextoService")
public class TipoDocumentoTextoServiceImpl extends GenericServiceImpl<TipoDocumentoTexto, Long, TipoDocumentoTextoDao> 
	implements TipoDocumentoTextoService {
    public TipoDocumentoTextoServiceImpl(TipoDocumentoTextoDao dao) { super(dao); }

	public List<TipoDocumentoTexto> pesquisarTiposDocumentoTextoPorSetor(Long codSetor) throws ServiceException {
		try {
			return dao.pesquisarTiposDocumentoTextoPorSetor(codSetor);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
	}

	public TipoDocumentoTexto recuperar(Long codigoTipoTexto) throws ServiceException {
		TipoDocumentoTexto tipoDocumentoTexto = null;
		try {
			tipoDocumentoTexto = dao.recuperar(codigoTipoTexto);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		return tipoDocumentoTexto;
	}

}
