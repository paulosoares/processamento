package br.gov.stf.estf.documento.model.service;

import java.util.List;

import br.gov.stf.estf.documento.model.dataaccess.TextoAndamentoProcessoDao;
import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TextoAndamentoProcesso;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface TextoAndamentoProcessoService extends GenericService<TextoAndamentoProcesso, Long, TextoAndamentoProcessoDao> {
	public List<TextoAndamentoProcesso> recuperarTextoAndamentoProcesso(Long codigoAndamentoProcesso, Long codigoDocumento) throws ServiceException;
	public TextoAndamentoProcesso recuperarTextoAndamentoProcesso(Long numero) throws ServiceException;
	public void persistirTextoAndamentoProcesso(TextoAndamentoProcesso textoAndamentoProcesso) throws ServiceException;
	public void persistirTexto(Texto texto) throws ServiceException;
	public void persistirDocumentoTexto(DocumentoTexto documentoTexto) throws ServiceException;
	public Texto recuperarTexto(Long codigoTexto) throws ServiceException;
	public List<TextoAndamentoProcesso> pesquisar(List<Texto> listaTextos) throws ServiceException; 
}
