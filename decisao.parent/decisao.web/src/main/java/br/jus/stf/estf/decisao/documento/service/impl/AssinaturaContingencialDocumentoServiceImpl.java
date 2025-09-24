package br.jus.stf.estf.decisao.documento.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.lowagie.text.DocumentException;

import br.gov.stf.eprocesso.servidorpdf.servico.modelo.ExtensaoEnum;
import br.gov.stf.estf.documento.model.service.AssinaturaDigitalService;
import br.gov.stf.estf.documento.model.service.DocumentoComunicacaoService;
import br.gov.stf.estf.documento.model.service.DocumentoEletronicoService;
import br.gov.stf.estf.documento.model.service.impl.AssinaturaDigitalServiceImpl;
import br.gov.stf.estf.documento.model.util.AssinaturaDto;
import br.gov.stf.estf.entidade.documento.DocumentoComunicacao;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoDocumentoTexto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.pesquisa.domain.ComunicacaoDto;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.security.Principal;
import br.jus.stf.estf.decisao.texto.support.DadosMontagemTextoBuilder;
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
 * Serviço que realiza a assinatura contingencial de documentos (textos ou comunicações).
 * Não utiliza o mecanismo de applet assinador.
 * 
 * @author Tomas.Godoi
 * 
 */
@Service("assinaturaContingencialDocumentoService")
public class AssinaturaContingencialDocumentoServiceImpl extends AbstractAssinaturaDocumentoService {

	@Autowired
	private OpenOfficeMontadorTextoService openOfficeMontadorTextoService;

	@Autowired
	private DadosMontagemTextoBuilder dadosMontagemTextoBuilder;
	
	@Autowired
	private DocumentoComunicacaoService documentoComunicacaoService;
	
	@Autowired
	private DocumentoEletronicoService documentoEletronicoService;

