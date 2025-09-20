package br.gov.stf.estf.processostf.model.dataaccess;


import java.util.List;

import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.IncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.processostf.model.service.exception.DuplicacaoChaveAntigaException;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
* DAO interface para a entidade Distribuicao.
* @see Distribuicao
*/
public interface IncidenteJulgamentoDao extends GenericDao <IncidenteJulgamento, Long> {
	
	public List<IncidenteJulgamento> pesquisar(Long idObjetoIncidentePrincipal, String siglaTipoRecurso) throws DaoException;
	
	public IncidenteJulgamento inserirIncidenteJulgamento(Long idObjetoIncidentePai, Long idTipoRecurso,Integer sequenciaCadeia)throws DaoException;

	public IncidenteJulgamento recuperarIncidenteJulgamento (String siglaClasse, Long numeroProcesso, Long tipoRecurso, Long tipoJulgamento) throws DaoException;
	
	public Integer proximaSequenciaCadeia(Long idObjetoIncidentePai,Long idTipoRecurso)throws DaoException;
	
	public Boolean existeSequenciaCadeia(Long idObjetoIncidentePai,Long idTipoRecurso,Integer numeroSequenciaCadeia)throws DaoException;
	
	public List<IncidenteJulgamento> recuperarIdObjetoIncidente(String siglaProcesso, Long numeroProcesso) throws DaoException;

	IncidenteJulgamento inserirIncidenteJulgamentoESTFDecisao(
			Long idObjetoIncidentePai, Long idTipoRecurso,
			Integer sequenciaCadeia) throws DaoException,
			DuplicacaoChaveAntigaException;

	public String excluirIncidenteJulgamento(Long ij) throws DaoException;

	public void pautarRJ(ObjetoIncidente<?> objetoIncidente) throws DaoException;

	public Ministro recuperarRedatorIncidente(ObjetoIncidente<?> objetoIncidente) throws DaoException;
}

