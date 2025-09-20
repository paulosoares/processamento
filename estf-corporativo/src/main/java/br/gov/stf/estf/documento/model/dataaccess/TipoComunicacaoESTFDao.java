package br.gov.stf.estf.documento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.documento.TipoComunicacaoESTF;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TipoComunicacaoESTFDao extends GenericDao<TipoComunicacaoESTF, Integer>{

	
	public List<TipoComunicacaoESTF> pesquisarTodos() throws DaoException;
	
	public List<TipoComunicacaoESTF> pesquisarPorTipo(Integer[] tipos) throws DaoException;
}
