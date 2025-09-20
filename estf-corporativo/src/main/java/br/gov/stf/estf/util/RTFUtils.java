package br.gov.stf.estf.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.rtf.RTFEditorKit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class RTFUtils {

	private static Log logger = LogFactory.getLog(RTFUtils.class);

	/** Converte um arquivo RTF em String. */
	public static String converterRtfToString(byte[] arquivoEletronico) {
		String retorno = new String();
		RTFEditorKit rtfParser = new RTFEditorKit();
		Document document = rtfParser.createDefaultDocument();
		try {
			rtfParser.read(new ByteArrayInputStream(arquivoEletronico), document, 0);
			retorno = document.getText(0, document.getLength());
		} catch (IOException e) {
			logger.error("Erro ao converter o arquivo RTF para String.", e);
		} catch (BadLocationException e) {
			logger.error("Erro ao converter o arquivo RTF para String.", e);
		}
		return retorno;
	}

	public static ByteArrayOutputStream converterStringParaRtf(String dados) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			RTFEditorKit kit = new RTFEditorKit();
			Document doc = kit.createDefaultDocument();
			doc.insertString(0, dados, null);

			kit.write(baos, doc, 0, doc.getLength());
			baos.close();

		} catch (IOException e) {
			logger.error("Erro ao converter o texto para rtf.", e);
		} catch (BadLocationException e) {
			logger.error("Erro ao converter o texto para rtf. Texto nao localizado.", e);
		}
		return baos;
	}
}