	@Override
	public void assinarTextosAutomaticamente(List<TextoDto> textos) throws ServiceException {
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
				throw new ServiceException(texto.toString() + " - Erro ao aplicar regras de assinatura contingencial. " + e.getMessage());
			}
		}
	}

	@Override
	public void assinarComunicacoesAutomaticamente(List<ComunicacaoDto> comunicacoes) throws ServiceException {
		logger.info("[Início da execução]: " + new Date());

		for (ComunicacaoDto comunicacaoDto : comunicacoes) {
			if (!comunicacaoDto.getDescricaoStatusDocumento().equals(DocumentoEletronico.SIGLA_DESCRICAO_STATUS_ASSINADO)
					&& !comunicacaoDto.getDescricaoStatusDocumento().equals(DocumentoEletronico.SIGLA_DESCRICAO_STATUS_AGUARDANDO)) {

				DocumentoComunicacao documentoComunicacao = documentoComunicacaoService.recuperarPorId(comunicacaoDto.getIdDocumentoComunicacao());
				String hashValidacao = documentoEletronicoService.gerarHashValidacao(documentoComunicacao);
				
				try {
					byte[] pdf = AssinaturaDigitalServiceImpl.adicionarRodapePdf(recuperarPDF(comunicacaoDto), AssinaturaDigitalServiceImpl.getRodapeAssinaturaEletronica(getMinistro(), hashValidacao));
					salvarDocumentoPdf(comunicacaoDto, pdf, hashValidacao);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					throw new ServiceException(comunicacaoDto.toString() + " - Erro ao aplicar regras de assinatura contingencial. " + e.getMessage());
				}
				
			}
		}
	}
	
	@Override
	public void assinarDocumentosAutomaticamente(List<TextoDto> textos, List<ComunicacaoDto> comunicacoes) throws ServiceException {
		assinarTextosAutomaticamente(textos);
		assinarComunicacoesAutomaticamente(comunicacoes);
	}

	private byte[] recuperarPDF(TextoDto textoDto) {
		long start = System.currentTimeMillis();
		try {
			Texto texto = textoService.recuperarTextoPorId(textoDto.getId());
			DadosMontagemTexto<Long> dadosMontagem;
			if (texto.getTipoTexto().equals(TipoTexto.ACORDAO)) {
				dadosMontagem = dadosMontagemTextoBuilder.montaDadosMontagemTexto(texto, false, montaArquivoDeEmentaAcordao(texto));

			} else if (texto.getTipoTexto().equals(TipoTexto.DECISAO_SOBRE_REPERCURSAO_GERAL)) {
				dadosMontagem = dadosMontagemTextoBuilder.montaDadosMontagemTexto(texto, false, montaArquivoDeEmentaDecisaoSobreRepercussaoGeral(texto));

			} else {
				dadosMontagem = dadosMontagemTextoBuilder.montaDadosMontagemTexto(texto, false);
			}

			// Preparando parâmetros para geração do PDF...
			final ByteArrayOutputStream conteudo = new ByteArrayOutputStream();
			TextoOutputStrategy<Long> outputStrategy = new ByteArrayOutputStrategy<Long>(new ByteArrayPersister<Long>() {
				public void persistByteArray(Long textoId, byte[] data) throws TextoOutputException, IOException {
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

			if (getInserirTimbre()) {
				ByteArrayInputStream conteudoStream = new ByteArrayInputStream(pdfComAutor);
				return PDFUtil.getInstancia().inserirCabecalhoArquivoPDF(conteudoStream);
			}
			return pdfComAutor;
		} catch (MontadorTextoServiceException e) {
			logger.error("Erro ao montar o PDF para assinatura", e);
			throw new RuntimeException(e);
		} catch (ServiceException e) {
			logger.error("Erro ao montar o PDF para assinatura", e);
			throw new RuntimeException(e);
		} catch (IOException e) {
			logger.error("Erro ao montar o PDF para assinatura", e);
			throw new RuntimeException(e);
		} catch (DocumentException e) {
			logger.error("Erro ao montar o PDF para assinatura", e);
			throw new RuntimeException(e);
		} catch (JDOMException e) {
			logger.error("Erro ao montar o PDF para assinatura", e);
			throw new RuntimeException(e);
		} finally {
			long end = System.currentTimeMillis();
			logger.info("Tempo total para geracao do PDF: [" + (end - start) + "] milisegundos.");
		}
	}

	private byte[] recuperarPDF(ComunicacaoDto comunicacaoDto) {
		long start = System.currentTimeMillis();
		try {
			DocumentoComunicacao documentoComunicacao = documentoComunicacaoService.recuperarPorId(comunicacaoDto.getIdDocumentoComunicacao());

			return documentoComunicacao.getDocumentoEletronico().getArquivo();
		} catch (ServiceException e) {
			logger.error("Erro ao recuperar o PDF para assinatura", e);
			throw new RuntimeException(e);
		} finally {
			long end = System.currentTimeMillis();
			logger.info("Tempo total para recuperação do PDF: [" + (end - start) + "] milisegundos.");
		}
	}

	private boolean salvarDocumentoPdf(TextoDto textoDto, byte[] pdf, Long sequencialDoDocumento, String hashValidacao, TipoDocumentoTexto tipoDocumentoTexto)
			throws ServiceException {
		long start = System.currentTimeMillis();
		String identificacaoDoTexto = montaIdentificacaoDoTexto(textoDto);

		try {
			logger.warn("PDF Gerado. Enviando texto [" + identificacaoDoTexto + "] para aplicacao das regras de assinatura...");
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
			logger.error("Problemas ao aplicar as regras de assinatura para o texto " + identificacaoDoTexto, e);
			throw new ServiceException(textoDto.toString() + " - Erro ao salvar pdf assinado. " + e.getMessage());
		} finally {
			long end = System.currentTimeMillis();
			logger.warn("Tempo para persistencia: [" + (end - start) + "] milisegundos.");
		}
		return true;
	}

	private void salvarDocumentoPdf(ComunicacaoDto comunicacaoDto, byte[] pdf, String hashValidacao) throws ServiceException {
		long start = System.currentTimeMillis();
		String identificacaoDaComunicacao = montaIdentificacaoDaComunicacao(comunicacaoDto);
		try {
			logger.warn("PDF Gerado. Enviando comunicacao [" + identificacaoDaComunicacao
					+ "] para aplicacao das regras de assinatura...");
			DocumentoComunicacao documentoComunicacao = documentoComunicacaoService.recuperarPorId(comunicacaoDto.getIdDocumentoComunicacao());
			salvarComunicacaoAssinada(comunicacaoDto, documentoComunicacao, pdf, hashValidacao);
			logger.warn("Regras de assinatura aplicadas com sucesso.");
		} catch (Exception e) {
			logger.error("Problemas ao aplicar as regras de assinatura para a comunicacao "
					+ identificacaoDaComunicacao + ". " + e.getMessage(), e);
			throw new ServiceException(comunicacaoDto.toString() + " - Erro ao salvar pdf assinado. " + e.getMessage());
		} finally {
			long end = System.currentTimeMillis();
			logger.warn("Tempo para persistencia: [" + (end - start) + "] milisegundos.");
		}
	}

	protected void salvarComunicacaoAssinada(ComunicacaoDto comunicacaoDto,
			DocumentoComunicacao documentoComunicacao, byte[] pdfAssinado, String hashValidacao) throws ServiceException {
		DocumentoEletronico documentoEletronico = documentoComunicacao.getDocumentoEletronico();
		documentoEletronico.setHashValidacao(hashValidacao);
		documentoEletronicoService.salvar(documentoEletronico);
		documentoComunicacaoService.salvarDocumentoComunicacaoAssinadoContingencialmenteeSTFDecisao(documentoComunicacao, pdfAssinado, new Date(),
				getUsuario().getId(), AssinaturaDigitalService.SIGLA_SISTEMA);
	}

	private String montaIdentificacaoDaComunicacao(ComunicacaoDto comunicacaoDto) {
		return comunicacaoDto.toString() + ":" + comunicacaoDto.getId();
	}
	
	private byte[] montaArquivoDeEmentaAcordao(Texto texto) throws ServiceException, FileNotFoundException, JDOMException, IOException,
			MontadorTextoServiceException {
		Texto acordao = texto;
		Texto ementa = textoService.recuperarEmenta(acordao, acordao.getMinistro());
		return concatenarArquivos(ementa, acordao);
	}

	private byte[] montaArquivoDeEmentaDecisaoSobreRepercussaoGeral(Texto texto) throws ServiceException, FileNotFoundException, JDOMException, IOException,
			MontadorTextoServiceException {
		Texto decisao = texto;
		Texto ementa = textoService.recuperarEmentaRepercussaoGeral(decisao, decisao.getMinistro());
		return concatenarArquivos(ementa, decisao);
	}

	/**
	 * Concatena dois arquivos RTF, retornando um único arquivo ODT. Permite adicionar
	 * uma quebra de página entre os dois arquivos.
	 * 
	 * @param ementa
	 *            o primeiro texto a ser concatenado
	 * @param acordao
	 *            o segundo texto a ser concatenado
	 * 
	 * @return o array de bytes do arquvios concatenado
	 */
	private byte[] concatenarArquivos(Texto ementa, Texto acordao) throws JDOMException, IOException, MontadorTextoServiceException, FileNotFoundException {
		File ementaAsOdt = converterArquivoParaOdt(ementa);
		File acordaoAsOdt = converterArquivoParaOdt(acordao);

		File resultado = openOfficeMontadorTextoService.concatenaArquivosOdt(ementaAsOdt, acordaoAsOdt, false);

		return IOUtils.toByteArray(new FileInputStream(resultado));
	}

	/**
	 * Converte o conteúdo (RTF) de um dado texto em um arquivo(File) ODT.
	 * 
	 * @param texto
	 *            o texto de entrada
	 * 
	 * @return o arquivo ODT
	 */
	private File converterArquivoParaOdt(Texto texto) throws MontadorTextoServiceException, IOException, FileNotFoundException {
		InputStream odtAsInputStream = openOfficeMontadorTextoService.converteArquivo(getTextoSource(texto), ExtensaoEnum.RTF, ExtensaoEnum.ODT);
		File odtAsFile = File.createTempFile(texto.getIdentificacao(), ".odt");
		FileOutputStream fos = new FileOutputStream(odtAsFile);
		IOUtils.copy(odtAsInputStream, fos);
		return odtAsFile;
	}

	/**
	 * Retorna o <code>TextoSource</code> para o conteúdo de um dado texto.
	 * 
	 * @param texto
	 *            o texto de entrada
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

	private boolean getInserirTimbre() {
		return false;
	}

	private String getObservacao() {
		return ""; // Por padrão, é vazia
	}
	
	/**
	 * Recupera o ministro cujo o gabinete o usuário está lotado.
	 * 
	 * @return o ministro do usuário
	 */
	public Ministro getMinistro() {
		return getPrincipal().getMinistro();
	}

	/**
	 * Recupera o usuário autenticado. Esse usuário é encapsulado em um objeto Principal que contém
	 * as credenciais do usuário.
	 * 
	 * @return o principal
	 */
	protected Principal getPrincipal() {
		return (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	private String montaIdentificacaoDoTexto(TextoDto textoDto) {
		return textoDto.toString() + ":" + textoDto.getId();
	}

}
