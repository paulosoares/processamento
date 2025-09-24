/**
 * 
 */
package br.jus.stf.estf.decisao.support.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.converter.DocumentConversionException;
import br.gov.stf.estf.converter.DocumentConverterService;
import br.gov.stf.estf.converter.DocumentSource;
import br.gov.stf.estf.converter.source.ByteArrayDocumentSource;
import br.gov.stf.estf.converter.target.OutputStreamDocumentTarget;
import br.jus.stf.estf.decisao.support.util.FormatoArquivo;

/**
 * @author Paulo.Estevao
 * @since 07.02.2013
 */
@Service("converterServiceLocal")
public class ConverterService {
	
	@Autowired
	private DocumentConverterService documentConverterService;

	public void converterHtmlParaPDF(InputStream input, OutputStream output) throws IOException, DocumentConversionException {
		try {
			byte[] inputData = IOUtils.toByteArray(input);
			DocumentSource source = new ByteArrayDocumentSource(inputData);
			OutputStreamDocumentTarget target = new OutputStreamDocumentTarget(output);
			documentConverterService.convertDocument(source,
					FormatoArquivo.HTML.getMimeType(), target, FormatoArquivo.PDF.getMimeType());
		} catch (IOException e) {
			throw e;
		} catch (DocumentConversionException e) {
			throw e;
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
	}
	
	public void converterHtmlParaPDF(byte[] input, OutputStream output) throws IOException, DocumentConversionException {
		try {
			DocumentSource source = new ByteArrayDocumentSource(input);
			OutputStreamDocumentTarget target = new OutputStreamDocumentTarget(output);
			documentConverterService.convertDocument(source,
					FormatoArquivo.HTML.getMimeType(), target, FormatoArquivo.PDF.getMimeType());
		} catch (DocumentConversionException e) {
			throw e;
		} finally {
			IOUtils.closeQuietly(output);
		}
	}

	public void converterHtmlParaRTF(InputStream input, OutputStream output) throws IOException {
		try {
			IOUtils.copy(input, output);
		} catch (IOException exception) {
			throw exception;
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
	}
	
	public void converterHtmlParaDOCX(InputStream input, OutputStream output) throws IOException, DocumentConversionException {
		try {
			byte[] inputData = IOUtils.toByteArray(input);
			DocumentSource source = new ByteArrayDocumentSource(inputData);
			OutputStreamDocumentTarget target = new OutputStreamDocumentTarget(output);
			documentConverterService.convertDocument(source,
					FormatoArquivo.HTML.getMimeType(), target, FormatoArquivo.DOC.getMimeType());
		} catch (IOException e) {
			throw e;
		} catch (DocumentConversionException e) {
			throw e;
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
	}
}
