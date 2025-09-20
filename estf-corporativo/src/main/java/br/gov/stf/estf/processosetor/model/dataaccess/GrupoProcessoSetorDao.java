package br.gov.stf.estf.processosetor.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.processosetor.GrupoProcessoSetor;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface GrupoProcessoSetorDao extends GenericDao<GrupoProcessoSetor, Long>{

	public Boolean verificarUnicidadeGrupoProcessoSetor(Long id, Long setorId, String nomeGrupo)
	throws DaoException;
	
	public GrupoProcessoSetor recuperarGrupoProcessoSetor(Long id)
	throws DaoException;
	
	public List<GrupoProcessoSetor> pesquisarGrupoProcessoSetor(String nomeGrupo, Boolean ativo, 
			Long idSetor, Long idGrupo, String siglaClasseProcessual, Long numeroProcessual)
	throws DaoException;
	
	public Boolean persistirGrupoProcessoSetor(GrupoProcessoSetor grupoProcessoSetor)
	throws DaoException;
	
	public Boolean excluirGrupoProcessoSetor(GrupoProcessoSetor grupoProcessoSetor)
	throws DaoException;
}
