package br.gov.stf.estf.processostf.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.Categoria;
import br.gov.stf.estf.processostf.model.dataaccess.CategoriaDao;
import br.gov.stf.estf.processostf.model.service.CategoriaService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("categoriaService")
public class CategoriaServiceImpl extends GenericServiceImpl<Categoria, Long, CategoriaDao> 
	implements CategoriaService{
    public CategoriaServiceImpl(CategoriaDao dao) { super(dao); }

	public List<Categoria> pesquisar(String sigla, String descricao,
			Boolean ativo) throws ServiceException {
		try {
			return dao.pesquisar(sigla, descricao, ativo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	
}
