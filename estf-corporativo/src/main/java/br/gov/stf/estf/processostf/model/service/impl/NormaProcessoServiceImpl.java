package br.gov.stf.estf.processostf.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.NormaProcesso;
import br.gov.stf.estf.processostf.model.dataaccess.NormaProcessoDao;
import br.gov.stf.estf.processostf.model.service.NormaProcessoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("normaProcessoService")
public class NormaProcessoServiceImpl extends GenericServiceImpl<NormaProcesso, Long, NormaProcessoDao> implements NormaProcessoService {
			
	public NormaProcessoServiceImpl(NormaProcessoDao dao) {
		super(dao);
	}
	
	public List<NormaProcesso> pesquisarNormasProcesso(Long codigo, String descricao, Short ano, String normaJurisprudencia)
		throws ServiceException {
		List<NormaProcesso> listaNormaProcesso = null;
		
		try {
			listaNormaProcesso = dao.pesquisarNormasProcesso(codigo, descricao, ano, normaJurisprudencia);
		} catch ( DaoException e ) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		
		return listaNormaProcesso;
	}
		
	
}
