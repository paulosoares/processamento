/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.jurisprudencia.AcordaoJurisprudencia;
import br.gov.stf.estf.entidade.jurisprudencia.PublicacaoAcordao;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.PublicacaoAcordaoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface PublicacaoAcordaoService extends GenericService<PublicacaoAcordao, Long, PublicacaoAcordaoDao> {
	List<PublicacaoAcordao> pesquisarPorAcordaoJurisprudencia(
			AcordaoJurisprudencia acordaoJurisprudencia) throws ServiceException;
}
