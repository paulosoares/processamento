/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.jurisprudencia.IncidenteAnalise;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.IncidenteAnaliseDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * @author Paulo.Estevao
 * @since 10.07.2012
 */
public interface IncidenteAnaliseService extends GenericService<IncidenteAnalise, Long, IncidenteAnaliseDao> {

	IncidenteAnalise recuperar(ObjetoIncidente<?> objetoIncidente) throws ServiceException;

	byte[] recuperarVisualizacaoSucessivo(IncidenteAnalise incidenteAnalise) throws ServiceException;

	List<IncidenteAnalise> pesquisarSucessivos(IncidenteAnalise incidenteAnalise) throws ServiceException;

	byte[] recuperarVisualizacaoIndexacaoPrincipal(IncidenteAnalise incidenteAnalise) throws ServiceException;
	
	byte[] recuperarVisualizacaoIndexacaoParagrafo(IncidenteAnalise incidenteAnalise, long idParagrafo) throws ServiceException;

	void carregarAcordaoPublicado(Long seqObjetoIncidente, Long seqDataPublicacao) throws ServiceException;

	void salvarTipoMateria(IncidenteAnalise incidenteAnalise) throws ServiceException;

	byte[] recuperarVisualizacaoLegislacoes(IncidenteAnalise incidenteAnalise) throws ServiceException;

	IncidenteAnalise recuperarPorObjetoIncidente(Long id) throws ServiceException;
	
	void salvarIncidenteAnaliseDesvinculado(IncidenteAnalise incidenteAnalise) throws ServiceException;
	
	void salvarPublicacao(Long idIncidenteAnalise, String txtPublicacao) throws ServiceException;

	String recuperarInformacaoPublicacao(IncidenteAnalise incidenteAnalise) throws ServiceException;
	
	/**
	 * Retorna se o incidente de análise é uma repercussão geral
	 * 
	 * @param objetoIncidenteId
	 * @return
	 * @throws ServiceException 
	 */
	Boolean temRepercussaoGeral(Long objetoIncidenteId) throws ServiceException;
	
	/**
	 * Retorna a quantidade de Acórdãos do mesmo sentido que está esturutrado em incidente de análise
	 * @param incidenteAnaliseId
	 * @return
	 */
	Integer quantidadeAcordaosMesmoSentido(Long incidenteAnaliseId);

}
