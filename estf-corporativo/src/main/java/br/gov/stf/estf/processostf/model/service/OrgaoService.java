package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.Orgao;
import br.gov.stf.estf.processostf.model.dataaccess.OrgaoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface OrgaoService extends GenericService<Orgao, Long, OrgaoDao>{
	
	public List<Orgao> pesquisarOrgaosAtivos() throws ServiceException;
	
	public List<Orgao> pesquisarPelaDescricaoOrgaosAtivos(String descricao) throws ServiceException;
}
