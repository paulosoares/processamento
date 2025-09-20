package br.gov.stf.estf.processostf.model.dataaccess;


// default package
// Generated 18/03/2008 11:02:23 by Hibernate Tools 3.1.0.beta5
import java.util.List;

import br.gov.stf.estf.entidade.processostf.Categoria;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;


/**
 * DAO interface for domain model class Distribuicao.
 * @see .Distribuicao
 * @author Hibernate Tools
 */

public interface CategoriaDao 
extends GenericDao <Categoria, Long> {
	public List<Categoria> pesquisar (String sigla, String descricao,Boolean ativo) throws DaoException;
	
	
}

