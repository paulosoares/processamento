package br.gov.stf.estf.jurisprudencia.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.jurisprudencia.Auditoria;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * @author Edvaldoo
 * @since 07.12.2020
 */
public interface AuditoriaDao extends GenericDao<Auditoria, Long> {

	List<Auditoria> pesquisarPelaReferenciaDoProcesso(Auditoria auditoria) throws DaoException;
}
