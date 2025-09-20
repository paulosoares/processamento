package br.gov.stf.estf.documento.model.service;

import java.util.List;

import br.gov.stf.estf.documento.model.dataaccess.PermissaoDeslocamentoDao;
import br.gov.stf.estf.entidade.documento.PermissaoDeslocamento;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface PermissaoDeslocamentoService extends GenericService<PermissaoDeslocamento, Long, PermissaoDeslocamentoDao> {

	List<PermissaoDeslocamento> pesquisar(Long setorOrigem, Long setorDestino, Boolean permissao) throws ServiceException;

	boolean isDeslocamentoAutorizado(Long setorOrigem, Long setorDestino) throws ServiceException;	

}