package br.gov.stf.estf.processostf.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.ProtocoloPublicado;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.processostf.model.dataaccess.ProtocoloPublicadoDao;
import br.gov.stf.estf.processostf.model.service.ProtocoloPublicadoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("protocoloPublicadoService")
public class ProtocoloPublicadoServiceImpl extends GenericServiceImpl<ProtocoloPublicado, Long, ProtocoloPublicadoDao> 
	implements ProtocoloPublicadoService {
    public ProtocoloPublicadoServiceImpl(ProtocoloPublicadoDao dao) { super(dao); }

	public List<ProtocoloPublicado> pesquisar(
			ConteudoPublicacao conteudoPublicacao,
			Boolean recuperarOcultos) throws ServiceException {
		List<ProtocoloPublicado> protocolos = null;
		try {
			protocolos = dao.pesquisar(conteudoPublicacao, recuperarOcultos);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		return protocolos;
	}

}
