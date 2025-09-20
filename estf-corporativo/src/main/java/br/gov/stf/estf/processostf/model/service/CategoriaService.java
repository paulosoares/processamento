package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.Categoria;
import br.gov.stf.estf.processostf.model.dataaccess.CategoriaDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface CategoriaService extends GenericService<Categoria, Long, CategoriaDao>{

	public List<Categoria> pesquisar (String sigla, String descricao,Boolean ativo) throws ServiceException;
	
	
}
