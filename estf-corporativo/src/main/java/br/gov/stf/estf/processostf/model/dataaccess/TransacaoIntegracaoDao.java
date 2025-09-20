package br.gov.stf.estf.processostf.model.dataaccess;

import br.gov.stf.estf.entidade.judiciario.TransacaoIntegracao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TransacaoIntegracaoDao extends GenericDao<TransacaoIntegracao, Long> {

	public Boolean houveRemessa(Long processoId) throws DaoException;
	
}
