package br.gov.stf.estf.usuario.model.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.usuario.IntegracaoDocumento;
import br.gov.stf.estf.usuario.model.dataaccess.IntegracaoDocumentoDao;
import br.gov.stf.estf.usuario.model.service.IntegracaoDocumentoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("integracaoDocumentoService")
public class IntegracaoDocumentoServiceImpl extends	GenericServiceImpl<IntegracaoDocumento, Long, IntegracaoDocumentoDao>
		implements IntegracaoDocumentoService {

	public IntegracaoDocumentoServiceImpl(IntegracaoDocumentoDao dao) {
		super(dao);
	}

	@Override
	public Date findMaxDataById(Long seqUsuarioExterno) throws ServiceException {

		Date retorno = null;
		
		try {
			retorno = dao.findMaxDatInclusao(seqUsuarioExterno);
		} catch(DaoException e) {
			throw new ServiceException(e);
		}     
		return retorno;
	}
}



