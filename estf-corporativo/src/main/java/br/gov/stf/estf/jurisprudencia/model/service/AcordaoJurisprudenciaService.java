/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service;

import java.util.Collection;

import javax.persistence.PersistenceException;

import br.gov.stf.estf.entidade.jurisprudencia.AcordaoJurisprudencia;
import br.gov.stf.estf.entidade.jurisprudencia.IncidenteAnalise;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.AcordaoJurisprudenciaDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * @author Paulo.Estevao
 * @since 20.08.2012
 */
public interface AcordaoJurisprudenciaService extends GenericService<AcordaoJurisprudencia, Long, AcordaoJurisprudenciaDao> {

	void atualizarDocumentoAcordaoMesmoSentidoAcordaoPrincipal(IncidenteAnalise incidenteAnalise)
			throws ServiceException;

	void atualizarDocumentoAcordaoMesmoSentidoAcordaosPrincipais(Collection<IncidenteAnalise> acordaosPrincipais)
			throws ServiceException;

	void atualizarDocumentoIndexacaoPrincipal(IncidenteAnalise incidenteAnalise) throws ServiceException;
	
	void inserirProcessoPrincipal(IncidenteAnalise incidenteAnalise) throws ServiceException;

	void atualizarDocumentoLegislacaoPrincipal(IncidenteAnalise incidenteAnalise) throws ServiceException;
	
	boolean hasJurisprudenciaRelevante(AcordaoJurisprudencia acordaoJurisprudencia)
			throws ServiceException;

	boolean hasConferenciaAcordao(AcordaoJurisprudencia acordaoJurisprudencia)
			throws ServiceException;

	int pesquisarPorObjetoIncidente(Long id) throws PersistenceException;
	
	void ordenarItensLegislacao (Long idLegislacao)throws ServiceException, DaoException;
	
	void salvarPublicacao(Long idAcordaoJurisprudencia, String txtPublicacao) throws ServiceException;
	
	/**
	 * Retorna o AcordaoJurisprudencia com base no ObjetoIncidente
	 * 
	 * @param oi
	 * @return
	 * @throws ServiceException 
	 */
	AcordaoJurisprudencia recuperarPorObjetoIncidente(ObjetoIncidente<?> oi) throws ServiceException;
	
	/**
	 * Retorna a quantidade de Acórdãos do mesmo sentido que está salvo na base do BRS
	 * @param objetoIncidenteId
	 * @return
	 */
	Integer quantidadeAcordaosMesmoSentidoBrs(Long objetoIncidenteId);

}
