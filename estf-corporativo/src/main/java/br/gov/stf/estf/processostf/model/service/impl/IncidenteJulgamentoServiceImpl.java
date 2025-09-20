package br.gov.stf.estf.processostf.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.IncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.processostf.model.dataaccess.IncidenteJulgamentoDao;
import br.gov.stf.estf.processostf.model.service.IncidenteJulgamentoService;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.exception.DuplicacaoChaveAntigaException;
import br.gov.stf.estf.processostf.model.service.exception.IncidenteJulgamentoException;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("incidenteJulgamentoService")
public class IncidenteJulgamentoServiceImpl extends GenericServiceImpl<IncidenteJulgamento, Long, IncidenteJulgamentoDao> 
    implements IncidenteJulgamentoService{
	
	private ObjetoIncidenteService objetoIncidenteService;
	
	
    public IncidenteJulgamentoServiceImpl(IncidenteJulgamentoDao dao, ObjetoIncidenteService objetoIncidenteService) { 
    	super(dao); 
    	this.objetoIncidenteService = objetoIncidenteService;
    }

	public List<IncidenteJulgamento> pesquisar(Long idObjetoIncidentePrincipal,
			String siglaTipoRecurso) throws ServiceException {
		
		try {
			return dao.pesquisar(idObjetoIncidentePrincipal, siglaTipoRecurso);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public IncidenteJulgamento inserirIncidenteJulgamento(
			Long idObjetoIncidentePai, Long idTipoRecurso,
			Integer sequenciaCadeia) throws ServiceException,IncidenteJulgamentoException {
		try {
			if( idObjetoIncidentePai == null )
				throw new IncidenteJulgamentoException("O objeto incidente pai deve ser informado.");
			if( idTipoRecurso == null )
				throw new IncidenteJulgamentoException("O tipo recurso deve ser informado.");
			
			if( sequenciaCadeia != null ){
				if( dao.existeSequenciaCadeia(idObjetoIncidentePai,idTipoRecurso, sequenciaCadeia) ){
					throw new IncidenteJulgamentoException("Já existe um incidente julgamento com a sequencia informada.");
				}
			}else{
				sequenciaCadeia = proximaSequenciaCadeia(idObjetoIncidentePai,idTipoRecurso);
			}
			
			return dao.inserirIncidenteJulgamento(idObjetoIncidentePai, idTipoRecurso, sequenciaCadeia);
			
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public IncidenteJulgamento inserirIncidenteJulgamentoESTFDecisao(
			Long idObjetoIncidentePai, Long idTipoRecurso,
			Integer sequenciaCadeia) throws ServiceException,IncidenteJulgamentoException, DuplicacaoChaveAntigaException {
		try {
			if( idObjetoIncidentePai == null )
				throw new IncidenteJulgamentoException("O objeto incidente pai deve ser informado.");
			if( idTipoRecurso == null )
				throw new IncidenteJulgamentoException("O tipo recurso deve ser informado.");
			
			if( sequenciaCadeia != null ){
				if( dao.existeSequenciaCadeia(idObjetoIncidentePai,idTipoRecurso, sequenciaCadeia) ){
					throw new IncidenteJulgamentoException("Já existe um incidente julgamento com a sequencia informada.");
				}
			}else{
				sequenciaCadeia = proximaSequenciaCadeia(idObjetoIncidentePai,idTipoRecurso);
			}
			
			return dao.inserirIncidenteJulgamentoESTFDecisao(idObjetoIncidentePai, idTipoRecurso, sequenciaCadeia);
			
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public Integer proximaSequenciaCadeia(Long idObjetoIncidentePai,Long idTipoRecurso)
	throws ServiceException {
		try {
			return dao.proximaSequenciaCadeia(idObjetoIncidentePai,idTipoRecurso);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	

	public IncidenteJulgamento inserirIncidenteJulgamento(String siglaClasse,
			Long numeroProcesso, Long tipoRecurso, Long tipoJulgamento)
			throws ServiceException {
		try {
			ObjetoIncidente objetoIncidente = objetoIncidenteService.recuperar(siglaClasse, numeroProcesso, tipoRecurso, null);
			return inserirIncidenteJulgamento(objetoIncidente.getId(), tipoJulgamento, 1);
		} catch (IncidenteJulgamentoException e) {
			throw new ServiceException(e);
		}
	}

	public IncidenteJulgamento recuperarIncidenteJulgamento(String siglaClasse,
			Long numeroProcesso, Long tipoRecurso, Long tipoJulgamento)
			throws ServiceException {
		try {
			return  dao.recuperarIncidenteJulgamento(siglaClasse, numeroProcesso, tipoRecurso, null);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public List<IncidenteJulgamento> recuperarIdObjetoIncidentes(String siglaProcesso, Long numeroProcesso) throws ServiceException{
		
		try {
			List<IncidenteJulgamento> lista = dao.recuperarIdObjetoIncidente (siglaProcesso, numeroProcesso);
			return lista;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public String excluirIncidenteJulgamento(Long ij)
	throws ServiceException {
		try {
			return dao.excluirIncidenteJulgamento(ij);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void pautarRJ(ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		try {
			dao.pautarRJ(objetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Ministro recuperarRedatorIncidente(ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		try {
			return dao.recuperarRedatorIncidente(objetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}

	
    
