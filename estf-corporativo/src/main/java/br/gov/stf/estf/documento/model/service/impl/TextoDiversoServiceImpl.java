package br.gov.stf.estf.documento.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.TextoDiversoDao;
import br.gov.stf.estf.documento.model.service.TextoDiversoService;
import br.gov.stf.estf.entidade.documento.TextoDiverso;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("textoDiversoService")
public class TextoDiversoServiceImpl extends GenericServiceImpl<TextoDiverso, Long, TextoDiversoDao> 
	implements TextoDiversoService {
    public TextoDiversoServiceImpl(TextoDiversoDao dao) { super(dao); }

	public List<TextoDiverso> pesquisar(Long codigoSetor, TipoTexto... tiposTexto)
			throws ServiceException {
		List<TextoDiverso> textoDiverso = null;
		try {
			textoDiverso = dao.pesquisar(codigoSetor, tiposTexto);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		return textoDiverso;
	}

	public TextoDiverso recuperarAberturaCertidao(Long codigoSetor, TipoTexto tipoTexto)
			throws ServiceException {
		List<TextoDiverso> textos = pesquisar(codigoSetor, tipoTexto);
		if ( textos==null || textos.size()==0 ) {
			return null;
		} else if ( textos.size()==1 ) {
			return textos.get(0);
		} else {
			throw new ServiceException("Mais de um resultado encontrado.");
		}
	}

	public TextoDiverso recuperar(TipoTexto tipoTexto) throws ServiceException {
		TextoDiverso textoDiverso = null;
		try {
			textoDiverso = dao.recuperar(tipoTexto);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		return textoDiverso;
	}
	
	public TextoDiverso recuperar(String descricaoTextoDiverso,	TipoTexto tipoTexto) throws ServiceException {
		try {
			return dao.recuperar(descricaoTextoDiverso, tipoTexto);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
	}
	
	public List<TextoDiverso> pesquisar(Long codigoSetor, String descricao) throws ServiceException {
		try {
			return dao.pesquisar( codigoSetor, descricao );
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
	}

}
