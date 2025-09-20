package br.gov.stf.estf.jurisprudencia.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.jurisprudencia.Auditoria;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.AuditoriaDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * @author Edvaldoo
 * @since 04.12.2020
 */
public interface AuditoriaService extends GenericService<Auditoria, Long, AuditoriaDao> {

	List<Auditoria> pesquisarPelaReferenciaDoProcesso(Auditoria auditoria) throws ServiceException;

}
