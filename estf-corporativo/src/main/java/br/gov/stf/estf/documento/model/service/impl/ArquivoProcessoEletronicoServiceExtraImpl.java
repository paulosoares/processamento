package br.gov.stf.estf.documento.model.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.exception.TextoInvalidoParaPecaException;
import br.gov.stf.estf.documento.model.service.ArquivoProcessoEletronicoService;
import br.gov.stf.estf.documento.model.service.DocumentoTextoService;
import br.gov.stf.estf.documento.model.service.PecaProcessoEletronicoService;
import br.gov.stf.estf.documento.model.service.TextoService;
import br.gov.stf.estf.documento.model.service.TipoPecaProcessoService;
import br.gov.stf.estf.documento.model.service.exception.NaoExisteDocumentoAssinadoException;
import br.gov.stf.estf.documento.model.util.DocumentoTextoUtil;
import br.gov.stf.estf.entidade.documento.ArquivoProcessoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoPecaProcesso;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.estf.entidade.documento.TipoSituacaoPeca;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Protocolo;
import br.gov.stf.framework.model.service.ServiceException;

@Service("arquivoProcessoEletronicoServiceExtra")
public class ArquivoProcessoEletronicoServiceExtraImpl implements ArquivoProcessoEletronicoServiceExtra {

	private final ArquivoProcessoEletronicoService arquivoProcessoEletronicoService;
	private final DocumentoTextoService documentoTextoService;
	private final TipoPecaProcessoService tipoPecaProcessoService;
	private final PecaProcessoEletronicoService pecaProcessoEletronicoService;
	private final TextoService textoService;

