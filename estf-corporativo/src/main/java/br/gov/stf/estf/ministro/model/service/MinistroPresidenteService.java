package br.gov.stf.estf.ministro.model.service;

import java.util.Date;
import java.util.Map;

import br.gov.stf.estf.entidade.ministro.MinistroPresidente;
import br.gov.stf.estf.entidade.ministro.MinistroPresidente.MinistroPresidenteId;
import br.gov.stf.estf.entidade.ministro.TipoOcorrenciaMinistro;
import br.gov.stf.estf.ministro.model.dataaccess.MinistroPresidenteDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface MinistroPresidenteService
		extends
		GenericService<MinistroPresidente, MinistroPresidenteId, MinistroPresidenteDao> {

	public MinistroPresidente recuperarMinistroPresidenteAtual() throws ServiceException;

	MinistroPresidente recuperarMinistro(TipoOcorrenciaMinistro tipoOcorrenciaMinistro) throws ServiceException;

	MinistroPresidente getMinistroPresidente() throws ServiceException;

	MinistroPresidente getMinistroVicePresidente() throws ServiceException;

	Map<TipoOcorrenciaMinistro, MinistroPresidente> getMapaDeMinistrosPresidentes() throws ServiceException;

	public MinistroPresidente recuperarMinistro(TipoOcorrenciaMinistro tipoOcorrenciaMinistro, Date dataPosse) throws ServiceException;
	
	public MinistroPresidente recuperarMinistroPresidenteNoPeriodo(TipoOcorrenciaMinistro tipoOcorrenciaMinistro, Date dataInicio, Date dataFim) throws ServiceException;
}
