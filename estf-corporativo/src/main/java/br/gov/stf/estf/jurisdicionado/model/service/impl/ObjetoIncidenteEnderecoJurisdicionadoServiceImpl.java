package br.gov.stf.estf.jurisdicionado.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.jurisdicionado.ObjetoIncidenteEnderecoJurisdicionado;
import br.gov.stf.estf.jurisdicionado.model.dataaccess.ObjetoIncidenteEnderecoJurisdicionadoDao;
import br.gov.stf.estf.jurisdicionado.model.service.ObjetoIncidenteEnderecoJurisdicionadoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("objetoIncidenteEnderecoJurisdicionadoService")
public class ObjetoIncidenteEnderecoJurisdicionadoServiceImpl extends GenericServiceImpl<ObjetoIncidenteEnderecoJurisdicionado, Long, ObjetoIncidenteEnderecoJurisdicionadoDao> 
 implements ObjetoIncidenteEnderecoJurisdicionadoService {

	public ObjetoIncidenteEnderecoJurisdicionadoServiceImpl(ObjetoIncidenteEnderecoJurisdicionadoDao dao){
		super(dao);
	}
	public List<ObjetoIncidenteEnderecoJurisdicionado> recuperarEnderecos(Long seqEnderecoJurisdicionado) throws ServiceException {
		try {
			return dao.recuperarEnderecos(seqEnderecoJurisdicionado);
		} catch (DaoException e) {
			throw new ServiceException("Erro na recuperação do endereço.");
		}
	}

}