	@Autowired
	private ArquivoProcessoEletronicoServiceExtraImpl(ArquivoProcessoEletronicoService arquivoProcessoEletronicoService,
			DocumentoTextoService documentoTextoService, TipoPecaProcessoService tipoPecaProcessoService,
			PecaProcessoEletronicoService pecaProcessoEletronicoService, TextoService textoService) {
		super();
		this.arquivoProcessoEletronicoService = arquivoProcessoEletronicoService;
		this.documentoTextoService = documentoTextoService;
		this.tipoPecaProcessoService = tipoPecaProcessoService;
		this.pecaProcessoEletronicoService = pecaProcessoEletronicoService;
		this.textoService = textoService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seebr.gov.stf.estf.documento.model.service.impl.
	 * ArquivoProcessoEletronicoServiceExtra
	 * #inserirArquivoProcessoEletronicoJuntado
	 * (br.gov.stf.estf.entidade.documento.DocumentoTexto, java.lang.String,
	 * br.gov.stf.estf.entidade.processostf.Protocolo)
	 */
	public void inserirArquivoProcessoEletronicoJuntado(DocumentoTexto documentoTexto, String siglaTipoPecaProcesso,
			Protocolo protocolo) throws ServiceException {
		try {
			PecaProcessoEletronico pecaProcessoEletronico = incluiPecaDeProcessoEletronico(siglaTipoPecaProcesso, protocolo);
			montaEIncluiArquivoEletronico(pecaProcessoEletronico, documentoTexto);
			alteraSituacaoDoDocumento(documentoTexto, TipoSituacaoDocumento.JUNTADO);
		} catch (ServiceException e) {
			throw new ServiceException(e);
		}

	}

	@SuppressWarnings("rawtypes")
	public void inserirArquivoProcessoEletronicoJuntado(DocumentoTexto documentoTexto, String siglaTipoPecaProcesso,
			ObjetoIncidente objetoIncidente) throws ServiceException {
		inserePecaEArquivoEletronicoSemAlterarSituacao(documentoTexto, siglaTipoPecaProcesso, objetoIncidente);
		alteraSituacaoDoDocumento(documentoTexto, TipoSituacaoDocumento.JUNTADO);
	}

	@SuppressWarnings("rawtypes")
	private void inserePecaEArquivoEletronicoSemAlterarSituacao(DocumentoTexto documentoTexto, String siglaTipoPecaProcesso,
			ObjetoIncidente objetoIncidente) throws ServiceException {
		PecaProcessoEletronico pecaProcessoEletronico = incluiPecaDeProcessoEletronico(siglaTipoPecaProcesso, objetoIncidente);
		montaEIncluiArquivoEletronico(pecaProcessoEletronico, documentoTexto);
	}

	private void alteraSituacaoDoDocumento(DocumentoTexto documentoTexto, TipoSituacaoDocumento tipoSituacaoDocumento)
			throws ServiceException {
		documentoTexto.setTipoSituacaoDocumento(tipoSituacaoDocumento);

		documentoTextoService.salvar(documentoTexto);
	}

	private void montaEIncluiArquivoEletronico(PecaProcessoEletronico pecaProcessoEletronico, DocumentoTexto documentoTexto)
			throws ServiceException {
		ArquivoProcessoEletronico arquivoProcessoEletronico = new ArquivoProcessoEletronico();
		arquivoProcessoEletronico.setDocumentoEletronico(documentoTexto.getDocumentoEletronico());
		arquivoProcessoEletronico.setPecaProcessoEletronico(pecaProcessoEletronico);
		arquivoProcessoEletronico.setNumeroOrdem(1L);
		arquivoProcessoEletronicoService.incluir(arquivoProcessoEletronico);
	}

	@SuppressWarnings("rawtypes")
	private PecaProcessoEletronico incluiPecaDeProcessoEletronico(String siglaTipoPecaProcesso, ObjetoIncidente protocolo)
			throws ServiceException {
		PecaProcessoEletronico pecaProcessoEletronico = montaPecaParaInclusao(siglaTipoPecaProcesso, protocolo);
		pecaProcessoEletronicoService.incluir(pecaProcessoEletronico);
		return pecaProcessoEletronico;
	}

	@SuppressWarnings("rawtypes")
	private PecaProcessoEletronico montaPecaParaInclusao(String siglaTipoPecaProcesso, ObjetoIncidente objetoIncidente)
			throws ServiceException {
		PecaProcessoEletronico pecaProcessoEletronico = new PecaProcessoEletronico();
		TipoPecaProcesso tipoPecaProcesso = tipoPecaProcessoService.recuperar(siglaTipoPecaProcesso);
		TipoSituacaoPeca tipoSituacaoPeca = TipoSituacaoPeca.JUNTADA;
		pecaProcessoEletronico.setTipoPecaProcesso(tipoPecaProcesso);
		pecaProcessoEletronico.setObjetoIncidente(objetoIncidente);
		pecaProcessoEletronico.setTipoSituacaoPeca(tipoSituacaoPeca);
		pecaProcessoEletronico.setTipoOrigemPeca(PecaProcessoEletronico.TIPO_ORIGEM_INTERNA);
		// Jubé - Inclusão de dados devido ao STF-Decisão. Dados definidos pelo
		// textual
		Long numeroDeOrdemDaPeca = pecaProcessoEletronicoService.recuperarProximoNumeroDeOrdem(objetoIncidente);
		pecaProcessoEletronico.setNumeroOrdemPeca(numeroDeOrdemDaPeca);
		pecaProcessoEletronico.setNumeroPagInicio(1L);
		pecaProcessoEletronico.setNumeroPagFim(1L);
		return pecaProcessoEletronico;
	}

	// --
	private void inserePecaProcessoEletronicoDoTexto(Texto texto) throws ServiceException, NaoExisteDocumentoAssinadoException,
			TextoInvalidoParaPecaException {
		if (isTipoDeTextoValido(texto)) {
			DocumentoTexto documentoAssinado = getDocumentoTextoAssinado(texto);
			inserePecaEArquivoEletronicoSemAlterarSituacao(documentoAssinado, recuperaSiglaDoTipoDePeca(texto), texto
					.getObjetoIncidente());
		} else {
			throw new TextoInvalidoParaPecaException("O tipo de texto (" + texto.getTipoTexto().getDescricao()
					+ ") não pode ser inserido como peça de processo eletrônico!");
		}

	}

	private boolean isTipoDeTextoValido(Texto texto) {
		TipoTexto tipoTexto = texto.getTipoTexto();
		return TipoTexto.ACORDAO.equals(tipoTexto)
				|| TipoTexto.RELATORIO.equals(tipoTexto)
				|| TipoTexto.DESPACHO.equals(tipoTexto)
				|| TipoTexto.DECISAO_MONOCRATICA.equals(tipoTexto)
				|| TipoTexto.MANIFESTACAO_SOBRE_PROPOSTA_SUMULA_VINCULANTE.equals(tipoTexto)
				|| (tipoTexto.getCodigo() >= TipoTexto.VOTO.getCodigo() && tipoTexto.getCodigo() <= TipoTexto.REVISAO_DE_APARTES
						.getCodigo());
	}

	private DocumentoTexto getDocumentoTextoAssinado(Texto texto) throws ServiceException, NaoExisteDocumentoAssinadoException {
		return DocumentoTextoUtil.recuperarDocumentoAssinadoDoTexto(texto);
	}

	private String recuperaSiglaDoTipoDePeca(Texto texto) throws ServiceException {
		// Jubé - Equiparação do código do tipo de Texto com o TipoPecaProcesso.
		// Segundo o textual, deve-se somar 1000.
		// Essa regra se aplicou a Despachos e Decisões.
		Long codigoDoTipoDePeca = texto.getTipoTexto().getCodigo() + 1000;
		// no caso de manifestação em proposta de súmula vinculante, os valores do sistema e da base não batem
		if (texto.getTipoTexto().equals(TipoTexto.MANIFESTACAO_SOBRE_PROPOSTA_SUMULA_VINCULANTE))
			codigoDoTipoDePeca = 1068L;
		TipoPecaProcesso tipoPecaProcesso = tipoPecaProcessoService.recuperarPorId(codigoDoTipoDePeca);
		// vtipoPecaProcesso.tipoPecaProcessoService.recuperarPorId(codigoDoTipoDePeca);
		return tipoPecaProcesso.getSigla();
	}

	/**
	 * Método que exclui a juntada de peças e cancela o PDF associado.
	 */
	public boolean excluirJuntadaDePecas(Texto texto) throws ServiceException, TextoInvalidoParaPecaException {
		return excluirJuntadaDePecas(texto, true);
	}

	public boolean excluirJuntadaDePecas(Texto texto, boolean cancelarPDF) throws ServiceException,
			TextoInvalidoParaPecaException {
		boolean alteracaoRealizada = true;
		PecaProcessoEletronico pecaProcessoEletronico = pecaProcessoEletronicoService
				.consultarPecaProcessoEletronicoDoTexto(texto);
		if (pecaProcessoEletronico != null && !isPecaSituacaoExcluida(pecaProcessoEletronico)) {
			excluirPeca(pecaProcessoEletronico, cancelarPDF);
		} else {
			alteracaoRealizada = false;
		}
		return alteracaoRealizada;
	}

	private void excluirPeca(PecaProcessoEletronico pecaProcessoEletronico, boolean cancelarPDF) throws ServiceException {
		pecaProcessoEletronicoService.excluir(pecaProcessoEletronico, true, cancelarPDF);
	}

	public boolean gravarJuntadaDePecas(Texto textoInformado) throws ServiceException, NaoExisteDocumentoAssinadoException,
			TextoInvalidoParaPecaException {
		Texto texto = textoService.recuperarPorId(textoInformado.getId());
		boolean alteracaoRealizada = true;
		PecaProcessoEletronico pecaProcessoEletronico = pecaProcessoEletronicoService
				.consultarPecaProcessoEletronicoDoTexto(texto);
		if (pecaProcessoEletronico == null || isPecaSituacaoExcluida(pecaProcessoEletronico)) {
			inserePecaProcessoEletronicoDoTexto(texto);
		} else if (isPecaPendenteDeJuntada(pecaProcessoEletronico)) {
			juntaPecaPendente(pecaProcessoEletronico);
		} else {
			alteracaoRealizada = false;
		}
		return alteracaoRealizada;
	}

	private void juntaPecaPendente(PecaProcessoEletronico pecaProcessoEletronico) throws ServiceException {
		TipoSituacaoPeca tipoSituacaoPeca = TipoSituacaoPeca.JUNTADA;
		pecaProcessoEletronico.setTipoSituacaoPeca(tipoSituacaoPeca);
		pecaProcessoEletronicoService.alterar(pecaProcessoEletronico);
	}

	private boolean isPecaPendenteDeJuntada(PecaProcessoEletronico pecaProcessoEletronico) {
		return (isPecaComSituacaoInformada(pecaProcessoEletronico, TipoSituacaoPeca.PENDENTE));
	}

	private boolean isPecaSituacaoExcluida(PecaProcessoEletronico pecaProcessoEletronico) {
		return isPecaComSituacaoInformada(pecaProcessoEletronico, TipoSituacaoPeca.EXCLUIDA);
	}

	private boolean isPecaComSituacaoInformada(PecaProcessoEletronico pecaProcessoEletronico, TipoSituacaoPeca tipoSituacaoPeca) {
		return tipoSituacaoPeca.equals(pecaProcessoEletronico.getTipoSituacaoPeca());
	}

	// --

}
