package br.gov.stf.estf.documento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TextoAndamentoProcesso;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TextoAndamentoProcessoDao extends GenericDao<TextoAndamentoProcesso, Long> {
	public List<TextoAndamentoProcesso> recuperarTextoAndamentoProcesso(Long codigoAndamentoProcesso, Long codigoDocumento) throws DaoException;
	public TextoAndamentoProcesso recuperarTextoAndamentoProcesso(Long numero) throws DaoException;
	public void persistirTextoAndamentoProcesso(TextoAndamentoProcesso textoAndamentoProcesso) throws DaoException;
	public void persistirTexto(Texto texto) throws DaoException;
	public void persistirDocumentoTexto(DocumentoTexto documentoTexto) throws DaoException;
	public Texto recuperarTexto(Long codigoTexto) throws DaoException;
}
