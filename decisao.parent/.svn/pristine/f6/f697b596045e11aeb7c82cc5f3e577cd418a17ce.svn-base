package br.jus.stf.estf.decisao.texto.support;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.jdom.JDOMException;

import br.gov.stf.eprocesso.servidorpdf.servico.modelo.ExtensaoEnum;
import br.gov.stf.estf.documento.model.service.TextoService;
import br.gov.stf.estf.documento.model.service.exception.TransicaoDeFaseInvalidaException;
import br.gov.stf.estf.documento.model.util.AssinaturaDto;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.assinadorweb.api.negocio.ResultadoAssinatura;
import br.jus.stf.assinadorweb.api.resposta.exception.RespostaException;
import br.jus.stf.estf.decisao.support.assinatura.AssinaturaHandler;
import br.jus.stf.estf.montadortexto.ByteArrayOutputStrategy;
import br.jus.stf.estf.montadortexto.DadosMontagemTexto;
import br.jus.stf.estf.montadortexto.MontadorTextoServiceException;
import br.jus.stf.estf.montadortexto.OpenOfficeMontadorTextoService;
import br.jus.stf.estf.montadortexto.TextoOutputException;
import br.jus.stf.estf.montadortexto.TextoOutputStrategy;
import br.jus.stf.estf.montadortexto.TextoSource;
import br.jus.stf.estf.montadortexto.tools.ByteArrayPersister;
import br.jus.stf.estf.montadortexto.tools.PDFUtil;

import com.lowagie.text.DocumentException;

public class AssinarTextoHandler extends AssinaturaHandler<TextoWrapper> {

	private DadosMontagemTextoBuilder dadosMontagemTextoBuilder;

	private OpenOfficeMontadorTextoService openOfficeMontadorTextoService;

	private TextoService textoService;

	public AssinarTextoHandler() {
	}

	public byte[] recuperarPDF(TextoWrapper textoWrapper) throws RespostaException {
		long start = System.currentTimeMillis();
		dadosMontagemTextoBuilder = (DadosMontagemTextoBuilder) textoWrapper.getApplicationContext().getBean(
				"dadosMontagemTextoBuilder");
		openOfficeMontadorTextoService = (OpenOfficeMontadorTextoService) textoWrapper.getApplicationContext().getBean(
				"openOfficeMontadorTextoService");
		textoService = getTextoService(textoWrapper);
		try {
			Texto texto = textoService.recuperarPorId(textoWrapper.getTexto().getId());
			DadosMontagemTexto<Long> dadosMontagem = criaDadosMontagemTexto(texto);

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

			if (textoWrapper.getInserirTimbre()) {
				ByteArrayInputStream conteudoStream = new ByteArrayInputStream(conteudo.toByteArray());
				return PDFUtil.getInstancia().inserirCabecalhoArquivoPDF(conteudoStream);
			}
			return conteudo.toByteArray();
		} catch (MontadorTextoServiceException e) {
			logger.error("Erro ao montar o PDF para assinatura", e);
			throw new RespostaException(e);
		} catch (ServiceException e) {
			logger.error("Erro ao montar o PDF para assinatura", e);
			throw new RespostaException(e);
		} catch (IOException e) {
			logger.error("Erro ao montar o PDF para assinatura", e);
			throw new RespostaException(e);
		} catch (DocumentException e) {
			logger.error("Erro ao montar o PDF para assinatura", e);
			throw new RespostaException(e);
		} catch (JDOMException e) {
			logger.error("Erro ao montar o PDF para assinatura", e);
			throw new RespostaException(e);
		} finally {
			long end = System.currentTimeMillis();
			logger.info("Tempo total para geracao do PDF: [" + (end - start) + "] milisegundos.");
		}
	}

	/**
	 * Gera dados de montagem diferentes de acordo com o tipo de texto, particularmente os textos de Acordão e Repercussão Geral
	 * Isso acontece porque esses textos tem que ser assinados juntamente com a Ementa, e o método faz a validação necessária
	 * para que isso ocorra. 
	 * @param texto
	 * @return
	 * @throws ServiceException
	 * @throws FileNotFoundException
	 * @throws JDOMException
	 * @throws IOException
	 * @throws MontadorTextoServiceException
	 */
	private DadosMontagemTexto<Long> criaDadosMontagemTexto(Texto texto) throws ServiceException,
			FileNotFoundException, JDOMException, IOException, MontadorTextoServiceException {
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
		return dadosMontagem;
	}

