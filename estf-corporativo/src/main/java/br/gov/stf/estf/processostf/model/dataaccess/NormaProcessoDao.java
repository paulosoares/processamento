package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.NormaProcesso;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface NormaProcessoDao extends GenericDao<NormaProcesso, Long> {
	
	public List<NormaProcesso> pesquisarNormasProcesso(Long codigo, String descricao, Short ano, String normaJurisprudencia)
	throws DaoException;

}
