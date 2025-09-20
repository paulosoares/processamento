/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.jurisprudencia.IncidenteAnalise;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * @author Paulo.Estevao
 * @since 10.07.2012
 */
public interface IncidenteAnaliseDao extends GenericDao<IncidenteAnalise, Long> {

	IncidenteAnalise recuperar(ObjetoIncidente<?> objetoIncidente) throws DaoException;

	byte[] recuperarVisualizacaoSucessivo(IncidenteAnalise incidenteAnalise) throws DaoException;

	List<IncidenteAnalise> pesquisarSucessivos(IncidenteAnalise incidenteAnalise) throws DaoException;

	byte[] recuperarVisualizacaoIndexacaoPrincipal(IncidenteAnalise incidenteAnalise) throws DaoException;
	
	byte[] recuperarVisualizacaoIndexacaoParagrafo(IncidenteAnalise incidenteAnalise, Long idParagrafo) throws DaoException;
	
	void carregarAcordaoPublicado(Long seqObjetoIncidente, Long seqDataPublicacao) throws DaoException;

	byte[] recuperarVisualizacaoLegislacoes(IncidenteAnalise incidenteAnalise) throws DaoException;

	String recuperarInformacaoPublicacao(IncidenteAnalise incidenteAnalise) throws DaoException;
	
	/**
	 * Retorna se o incidente de análise é uma repercussão geral
	 * 
	 * @param objetoIncidenteId
	 * @return
	 * @throws DaoException 
	 */
	Boolean temRepercussaoGeral(Long objetoIncidenteId) throws DaoException;
	
	/**
	 * Retorna a quantidade de Acórdãos do mesmo sentido que está esturutrado em incidente de análise
	 * @param incidenteAnaliseId
	 * @return
	 */
	Integer quantidadeAcordaosMesmoSentido(Long incidenteAnaliseId);

}
