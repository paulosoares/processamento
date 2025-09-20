package br.gov.stf.estf.assinatura.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.stf.estf.assinatura.service.DocumentoHandlerServiceLocal;
import br.gov.stf.estf.documento.model.service.DocumentoComunicacaoService;
import br.gov.stf.estf.documento.model.service.DocumentoEletronicoService;
import br.gov.stf.estf.documento.model.service.FaseComunicacaoService;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.DocumentoComunicacao;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.FaseComunicacao;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.estf.entidade.documento.FaseComunicacao.FlagFaseAtual;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.stfoffice.handler.HandlerException;

@Service("documentoHandlerServiceLocal")
public class DocumentoHandlerServiceLocalImpl implements DocumentoHandlerServiceLocal {

	@Autowired
	private DocumentoComunicacaoService documentoComunicacaoService;
	@Autowired
	private DocumentoEletronicoService documentoEletronicoService;
	@Autowired
	private FaseComunicacaoService faseComunicacaoService;

	/* (non-Javadoc)
	 * @see br.gov.stf.estf.assinatura.service.impl.DocumentoHandlerService#gerarPdf(byte[], br.gov.stf.estf.entidade.documento.Comunicacao, java.lang.String)
	 */
	@Override
	@Transactional(rollbackFor = { HandlerException.class })
	public void gerarPdf(byte[] pdf, Comunicacao comunicacao, String user) throws HandlerException {
		if (comunicacao != null) {
			DocumentoComunicacao documentoComunicacaoNaoCancelado = null;
			Boolean documentoCancelado = false;
			try {
				documentoComunicacaoNaoCancelado = documentoComunicacaoService.recuperarNaoCancelado(comunicacao);
			} catch (ServiceException e) {
				e.printStackTrace();
			}

			if (documentoComunicacaoNaoCancelado != null) {

				// List<DocumentoComunicacao> lista = new LinkedList<DocumentoComunicacao>();
				// lista.add(documentoComunicacaoNaoCancelado);
				try {
					documentoCancelado = documentoComunicacaoService.cancelarDocumentos(
							documentoComunicacaoNaoCancelado,
							"Documento cancelado para a criação do um novo PDF pelo Expediente Automatizado.", user);

				} catch (ServiceException e) {
					e.printStackTrace();
					throw new HandlerException("Erro ao cancelar o documento");
				}
			}

			if (documentoCancelado) {
				documentoComunicacaoNaoCancelado = null;
			}

			if (documentoComunicacaoNaoCancelado == null && comunicacao != null) {
				DocumentoComunicacao novoDocumentoTexto = new DocumentoComunicacao();

				try {
					DocumentoEletronico documentoEletronico = documentoEletronicoService.criaESalvaDocumentoEletronicoAssinador(pdf,
							"PROCESSAMENTO", comunicacao.getModeloComunicacao());
					novoDocumentoTexto.setComunicacao(comunicacao);
					novoDocumentoTexto.setDocumentoEletronico(documentoEletronico);
					novoDocumentoTexto.setTipoSituacaoDocumento(TipoSituacaoDocumento.GERADO);

					documentoComunicacaoService.salvar(novoDocumentoTexto);
					documentoComunicacaoService.flushSession();

					// se a fase atual for 'AGUARDANDO ASSINATURA' o sistema não
					// lança fase de PDF gerado
					if (podeIncluirFasePdfGerado(comunicacao, novoDocumentoTexto) ) {
						
						String descricaoFaseAtual = null;
						
						try{
							descricaoFaseAtual = comunicacao.getObservacaoFaseAtual();
						} catch (NullPointerException e){
							//Não tem descrição, tem que ficar null
						}
						
						faseComunicacaoService.incluirFase(TipoFaseComunicacao.PDF_GERADO, comunicacao, descricaoFaseAtual, null);
					}

				} catch (ServiceException e) {
					e.printStackTrace();
					throw new HandlerException("Erro ao salvar PDF");
				}
			}
		} else {
			throw new HandlerException("Erro ao salvar PDF: texto vazio");
		}

	}

	private boolean podeIncluirFasePdfGerado(Comunicacao comunicacao, DocumentoComunicacao novoDocumentoTexto) throws HandlerException {
		List<FaseComunicacao> listaFases = comunicacao.getFases();
		
		TipoFaseComunicacao tipoFaseComunicacaoAtual = null;
		for (FaseComunicacao faseComunicacao : listaFases) {
			if (faseComunicacao.getFlagFaseAtual().equals(FlagFaseAtual.S)){
				tipoFaseComunicacaoAtual = faseComunicacao.getTipoFase();
			}
		}
		
		if (tipoFaseComunicacaoAtual == null)
			throw new HandlerException("Erro ao salvar PDF: fase não identificada");
		
		return !tipoFaseComunicacaoAtual.equals(TipoFaseComunicacao.AGUARDANDO_ASSINATURA) 
			&& !tipoFaseComunicacaoAtual.equals(TipoFaseComunicacao.EM_REVISAO)
			&& !novoDocumentoTexto.getTipoSituacaoDocumento().getDescricao().toUpperCase().contains("CANCELADO");
	}

}
