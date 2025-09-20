package br.gov.stf.estf.julgamento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.julgamento.EnvolvidoSessao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface EnvolvidoSessaoDao extends GenericDao<EnvolvidoSessao, Long>{

	public List<EnvolvidoSessao> pesquisar( String nomeEnvolvido, Boolean ministro, Boolean ministroSubstituto, Long idSessao, Long... codCompetencia ) throws DaoException;
	public EnvolvidoSessao recuperar( Long idSessao, Long codTipoCompetenciaEnvolvido ) throws DaoException;
	
}
