package br.gov.stf.estf.documento.model.dataaccess;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface DocumentoTextoDao extends GenericDao<DocumentoTexto, Long> {
	public DocumentoTexto recuperarNaoCancelado(Texto texto, Long codigoTipoDocumentoTexto) throws DaoException;

	public List<DocumentoTexto> pesquisarDocumentosSetor(Setor setor, TipoSituacaoDocumento tipoSituacaoDocumento,
			Date dataInicio, Date dataFim, String classeProcesso, Long numeroProcesso) throws DaoException;

	public List<DocumentoTexto> pesquisarDocumentosSetor(Setor setor, List<TipoSituacaoDocumento> tipoSituacaoDocumento)
			throws DaoException;

	public Boolean cancelarDocumento(DocumentoEletronico documentoEletronico, String descricaoCancelamento) throws DaoException;

	public DocumentoTexto recuperar(DocumentoEletronico documentoEletronico) throws DaoException;

	public List<Long> pesquisaTextualIdDocumentoEletronico(String classeProcessual, Long numeroProcesso, Long codigoMinistro,
			TipoTexto tipoTexto, String descricao) throws DaoException;

	public DocumentoTexto recuperarDocumentoTextoMaisRecente(Texto texto) throws DaoException;

	public List<DocumentoTexto> pesquisarDocumentosTextoDoTexto(Texto texto) throws DaoException;

	public DocumentoTexto recuperarDocumentoTextoMaisRecenteNaoCancelado(Texto texto,
			boolean verificarDocumentoEletronicoCancelado) throws DaoException;

	public DocumentoTexto recuperarDocumentoTextoMaisRecenteNaoCanceladoExtratoDeAta(Texto texto,
			boolean verificarDocumentoEletronicoCancelado) throws DaoException;

	public DocumentoTexto recuperarDocumentoTextoAssinadoPorUltimo(Texto texto) throws DaoException;

	DocumentoTexto recuperarDocumentoComDocumentoEletronico(Long idDocumentoTexto) throws DaoException;
	
	public DocumentoTexto recuperarUltimoExtratoAtaAssinadoReferenteUltimaSessao(ObjetoIncidente<?> oi) throws DaoException;
}
