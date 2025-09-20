package br.gov.stf.estf.documento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.documento.TipoDocumentoTexto;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TipoDocumentoTextoDao extends GenericDao<TipoDocumentoTexto, Long> {
	
	public List<TipoDocumentoTexto> pesquisarTiposDocumentoTextoPorSetor(Long codSetor)	throws DaoException;
	
	public TipoDocumentoTexto recuperar (Long codigoTipoTexto) throws DaoException;
	
}