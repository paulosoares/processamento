/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.jurisprudencia.IndexacaoIncidenteAnalise;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.IndexacaoIncidenteAnaliseDao;
import br.gov.stf.estf.jurisprudencia.model.service.IndexacaoIncidenteAnaliseService;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Paulo.Estevao
 * @since 10.07.2012
 */
@Service("indexacaoIncidenteAnaliseService")
public class IndexacaoIncidenteServiceImpl extends GenericServiceImpl<IndexacaoIncidenteAnalise, Long, IndexacaoIncidenteAnaliseDao> implements
		IndexacaoIncidenteAnaliseService {

	protected IndexacaoIncidenteServiceImpl(IndexacaoIncidenteAnaliseDao dao) {
		super(dao);
	}

}
