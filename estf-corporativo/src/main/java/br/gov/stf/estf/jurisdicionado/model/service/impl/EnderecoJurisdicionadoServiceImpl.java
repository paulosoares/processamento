package br.gov.stf.estf.jurisdicionado.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.jurisdicionado.EnderecoJurisdicionado;
import br.gov.stf.estf.jurisdicionado.model.dataaccess.EnderecoJurisdicionadoDao;
import br.gov.stf.estf.jurisdicionado.model.service.EnderecoJurisdicionadoService;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("enderecoJurisdicionadoService")
public class EnderecoJurisdicionadoServiceImpl extends GenericServiceImpl<EnderecoJurisdicionado, Long, EnderecoJurisdicionadoDao> 
implements EnderecoJurisdicionadoService {

	public EnderecoJurisdicionadoServiceImpl(EnderecoJurisdicionadoDao dao){
		super(dao);
	}

	
}
