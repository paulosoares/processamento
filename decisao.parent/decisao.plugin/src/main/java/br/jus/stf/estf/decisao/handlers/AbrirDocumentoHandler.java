package br.jus.stf.estf.decisao.handlers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.lf5.util.StreamUtils;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.xpath.XPath;
import org.jopendocument.dom.ODPackage;
import org.jopendocument.dom.ODSingleXMLDocument;
import org.jopendocument.dom.ODXMLDocument;
import org.jopendocument.dom.template.TemplateException;
import org.picocontainer.MutablePicoContainer;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import br.jus.stf.estf.decisao.ConfiguracaoTexto;
import br.jus.stf.estf.decisao.DecisaoActions;
import br.jus.stf.estf.decisao.DecisaoService;
import br.jus.stf.estf.decisao.DecisaoStandardToolBar;
import br.jus.stf.estf.decisao.DecisaoVersaoInfo;
import br.jus.stf.estf.decisao.DocAbrirDecisaoId;
import br.jus.stf.estf.decisao.DocDecisaoId;
import br.jus.stf.estf.decisao.DocNovaDecisaoId;
import br.jus.stf.estf.decisao.StfOfficeDecisaoURI;
import br.jus.stf.estf.decisao.config.DecisaoParameters;
import br.jus.stf.estf.decisao.exception.ServerException;
import br.jus.stf.stfoffice.ActivityListener;
import br.jus.stf.stfoffice.DocumentService;
import br.jus.stf.stfoffice.DocumentService.StfOfficeDialogOption;
import br.jus.stf.stfoffice.DocumentService.StfOfficeDialogType;
import br.jus.stf.stfoffice.DocumentServiceException;
import br.jus.stf.stfoffice.PluginActionHandler;
import br.jus.stf.stfoffice.PluginRequisicaoException;
import br.jus.stf.stfoffice.StfOfficeService;
import br.jus.stf.stfoffice.StfOfficeServiceException;
import br.jus.stf.stfoffice.client.Handles;
import br.jus.stf.stfoffice.client.ui.StfOfficeStandardToolBar;
import br.jus.stf.stfoffice.client.util.OpenOfficeUtil;
import br.jus.stf.stfoffice.servlet.DocumentoId;
import br.jus.stf.stfoffice.support.STFOfficeFileManager;

