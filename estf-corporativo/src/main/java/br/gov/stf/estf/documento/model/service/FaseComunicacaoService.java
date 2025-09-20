package br.gov.stf.estf.documento.model.service;

import br.gov.stf.estf.documento.model.dataaccess.FaseComunicacaoDao;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.FaseComunicacao;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface FaseComunicacaoService extends GenericService<FaseComunicacao, Long, FaseComunicacaoDao>{
	
	public FaseComunicacao pesquisarFaseAtual(Long idComunicacao) throws ServiceException;
			
	public void incluirFase(TipoFaseComunicacao tipoFase, Comunicacao comunicacao, String observacao, String usuario) throws ServiceException;
	

}