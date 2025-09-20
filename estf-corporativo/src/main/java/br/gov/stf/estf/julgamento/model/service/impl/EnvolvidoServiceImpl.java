package br.gov.stf.estf.julgamento.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.julgamento.Envolvido;
import br.gov.stf.estf.julgamento.model.dataaccess.EnvolvidoDao;
import br.gov.stf.estf.julgamento.model.service.EnvolvidoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("envolvidoService")
public class EnvolvidoServiceImpl extends GenericServiceImpl<Envolvido, Long, EnvolvidoDao> implements EnvolvidoService {
    
    public EnvolvidoServiceImpl(EnvolvidoDao dao) { super(dao); }

	@Override
	public List<Envolvido> pesquisar(String sugestaoNome)
			throws ServiceException {
		try {
			return dao.pesquisar(sugestaoNome);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	} 

}
