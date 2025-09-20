package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.NormaProcesso;
import br.gov.stf.estf.processostf.model.dataaccess.NormaProcessoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface NormaProcessoService extends GenericService<NormaProcesso, Long, NormaProcessoDao> {

	public List<NormaProcesso> pesquisarNormasProcesso(Long codigo, String descricao, Short ano, String normaJurisprudencia)
	throws ServiceException;
}
