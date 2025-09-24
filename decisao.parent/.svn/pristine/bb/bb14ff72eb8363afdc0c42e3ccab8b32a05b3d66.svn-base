package br.jus.stf.estf.decisao.texto.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.providers.ldap.authenticator.BindAuthenticator;

import com.lowagie.text.DocumentException;
import br.gov.stf.eprocesso.servidorpdf.servico.modelo.ExtensaoEnum;
import br.gov.stf.estf.documento.model.service.AssinaturaDigitalService;
import br.gov.stf.estf.documento.model.service.DocumentoEletronicoService;
import br.gov.stf.estf.documento.model.service.impl.AssinaturaDigitalServiceImpl;
import br.gov.stf.estf.documento.model.util.AssinaturaDto;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoDocumentoTexto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.gov.stf.estf.entidade.documento.tipofase.TipoTransicaoFaseTexto;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.action.handlers.CheckMinisterId;
import br.jus.stf.estf.decisao.support.action.handlers.CheckRestrictions;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.handlers.States;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.texto.support.DadosMontagemTextoBuilder;
import br.jus.stf.estf.decisao.texto.support.TextoBloqueadoException;
import br.jus.stf.estf.montadortexto.ByteArrayOutputStrategy;
import br.jus.stf.estf.montadortexto.DadosMontagemTexto;
import br.jus.stf.estf.montadortexto.MontadorTextoServiceException;
import br.jus.stf.estf.montadortexto.OpenOfficeMontadorTextoService;
import br.jus.stf.estf.montadortexto.TextoOutputException;
import br.jus.stf.estf.montadortexto.TextoOutputStrategy;
import br.jus.stf.estf.montadortexto.TextoSource;
import br.jus.stf.estf.montadortexto.tools.ByteArrayPersister;
import br.jus.stf.estf.montadortexto.tools.PDFUtil;

/**
 * @author Rodrigo Barreiros
 * @see 27.05.2010
 */
@Action(id = "assinarContingencialmenteActionFacesBean", name = "Assinar Contingencialmente", view = "/acoes/texto/assinarContingencialmente.xhtml", height = 200, width = 500)
@Restrict({ActionIdentification.ASSINAR_CONTINGENCIALMENTE})
@States({ FaseTexto.LIBERADO_ASSINATURA })
@RequiresResources(Mode.Many)
@CheckMinisterId
@CheckRestrictions
public class AssinarContingencialmenteActionFacesBean extends AbstractAlterarFaseDoTextoActionFacesBean<TextoDto> {

	private boolean existeTextoNaoSeraAssinado;
	private Boolean assinar = false;
	private Boolean inserirTimbre;
	private String password;
	
	@Autowired
	@Qualifier(value="bindAuthenticator")
	private BindAuthenticator bindAuthenticator;

	public List<TextoDto> listaTextosValidos = new ArrayList<TextoDto>();
	
	@Autowired
	private OpenOfficeMontadorTextoService openOfficeMontadorTextoService;
	
	@Autowired
	private DadosMontagemTextoBuilder dadosMontagemTextoBuilder;
	
	@Autowired
	private DocumentoEletronicoService documentoEletronicoService;

	public void setAssinar(Boolean assinar) {
		this.assinar = assinar;
	}

	public Boolean getAssinar() {
		return assinar;
	}

	public Boolean getInserirTimbre() {
		return inserirTimbre;
	}

	public void setInserirTimbre(Boolean inserirTimbre) {
		this.inserirTimbre = inserirTimbre;
	}

	public List<TextoDto> getListaTextosValidos() {
		return listaTextosValidos;
	}

