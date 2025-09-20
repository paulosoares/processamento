package br.gov.stf.estf.usuario.model.dataaccess;

import java.util.Date;

import br.gov.stf.estf.entidade.usuario.IntegracaoDocumento;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface IntegracaoDocumentoDao extends GenericDao<IntegracaoDocumento, Long> {
	
	public Date findMaxDatInclusao(Long seqUsuarioExterno) throws DaoException;
}
