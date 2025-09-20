package br.gov.stf.estf.documento.model.service;

import java.util.List;

import br.gov.stf.estf.documento.model.dataaccess.TextoDiversoDao;
import br.gov.stf.estf.entidade.documento.TextoDiverso;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface TextoDiversoService extends GenericService<TextoDiverso, Long, TextoDiversoDao> {
	public List<TextoDiverso> pesquisar (Long codigoSetor, TipoTexto... tiposTexto) throws ServiceException;
	public TextoDiverso recuperarAberturaCertidao (Long codigoSetor, TipoTexto tipoTexto) throws ServiceException;
	public TextoDiverso recuperar (TipoTexto tipoTexto) throws ServiceException;
	
	public TextoDiverso recuperar( String descricaoTextoDiverso, TipoTexto tipoTexto) throws ServiceException;
	
	public List<TextoDiverso> pesquisar(Long codigoSetor, String descricao) throws ServiceException;
}
