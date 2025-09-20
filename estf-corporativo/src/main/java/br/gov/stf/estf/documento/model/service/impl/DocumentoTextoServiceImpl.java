package br.gov.stf.estf.documento.model.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.DocumentoTextoDao;
import br.gov.stf.estf.documento.model.service.AssinaturaDigitalService;
import br.gov.stf.estf.documento.model.service.DocumentoEletronicoService;
import br.gov.stf.estf.documento.model.service.DocumentoTextoService;
import br.gov.stf.estf.documento.model.service.PecaProcessoEletronicoService;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoDocumentoTexto;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.estf.entidade.documento.TipoSituacaoPeca;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("documentoTextoService")
public class DocumentoTextoServiceImpl extends GenericServiceImpl<DocumentoTexto, Long, DocumentoTextoDao> implements
		DocumentoTextoService {

	private final DocumentoEletronicoService documentoEletronicoService;
	private final PecaProcessoEletronicoService pecaProcessoEletronicoService;

	public DocumentoTextoServiceImpl(DocumentoTextoDao dao, PecaProcessoEletronicoService pecaProcessoEletronicoService,
			DocumentoEletronicoService documentoEletronicoService, AssinaturaDigitalService assinaturaDigitalService) {
		super(dao);
		this.documentoEletronicoService = documentoEletronicoService;
		this.pecaProcessoEletronicoService = pecaProcessoEletronicoService;
	}

	@Override
	public DocumentoTexto salvar(DocumentoTexto entidade) throws ServiceException {

		//Verifica se já existe um DocumentoTexto gerado para este Texto

		TipoDocumentoTexto tipoDocumentoTexto = entidade.getTipoDocumentoTexto();

		DocumentoTexto documentoTexto;

		if (tipoDocumentoTexto != null) {
			documentoTexto = recuperarNaoCancelado(entidade.getTexto(), entidade.getTipoDocumentoTexto().getId());
		} else {
			documentoTexto = recuperarNaoCancelado(entidade.getTexto(), null);
		}

		if (documentoTexto != null) {

			// O PDF NAO PODE SER SALVO
			if (!documentoTexto.podeSalvar()) {

				throw new ServiceException("Não foi possível salvar o Documento - Situação: "
						+ documentoTexto.getTipoSituacaoDocumento().getSigla());

				// O PDF SE ENCONTRA NA SITUAÇÃO GERADO (E OUTROS), PORTANTO
				// SERÁ SOBREESCRITO
			} else {
				documentoTexto.getDocumentoEletronico().setArquivo(entidade.getDocumentoEletronico().getArquivo());
				documentoTexto.setTipoSituacaoDocumento(entidade.getTipoSituacaoDocumento());
				entidade = documentoTexto;
			}
		} else {
			documentoEletronicoService.incluir(entidade.getDocumentoEletronico());
		}

		try {
			dao.salvar(entidade);
			return entidade;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

	}

	public DocumentoTexto recuperarNaoCancelado(Texto texto, Long codigoTipoDocumentoTexto) throws ServiceException {
		DocumentoTexto documentoTexto = null;
		try {
			documentoTexto = dao.recuperarNaoCancelado(texto, codigoTipoDocumentoTexto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return documentoTexto;
	}

	public DocumentoTexto salvarDocumentoTextoAssinado(DocumentoTexto documentoTexto, byte[] pdfAssinado, byte[] assinatura,
			byte[] carimboTempo, Date dataCarimboTempo) throws ServiceException {

		if (documentoTexto == null || pdfAssinado == null || pdfAssinado.length == 0 || assinatura == null
				|| assinatura.length == 0 || carimboTempo == null || carimboTempo.length == 0) {

			throw new IllegalArgumentException("Faltando parâmetros para salvar o documento.");
		}

		documentoEletronicoService.salvarDocumentoEletronicoAssinado(documentoTexto.getDocumentoEletronico(), pdfAssinado,
				assinatura, carimboTempo, dataCarimboTempo);

		documentoTexto.setTipoSituacaoDocumento(TipoSituacaoDocumento.ASSINADO_DIGITALMENTE);

		alterar(documentoTexto);

		return documentoTexto;
	}

	public List<DocumentoTexto> pesquisarDocumentosSetor(Setor setor, TipoSituacaoDocumento tipoSituacaoDocumento,
			Date dataInicio, Date dataFim, String classeProcesso, Long numeroProcesso) throws ServiceException {
		try {
			return dao
					.pesquisarDocumentosSetor(setor, tipoSituacaoDocumento, dataInicio, dataFim, classeProcesso, numeroProcesso);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public List<DocumentoTexto> pesquisarDocumentosSetor(Setor setor, List<TipoSituacaoDocumento> tipoSituacaoDocumento)
			throws ServiceException {
		try {
			return dao.pesquisarDocumentosSetor(setor, tipoSituacaoDocumento);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public Boolean cancelarDocumentos(List<DocumentoTexto> listaDocumentoTexto, String motivoCancelamento)
			throws ServiceException {
		TipoSituacaoDocumento situacaoDocumento = TipoSituacaoDocumento.CANCELADO_PELO_MINISTRO;
		return cancelarDocumentos(listaDocumentoTexto, motivoCancelamento, situacaoDocumento);
	}

	public Boolean cancelarDocumentos(List<DocumentoTexto> listaDocumentoTexto, String motivoCancelamento,
			TipoSituacaoDocumento situacaoDocumento) throws ServiceException {
		try {
			for (DocumentoTexto documentoTexto : listaDocumentoTexto) {
				// MUDA A SITUACAO DA PECA PARA O CASO DE CANCELAMENTO DE UM
				// DOCUMENTO JUNTADO
				if (TipoSituacaoDocumento.JUNTADO.equals(documentoTexto.getTipoSituacaoDocumento())) {

					PecaProcessoEletronico pecaProcessoEletronico = pecaProcessoEletronicoService.recuperarPeca(documentoTexto);

					pecaProcessoEletronico.setTipoSituacaoPeca(TipoSituacaoPeca.EXCLUIDA);
					pecaProcessoEletronicoService.salvar(pecaProcessoEletronico);
				}

				documentoTexto.setTipoSituacaoDocumento(situacaoDocumento);
				dao.salvar(documentoTexto);
				documentoEletronicoService.cancelarDocumento(documentoTexto.getDocumentoEletronico(), motivoCancelamento);

			}

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return true;
	}

	public DocumentoTexto recuperar(DocumentoEletronico documentoEletronico) throws ServiceException {
		try {
			return dao.recuperar(documentoEletronico);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public List<Long> pesquisaTextualIdDocumentoEletronico(String classeProcessual, Long numeroProcesso, Long codigoMinistro,
			TipoTexto tipoTexto, String descricao) throws ServiceException {

		try {
			return dao.pesquisaTextualIdDocumentoEletronico(classeProcessual, numeroProcesso, codigoMinistro, tipoTexto,
					descricao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

	}

	public DocumentoTexto recuperarDocumentoTextoMaisRecente(Texto texto) throws ServiceException {
		try {
			return dao.recuperarDocumentoTextoMaisRecente(texto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public DocumentoEletronico recuperarDocumentoEletronicoNaoCancelado(Texto texto) throws ServiceException {
		DocumentoTexto documentoTexto = recuperarDocumentoTextoMaisRecenteNaoCancelado(texto, true);
		return documentoTexto.getDocumentoEletronico();
	}

	public DocumentoTexto recuperarDocumentoTextoMaisRecenteNaoCancelado(Texto texto) throws ServiceException {
		return recuperarDocumentoTextoMaisRecenteNaoCancelado(texto, false);
	}

	public DocumentoTexto recuperarDocumentoTextoMaisRecenteNaoCancelado(Texto texto,
			Boolean verificarDocumentoEletronicoCancelado) throws ServiceException {
		try {
			return dao.recuperarDocumentoTextoMaisRecenteNaoCancelado(texto, verificarDocumentoEletronicoCancelado);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public DocumentoTexto recuperarDocumentoTextoMaisRecenteNaoCanceladoExtratoDeAta(Texto texto,
			Boolean verificarDocumentoEletronicoCancelado) throws ServiceException {
		try {
			return dao.recuperarDocumentoTextoMaisRecenteNaoCanceladoExtratoDeAta(texto, verificarDocumentoEletronicoCancelado);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public void excluirDocumentosTextoDoTexto(Texto texto) throws ServiceException {
		List<DocumentoTexto> documentosTexto = pesquisarDocumentosTextoDoTexto(texto);
		for (DocumentoTexto documentoTexto : documentosTexto) {
			if (!documentoTexto.getDocumentoEletronicoView().getDescricaoStatusDocumento().equals(
					DocumentoEletronico.SIGLA_DESCRICAO_STATUS_CANCELADO)) {
				documentoEletronicoService.cancelarDocumento(documentoTexto.getDocumentoEletronico(), "");
			}
		}
		excluirTodos(documentosTexto);
	}

	private List<DocumentoTexto> pesquisarDocumentosTextoDoTexto(Texto texto) throws ServiceException {
		try {
			return dao.pesquisarDocumentosTextoDoTexto(texto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public DocumentoTexto recuperarDocumentoTextoAssinadoPorUltimo(Texto texto) throws ServiceException {
		try {
			return dao.recuperarDocumentoTextoAssinadoPorUltimo(texto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public DocumentoTexto recuperarDocumentoComDocumentoEletronico(Long idDocumentoTexto) throws ServiceException {
		try {
			return dao.recuperarDocumentoComDocumentoEletronico(idDocumentoTexto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public DocumentoTexto recuperarUltimoExtratoAtaAssinadoReferenteUltimaSessao(ObjetoIncidente<?> oi) throws ServiceException{
		try {
			return dao.recuperarUltimoExtratoAtaAssinadoReferenteUltimaSessao(oi);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}
