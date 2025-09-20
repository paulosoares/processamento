package br.gov.stf.estf.documento.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.TextoPeticaoDao;
import br.gov.stf.estf.documento.model.service.TextoPeticaoService;
import br.gov.stf.estf.entidade.documento.TextoPeticao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/*
 * public class DistribuicaoServiceImpl extends GenericServiceImpl<Distribuicao, DistribuicaoId, DistribuicaoDao> 
    implements DistribuicaoService{
    public DistribuicaoServiceImpl(DistribuicaoDao dao) { super(dao); }
 * */
@Service("textoPeticaoService")
public class TextoPeticaoServiceImpl extends GenericServiceImpl<TextoPeticao, Long, TextoPeticaoDao> implements TextoPeticaoService {
    public TextoPeticaoServiceImpl(TextoPeticaoDao dao) { super(dao); }
	public List<TextoPeticao> pesquisar (Integer numeroMateria, Short anoMateria) throws ServiceException {
		List<TextoPeticao> textosPeticao = null;
		
		try {
			textosPeticao = dao.pesquisar(numeroMateria, anoMateria);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		
		return textosPeticao;
	}
}
