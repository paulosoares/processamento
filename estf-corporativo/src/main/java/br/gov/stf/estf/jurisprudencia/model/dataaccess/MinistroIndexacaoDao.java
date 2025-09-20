package br.gov.stf.estf.jurisprudencia.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.jurisprudencia.IndexacaoIncidenteAnalise;
import br.gov.stf.estf.entidade.jurisprudencia.MinistroIndexacao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * @author Rafael.Dias
 * @since 24.10.2012
 */
public interface MinistroIndexacaoDao extends GenericDao<MinistroIndexacao, MinistroIndexacao.MinistroIndexacaoId> {
	public List<MinistroIndexacao> pesquisarMinistroIndexacao(IndexacaoIncidenteAnalise indexacaoIncidenteAnalise) throws DaoException;
}
