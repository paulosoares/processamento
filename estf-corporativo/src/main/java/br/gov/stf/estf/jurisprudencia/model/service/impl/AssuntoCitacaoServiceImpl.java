/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.jurisprudencia.AssuntoCitacao;
import br.gov.stf.estf.entidade.jurisprudencia.IncidenteAnalise;
import br.gov.stf.estf.entidade.jurisprudencia.TipoCitacao;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.AssuntoCitacaoDao;
import br.gov.stf.estf.jurisprudencia.model.service.AssuntoCitacaoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Paulo.Estevao
 * @since 17.10.2012
 */
@Service("assuntoCitacaoService")
public class AssuntoCitacaoServiceImpl extends GenericServiceImpl<AssuntoCitacao, Long, AssuntoCitacaoDao> implements AssuntoCitacaoService {

	protected AssuntoCitacaoServiceImpl(AssuntoCitacaoDao dao) {
		super(dao);
	}

	@Override
	public List<AssuntoCitacao> pesquisar(IncidenteAnalise incidenteAnalise, TipoCitacao tipoCitacao)
			throws ServiceException {
		try {
			return dao.pesquisar(incidenteAnalise, tipoCitacao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
}
