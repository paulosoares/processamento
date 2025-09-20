package br.gov.stf.estf.util;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.gov.stf.framework.model.service.ServiceException;

public class Conversor {
	
	private static final Log log = LogFactory.getLog(Conversor.class);
	
	public static byte[] converterRtfParaPdfComConversorExterno(byte[] rtf) throws IOException, ServiceException {
		String url = TipoAmbiente.getInstance().getUrlConversorRtfPdfSTFDigital();
		log.info("Chamando conversor: " + url);
		PostMethod filePost = new PostMethod(url);
		filePost.getParams().setBooleanParameter(HttpMethodParams.USE_EXPECT_CONTINUE, false);

		Part[] parts = new Part[1];
		parts[0] = new FilePart("arquivo", new ByteArrayPartSource("arquivo", rtf));
		MultipartRequestEntity entity = new MultipartRequestEntity(parts, filePost.getParams());
		filePost.setRequestEntity(entity);

		try {
			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams().setConnectionTimeout(30000);

			int status = client.executeMethod(filePost);

			if (status == HttpStatus.SC_OK) {
				return IOUtils.toByteArray(filePost.getResponseBodyAsStream());
			} else {
				String msg = String.format("Erro HTTP %d ao chamar o serviço de conversão de documentos do STF Digital.", status);
				throw new ServiceException(msg);
			}
		} catch (Exception ex) {
			throw new ServiceException(ex);
		} finally {
			filePost.releaseConnection();
		}
	}
}
