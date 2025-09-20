package br.gov.stf.estf.assinatura.stfoffice.editor;

import javax.servlet.http.HttpSession;

import br.gov.stf.estf.documento.model.service.ArquivoEletronicoService;
import br.gov.stf.estf.documento.model.service.ModeloComunicacaoService;
import br.gov.stf.estf.documento.model.service.TipoPermissaoModeloComunicacaoService;
import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao.FlagGenericaAcessoDocumento;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao.FlagGenericaModeloComunicacao;
import br.gov.stf.estf.localizacao.model.service.SetorService;
import br.gov.stf.framework.model.entity.Flag;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.util.ApplicationFactory;
import br.gov.stf.framework.util.IServiceLocator;
import br.gov.stf.stfoffice.editor.handler.IEditorHandler;
import br.gov.stf.stfoffice.handler.HandlerException;

public class AbrirModeloHandler implements IEditorHandler {

	private RequisicaoAbrirModelo requisicaoNovoModelo;
	private ArquivoEletronicoService arquivoEletronicoService;
	private ModeloComunicacaoService modeloComunicacaoService;
	private SetorService setorService;
	private TipoPermissaoModeloComunicacaoService tipoPermissaoService;

	public AbrirModeloHandler() {
		IServiceLocator locator = ApplicationFactory.getInstance()
				.getServiceLocator();
		arquivoEletronicoService = (ArquivoEletronicoService) locator
				.getService("arquivoEletronicoService");
		modeloComunicacaoService = (ModeloComunicacaoService) locator
				.getService("modeloComunicacaoService");
		setorService = (SetorService) locator.getService("setorService");
		tipoPermissaoService = (TipoPermissaoModeloComunicacaoService) locator
				.getService("tipoPermissaoModeloComunicacaoService");
	}

	public void setAtributos(RequisicaoAbrirModelo requisicaoNovoModelo,
			HttpSession session) {
		this.requisicaoNovoModelo = requisicaoNovoModelo;
	}

	public byte[] recuperarDocumento() throws HandlerException {
		return requisicaoNovoModelo.getDocumento();
	}

	public void fecharDocumento() throws HandlerException {

	}

	public void gerarPDF(byte[] pdf) {

	}

	/*
	 * public void gerarPDF(byte[] pdf) throws HandlerException {
	 * DocumentoEletronico documentoEletronico = new DocumentoEletronico();
	 * DocumentoTexto documentoTextoNaoCancelado = null; DocumentoTexto
	 * novoDocumentoTexto = new DocumentoTexto(); Boolean documentoCancelado =
	 * false; Texto texto = requisicaoNovoTexto.getTexto();
	 * 
	 * if ( texto!=null ) { try { documentoTextoNaoCancelado =
	 * documentoTextoService.recuperarNaoCancelado(texto,
	 * TipoDocumentoTexto.COD_TIPO_DOCUMENTO_TEXTO_PADRAO); } catch
	 * (ServiceException e) { e.printStackTrace(); } }
	 * 
	 * if (documentoTextoNaoCancelado != null){
	 * 
	 * List<DocumentoTexto> lista = new LinkedList<DocumentoTexto>();
	 * lista.add(documentoTextoNaoCancelado); try { documentoCancelado =
	 * documentoTextoService.cancelarDocumentos(lista,
	 * "Documento cancelado para a criação do um novo PDF pelo AssinadorWeb.");
	 * } catch (ServiceException e) { e.printStackTrace(); throw new
	 * HandlerException("Erro ao cancelar o documento"); } }
	 * 
	 * if (documentoTextoNaoCancelado == null && texto != null){ try {
	 * documentoEletronico =
	 * documentoEletronicoService.criaESalvaDocumentoEletronicoAssinador(pdf,
	 * "PROCESSAMENTO"); novoDocumentoTexto.setTexto(texto);
	 * novoDocumentoTexto.setDocumentoEletronico(documentoEletronico);
	 * novoDocumentoTexto
	 * .setTipoDocumentoTexto(tipoDocumentoTextoService.recuperarPorId(5L));
	 * novoDocumentoTexto
	 * .setTipoSituacaoDocumento(TipoSituacaoDocumento.GERADO);
	 * 
	 * documentoTextoService.salvar(novoDocumentoTexto); } catch
	 * (ServiceException e) { e.printStackTrace(); throw new
	 * HandlerException("Erro ao salvar PDF"); } } else { throw new
	 * HandlerException("Erro ao salvar PDF: texto vazio"); }
	 * 
	 * 
	 * 
	 * }
	 */

	/**
	 * Método responsável em escolher se o modelo será salvo pela primeira vez
	 * ou será atualizado na base de dados
	 */
	public void salvarDocumento(byte[] odt) throws HandlerException {
		if (requisicaoNovoModelo.getModeloComunicacao() != null) {
			salvarModelo(odt);
		} else {
			salvarNovoModelo(odt);
		}
	}

	/**
	 * Método responsável em atualizar o modelo já criado
	 * 
	 * @throws HandlerException
	 */
	private void salvarModelo(byte[] odt) throws HandlerException {
		ArquivoEletronico modelo = requisicaoNovoModelo.getModeloComunicacao()
				.getArquivoEletronico();
		modelo.setConteudo(odt);

		try {
			// atualiza o modelo de documento no banco de dados.
			arquivoEletronicoService.salvar(modelo);

		} catch (ServiceException e) {
			e.printStackTrace();
			throw new HandlerException("Erro ao salvar modelo");
		}

	}

