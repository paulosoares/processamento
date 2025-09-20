package br.gov.stf.estf.localizacao.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.TipoStatusSetor;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TipoStatusSetorDao extends GenericDao<TipoStatusSetor, Long> {
	
	public Boolean verificarUnicidade(String descricao,Long idSetor)throws DaoException;
	
	public Boolean verificarDependecia(Long idTipoStatusSetor)throws DaoException;
	
	public List <TipoStatusSetor> pesquisarTipoStatusSetor(String descricao, Long idSetor, 
			Boolean comumEntreSetores, Boolean ativo) throws DaoException;
	
	public TipoStatusSetor recuperarTipoStatusSetor(Long idStatus) throws DaoException;
	
	public Boolean persistirTipoStatusSetor(TipoStatusSetor status) throws DaoException;
	
	public Boolean excluirTipoStatusSetor(TipoStatusSetor status)throws DaoException;
	
	
}
