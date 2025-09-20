/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess;

import javax.persistence.PersistenceException;

import br.gov.stf.estf.entidade.jurisprudencia.AcordaoJurisprudencia;
import br.gov.stf.estf.entidade.jurisprudencia.IncidenteAnalise;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * @author Paulo.Estevao
 * @since 20.08.2012
 */
public interface AcordaoJurisprudenciaDao extends GenericDao<AcordaoJurisprudencia, Long> {

	void inserirProcessoPrincipal(IncidenteAnalise incidenteAnalise) throws DaoException;

	boolean hasJurisprudenciaRelevante(AcordaoJurisprudencia acordaoJurisprudencia) throws DaoException;

	boolean hasConferenciaAcordao(AcordaoJurisprudencia acordaoJurisprudencia) throws DaoException;
	
	int pesquisarPorObjetoIncidente(Long id) throws PersistenceException;

	void ordenarItensLegislacao(Long idLegislacao) throws DaoException;
	
	void salvarPublicacao(Long idAcordaoJurisprudencia, String txtPublicacao) throws DaoException;
	
	/**
	 * Retorna o AcordaoJurisprudencia com base no ObjetoIncidente
	 * 
	 * @param oi
	 * @return
	 * @throws DaoException 
	 */
	AcordaoJurisprudencia recuperarPorObjetoIncidente(ObjetoIncidente<?> oi) throws DaoException;

	/**
	 * Retorna a quantidade de Acórdãos do mesmo sentido que está salvo na base do BRS
	 * @param objetoIncidenteId
	 * @return
	 */
	Integer quantidadeAcordaosMesmoSentidoBrs(Long objetoIncidenteId);
	
}
