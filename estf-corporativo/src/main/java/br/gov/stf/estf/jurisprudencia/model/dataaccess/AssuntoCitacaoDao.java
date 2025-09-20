/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.jurisprudencia.AssuntoCitacao;
import br.gov.stf.estf.entidade.jurisprudencia.IncidenteAnalise;
import br.gov.stf.estf.entidade.jurisprudencia.TipoCitacao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * @author Paulo.Estevao
 * @since 17.10.2012
 */
public interface AssuntoCitacaoDao extends GenericDao<AssuntoCitacao, Long> {

	List<AssuntoCitacao> pesquisar(IncidenteAnalise incidenteAnalise, TipoCitacao tipoCitacao) throws DaoException;

}
