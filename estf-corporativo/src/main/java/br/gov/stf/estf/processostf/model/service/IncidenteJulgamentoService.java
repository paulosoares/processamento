package br.gov.stf.estf.processostf.model.service;


import java.util.List;

import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.IncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.processostf.model.dataaccess.IncidenteJulgamentoDao;
import br.gov.stf.estf.processostf.model.service.exception.DuplicacaoChaveAntigaException;
import br.gov.stf.estf.processostf.model.service.exception.IncidenteJulgamentoException;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

/**
* Interface service para a entidade Distribuicao
* @see .Distribuicao
* @author SSGJ
*/
public interface IncidenteJulgamentoService extends GenericService <IncidenteJulgamento, Long, IncidenteJulgamentoDao> {
	public List<IncidenteJulgamento> pesquisar(Long idObjetoIncidentePrincipal, String siglaTipoRecurso) throws ServiceException;
	public IncidenteJulgamento inserirIncidenteJulgamento(Long idObjetoIncidentePai, Long idTipoRecurso,Integer sequenciaCadeia)
	throws ServiceException,IncidenteJulgamentoException;
	
	public IncidenteJulgamento inserirIncidenteJulgamento(String siglaClasse, Long numeroProcesso, Long tipoRecurso, Long tipoJulgamento)throws ServiceException;
	public IncidenteJulgamento recuperarIncidenteJulgamento (String siglaClasse, Long numeroProcesso, Long tipoRecurso, Long tipoJulgamento) throws ServiceException;
	public Integer proximaSequenciaCadeia(Long idObjetoIncidentePai,Long idTipoRecurso)throws ServiceException;
	
	public List<IncidenteJulgamento> recuperarIdObjetoIncidentes(String siglaProcesso, Long numeroProcesso) throws ServiceException;
	IncidenteJulgamento inserirIncidenteJulgamentoESTFDecisao(
			Long idObjetoIncidentePai, Long idTipoRecurso,
			Integer sequenciaCadeia) throws ServiceException,
			IncidenteJulgamentoException, DuplicacaoChaveAntigaException;
	public String excluirIncidenteJulgamento(Long ij)throws ServiceException;
	public void pautarRJ(ObjetoIncidente<?> objetoIncidente) throws ServiceException;
	public Ministro recuperarRedatorIncidente(ObjetoIncidente<?> objetoIncidente) throws ServiceException;
}