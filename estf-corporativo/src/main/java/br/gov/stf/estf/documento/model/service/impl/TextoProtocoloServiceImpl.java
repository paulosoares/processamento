package br.gov.stf.estf.documento.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.TextoProtocoloDao;
import br.gov.stf.estf.documento.model.service.TextoProtocoloService;
import br.gov.stf.estf.entidade.documento.TextoPeticao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("textoProtocoloService")
public class TextoProtocoloServiceImpl extends GenericServiceImpl<TextoPeticao, Long, TextoProtocoloDao> 
	implements TextoProtocoloService {
    public TextoProtocoloServiceImpl(TextoProtocoloDao dao) { super(dao); }

	public List<TextoPeticao> pesquisar(Integer numMateria,
			Short anoMateria) throws ServiceException {
		List<TextoPeticao> textos = null;
		try {
			textos = dao.pesquisar(numMateria, anoMateria);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		return textos;
	}

}
