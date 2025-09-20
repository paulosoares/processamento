package br.gov.stf.estf.documento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.documento.PermissaoDeslocamento;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface PermissaoDeslocamentoDao extends GenericDao<PermissaoDeslocamento, Long> {

	List<PermissaoDeslocamento> pesquisar(Long setorOrigem, Long setorDestino, Boolean permissao) throws DaoException;

}
