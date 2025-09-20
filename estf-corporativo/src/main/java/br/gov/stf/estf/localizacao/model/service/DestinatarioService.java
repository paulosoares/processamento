package br.gov.stf.estf.localizacao.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Destinatario;
import br.gov.stf.estf.localizacao.model.dataaccess.DestinatarioDao;
import br.gov.stf.estf.localizacao.model.util.DestinatarioOrgaoOrigemResult;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface DestinatarioService extends GenericService<Destinatario, Long, DestinatarioDao> {
	
	List<Destinatario> recuperarDestinatarioDaOrigem(Long codOrigem, String id) throws ServiceException;
	
	public List<Destinatario> pesquisarDestinatarioDescricao(String descricao) throws ServiceException;

	public List<DestinatarioOrgaoOrigemResult> pesquisarDestinatario(Long codOrigem,
			Long codDestinatario) throws ServiceException;

}
