/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.jurisprudencia.AssuntoCitacao;
import br.gov.stf.estf.entidade.jurisprudencia.IncidenteAnalise;
import br.gov.stf.estf.entidade.jurisprudencia.TipoCitacao;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.AssuntoCitacaoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * @author Paulo.Estevao
 * @since 17.10.2012
 */
public interface AssuntoCitacaoService extends GenericService<AssuntoCitacao, Long, AssuntoCitacaoDao> {

	List<AssuntoCitacao> pesquisar(IncidenteAnalise incidenteAnalise, TipoCitacao tipoCitacao) throws ServiceException;

}
