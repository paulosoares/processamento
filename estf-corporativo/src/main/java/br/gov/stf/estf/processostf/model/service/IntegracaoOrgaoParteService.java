package br.gov.stf.estf.processostf.model.service;

import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.processostf.IntegracaoOrgaoParte;
import br.gov.stf.estf.entidade.processostf.IntegracaoOrgaoParte.IntegracaoOrgaoParteId;
import br.gov.stf.estf.processostf.model.dataaccess.IntegracaoOrgaoParteDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface IntegracaoOrgaoParteService extends GenericService<IntegracaoOrgaoParte, IntegracaoOrgaoParteId, IntegracaoOrgaoParteDao> {
	public Origem recuperarOrigem (Long parte) throws ServiceException;
}