	/**
	 * Método responsável em salvar os modelos de documentos e o
	 * ArquivoEletronico no banco
	 * 
	 * @throws HandlerException
	 */
	private void salvarNovoModelo(byte[] odt) throws HandlerException {

		try {
			// método responsável em salvar o ArquivoEletronico e recuperar as
			// informações
			// do ModeloComunicacao
			ModeloComunicacao modelo = modeloComunicacaoService
					.incluirNovoDocumento(
							requisicaoNovoModelo.getTipoComunicacao().getId(), odt,
							"ODT");

			// seta o nome para poder vincular o nome do modelo ao
			// seq_arquivo_eletronico
			// Este nome representa o código que será gerado pelo usuário para
			// representar
			// o modelo
			modelo.setDscModelo(requisicaoNovoModelo.getNomeDocumento());

			if (requisicaoNovoModelo.getFlagPartes().equals(Flag.NAO)) {
				modelo.setFlagPartes(FlagGenericaModeloComunicacao.N);
			} else {
				modelo.setFlagPartes(FlagGenericaModeloComunicacao.S);
			}

			if (requisicaoNovoModelo.getFlagAssinaturaMinistro().equals(Flag.NAO)) {
				modelo.setFlagAssinaturaMinistro(FlagGenericaModeloComunicacao.N);
			} else {
				modelo.setFlagAssinaturaMinistro(FlagGenericaModeloComunicacao.S);
			}

			if (requisicaoNovoModelo.getFlagCampoLivre().equals(Flag.NAO)) {
				modelo.setFlagCampoLivre(FlagGenericaModeloComunicacao.N);
			} else {
				modelo.setFlagCampoLivre(FlagGenericaModeloComunicacao.S);
			}

			if (requisicaoNovoModelo.getFlagPecaProcessoEletronico()
					.equals(Flag.NAO)) {
				modelo.setFlagVinculoPecaProcessoElet(FlagGenericaModeloComunicacao.N);
			} else {
				modelo.setFlagVinculoPecaProcessoElet(FlagGenericaModeloComunicacao.S);
			}

			if (requisicaoNovoModelo.getFlagDuasAssinatura().equals(Flag.NAO)) {
				modelo.setFlagDuasAssinaturas(FlagGenericaModeloComunicacao.N);
			} else {
				modelo.setFlagDuasAssinaturas(FlagGenericaModeloComunicacao.S);
			}

			if (requisicaoNovoModelo.getFlagProcessoLote().equals(Flag.NAO)) {
				modelo.setFlagProcessoLote(FlagGenericaModeloComunicacao.N);
			} else {
				modelo.setFlagProcessoLote(FlagGenericaModeloComunicacao.S);
			}
			
			if (requisicaoNovoModelo.getFlagRestringePeca().equals("P")){
				modelo.setFlagTipoAcessoDocumentoPeca(FlagGenericaAcessoDocumento.P);
			}else{
				modelo.setFlagTipoAcessoDocumentoPeca(FlagGenericaAcessoDocumento.I);
			}
			
			if (requisicaoNovoModelo.getFlagEncaminharParaDJe().equals(Flag.NAO)) {
				modelo.setFlagEncaminharParaDJe(FlagGenericaModeloComunicacao.N);
			} else {
				modelo.setFlagEncaminharParaDJe(FlagGenericaModeloComunicacao.S);
			}
			
			if (requisicaoNovoModelo.getFlagAlterarObsAndamento().equals(Flag.NAO)) {
				modelo.setFlagAlterarObsAndamento(FlagGenericaModeloComunicacao.N);
			} else {
				modelo.setFlagAlterarObsAndamento(FlagGenericaModeloComunicacao.S);
			}

			if (requisicaoNovoModelo.getFlagJuntadaPecaProc().equals(Flag.NAO)) {
				modelo.setFlagJuntadaPecaProc(FlagGenericaModeloComunicacao.N);
			} else {
				modelo.setFlagJuntadaPecaProc(FlagGenericaModeloComunicacao.S);
			}
			
			if (requisicaoNovoModelo.getFlagSemAndamento().equals(Flag.NAO)) {
				modelo.setFlagSemAndamento(FlagGenericaModeloComunicacao.N);
			} else {
				modelo.setFlagSemAndamento(FlagGenericaModeloComunicacao.S);
			}
			if (requisicaoNovoModelo.getIdSetorDestino() != null) {
				modelo.setSetorDestino(setorService
						.recuperarPorId(requisicaoNovoModelo
								.getIdSetorDestino()));
			} else {
				modelo.setSetorDestino(null);
			}

			// seta a flag ativo do modelo com 'S' de sim
			modelo.setFlagAtivo(requisicaoNovoModelo.getFlgAtivo());
			modelo.setAndamento(requisicaoNovoModelo.getAndamentoModelo());
			modelo.setTipoComunicacao(requisicaoNovoModelo.getTipoComunicacao());
			modelo.setTipoPermissao(requisicaoNovoModelo.getTipoPermissaoModeloComunicacao());

			// salvar o modelo do documento no banco de dados.
			modeloComunicacaoService.salvar(modelo);

			requisicaoNovoModelo.setModeloComunicacao(modelo);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new HandlerException("Erro ao inserir novo modelo");
		}

	}

}
