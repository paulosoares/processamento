package br.jus.stf.estf.decisao.support.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

public final class ReportUtils {

	public static final FormatoArquivo FORMATO_PADRAO = FormatoArquivo.PDF;
	public static final String NOME_ARQUIVO_PADRAO = "report";

	/**
	 * Construtor de classes utilitárias deve ser escondido.
	 */
	private ReportUtils() {
	}

	public static void report(InputStream is) {
		report(is, NOME_ARQUIVO_PADRAO, FORMATO_PADRAO);
	}

	public static void report(InputStream is, FormatoArquivo formato) {
		report(is, NOME_ARQUIVO_PADRAO, formato);
	}

	public static void report(InputStream is, String nomeArquivo, FormatoArquivo formato) {		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		response.setHeader("Content-disposition", "attachment; filename=\"" + nomeArquivo + formato.getExtensao() + "\"");
		response.setContentType(formato.getMimeType());

		try {
			IOUtils.copy(is, response.getOutputStream());
		} catch (IOException e) {
			throw new NestedRuntimeException(e);
		} finally {
			IOUtils.closeQuietly(is);
		}

		facesContext.responseComplete();
	}

	public static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		// Get the size of the file
		long length = file.length();

		// if (length > Integer.MAX_VALUE) {
		// // File is too large
		// }

		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}

		// Close the input stream and return bytes
		is.close();
		return bytes;
	}

}
