package br.gov.stf.estf.processostf.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.ProtocoloPublicado;
import br.gov.stf.estf.entidade.processostf.TextoAssociadoProtocolo;
import br.gov.stf.estf.entidade.processostf.TextoAssociadoProtocolo.TextoAssociadoProtocoloId;
import br.gov.stf.estf.entidade.processostf.TextoAssociadoProtocolo.TipoAssociacao;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.processostf.model.dataaccess.TextoAssociadoProtocoloDao;
import br.gov.stf.estf.processostf.model.service.TextoAssociadoProtocoloService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("textoAssociadoProtocoloService")
public class TextoAssociadoProtocoloServiceImpl extends GenericServiceImpl<TextoAssociadoProtocolo, TextoAssociadoProtocoloId, TextoAssociadoProtocoloDao> 
	implements TextoAssociadoProtocoloService {
    public TextoAssociadoProtocoloServiceImpl(TextoAssociadoProtocoloDao dao) { super(dao); }

	public List<TextoAssociadoProtocolo> pesquisar(ProtocoloPublicado protocoloPublicado ) throws ServiceException {
		List<TextoAssociadoProtocolo> protocolos = null;
		try {
			protocolos = dao.pesquisar(protocoloPublicado);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		
		return protocolos;
	}

	public List<TextoAssociadoProtocolo> pesquisar(
			ConteudoPublicacao conteudoPublicacao,
			TipoAssociacao... tipoAssociacao) throws ServiceException {
		List<TextoAssociadoProtocolo> protocolos = null;
		try {
			protocolos = dao.pesquisar(conteudoPublicacao, tipoAssociacao);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		
		return protocolos;
	}

	public TextoAssociadoProtocolo recuperar(
			ProtocoloPublicado protocoloPublicado, TipoAssociacao tipoAssociacao)
			throws ServiceException {
		TextoAssociadoProtocolo textoAssociadoProtocolo = null;
		try {
			textoAssociadoProtocolo = dao.recuperar(protocoloPublicado, tipoAssociacao);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		return textoAssociadoProtocolo;
	}

}
