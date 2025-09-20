package br.gov.stf.estf.usuario.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.Parte;
import br.gov.stf.estf.entidade.usuario.Pessoa;
import br.gov.stf.estf.usuario.model.dataaccess.PessoaDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface PessoaService extends GenericService<Pessoa, Long, PessoaDao> {
	public Pessoa recuperarPorId(Long codigoPessoaDestinatario) throws ServiceException;

	List<Pessoa> recuperarApenasPartesIntegradas(List<Parte> partes) throws ServiceException;

}
