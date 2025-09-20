package br.gov.stf.estf.documento.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.MotivoInaptidaoDao;
import br.gov.stf.estf.documento.model.service.MotivoInaptidaoService;
import br.gov.stf.estf.entidade.documento.MotivoInaptidao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("motivoInaptidaoService")
public class MotivoInaptidaoServiceImpl extends GenericServiceImpl<MotivoInaptidao, Long, MotivoInaptidaoDao>
		implements MotivoInaptidaoService {

	public MotivoInaptidaoServiceImpl(MotivoInaptidaoDao dao) {
		super(dao);
	}
	
	@Override
	public List<MotivoInaptidao> pesquisarTodos() throws ServiceException {
		try {
			return dao.pesquisarTodos();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