import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class AbrirDocumentoHandler implements PluginActionHandler {
	private static final String PREFIXO_ESTILO_DECISAO = "STF-";
	private static final String STF_PADRAO = PREFIXO_ESTILO_DECISAO + "Padrão";
	private static final Log log = LogFactory.getLog(AbrirDocumentoHandler.class);
	private static final String SEPARADOR = System.getProperty("file.separator");
	private static final String PATH_USER_OPEN_OFFICE = System.getenv("APPDATA") + SEPARADOR + "BrOffice.org2"
			+ SEPARADOR + "user" + SEPARADOR;
	public static final String PATH_MACRO = PATH_USER_OPEN_OFFICE + "basic" + SEPARADOR;
	public static final String PATH_ATALHO = PATH_USER_OPEN_OFFICE + SEPARADOR + "config" + SEPARADOR + "soffice.cfg"
			+ SEPARADOR + "modules" + SEPARADOR + "swriter" + SEPARADOR + "accelerator" + SEPARADOR + "pt-BR"
			+ SEPARADOR;
	private static final String PATH_ARQUIVO_ATALHO = "style.xml";
	private static final Namespace NAMESPACE_TEXT = Namespace.getNamespace("text",
			"urn:oasis:names:tc:opendocument:xmlns:text:1.0");
	private static final String DECISAO_SOBRE_REPERCURSAO_GERAL = "Decisão sobre Repercussão Geral";
	private static final Long CODIGO_DECISAO_SOBRE_REPERCURSAO_GERAL = 55L;

	@Handles(DecisaoActions.ACAO_MANTER_SESSAO_USUARIO)
	public void manterSessaoUsuario(DecisaoService service, StfOfficeDecisaoURI soURI, StfOfficeService officeService,
			MutablePicoContainer requestContainer) throws PluginRequisicaoException {
		try {
			service.manterSessaoUsuarioServidor();
		} catch (ServerException e) {
			throw new PluginRequisicaoException(e.getMessage(), e);
		}
	}

	@Handles(DecisaoActions.ACAO_ABRIR_DOCUMENTO)
	public void abrirDocumento(DecisaoService service, StfOfficeDecisaoURI soURI, StfOfficeService officeService,
			MutablePicoContainer requestContainer) throws PluginRequisicaoException {

		DocDecisaoId id = soURI.getDocId();
		Long seqTexto = id.getSeqTexto();
		
		try {
			if (service.isTextoEmEdicaoConcorrente(seqTexto) && service.isUsuarioDesbloqueadorTextos()) {
				String nomeCompleto = service.getUsuarioBloqueadorTexto(seqTexto);
				
				StfOfficeDialogOption resposta = officeService.enviarDialogo("Este documento está bloqueado pelo usuário ["+nomeCompleto+"]. Deseja desbloqueá-lo e abri-lo para edição assim mesmo?", "Abrir Documento", 
						new StfOfficeDialogOption[]{StfOfficeDialogOption.YES, StfOfficeDialogOption.NO}, StfOfficeDialogType.QUESTION);
	
				if (resposta.equals(DocumentService.StfOfficeDialogOption.YES))
					service.desbloquearDocumentoAdmin(seqTexto);
				else
					System.exit(0);
			}
		} catch (ServerException e) {
			throw new PluginRequisicaoException("Não foi possível desbloquear o texto!", e);
		}
		
		DocAbrirDecisaoId abrirId = (DocAbrirDecisaoId) id;
		requestContainer.addComponent(id);
		boolean somenteLeitura = abrirId.getSomenteLeitura();
		boolean somenteSalvar = false;
		
		try {
			if(abrirId != null && abrirId.getNome() != null && abrirId.getNome().contains(DECISAO_SOBRE_REPERCURSAO_GERAL)) {
				somenteLeitura = service.verificaPorTextoDocumentoReadOnlyDecisaoRepercussaoGeral(abrirId.getSeqTexto());
				//faz esta inversão do valor de somenteLeitura, porque a idéia do somenteSalvar é oposta
				somenteSalvar = !abrirId.getSomenteLeitura();
			}	
			abreDocumento(service, officeService, requestContainer, abrirId, somenteLeitura, somenteSalvar);
		} catch (Exception e) {
			throw new PluginRequisicaoException(e.getMessage(), e);
		}

	}

	private synchronized void abreDocumento(DecisaoService service, StfOfficeService officeService,
			MutablePicoContainer requestContainer, DocDecisaoId docId, boolean somenteLeitura, boolean somenteSalvar)
			throws ServerException, SAXException, IOException, ParserConfigurationException, TemplateException, JDOMException,
			StfOfficeServiceException, DocumentServiceException {
//		DocumentTracer tracer = new DocumentTracer("AbrirDocumento");

		// aplicar estilo, macros e atalhos
		File textoFinal = prepararTextoAbertura(service, docId, requestContainer);

//		tracer.trace("textoComCabecalho.odt", textoFinal);

		
		String mensagemPerfilEditar = service.verificaPerfilEditarTexto(docId.getSeqTexto());
		if (mensagemPerfilEditar != null && mensagemPerfilEditar.trim().length() > 0) {
			somenteLeitura = true;
		}
		
		String mensagemBloqueio = service.verificaDocumentoBloqueado(docId.getSeqTexto(), somenteLeitura);
		if (mensagemBloqueio != null && mensagemBloqueio.trim().length() > 0) {
			somenteLeitura = true;
		}
		
		docId.setSomenteLeitura(somenteLeitura);
		
		executaAberturaDoDocumento(service, officeService, requestContainer, docId, somenteLeitura, somenteSalvar, textoFinal, mensagemBloqueio, mensagemPerfilEditar);
	}

	private void executaAberturaDoDocumento(DecisaoService service, StfOfficeService officeService, MutablePicoContainer requestContainer, DocDecisaoId docId, 
			boolean somenteLeitura, boolean somenteSalvar, File textoFinal,	String mensagemBloqueio, String mensagemPermissaoEditarTexto) 
	throws ServerException, DocumentServiceException, StfOfficeServiceException {
		// abre o arquivo preenchido
		String mensagemDeRepercussaoGeral = null;
		if(docId != null && docId.getTipoTexto() != null && docId.getTipoTexto().equals(CODIGO_DECISAO_SOBRE_REPERCURSAO_GERAL)) {
			mensagemDeRepercussaoGeral = validarGeracaoDecisaoRepercussaoGeral(service, docId);
		}
		
		officeService.abreDocumento(docId.getNome(), textoFinal, requestContainer, mensagemBloqueio, mensagemPermissaoEditarTexto, somenteLeitura, somenteSalvar, docId.getRodape(), mensagemDeRepercussaoGeral);
	}

	@Handles(DecisaoActions.ACAO_NOVO_DOCUMENTO)
	public void novoDocumento(DecisaoService service, StfOfficeDecisaoURI soURI, StfOfficeService officeService,
			MutablePicoContainer docContainer) throws PluginRequisicaoException {

		DocDecisaoId id = soURI.getDocId();
		try {

			boolean somenteLeitura = false;
			boolean somenteSalvar = false;
			boolean bloqueado = false;
			if (id instanceof DocNovaDecisaoId) {

				// Motar o texto de decisão de reperucussão geral
				DocNovaDecisaoId idNovo = (DocNovaDecisaoId) id;
//				File rtf = File.createTempFile("texto_stfoffice_edecisao", ".rtf");
//				File rtf = STFOfficeFileManager.getInstancia().criaArquivoTemporario(idNovo, ".rtf");
//				rtf.deleteOnExit();

				if(idNovo != null && idNovo.getTipoTexto() != null && idNovo.getTipoTexto().equals(CODIGO_DECISAO_SOBRE_REPERCURSAO_GERAL)) {
					somenteLeitura = service.verificaDocumentoReadOnlyDecisaoRepercussaoGeral(idNovo.getObjetoIncidente());
					somenteSalvar = service.verificarPodeCriarDecisaoRepercussaoGeral(idNovo.getObjetoIncidente());
					if(somenteLeitura && !somenteSalvar)
						somenteSalvar = true;
				}
			}

			// prepara o texto para abertura
			File textoCabecalho = prepararTextoAbertura(service, id, docContainer);
			padronizaPrimeiroParagrafo(textoCabecalho);

			docContainer.addComponent(id);
			// abre o arquivo preenchido
			executaAberturaDoDocumento(service, officeService, docContainer, id, somenteLeitura, somenteSalvar, textoCabecalho, null, null);
		} catch (Exception e) {
			throw new PluginRequisicaoException(e.getMessage(), e);
		}
	}

	private void padronizaPrimeiroParagrafo(File textoCabecalho) throws IOException, JDOMException {
		ODSingleXMLDocument d = ODSingleXMLDocument.createFromFile(textoCabecalho);
		Element e = recuperaPrimeiroParagrafo(d);
		if (e != null && e.getTextTrim().equals("")) {
			e.removeAttribute("style-name", NAMESPACE_TEXT);
			DocumentoUtil.insereAtributo(e, STF_PADRAO, "style-name", NAMESPACE_TEXT);
		}
		d.saveAs(textoCabecalho);
	}


	@Handles(DecisaoActions.ACAO_SALVAR_DOCUMENTO)
	public void salvarDocumento(DecisaoService decisaoService, StfOfficeDecisaoURI soURI, ActivityListener activityListener,
			DocumentService documentService) throws PluginRequisicaoException {
		salvarDocumentoGenerico(decisaoService, soURI, activityListener, documentService, false);
	}
	
	/**
	 * Método utilizado para retirar as bookmarks do documento. Essa tag gera formatações inválidas.
	 * @param odtSemCabecalho
	 */

	private File removeCabecalhoDoDocumento(ActivityListener activityListener, File novoArquivoOdt, DecisaoService decisaoService, DocumentoId docId)
			throws IOException, JDOMException, ServerException {
		activityListener.activityIsHappening("Removendo cabeçalho do documento...", 0.75);
		File odtSemCabecalho = decisaoService.removerCabecalhoDoTexto(novoArquivoOdt, docId);
		activityListener.activityIsHappening("Cabeçalho removido!", 0.85);
		return odtSemCabecalho;
	}
	
	private File removerCabecalhoDaRepercussaoGeral(ActivityListener activityListener, File novoArquivoOdt, DecisaoService decisaoService, DocumentoId docId)
			throws IOException, JDOMException, ServerException {
		activityListener.activityIsHappening("Removendo cabeçalho do documento...", 0.75);
		File odtSemCabecalho = decisaoService.removerCabecalhoDaRepercussaoGeral(novoArquivoOdt, docId);
		activityListener.activityIsHappening("Cabeçalho removido!", 0.85);
		return odtSemCabecalho;
	}
	
	private File removerEmentaDaRepercussaoGeral(ActivityListener activityListener, File novoArquivoOdt, DecisaoService decisaoService, DocumentoId docId)			throws IOException, JDOMException, ServerException {
		return decisaoService.excluirEmentaDoDocumento(novoArquivoOdt, docId);
	}

	@Handles(DecisaoActions.ACAO_GERAR_PDF)
	public void gerarPDF(DecisaoService decisaoService, DecisaoParameters dparams, StfOfficeDecisaoURI soURI,
			StfOfficeService officeService, MutablePicoContainer docContainer, ActivityListener activityListener,
			DocumentService documentService) throws PluginRequisicaoException {
		DocDecisaoId docId = soURI.getDocId();
		try {
			if (docId instanceof DocNovaDecisaoId && docId.getSeqTexto() == null) {
				activityListener.activityEnded();
				documentService.enviarDialogo("É necessário primeiramente salvar o documento.", "Gerar PDF",
						new DocumentService.StfOfficeDialogOption[] { DocumentService.StfOfficeDialogOption.OK },
						DocumentService.StfOfficeDialogType.INFO);
			} else {
				activityListener.activityStarted("Gerando PDF ...");
//				DocumentTracer tracer = new DocumentTracer("GerarPDF");
				// File novoArquivoOdt = documentService.salvarDocumento();
				activityListener.activityIsHappening("Arquivo ODT salvo", 0.33);
				// tracer.trace("arquivoOdtSalvo.odt",novoArquivoOdt);
				File pdf = documentService.gerarPDF();
				activityListener.activityIsHappening("PDF gerado", 0.66);
//				tracer.trace("arquivoPDFGerado.pdf", pdf);
				decisaoService.salvarPDF(docId, pdf);
				activityListener.activityEnded();
			}
		} catch (Exception e) {
			activityListener.activityEnded();
			throw new PluginRequisicaoException("Erro durante a execucao da operacao", e);
		}
	}

	@Handles(DecisaoActions.ACAO_RECUPERAR_VERSOES_DOCUMENTO)
	public void recuperarVersoesDocumento(DecisaoService service, StfOfficeDecisaoURI soURI,
			MutablePicoContainer requestContainer) throws PluginRequisicaoException {

		DocDecisaoId id = soURI.getDocId();
		try {
			List<DecisaoVersaoInfo> listaVersoes = service.recuperarVersoesDocumento(id);
			if (log.isInfoEnabled()) {
				log.info("Foram encontradas " + listaVersoes.size() + " versões do documento");
			}
			// new VersaoTextoDialog(listaVersoes, soURI, requestContainer);
		} catch (Exception e) {
			throw new PluginRequisicaoException(e.getMessage(), e);
		}

	}

	@Handles(DecisaoActions.ACAO_FECHAR_DOCUMENTO)
	public void fecharDocumento(DecisaoService decisaoService, DecisaoParameters dparams, StfOfficeDecisaoURI soURI,
			StfOfficeService officeService, MutablePicoContainer docContainer, ActivityListener activityListener,
			DocumentService documentService) throws PluginRequisicaoException {
		try {
			if (log.isTraceEnabled()) {
				log.trace("Fechando documento");
			}
			if (documentService.isModified()) {
				DocumentService.StfOfficeDialogOption resposta = documentService.enviarDialogo(
						"O documento foi alterado. Deseja Salvar as alterações?", "Fechar Documento",
						new DocumentService.StfOfficeDialogOption[] { DocumentService.StfOfficeDialogOption.YES,
								DocumentService.StfOfficeDialogOption.NO, DocumentService.StfOfficeDialogOption.CANCEL },
						DocumentService.StfOfficeDialogType.QUESTION);
				log.trace("Resposta Dialog: " + resposta.name());
				if (resposta.equals(DocumentService.StfOfficeDialogOption.YES)) {
					salvarDocumentoGenerico(decisaoService, soURI, activityListener, documentService, true);
				} else if (resposta.equals(DocumentService.StfOfficeDialogOption.NO)) {
					fecharDesbloquearDocumento(documentService, decisaoService, soURI);
				}
			} else {
				fecharDesbloquearDocumento(documentService, decisaoService, soURI);
			}
		} catch (Exception e) {
			activityListener.activityEnded();
			throw new PluginRequisicaoException(e);
		}
	}
	
	public void salvarDocumentoGenerico(DecisaoService decisaoService, StfOfficeDecisaoURI soURI,
			ActivityListener activityListener, DocumentService documentService, boolean fecharDocumento)
			throws PluginRequisicaoException {

		activityListener.activityStarted("Salvando documento ...");
//		DocumentTracer tracer = new DocumentTracer("SalvarDocumento");
		DocDecisaoId docId = soURI.getDocId();
		try {
//			if (possuiEstiloInvalido(odtSemSecoes)) {
			
			// Código para verificação da existência de tabela
			if (DocumentoUtil.existeTabela(documentService.getBean())) {
				StfOfficeDialogOption resposta = documentService
						.enviarDialogo(
								"O documento possui tabelas que podem prejudicar a formatação do DJ! "
										+ "Para remover uma tabela do texto, posicione o cursor dentro da tabela e clique no botão Converter Tabela em Texto.\n"
										+ "Deseja salvar assim mesmo?",
								"Existem tabelas no documento",
								new StfOfficeDialogOption[] {
										StfOfficeDialogOption.YES,
										StfOfficeDialogOption.NO },
								StfOfficeDialogType.QUESTION);
				if (resposta.equals(StfOfficeDialogOption.NO)) {
					activityListener.activityEnded();
					return;
				}
			}
			
			if (DocumentoUtil.existeTextoDespadronizado(documentService.getBean())) {
				StfOfficeDialogOption resposta = documentService
						.enviarDialogo(
								"O documento possui formatação fora do padrão! Deseja que o sistema retire as formatações inválidas automaticamente?",
								"Formatação inválida", new StfOfficeDialogOption[] { StfOfficeDialogOption.YES,
										StfOfficeDialogOption.NO }, StfOfficeDialogType.QUESTION);
				if (resposta.equals(StfOfficeDialogOption.YES)) {
					activityListener.activityIsHappening("Retirando formatações inválidas...", 0.45);
					DocumentoUtil.padronizarFormatacao(documentService.getBean());
					activityListener.activityIsHappening("Formatações inválidas retiradas!", 0.55);
//					odtSemSecoes = DocumentoUtil.padronizaEstilosDoDocumento(odtSemSecoes);
				} else {
					activityListener.activityEnded();
					documentService
							.enviarDialogo(
									"O texto não poderá ser salvo até que as formatações inválidas sejam retiradas! Por favor, proceda com a correção manual desses problemas!",
									"Erro ao salvar o documento", new StfOfficeDialogOption[] { StfOfficeDialogOption.OK },
									StfOfficeDialogType.ERROR);
					return;
				}

			}
			
			if (decisaoService.isTextoEmEdicaoConcorrente(docId.getSeqTexto())) {
				activityListener.activityEnded();
				documentService.enviarDialogo(
								"O texto não poderá ser salvo porque você não detém mais o bloqueio do documento. Salve o texto com Ctrl + Shift + Z, feche o documento e abra-o novamente.",
								"Erro ao salvar o documento", new StfOfficeDialogOption[] { StfOfficeDialogOption.OK },
								StfOfficeDialogType.ERROR);
				return;

			}
			activityListener.activityIsHappening("Salvando arquivo temporário...", 0.60);
			File novoArquivoOdt = documentService.salvarDocumento();
			activityListener.activityIsHappening("Arquivo ODT temporário salvo!", 0.70);
//			tracer.trace("arquivoOdtSalvo.odt", novoArquivoOdt);
			
			File odtSemSecoes = null;
			File odtSemCabecalho = null;
			File odtSemEmentaCabecalho = null;
			
			// TODO Alterar a forma de recuperar o tipo de texto de decisão da repercussão geral (codigo tipo texto = 55)
			// talvez incluir o tipo texto no DocDecisaoId 
			if(docId != null && docId.getNome() != null && docId.getNome().trim().length() > 0 && docId.getNome().contains(DECISAO_SOBRE_REPERCURSAO_GERAL)) {
				odtSemCabecalho = removerCabecalhoDaRepercussaoGeral(activityListener, novoArquivoOdt, decisaoService, docId);
//				tracer.trace("odtSemCabecalho.odt", odtSemCabecalho);
				odtSemEmentaCabecalho = removerEmentaDaRepercussaoGeral(activityListener, odtSemCabecalho, decisaoService, docId);
//				tracer.trace("odtSemEmenta.odt", odtSemCabecalho);
				odtSemSecoes = odtSemEmentaCabecalho;
			} else {
				odtSemCabecalho = removeCabecalhoDoDocumento(activityListener, novoArquivoOdt, decisaoService, docId);
//				tracer.trace("odtSemCabecalho.odt", odtSemCabecalho);
				odtSemSecoes = odtSemCabecalho;
			}
			

			activityListener.activityIsHappening("Verificando formatação do documento", 0.25);

			odtSemSecoes = DocumentoUtil.removeBookMarksDoDocumento(odtSemSecoes);
//			File novoArquivoRtf = File.createTempFile("decisao_atual", ".rtf");
			File novoArquivoRtf = STFOfficeFileManager.getInstancia().criaArquivoTemporario(docId, ".rtf", "decisao_atual");
			activityListener.activityIsHappening("Convertendo documento para RTF...", 0.90);
			decisaoService.odtToRtf(odtSemSecoes, novoArquivoRtf);
			activityListener.activityIsHappening("Conversão para RTF completa!", 0.95);
			odtSemSecoes.delete();
			activityListener.activityIsHappening("Salvando documento...", 0.96);
			decisaoService.salvarDocumento(docId, novoArquivoRtf);

			//bloqueia o documento caso o usuarioBloqueio seja null
			decisaoService.bloquearTexto(docId);
			
			activityListener.activityIsHappening("Documento salvo!", 1);
			activityListener.activityEnded();
			if (fecharDocumento) {
				fecharDesbloquearDocumento(documentService, decisaoService, soURI);
			}
//			tracer.trace("arquivoRtfFinal.rtf", novoArquivoRtf);
						
		} catch (DocumentServiceException e1) {
			activityListener.activityEnded();
			try {
				documentService
						.enviarDialogo(
								"Ocorreu um problema ao salvar o texto. Evite perdê-lo salvando-o na sua máquina com o comando Ctrl + Shift + Z. (DocumentServiceException: " + e1.getMessage() + ")",
								"Erro ao salvar o texto", new StfOfficeDialogOption[] { StfOfficeDialogOption.OK },
								StfOfficeDialogType.ERROR);
				log.error(e1);
			} catch (DocumentServiceException ei1) {
				log.error(ei1);
				ei1.printStackTrace();
			}
			e1.printStackTrace();
			throw new PluginRequisicaoException("Erro durante a execução da operação (DocumentServiceException)", e1);
			
		} catch (ServerException e2) {
			activityListener.activityEnded();
			try {
				documentService
						.enviarDialogo(
								"Ocorreu um problema ao salvar o texto. Evite perdê-lo salvando-o na sua máquina com o comando Ctrl + Shift + Z. (ServerException: " + e2.getMessage() + ")",
								"Erro ao salvar o texto", new StfOfficeDialogOption[] { StfOfficeDialogOption.OK },
								StfOfficeDialogType.ERROR);
				log.error(e2);
			} catch (DocumentServiceException ei2) {
				log.error(ei2);
				ei2.printStackTrace();
			}
			e2.printStackTrace();
			throw new PluginRequisicaoException("Erro durante a execução da operação (ServerException)", e2);
			
		} catch (JDOMException e3) {
			activityListener.activityEnded();
			try {
				documentService
						.enviarDialogo(
								"Ocorreu um problema ao salvar o texto. Evite perdê-lo salvando-o na sua máquina com o comando Ctrl + Shift + Z. (JDOMException: " + e3.getMessage() + ")",
								"Erro ao salvar o texto", new StfOfficeDialogOption[] { StfOfficeDialogOption.OK },
								StfOfficeDialogType.ERROR);
				log.error(e3);
			} catch (DocumentServiceException ei3) {
				log.error(ei3);
				ei3.printStackTrace();
			}
			e3.printStackTrace();
			throw new PluginRequisicaoException("Erro ao remover partes do texto (JDOMException)", e3);
			
		} catch (IOException e4) {
			activityListener.activityEnded();
			try {
				documentService
						.enviarDialogo(
								"Ocorreu um problema ao salvar o texto. Evite perdê-lo salvando-o na sua máquina com o comando Ctrl + Shift + Z. (IOException: " + e4.getMessage() + ")",
								"Erro ao salvar o texto", new StfOfficeDialogOption[] { StfOfficeDialogOption.OK },
								StfOfficeDialogType.ERROR);
				log.error(e4);
			} catch (DocumentServiceException ei4) {
				log.error(ei4);
				ei4.printStackTrace();
			}
			e4.printStackTrace();
			throw new PluginRequisicaoException("Erro de IO durante a execução da operação (IOException)", e4);
		
		} catch (ValidacaoEstiloException e5) {
			activityListener.activityEnded();
			try {
				documentService
						.enviarDialogo(
								"Ocorreu um problema ao salvar o texto. Evite perdê-lo salvando-o na sua máquina com o comando Ctrl + Shift + Z. (IOException: " + e5.getMessage() + ")",
								"Erro ao salvar o texto", new StfOfficeDialogOption[] { StfOfficeDialogOption.OK },
								StfOfficeDialogType.ERROR);
				log.error(e5);
			} catch (DocumentServiceException ei5) {
				log.error(ei5);
				ei5.printStackTrace();
			}
			e5.printStackTrace();
			throw new PluginRequisicaoException("Erro de IO durante a execução da operação (IOException)", e5);
		}
			
	}

	private void fecharDesbloquearDocumento(DocumentService documentService, DecisaoService decisaoService,
			StfOfficeDecisaoURI soURI) throws ServerException, DocumentServiceException {
		documentService.fecharDocumento(soURI.getUri());
		
		boolean desbloquear = !soURI.getDocId().getSomenteLeitura(); 
		if (desbloquear) {
			decisaoService.desbloquearDocumento(soURI.getDocId().getSeqTexto());
		}
	}

	private File prepararTextoAbertura(DecisaoService service, DocDecisaoId id, MutablePicoContainer requestContainer)
			throws ServerException, SAXException, IOException, ParserConfigurationException, TemplateException, JDOMException, DocumentServiceException {

		// recupera as configuracoes para o texto
		ConfiguracaoTexto configuracaoTexto = service.recuperarConfiguracaoTextoSetor(id);
		byte[] macro = null;
		byte[] estilo = null;
		byte[] atalho = null;

		if (configuracaoTexto != null) {
			macro = configuracaoTexto.getXmlMacro();
			// estilo = configuracaoTexto.getTextoEstilo();
			atalho = configuracaoTexto.getXmlAtalho();
		}

		// preenche o template com as informacoes do cabecalho
		File textoCabecalho = service.recuperarTemplateDoTexto(id);
		// Método inserido para retirar estilos que foram inseridos pela
		// conversão de RTF para ODT
//		textoCabecalho = DocumentoUtil.padronizaEstilosAutomaticos(textoCabecalho);

		// adiciona as macros e atalhos ao documento
		File textoConfigurado = adicionarConfiguracaoTexto(atalho, macro, textoCabecalho);

		File textoComEstilo = adicionarEstiloDocumento(textoConfigurado, estilo, id);

		DecisaoStandardToolBar decisaoStandardToolBar = new DecisaoStandardToolBar(requestContainer, textoComEstilo);
		requestContainer.addComponent(StfOfficeStandardToolBar.class, decisaoStandardToolBar);
		
		

		return textoComEstilo;
	}

	/**
	 * Monta os dados relativos à repercussão geral
	 * 
	 * @param decisaoService
	 * @param id
	 * @return
	 * @throws ServerException
	 * @throws DocumentServiceException
	 */
	private String validarGeracaoDecisaoRepercussaoGeral(DecisaoService decisaoService, DocDecisaoId id) throws ServerException, DocumentServiceException {
		String mensagemDeRepercussaoGeral = null;
		if (possuiVotoDivergente(decisaoService, id)) {
			mensagemDeRepercussaoGeral = "Há voto(s) divergente(s) e a decisão precisa ser editada manualmente. Em seguida, o texto deverá ser salvo e liberado para publicação.";
		}
		return mensagemDeRepercussaoGeral;
	}
	
	private Boolean possuiVotoDivergente(DecisaoService decisaoService, DocDecisaoId id) throws ServerException {
		return decisaoService.recuperarPossuiVotoDivergente(id.getObjetoIncidente());
	}

	private File adicionarEstiloDocumento(File textoConfigurado, byte[] estilo, DocumentoId docId) throws ParserConfigurationException,
			SAXException, IOException, JDOMException {
		if (estilo != null) {

			salvaArquivoDeEstiloNoDisco(estilo);
			// remove os estilos do usuario antigos
			ODPackage odt = new ODPackage(textoConfigurado);
			ODXMLDocument xmlContent = odt.getContent();
			org.jdom.Element root = xmlContent.getDocument().getRootElement();
			XPath path = xmlContent.getXPath("//style:style");
			List<org.jdom.Element> nos = path.selectNodes(root);
			for (org.jdom.Element no : nos) {
				String nome = DocumentoUtil.getAttributeValue("name", no);
				if (nome != null && nome.startsWith(PREFIXO_ESTILO_DECISAO)) {
					no.detach();
				}
			}

			// recupera os estilos do usuario

			odt.rmFile(PATH_ARQUIVO_ATALHO);
			// odt.putFile(PATH_ARQUIVO_ATALHO, estilo);
			textoConfigurado = odt.save();

			// textoConfigurado =
			// OpenOfficeUtil.incluiArquivoDeEstiloNoDocumento(textoConfigurado,
			// estilo);
			Document doc = null;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(new ByteArrayInputStream(estilo));
			org.w3c.dom.Element raiz = doc.getDocumentElement();
			NodeList props = raiz.getElementsByTagName("style:style");
			List<Node> nosEstilos = new ArrayList<Node>();
			Node noPadrao = null;
			for (int i = 0; i < props.getLength(); i++) {
				Node prop = props.item(i);
				NamedNodeMap atts = prop.getAttributes();
				Node att = atts.getNamedItem("style:name");
				if (att != null && att.getNodeValue().startsWith(PREFIXO_ESTILO_DECISAO)) {
					nosEstilos.add(prop);
					if (att.getNodeValue().equalsIgnoreCase(STF_PADRAO)) {
						noPadrao = prop;
					}
				}
			}

			//

			// adiciona os estilos do usuario ao documento
//			File textoComEstilo = File.createTempFile("texto_stfoffice_estilo_decisao", ".odt");
			File textoComEstilo = STFOfficeFileManager.getInstancia().criaArquivoTemporario(docId, ".odt", "estilo_decisao");
			ZipInputStream zipin = new ZipInputStream(new FileInputStream(textoConfigurado));
			ZipOutputStream zipout = new ZipOutputStream(new FileOutputStream(textoComEstilo));
			while (true) {
				ZipEntry entry = zipin.getNextEntry();
				if (entry == null)
					break;
				zipout.putNextEntry(entry);
				if (entry.getName().equals("content.xml")) {
					byte[] content = IOUtils.toByteArray(zipin);
					doc = db.parse(new ByteArrayInputStream(content));
					raiz = doc.getDocumentElement();

					// adiciona os estilos de usuario a lista de estilos
					// possiveis
					// do documento

					props = raiz.getElementsByTagName("office:styles");
					Node noEstilos = props.item(0);
					for (Node noSTF : nosEstilos) {
						Node novoNo = doc.importNode(noSTF, true);
						noEstilos.appendChild(novoNo);
					}
					// coloca o STF-Padrão como estilo padrão do documento
					props = raiz.getElementsByTagName("style:default-style");
					for (int i = 0; i < props.getLength(); i++) {
						Node noEstiloPadraoAtual = props.item(i);
						NamedNodeMap attsNo = noEstiloPadraoAtual.getAttributes();
						if (attsNo.getNamedItem("style:family").getNodeValue().equals("paragraph")) {
							Node novoNo = doc.importNode(noPadrao, true);
							noEstiloPadraoAtual.replaceChild(novoNo.getChildNodes().item(0), noEstiloPadraoAtual.getChildNodes()
									.item(0));
							noEstiloPadraoAtual.replaceChild(novoNo.getChildNodes().item(0), noEstiloPadraoAtual.getChildNodes()
									.item(1));
						}
					}

					XMLSerializer serial = new XMLSerializer();
					serial.setOutputByteStream(zipout);
					serial.serialize(doc);
				} else {
					IOUtils.copy(zipin, zipout);
				}
			}
			zipin.close();
			zipout.close();
			// modificaEstiloPadraoDoDocumento(textoComEstilo);
			return textoComEstilo;
		}
		return textoConfigurado;
	}

	private void salvaArquivoDeEstiloNoDisco(byte[] estilo) throws IOException {
		File estiloTemp = File.createTempFile("estiloTemp", ".xml");
		FileOutputStream out = new FileOutputStream(estiloTemp);
		StreamUtils.copy(new ByteArrayInputStream(estilo), out);
		out.close();
	}

	public static void addFilesToExistingZip(File zipFile, File[] files) throws IOException {
		// get a temp file
		File tempFile = File.createTempFile(zipFile.getName(), null);
		// delete it, otherwise you cannot rename your existing zip to it.
		tempFile.delete();

		boolean renameOk = zipFile.renameTo(tempFile);
		if (!renameOk) {
			throw new RuntimeException("could not rename the file " + zipFile.getAbsolutePath() + " to "
					+ tempFile.getAbsolutePath());
		}
		byte[] buf = new byte[1024];

		ZipInputStream zin = new ZipInputStream(new FileInputStream(tempFile));
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));

		ZipEntry entry = zin.getNextEntry();
		while (entry != null) {
			String name = entry.getName();
			boolean notInFiles = true;
			for (File f : files) {
				if (f.getName().equals(name)) {
					notInFiles = false;
					break;
				}
			}
			if (notInFiles) {
				// Add ZIP entry to output stream.
				out.putNextEntry(new ZipEntry(name));
				// Transfer bytes from the ZIP file to the output file
				int len;
				while ((len = zin.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
			}
			entry = zin.getNextEntry();
		}
		// Close the streams
		zin.close();
		// Compress the files
		for (int i = 0; i < files.length; i++) {
			InputStream in = new FileInputStream(files[i]);
			// Add ZIP entry to output stream.
			out.putNextEntry(new ZipEntry(files[i].getName()));
			// Transfer bytes from the file to the ZIP file
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			// Complete the entry
			out.closeEntry();
			in.close();
		}
		// Complete the ZIP file
		out.close();
		tempFile.delete();
	}

	// OK
	private File adicionarConfiguracaoTexto(byte[] atalho, byte[] macro, File texto) throws FileNotFoundException, IOException {
		return OpenOfficeUtil.incluiArquivosDeAutomacao(texto, atalho, macro);
	}

	private Element recuperaPrimeiroParagrafo(ODSingleXMLDocument d) throws JDOMException {
		String xpath = "//office:text/text:p";
		Element e = null;
		List<?> l = d.getXPath(xpath).selectNodes(d.getBody());
		if (!l.isEmpty()) {
			e = (Element) l.get(0);
			if (log.isTraceEnabled()) {
				log.trace("corrigePrimeiroParagrafo: text = [" + e.getTextTrim() + "]");
			}
			// Retira a linha em branco adicionada no inicio do texto
			log.warn("A linha contem: " + e.getText() + ", com TRIM: " + e.getTextTrim());
		}
		return e;
	}

	// OK
/*
	public static void main(String[] args) throws IOException, TemplateException, JDOMException {
		// Load the template.
		// Java 5 users will have to use RhinoFileTemplate instead
		File templateFile = new File("d:\\cabecalho_template.odt");
		log.trace("fillTemplate: classpath do plugin: " + StfOfficeDecisaoPluginOld.class.getClassLoader()
				+ ", classloader do jdom: " + org.jaxen.jdom.JDOMXPath.class.getClassLoader());

		JavaScriptFileTemplate template = new JavaScriptFileTemplate(templateFile);

		// Fill with sample values.
		template.setField("dataSessao", "");
		template.setField("colegiado", "");
		template.setField("descricao", "RE 558676");
		template.setField("tituloRelator", "Relator");
		template.setField("nomeRelator", "Nome Relator");
		ArrayList<ParteCabecalho> partes = new ArrayList<ParteCabecalho>();
		for (int i = 0; i < 5; i++) {
			ParteCabecalho parteCabecalho = new ParteCabecalho("Categoria " + i, "Nome " + i, "", false);
			partes.add(parteCabecalho);
		}
		template.setField("partes", partes);

		File result = new File("d:\\resultado_cabecalho.odt");
		// result.deleteOnExit();

		// Save to file.
		template.saveAs(result);

	}
*/
}
