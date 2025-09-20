package br.gov.stf.estf.documento.model.service;

import br.gov.stf.estf.documento.model.dataaccess.DeslocamentoComunicacaoDao;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.DeslocamentoComunicacao;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface DeslocamentoComunicacaoService extends GenericService<DeslocamentoComunicacao, Long, DeslocamentoComunicacaoDao>{
	
	public void incluirDeslocamento(Comunicacao comunicacao,Long idSetor,TipoFaseComunicacao fase) throws ServiceException;
	

}