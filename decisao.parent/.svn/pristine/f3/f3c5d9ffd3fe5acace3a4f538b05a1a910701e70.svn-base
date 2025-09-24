package br.jus.stf.estf.decisao.documento.support.assinador.algs;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CRL;
import java.security.cert.Certificate;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.jus.stf.estf.decisao.assinatura.AssinaturaTestUtil;
import br.jus.stf.estf.decisao.assinatura.demo.AbstractAssinaturaDemoTest;
import br.jus.stf.estf.decisao.documento.support.assinador.AssinadorPorPartes;
import br.jus.stf.estf.decisao.documento.support.assinador.ContextoAssinatura;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfPKCS7;
import com.itextpdf.text.pdf.PdfReader;

public class SHA256DetachedAssinadorPorPartesTest extends AbstractAssinaturaDemoTest {

	private AssinadorPorPartes app;
	private Certificate[] cadeia;
	private byte[] pdf;
	private String reason;
	private ContextoAssinatura ca;
	private PrivateKey key;
	private CRL[] crls;

	@Before
	public void setUp() throws Exception {
		Security.addProvider(new BouncyCastleProvider());

		cadeia = recuperarCadeia();
		pdf = getPdf();
		reason = "RAZAO";
		ca = new ContextoAssinatura("12345");
		key = AssinaturaTestUtil.getPrivateKeyPessoa001();
		crls = AssinaturaTestUtil.getCrls();

	}

	private byte[] getPdf() throws IOException {
		InputStream is = AssinaturaTestUtil.getInputStreamFromClasspath(PDF_DE_TESTE_001);
		byte[] pdf = IOUtils.toByteArray(is);
		IOUtils.closeQuietly(is);
		return pdf;
	}

	private String assinar(byte[] dataToSign) throws GeneralSecurityException {
		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initSign(key);
		signature.update(dataToSign);

		byte[] signed = signature.sign();

		return new String(Base64.encode(signed));
	}

	@Test
	public void testGerarPdfAssinado() throws Exception {
		app = new SHA256DetachedAssinadorPorPartes(false);
		byte[] dataToSign = app.preAssinar(cadeia, crls, pdf, reason, ca);
		String assinatura = assinar(dataToSign);
		byte[] pdfAssinado = app.posAssinar(ca, assinatura);

		validarPdf(pdfAssinado);
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
//			Assert.assertTrue("Timestamp deveria ser válido.", pdfPKCS7.verifyTimestampImprint());
		}

	}

}
