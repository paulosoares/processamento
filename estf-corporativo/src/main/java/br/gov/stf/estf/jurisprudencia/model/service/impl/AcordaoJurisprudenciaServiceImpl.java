/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.jurisprudencia.AcordaoJurisprudencia;
import br.gov.stf.estf.entidade.jurisprudencia.IncidenteAnalise;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.AcordaoJurisprudenciaDao;
import br.gov.stf.estf.jurisprudencia.model.service.AcordaoJurisprudenciaService;
import br.gov.stf.estf.jurisprudencia.model.service.IncidenteAnaliseService;
import br.gov.stf.estf.jurisprudencia.model.service.LegislacaoIncidenteAnaliseService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Paulo.Estevao
 * @since 20.08.2012
 */
@Service("acordaoJurisprudenciaService")
public class AcordaoJurisprudenciaServiceImpl extends GenericServiceImpl<AcordaoJurisprudencia, Long, AcordaoJurisprudenciaDao> implements
		AcordaoJurisprudenciaService {
	
	@Autowired
	private IncidenteAnaliseService incidenteAnaliseService;
	
	@Autowired
	private LegislacaoIncidenteAnaliseService legislacaoIncidenteAnaliseService;
	
	protected AcordaoJurisprudenciaServiceImpl(AcordaoJurisprudenciaDao dao) {
		super(dao);
	}

	@Override
	public void atualizarDocumentoAcordaoMesmoSentidoAcordaosPrincipais(Collection<IncidenteAnalise> acordaosPrincipais) throws ServiceException {
		for (IncidenteAnalise incidenteAnalise : acordaosPrincipais) {
			atualizarDocumentoAcordaoMesmoSentidoAcordaoPrincipal(incidenteAnalise);
		}
	}

	@Override
	public void atualizarDocumentoAcordaoMesmoSentidoAcordaoPrincipal(IncidenteAnalise incidenteAnalise) throws ServiceException {
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		try {
		
			byte[] documentoSucessivo = incidenteAnaliseService.recuperarVisualizacaoSucessivo(incidenteAnalise);
			
			if (documentoSucessivo != null) {
				outputStream.write(documentoSucessivo);
			}

			byte documentoAcordaoMesmoSentido[] = outputStream.toByteArray();
			AcordaoJurisprudencia acordaoJurisprudencia = recuperarPorId(incidenteAnalise.getAcordaoJurisprudencia().getId());
			acordaoJurisprudencia.setAcordaoMesmoSentido(new String(documentoAcordaoMesmoSentido, "UTF-8"));
			salvar(acordaoJurisprudencia);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void atualizarDocumentoIndexacaoPrincipal(IncidenteAnalise incidenteAnalise) throws ServiceException {
		AcordaoJurisprudencia acordaoJurisprudencia = recuperarPorId(incidenteAnalise.getAcordaoJurisprudencia().getId());
		byte[] documentoIndexacao = incidenteAnaliseService.recuperarVisualizacaoIndexacaoPrincipal(incidenteAnalise);
		try {
			acordaoJurisprudencia.setIndexacao(documentoIndexacao != null ? new String(documentoIndexacao, "UTF-8") : null);
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException(e);
		}
		salvar(acordaoJurisprudencia);
	}
	
	@Override
	public void inserirProcessoPrincipal (IncidenteAnalise incidenteAnalise) throws ServiceException{
		try {
			dao.inserirProcessoPrincipal(incidenteAnalise);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	public void ordenarItensLegislacao (Long idLegislacao) throws ServiceException, DaoException{
		dao.ordenarItensLegislacao(idLegislacao);
	}
	@Override
	public void atualizarDocumentoLegislacaoPrincipal(IncidenteAnalise incidenteAnalise) throws ServiceException {
		try {
			incidenteAnalise = incidenteAnaliseService.recuperarPorId(incidenteAnalise.getId());

			AcordaoJurisprudencia acordaoJurisprudencia = recuperarPorId(incidenteAnalise.getAcordaoJurisprudencia()
					.getId());
			
			byte[] visualizacao = incidenteAnaliseService.recuperarVisualizacaoLegislacoes(incidenteAnalise);
			if (visualizacao != null) {
			       acordaoJurisprudencia.setLegislacao(new String(visualizacao, "UTF-8"));
			}else{
				acordaoJurisprudencia.setLegislacao(null);
			}
			
			salvar(acordaoJurisprudencia);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public boolean hasJurisprudenciaRelevante(AcordaoJurisprudencia acordaoJurisprudencia) throws ServiceException{
		boolean result = false;
		
		try {
			result = dao.hasJurisprudenciaRelevante(acordaoJurisprudencia);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return result;
	}
	
	@Override
	public boolean hasConferenciaAcordao(AcordaoJurisprudencia acordaoJurisprudencia) throws ServiceException{
		boolean result = false;
		
		try {
			result = dao.hasConferenciaAcordao(acordaoJurisprudencia);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return result;
	}

	@Override
	public int pesquisarPorObjetoIncidente(Long id) throws PersistenceException {
		try {
			return dao.pesquisarPorObjetoIncidente(id);
		} catch (PersistenceException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public void salvarPublicacao(Long idAcordaoJurisprudencia, String txtPublicacao) throws ServiceException {
		try {
			dao.salvarPublicacao(idAcordaoJurisprudencia, txtPublicacao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public AcordaoJurisprudencia recuperarPorObjetoIncidente(
			ObjetoIncidente<?> oi) throws ServiceException {

		try {
			return this.dao.recuperarPorObjetoIncidente(oi);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("Não foi possível recuperar a Repercussão Geral com base no Objeto Incidente Informado.", e);
		}
	}

	@Override
	public Integer quantidadeAcordaosMesmoSentidoBrs(Long objetoIncidenteId) {

		return this.dao.quantidadeAcordaosMesmoSentidoBrs(objetoIncidenteId);
	}
}