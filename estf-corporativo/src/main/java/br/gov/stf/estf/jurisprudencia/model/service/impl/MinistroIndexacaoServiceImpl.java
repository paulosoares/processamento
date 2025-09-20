/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.jurisprudencia.IndexacaoIncidenteAnalise;
import br.gov.stf.estf.entidade.jurisprudencia.MinistroIndexacao;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.MinistroIndexacaoDao;
import br.gov.stf.estf.jurisprudencia.model.service.MinistroIndexacaoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Paulo.Estevao
 * @since 10.07.2012
 */
@Service("ministroIndexacaoService")
public class MinistroIndexacaoServiceImpl extends GenericServiceImpl<MinistroIndexacao, MinistroIndexacao.MinistroIndexacaoId, MinistroIndexacaoDao> implements
			MinistroIndexacaoService {

	protected MinistroIndexacaoServiceImpl(MinistroIndexacaoDao dao) {
		super(dao);
	}

	@Override
	public List<MinistroIndexacao> pesquisarMinistroIndexacao(IndexacaoIncidenteAnalise indexacaoIncidenteAnalise)
			throws DaoException {
		return dao.pesquisarMinistroIndexacao(indexacaoIncidenteAnalise);
	}

}
