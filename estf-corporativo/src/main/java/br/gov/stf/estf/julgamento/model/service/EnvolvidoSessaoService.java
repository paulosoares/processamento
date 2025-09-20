package br.gov.stf.estf.julgamento.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.julgamento.EnvolvidoSessao;
import br.gov.stf.estf.julgamento.model.dataaccess.EnvolvidoSessaoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface EnvolvidoSessaoService extends GenericService<EnvolvidoSessao, Long, EnvolvidoSessaoDao> {

	public List<EnvolvidoSessao> pesquisar( String nomeEnvolvido, Boolean ministro, Boolean ministroSubstituto, Long idSessao, Long... codCompetencia ) throws ServiceException;
	public EnvolvidoSessao recuperar( Long idSessao, Long codTipoCompetenciaEnvolvido ) throws ServiceException;
	
}
