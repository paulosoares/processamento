package br.gov.stf.estf.jurisdicionado.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.jurisdicionado.ObjetoIncidenteEnderecoJurisdicionado;
import br.gov.stf.estf.jurisdicionado.model.dataaccess.ObjetoIncidenteEnderecoJurisdicionadoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ObjetoIncidenteEnderecoJurisdicionadoService extends GenericService<ObjetoIncidenteEnderecoJurisdicionado, Long, ObjetoIncidenteEnderecoJurisdicionadoDao> { 
	public List<ObjetoIncidenteEnderecoJurisdicionado> recuperarEnderecos(Long seqEnderecoJurisdicionado) throws ServiceException;

}
