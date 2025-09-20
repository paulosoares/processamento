package br.gov.stf.estf.assinatura.visao.servlet;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;

import br.gov.stf.estf.documento.model.dataaccess.DocumentoEletronicoDao;
import br.gov.stf.framework.util.ApplicationFactory;
import br.gov.stf.framework.util.IServiceLocator;

public class DocumentoDownloadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static final String ARQUIVO_DOCUMENTO_PARAM = "id";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {

		String idStr = request.getParameter(ARQUIVO_DOCUMENTO_PARAM);

		if( idStr == null || !NumberUtils.isNumber(idStr) )
			throw new ServletException("Parâmetro do documento inválido");

		Long idArquivoDocumento = Long.valueOf(idStr);

		try {
			Blob blob = recuperarArquivoDocumento(idArquivoDocumento);

			download1(response, blob, "Documento");
			//download2(response, blob);		
		}
		catch( Exception e ) {
			e.printStackTrace();
		}

		response.flushBuffer();
		//response.reset();
		//response.resetBuffer();
	}

	private Blob recuperarArquivoDocumento(Long seqArquivoDocumento) throws Exception  {
		DocumentoEletronicoDao documentoEletronicoDao = 
			(DocumentoEletronicoDao) getService("documentoEletronicoDao");

		Blob blob = documentoEletronicoDao.recuperarArquivoDocumento(seqArquivoDocumento);

		return blob;
	}

	public void download1(HttpServletResponse response, Blob blob, String nomeArquivo) 
	throws Exception {
		response.setContentType("application/octet-stream");
		response.setHeader("Content-disposition","attachment; filename=" + nomeArquivo + ".pdf");
		response.setHeader("cache-control", "no-cache");

		response.setBufferSize(1024);
		OutputStream os = response.getOutputStream(); 
		BufferedOutputStream out = new BufferedOutputStream( os );

		InputStream is = blob.getBinaryStream();
		//		BufferedInputStream bis = new BufferedInputStream(is);
		//		IOUtils.copy(bis, out);


		try {
			byte[] buffer = new byte[4 * 1024]; //4k
			for( int i = is.read(buffer); i != -1; i = is.read(buffer) ) {
				out.write(buffer, 0 ,i);
			}

			out.flush();
			response.flushBuffer();
		}
		finally {
			is.close();
			out.close();
		}		
	}	

	/*public Object getService(String serviceName) {
		if( locator == null )
			locator = ApplicationFactory.getInstance().getServiceLocator();

		return locator.getService(serviceName);
	}*/

	/*public Object getService(String nomeServico) {
		IServiceLocator locator = ServiceLocator.getInstance();
        return locator.getService(nomeServico);	}
   	}*/
	
	 public Object getService(String nomeServico) {

	    	IServiceLocator locator = ApplicationFactory.getInstance().getServiceLocator();
	        
	        return locator.getService(nomeServico);
	 }

}
/*
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import javax.servlet.http.HttpServletResponse;

public class Download {
	public static void downloadFile(HttpServletResponse res, String filename) // filename
	// could
	// full
	// path!
	throws Exception {
		downloadFile(res, filename, filename);
	}

	public static void downloadFile(HttpServletResponse res, String filename,
			String displayFileName) // filename could be full path!
	throws Exception {
		File file = new File(filename);
		String contentType = "";
		boolean ifXLSDOC = false;
		// res.setContentType(URLConnection.guessContentTypeFromName(filename));
		if (filename.toLowerCase().endsWith(".xls")) {
			contentType = "application/vnd.ms-excel";
			ifXLSDOC = true;
		}
		if (filename.toLowerCase().endsWith(".xlt")) {
			contentType = "application/vnd.ms-excel";
			ifXLSDOC = true;
		} else if (filename.toLowerCase().endsWith(".csv")) {
			contentType = "application/vnd.ms-excel";
			ifXLSDOC = true;
		} else if (filename.toLowerCase().endsWith(".doc")) {
			contentType = "application/msword";
			ifXLSDOC = true;
		} else if (filename.toLowerCase().endsWith(".rtf")) {
			contentType = "application/msword";
			ifXLSDOC = true;
		} else if (filename.toLowerCase().endsWith(".pdf")) {
			contentType = "application/pdf";
			ifXLSDOC = true;
		} else if (filename.toLowerCase().endsWith(".ppt")) {
			contentType = "application/ms-powerpoint";
			ifXLSDOC = true;
		} else if (filename.toLowerCase().endsWith(".pot")) {
			contentType = "application/ms-powerpoint";
			ifXLSDOC = true;
		} else if (filename.toLowerCase().endsWith(".html")) {
			contentType = "text/html";
		} else if (filename.toLowerCase().endsWith(".htm")) {
			contentType = "text/html";
		} else if (filename.toLowerCase().endsWith(".gif")) {
			contentType = "image/gif";
		} else if (filename.toLowerCase().endsWith(".jpg")) {
			contentType = "image/jpeg";
		} else if (filename.toLowerCase().endsWith(".jpeg")) {
			contentType = "image/jpeg";
		} else if (filename.toLowerCase().endsWith(".dot")) {
			contentType = "application/msword";
			ifXLSDOC = true;
		} else if (filename.toLowerCase().endsWith(".vsd")) {
			contentType = "application/vnd.visio";
			ifXLSDOC = true;
		} else if (filename.toLowerCase().endsWith(".mpp")) {
			contentType = "application/vnd.ms-project";
			ifXLSDOC = true;
		} else if (filename.toLowerCase().endsWith(".zip")) {
			contentType = "application/ZIP";
		} else if (filename.toLowerCase().endsWith(".txt")) {
			contentType = "text/plain";
		} else {
			contentType = "application/octet-stream";
		}
		// Set the headers.
		res.setContentType(contentType);
		if (ifXLSDOC) {
			res.addHeader("Content-Disposition", "attachment; filename="
					displayFileName);
		} else {
			res.addHeader("Content-Disposition", "inline; filename="
					displayFileName);
		}
		// Send the file.
		OutputStream out = res.getOutputStream();
		InputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(file));
			res.setContentLength(in.available());
			byte[] buf = new byte[4 * 1024]; // 4K byte buffer
			int bytesRead;
			while ((bytesRead = in.read(buf)) != -1) {
				out.write(buf, 0, bytesRead);
			}
			if (in != null) {
				in.close();
			}
			out.flush();
			out.close();
			res.reset();
			res.resetBuffer();
		} catch (Exception e) {
		}
	}
	public static void downloadFileBlob(Blob blob, HttpServletResponse res,
			String filename, String displayFileName) // filename could be
	// full path!
	throws Exception {
		//File file = new File(blob.toString());
		String contentType = "";
		boolean ifXLSDOC = false;
		// res.setContentType(URLConnection.guessContentTypeFromName(filename));
		if (filename.toLowerCase().endsWith(".xls")) {
			contentType = "application/vnd.ms-excel";
			ifXLSDOC = true;
		}
		if (filename.toLowerCase().endsWith(".xlt")) {
			contentType = "application/vnd.ms-excel";
			ifXLSDOC = true;
		} else if (filename.toLowerCase().endsWith(".csv")) {
			contentType = "application/vnd.ms-excel";
			ifXLSDOC = true;
		} else if (filename.toLowerCase().endsWith(".doc")) {
			contentType = "application/msword";
			ifXLSDOC = true;
		} else if (filename.toLowerCase().endsWith(".rtf")) {
			contentType = "application/msword";
			ifXLSDOC = true;
		} else if (filename.toLowerCase().endsWith(".pdf")) {
			contentType = "application/pdf";
			ifXLSDOC = true;
		} else if (filename.toLowerCase().endsWith(".ppt")) {
			contentType = "application/ms-powerpoint";
			ifXLSDOC = true;
		} else if (filename.toLowerCase().endsWith(".pot")) {
			contentType = "application/ms-powerpoint";
			ifXLSDOC = true;
		} else if (filename.toLowerCase().endsWith(".html")) {
			contentType = "text/html";
		} else if (filename.toLowerCase().endsWith(".htm")) {
			contentType = "text/html";
		} else if (filename.toLowerCase().endsWith(".gif")) {
			contentType = "image/gif";
		} else if (filename.toLowerCase().endsWith(".jpg")) {
			contentType = "image/jpeg";
		} else if (filename.toLowerCase().endsWith(".jpeg")) {
			contentType = "image/jpeg";
		} else if (filename.toLowerCase().endsWith(".dot")) {
			contentType = "application/msword";
			ifXLSDOC = true;
		} else if (filename.toLowerCase().endsWith(".vsd")) {
			contentType = "application/vnd.visio";
			ifXLSDOC = true;
		} else if (filename.toLowerCase().endsWith(".mpp")) {
			contentType = "application/vnd.ms-project";
			ifXLSDOC = true;
		} else if (filename.toLowerCase().endsWith(".zip")) {
			contentType = "application/ZIP";
		} else if (filename.toLowerCase().endsWith(".txt")) {
			contentType = "text/plain";
		} else {
			contentType = "application/octet-stream";
		}
		// Set the headers.
		res.setContentType(contentType);
		if (ifXLSDOC) {
			res.addHeader("Content-Disposition", "attachment; filename="
					displayFileName);
		} else {
			res.addHeader("Content-Disposition", "inline; filename="
					displayFileName);
		}
		// Send the file.
		OutputStream out = res.getOutputStream();
		InputStream in = null;
		try {
			in = blob.getBinaryStream();
			res.setContentLength(in.available());
			byte[] buf = new byte[4 * 1024]; // 4K byte buffer
			int bytesRead;
			while ((bytesRead = in.read(buf)) != -1) {
				out.write(buf, 0, bytesRead);
			}
			if (in != null) {
				in.close();
			}
			out.flush();
			out.close();
			res.reset();
			res.resetBuffer();
		} catch (Exception e) {
		}
	}
}


 */