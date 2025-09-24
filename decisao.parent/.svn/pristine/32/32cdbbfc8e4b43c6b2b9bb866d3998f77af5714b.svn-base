package br.jus.stf.estf.decisao.documento.support.assinador;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.SignatureException;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Store;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import br.jus.stf.estf.decisao.assinatura.AssinaturaTestUtil;
import br.jus.stf.estf.decisao.assinatura.demo.AbstractAssinaturaDemoTest;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfPKCS7;
import com.itextpdf.text.pdf.PdfReader;

@Ignore
public class ValidarPdfComProblemaTest extends AbstractAssinaturaDemoTest {

	@Before
	public void setUp() throws Exception {
		Security.addProvider(new BouncyCastleProvider());
	}

	private byte[] getPdf() throws IOException {
//		InputStream is = AssinaturaTestUtil.getInputStreamFromClasspath("pdf-problematico.pdf");
		InputStream is = AssinaturaTestUtil.getInputStreamFromClasspath("pdf-com-timestamp-valido.pdf");
		byte[] pdf = IOUtils.toByteArray(is);
		IOUtils.closeQuietly(is);
		return pdf;
	}

	@Test
	public void testGerarPdfAssinado() throws Exception {
		byte[] pdf = getPdf();
		
		validarPdf(pdf);
		
		System.out.println(pdf);
	}

	private void validarPdf(byte[] pdfAssinado) throws IOException, NoSuchAlgorithmException {
		PdfReader reader = new PdfReader(pdfAssinado);

		// Recupera os campos das assinaturas do pdf.
		AcroFields acroFields = reader.getAcroFields();
		
		for (String nomeCampo : acroFields.getSignatureNames()) {			
			PdfPKCS7 pdfPKCS7 = acroFields.verifySignature(nomeCampo, "BC");
			try {
				Assert.assertTrue("Assinatura deveria ser válida.", pdfPKCS7.verify());
			} catch (SignatureException e) {
				Assert.fail("Erro ao verificar a assinatura.");
			}
			Assert.assertTrue(pdfPKCS7.verifyTimestampImprint());
			Store certificates = pdfPKCS7.getTimeStampToken().getCertificates();
			for (Object obj : certificates.getMatches(null)) {
				X509CertificateHolder ch = (X509CertificateHolder)obj;
				System.out.println(ch.getSubject().toString());
			}
			System.out.println(pdfPKCS7);
		}
		
	}

}
