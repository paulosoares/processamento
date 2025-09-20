package br.gov.stf.estf.jurisprudencia.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.jurisprudencia.IndexacaoIncidenteAnalise;
import br.gov.stf.estf.entidade.jurisprudencia.MinistroIndexacao;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.MinistroIndexacaoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.GenericService;

/**
 * @author Rafael.Dias
 * @since 24.10.2012
 */
public interface MinistroIndexacaoService extends
		GenericService<MinistroIndexacao, MinistroIndexacao.MinistroIndexacaoId, MinistroIndexacaoDao> {
	    
	public List<MinistroIndexacao> pesquisarMinistroIndexacao(IndexacaoIncidenteAnalise indexacaoIncidenteAnalise) throws DaoException;
}
