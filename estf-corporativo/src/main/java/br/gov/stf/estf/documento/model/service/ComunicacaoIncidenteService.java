package br.gov.stf.estf.documento.model.service;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.documento.model.dataaccess.ComunicacaoIncidenteDao;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ComunicacaoIncidenteService extends GenericService<ComunicacaoIncidente, Long, ComunicacaoIncidenteDao> {

	public ObjetoIncidente<?> selecionaObjetoIncidente(Long idDocumento) throws ServiceException;

	public List<ComunicacaoIncidente> verificaSeExisteProcessosVinculados(Comunicacao comunicacao) throws ServiceException;

	public void lancarAndamento(Date data, Comunicacao comunicacao, Long codigoAndamento, String usuario) throws ServiceException;

	public ComunicacaoIncidente recuperarPorAndamento(Long idAndamento) throws ServiceException;
}
