package br.gov.stf.estf.configuracao.model.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.configuracao.model.dataaccess.AlertaSistemaDao;
import br.gov.stf.estf.configuracao.model.service.AlertaSistemaService;
import br.gov.stf.estf.entidade.configuracao.AlertaSistema;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("alertaSistemaService")
public class AlertaSistemaServiceImpl extends GenericServiceImpl<AlertaSistema, Long, AlertaSistemaDao> implements AlertaSistemaService {

	public AlertaSistemaServiceImpl(AlertaSistemaDao dao) {
		super(dao);
	}
	
	@Override
	public List<AlertaSistema> recuperarValor(String siglaSistema, String chave)  throws ServiceException{
		try {
			return dao.recuperarValor(siglaSistema, chave);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	

}
