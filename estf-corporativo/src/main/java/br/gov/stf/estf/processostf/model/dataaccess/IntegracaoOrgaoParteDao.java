package br.gov.stf.estf.processostf.model.dataaccess;

import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.processostf.IntegracaoOrgaoParte;
import br.gov.stf.estf.entidade.processostf.IntegracaoOrgaoParte.IntegracaoOrgaoParteId;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface IntegracaoOrgaoParteDao extends GenericDao<IntegracaoOrgaoParte, IntegracaoOrgaoParteId> {
	public IntegracaoOrgaoParte recuperar (Long parte, Origem origem) throws DaoException;
}