	private byte[] montaArquivoDeEmentaAcordao(Texto texto) throws ServiceException, FileNotFoundException,
			JDOMException, IOException, MontadorTextoServiceException {
		Texto acordao = texto;
		Texto ementa = textoService.recuperar(acordao.getObjetoIncidente(), TipoTexto.EMENTA, acordao.getMinistro()
				.getId());
		return concatenarArquivos(ementa, acordao);
	}

	private byte[] montaArquivoDeEmentaDecisaoSobreRepercussaoGeral(Texto texto) throws ServiceException,
			FileNotFoundException, JDOMException, IOException, MontadorTextoServiceException {
		Texto decisao = texto;
		Texto ementa = textoService.recuperar(decisao.getObjetoIncidente(), TipoTexto.EMENTA_SOBRE_REPERCURSAO_GERAL,
				decisao.getMinistro().getId());
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
	 * Retorna o {@link TextoSource} para o conteúdo de um dado texto.
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

	public void salvarDocumentoPdf(TextoWrapper textoWrapper, ResultadoAssinatura resultadoAssinatura) throws RespostaException {
		long start = System.currentTimeMillis();
		textoService = getTextoService(textoWrapper);
		String identificacaoDoTexto = montaIdentificacaoDoTexto(textoWrapper);
		try {
			logger.warn("PDF Assinado. Enviando texto [" + identificacaoDoTexto + "] para aplicacao das regras de assinatura...");
			AssinaturaDto assinaturaDto = montaAssinaturaDto(textoWrapper, resultadoAssinatura);
			assinarTexto(assinaturaDto);
			logger.warn("Regras de assinatura aplicadas com sucesso.");
		} catch (Exception e) {
			logger.error("Problemas ao aplicar as regras de assinatura para o texto " + identificacaoDoTexto, e);
			throw new RespostaException(e);
		} finally {
			long end = System.currentTimeMillis();
			logger.warn("Tempo para persistencia: [" + (end - start) + "] milisegundos.");
		}
	}

	/**
	 * Chama o método da service que vai gravar o documento assinado no banco
	 * @param assinaturaDto
	 * @throws ServiceException
	 * @throws TransicaoDeFaseInvalidaException
	 */
	protected void assinarTexto(AssinaturaDto assinaturaDto) throws ServiceException, TransicaoDeFaseInvalidaException {
		textoService.assinarTexto(assinaturaDto);
	}

	private TextoService getTextoService(TextoWrapper textoWrapper) {
		return (TextoService) textoWrapper.getApplicationContext().getBean("textoService");
	}

	/**
	 * Monta o objeto que contém os dados necessários para persistir o documento assinado.
	 * @param textoWrapper
	 * @param resultadoAssinatura
	 * @return
	 * @throws ServiceException
	 */
	private AssinaturaDto montaAssinaturaDto(TextoWrapper textoWrapper, ResultadoAssinatura resultadoAssinatura) throws ServiceException {
		Long id = textoWrapper.getTexto().getId();
		Texto texto = textoService.recuperarPorId(id);
		AssinaturaDto assinaturaDto = new AssinaturaDto();
		assinaturaDto.setCarimbo(resultadoAssinatura.getPdfCarimbado());
		assinaturaDto.setConteudoAssinado(resultadoAssinatura.getPdfAssinado());
		assinaturaDto.setDataCarimboTempo(resultadoAssinatura.getDataCarimboDeTempo());
		assinaturaDto.setSequencialDocumentoEletronico(textoWrapper.getSequencialDocumentoEletronico());
		assinaturaDto.setHashValidacao(textoWrapper.getHashValidacao());
		assinaturaDto.setSiglaSistema(SIGLA_SISTEMA);
		assinaturaDto.setTexto(texto);
		assinaturaDto.setTipo(textoWrapper.getTipoDocumentoTexto());
		assinaturaDto.setUsuarioLogado(textoWrapper.getIdUsuario());
		assinaturaDto.setObservacao(textoWrapper.getObservacao());
		assinaturaDto.setTipoAssinatura(textoWrapper.getTipoAssinatura());
		assinaturaDto.setAssinatura(resultadoAssinatura.getAssinatura());
		assinaturaDto.setSubjectDN(resultadoAssinatura.getSubjectDN());
		return assinaturaDto;
	}

	private String montaIdentificacaoDoTexto(TextoWrapper textoWrapper) {
		return textoWrapper.getTexto().toString() + ":" + textoWrapper.getTexto().getId();
	}
}