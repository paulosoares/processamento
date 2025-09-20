package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.Orgao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface OrgaoDao extends GenericDao<Orgao, Long> {

	public List<Orgao> pesquisarOrgaosAtivos() throws DaoException;
	
	public List<Orgao> pesquisarPelaDescricaoOrgaosAtivos(String descricao) throws DaoException;
}
