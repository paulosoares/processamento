package br.gov.stf.estf.processosetor.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processosetor.ControleDistribuicao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface DistribuicaoProcessoSetorDao extends GenericDao<ControleDistribuicao, Long>{
	
	public Boolean persistirMapaDistribuicao(ControleDistribuicao mapaDistribuicao) throws DaoException;
	public Boolean excluirControleDistribuicao(ControleDistribuicao controleDistribuicao)throws DaoException;
	
	public List<ControleDistribuicao> pesquisarControleDistribuicao( Long id,Long idGrupoUsuario, String sigClasse, String tipoJuglamento,String sigUsuario ) 
	throws DaoException;

	public Long pesquisarGrupoDistribuicao(Processo processo) throws DaoException;
	public void subtrairMapaDistribuicao(Long quantidade, Long grupoDistribuicao, Ministro ministro) throws DaoException;
	
}