	public void setListaTextosValidos(List<TextoDto> listaTextosValidos) {
		this.listaTextosValidos = listaTextosValidos;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @see br.jus.stf.estf.decisao.texto.web.AbstractAlterarFaseDoTextoActionFacesBean#getDestino()
	 */
	@Override
	protected TipoTransicaoFaseTexto getDestino() {
		return TipoTransicaoFaseTexto.ASSINAR_DIGITALMENTE;
	}

	private void adicionaTextoQueNaoSeraAssinado(Texto texto, String motivo) {
		FacesMessages.instance().add(Severity.WARN, String.format("%s: %s", texto.getIdentificacaoCompleta(), motivo));
		existeTextoNaoSeraAssinado = true;
	}

	public boolean getExistemTextosNaoSeraoAssinados() {
		return existeTextoNaoSeraAssinado;
	}
	
	public boolean getExistemTextosIguais(){
		return textosIguaisAdicionados != null && textosIguaisAdicionados.size() > 0;
	}
	
	public void validateAndExecute() {
		try {
			Set<TextoDto> textos = getResources();
			for (TextoDto texto : textos) {
				if (isTextoValidoParaAssinar(texto)) {
					List<Texto> textosIguais = recuperaTextosIguaisParaTransicaoDeFase(texto);
					// possui textos iguais
					if (textosIguais != null && textosIguais.size() > 0) {
						textosIguais.add(0, textoService.recuperarTextoPorId(texto.getId()));
						for (Texto ti : textosIguais) {
							TextoDto textoIgualDto = TextoDto.valueOf(ti);
							// Adiciona a mensagem mesmo que o texto tenha sido selecionado.
							if (!textosIguaisAdicionados.contains(textoIgualDto)) {
								verificaTextoValidoParaAssinar(ti);
								adicionaMensagemTextoParaAssinar(ti);
								textosIguaisAdicionados.add(textoIgualDto);
							}
						}
					}
				}
			}
			if (hasMessages()) {
				sendToInformations();
			} else {
				sendToAuthentication();
			}
		} catch (Exception e) {
			addError(e.getMessage());
			sendToErrors();
		}
	}
	
	public void sendToAuthentication() {
		getDefinition().setFacet("autenticacao");
	}
	
	public void validarAutenticacao() {
		try {
			bindAuthenticator.authenticate(new UsernamePasswordAuthenticationToken(getUsuario().getId(), password));
			executaAssinaturaTextos();
		} catch (Exception e) {
			e.printStackTrace();
			addError("Senha inválida!");
		}
	}

	@Override
	public void sendToInformations() {
		getDefinition().setFacet("confirmacao");
		getDefinition().setHeight(defineAlturaDaTela());
		cleanMessages();
	}

	private int defineAlturaDaTela() {
		int tamanho = 250;
		if (getExistemTextosIguais() && getExistemTextosNaoSeraoAssinados()){
			//Duplica a altura da tela caso vá mostrar os dois casos.
			tamanho *= 2;
		}
		return tamanho;
	}

	public void executaAssinaturaTextos() {
		cleanMessages(); // Limpando as mensagens, pois elas já foram validadas no fluxo de validação
		existeTextoNaoSeraAssinado = false;
		try {
			assinarTextos(getListaTextosValidos());
		} catch (Exception e) {
			logger.error("Erro ao assinar textos contingencialmente!", e);
			addError(String
					.format("Erro ao assinar textos contingencialmente: %s ", getMensagemDeErroPadrao(e)));
		}
		
		setRefresh(true);
		
		if (!hasMessages()) {
			sendToConfirmation();
		} else {
			sendToErrors();
		}
	}

	private void assinarTextos(Collection<TextoDto> textos) {
		TipoDocumentoTexto tipoDocumentoTexto = null;
		logger.info("[Início da execução]: " + new Date());
		tipoDocumentoTexto = textoService.recuperarTipoDocumentoTextoPorId(TipoDocumentoTexto.COD_TIPO_DOCUMENTO_TEXTO_PADRAO);

		for (TextoDto texto : textos) {
			Long sequencialDoDocumento = textoService.recuperarSequencialDoDocumentoEletronico(texto);
			String hashValidacao = documentoEletronicoService.gerarHashValidacao(sequencialDoDocumento);
			try {
				byte[] pdf = AssinaturaDigitalServiceImpl.adicionarRodapePdf(recuperarPDF(texto), AssinaturaDigitalServiceImpl.getRodapeAssinaturaEletronica(getMinistro(), hashValidacao));
				salvarDocumentoPdf(texto, pdf, sequencialDoDocumento, hashValidacao, tipoDocumentoTexto);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				addError(texto.toString() + " - Erro ao aplicar regras de assinatura contingencial. " + e.getMessage());
			}
		}
	}
	
	private boolean salvarDocumentoPdf(TextoDto textoDto, byte[] pdf, Long sequencialDoDocumento, String hashValidacao, TipoDocumentoTexto tipoDocumentoTexto) {
		long start = System.currentTimeMillis();
		String identificacaoDoTexto = montaIdentificacaoDoTexto(textoDto);
		
		try {
			logger.warn("PDF Gerado. Enviando texto [" + identificacaoDoTexto
					+ "] para aplicacao das regras de assinatura...");
			AssinaturaDto assinaturaDto = new AssinaturaDto();
			assinaturaDto.setConteudoAssinado(pdf);
			assinaturaDto.setSequencialDocumentoEletronico(sequencialDoDocumento);
			assinaturaDto.setHashValidacao(hashValidacao);
			assinaturaDto.setSiglaSistema(AssinaturaDigitalService.SIGLA_SISTEMA);
			assinaturaDto.setTexto(textoService.recuperarTextoPorId(textoDto.getId()));
			assinaturaDto.setTipo(tipoDocumentoTexto);
			assinaturaDto.setUsuarioLogado(getPrincipal().getUsuario().getId());
			assinaturaDto.setObservacao(getObservacao());
			assinaturaDto.setSubjectDN(null);
			textoService.assinarTextoContingencialmente(assinaturaDto);
			logger.warn("Regras de assinatura aplicadas com sucesso.");
		} catch (Exception e) {
			addError(textoDto.toString() + " - Erro ao salvar pdf assinado. " + e.getMessage());
			logger.error("Problemas ao aplicar as regras de assinatura para o texto " + identificacaoDoTexto, e);
			return false;
		} finally {
			long end = System.currentTimeMillis();
			logger.warn("Tempo para persistencia: [" + (end - start) + "] milisegundos.");
		}
		return true;
	}
	
	private String montaIdentificacaoDoTexto(TextoDto textoDto) {
		return textoDto.toString() + ":" + textoDto.getId();
	}
	
	private byte[] recuperarPDF(TextoDto textoDto) {
		long start = System.currentTimeMillis();
		try {
			Texto texto = textoService.recuperarTextoPorId(textoDto.getId());
			DadosMontagemTexto<Long> dadosMontagem;
			if (texto.getTipoTexto().equals(TipoTexto.ACORDAO)) {
				dadosMontagem = dadosMontagemTextoBuilder.montaDadosMontagemTexto(texto,
						false, montaArquivoDeEmentaAcordao(texto));

			} else if (texto.getTipoTexto().equals(TipoTexto.DECISAO_SOBRE_REPERCURSAO_GERAL)) {
				dadosMontagem = dadosMontagemTextoBuilder.montaDadosMontagemTexto(texto,
						false, montaArquivoDeEmentaDecisaoSobreRepercussaoGeral(texto));

			} else {
				dadosMontagem = dadosMontagemTextoBuilder.montaDadosMontagemTexto(texto, false);
			}

			// Preparando parâmetros para geração do PDF...
			final ByteArrayOutputStream conteudo = new ByteArrayOutputStream();
			TextoOutputStrategy<Long> outputStrategy = new ByteArrayOutputStrategy<Long>(
					new ByteArrayPersister<Long>() {
						public void persistByteArray(Long textoId, byte[] data) throws TextoOutputException,
								IOException {
							conteudo.write(data);
						}
					});
			// Executando rotina de geração de PDF...
			long startConversao = System.currentTimeMillis();
			logger.warn("Tempo para recuperacao dos dados: [" + (startConversao - start) + "] milisegundos.");
			openOfficeMontadorTextoService.criarTextoPDF(dadosMontagem, outputStrategy, true);
			long endConversao = System.currentTimeMillis();
			logger.warn("Tempo para conversao: [" + (endConversao - startConversao) + "] milisegundos.");
			
			byte[] pdfComAutor = PDFUtil.getInstancia().inserirAutor(new ByteArrayInputStream(conteudo.toByteArray()), getMinistro().getNome());
			
			if (inserirTimbre) {
				ByteArrayInputStream conteudoStream = new ByteArrayInputStream(pdfComAutor);
				return PDFUtil.getInstancia().inserirCabecalhoArquivoPDF(conteudoStream);
			}
			return pdfComAutor;
		} catch (MontadorTextoServiceException e) {
			e.printStackTrace();
			logger.error("Erro ao montar o PDF para assinatura", e);
			throw new RuntimeException(e);
		} catch (ServiceException e) {
			e.printStackTrace();
			logger.error("Erro ao montar o PDF para assinatura", e);
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Erro ao montar o PDF para assinatura", e);
			throw new RuntimeException(e);
		} catch (DocumentException e) {
			e.printStackTrace();
			logger.error("Erro ao montar o PDF para assinatura", e);
			throw new RuntimeException(e);
		} catch (JDOMException e) {
			e.printStackTrace();
			logger.error("Erro ao montar o PDF para assinatura", e);
			throw new RuntimeException(e);
		} finally {
			long end = System.currentTimeMillis();
			logger.info("Tempo total para geracao do PDF: [" + (end - start) + "] milisegundos.");
		}
	}

	private byte[] montaArquivoDeEmentaAcordao(Texto texto)
			throws ServiceException, FileNotFoundException, JDOMException,
			IOException, MontadorTextoServiceException {
		Texto acordao = texto;
		Texto ementa = textoService.recuperarEmenta(acordao, acordao.getMinistro());
		return concatenarArquivos(ementa, acordao);
	}

	private byte[] montaArquivoDeEmentaDecisaoSobreRepercussaoGeral(Texto texto)
			throws ServiceException, FileNotFoundException, JDOMException,
			IOException, MontadorTextoServiceException {
		Texto decisao = texto;
		Texto ementa = textoService.recuperarEmentaRepercussaoGeral(decisao, decisao.getMinistro());
		return concatenarArquivos(ementa, decisao);
	}

	/**
	 * Concatena dois arquivos RTF, retornando um único arquivo ODT. Permite adicionar
	 * uma quebra de página entre os dois arquivos.
	 * 
	 * @param ementa o primeiro texto a ser concatenado
	 * @param acordao o segundo texto a ser concatenado
	 * 
	 * @return o array de bytes do arquvios concatenado
	 */
	private byte[] concatenarArquivos(Texto ementa, Texto acordao) throws JDOMException, IOException,
			MontadorTextoServiceException, FileNotFoundException {
		File ementaAsOdt = converterArquivoParaOdt(ementa);
		File acordaoAsOdt = converterArquivoParaOdt(acordao);

		File resultado = openOfficeMontadorTextoService.concatenaArquivosOdt(ementaAsOdt, acordaoAsOdt, false);

		return IOUtils.toByteArray(new FileInputStream(resultado));
	}
	
	/**
	 * Converte o conteúdo (RTF) de um dado texto em um arquivo(File) ODT.
	 *   
	 * @param texto o texto de entrada
	 * 
	 * @return o arquivo ODT
	 */
	private File converterArquivoParaOdt(Texto texto) throws MontadorTextoServiceException, IOException,
			FileNotFoundException {
		InputStream odtAsInputStream = openOfficeMontadorTextoService.converteArquivo(getTextoSource(texto),
				ExtensaoEnum.RTF, ExtensaoEnum.ODT);
		File odtAsFile = File.createTempFile(texto.getIdentificacao(), ".odt");
		FileOutputStream fos = new FileOutputStream(odtAsFile);
		IOUtils.copy(odtAsInputStream, fos);
		return odtAsFile;
	}
	
	/**
	 * Retorna o <code>TextoSource</code> para o conteúdo de um dado texto.
	 * 
	 * @param texto o texto de entrada
	 * 
	 * @return o <code>TextoSource</code>
	 */
	private TextoSource getTextoSource(final Texto texto) {
		return new TextoSource() {
			@Override
			public byte[] getByteArray() throws IOException, MontadorTextoServiceException {
				return texto.getArquivoEletronico().getConteudo();
			}
		};
	}
	
	@Override
	public boolean hasMessages() {
		return super.hasMessages() || existeTextoNaoSeraAssinado;
	}

	/**
	 * Adiciona mensagem de texto igual.
	 * @param ti
	 */
	private void adicionaMensagemTextoParaAssinar(Texto ti) {
		addInformation(ti.getIdentificacaoCompleta());
	}

	/**
	 * Método que verifica se o texto é válido para assinatura. Se for, adiciona o texto à lista
	 * de textos para assinatura. Caso contrário, inclui o texto na lista de textos não liberados.
	 * @param texto
	 * @return
	 */
	private boolean isTextoValidoParaAssinar(TextoDto textoDto) {
		Texto texto = textoService.recuperarTextoPorId(textoDto.getId());
		return verificaTextoValidoParaAssinar(texto);
	}

	private boolean verificaTextoValidoParaAssinar(Texto texto) {
		if (!getListaTextosValidos().contains(texto)) {
			String mensagemDeErro = adicionaTextoParaAssinatura(texto);
			if (mensagemDeErro != null) {
				adicionaTextoQueNaoSeraAssinado(texto, mensagemDeErro);
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * Método que verifica se o texto é válido para assinatura. Caso haja algum problema, retorna uma 
	 * mensagem de texto contendo o erro. Caso não haja, retorna null. 
	 * @param texto O texto para assinatura
	 * @return A mensagem de erro caso haja algum; null se não houver erro.
	 * @throws ServiceException
	 */
	private String adicionaTextoParaAssinatura(Texto texto) {
		try {
			textoService.verificaTextoBloqueado(texto);
			if (texto.getTipoTexto().equals(TipoTexto.ACORDAO)) {
				Texto ementa = verificaEmentaHabilitadaParaAssinatura(texto);
				if (ementa.getTipoFaseTextoDocumento().equals(FaseTexto.LIBERADO_ASSINATURA)) {
					adicionaTextoValido(ementa);
				}
			} else if (texto.getTipoTexto().equals(TipoTexto.DECISAO_SOBRE_REPERCURSAO_GERAL)) {
				Texto ementaRepGeral = verificaEmentaRepercussaoGeralHabilitadaParaAssinatura(texto);
				if (ementaRepGeral.getTipoFaseTextoDocumento().equals(FaseTexto.LIBERADO_ASSINATURA)) {
					adicionaTextoValido(ementaRepGeral);
				}
			}
			adicionaTextoValido(texto);
			return null;
		} catch (ServiceException e) {
			return e.getMessage();
		} catch (TextoBloqueadoException e) {
			return e.getMessage();
		}
	}

	/**
	 * Adiciona um texto válido, verificando se o mesmo já não se encontra na lista, 
	 * evitando a assinatura duplicada.
	 * @param texto
	 */
	private void adicionaTextoValido(Texto texto) {
		TextoDto textoDto = TextoDto.valueOf(texto);
		if (!getListaTextosValidos().contains(textoDto)) {
			getListaTextosValidos().add(textoDto);
		}
	}

	/**
	 * Verifica se o acórdão possui uma ementa gerada, e se ela está em uma fase maior ou igual a Liberado Para Assinatura.  
	 * @param texto
	 * @return
	 * @throws ServiceException
	 */
	protected Texto verificaEmentaHabilitadaParaAssinatura(Texto texto) throws ServiceException {
		Texto ementa = textoService.recuperarEmenta(texto, getMinistro());
		if (ementa == null) {
			throw new ServiceException(
					"O acórdão não poderá ser assinado pois não existe ementa gerada para o processo!");
		}
		if (isFaseMenorLiberadoParaAssinatura(ementa.getTipoFaseTextoDocumento())) {
			throw new ServiceException(
					"O acordão não poderá ser assinado pois a ementa não foi liberada para assinatura!");
		}
		return ementa;
	}
	
	/**
	 * Verifica se a decisão sobre repercussão geral possui uma ementa sobre repercussão geral gerada, e se ela está em uma fase maior ou igual a Liberado Para Assinatura.  
	 * @param texto
	 * @return
	 * @throws ServiceException
	 */
	protected Texto verificaEmentaRepercussaoGeralHabilitadaParaAssinatura(Texto texto) throws ServiceException {
		Texto ementaRepGeral = textoService.recuperarEmentaRepercussaoGeral(texto, getMinistro());
		if (ementaRepGeral == null) {
			throw new ServiceException(
					"A decisão sobre repercussão geral não poderá ser assinada pois não existe ementa sobre repercussão gerada para o processo!");
		}
		if (isFaseMenorLiberadoParaAssinatura(ementaRepGeral.getTipoFaseTextoDocumento())) {
			throw new ServiceException(
					"A decisão sobre repercussão geral não poderá ser assinada pois a ementa sobre repercussão não foi liberada para assinatura!");
		}
		return ementaRepGeral;
	}
	
	private boolean isFaseMenorLiberadoParaAssinatura(FaseTexto fase) {
		return fase.compareTo(FaseTexto.LIBERADO_ASSINATURA) < 0;
	}

	public void voltar() {
		getDefinition().setFacet("principal");
		getDefinition().setHeight(200);
	}
}
