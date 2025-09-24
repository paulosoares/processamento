package br.jus.stf.estf.decisao.assinatura.demo;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.commons.codec.binary.Base64;
import org.junit.Ignore;
import org.junit.Test;

import br.jus.stf.estf.decisao.assinatura.AssinaturaTestUtil;

import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPKCS7;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignature;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;

@Ignore
public class AssinaturaPdfSHA1withRSADemoTest extends AbstractAssinaturaDemoTest {

	@Test
	public void testGerarPdfAssinadoSHA1ComCadeia() throws Exception {
		PdfReader reader = getPdfReader();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrivateKey key = AssinaturaTestUtil.getPrivateKeyPessoa001();
		Certificate[] chain = recuperarCadeia();

		PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');
		PdfSignatureAppearance appearance = stamper.getSignatureAppearance();

		appearance.setSignDate(Calendar.getInstance());
		appearance.setReason("MOTIVO");

		PdfSignature dic = new PdfSignature(PdfName.ADOBE_PPKLITE, PdfName.ADBE_PKCS7_DETACHED);
		dic.setReason(appearance.getReason());
		dic.setDate(new PdfDate(appearance.getSignDate()));
		appearance.setCryptoDictionary(dic);
		HashMap<PdfName, Integer> exc = new HashMap<PdfName, Integer>();
		exc.put(PdfName.CONTENTS, new Integer(8192 * 2 + 2));
		appearance.preClose(exc);

		PdfPKCS7 sgnNew = new PdfPKCS7(null, chain, null, "SHA1", null, false);

		MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
		byte buf[] = new byte[8192];
		int n;
		InputStream inp = appearance.getRangeStream();
		while ((n = inp.read(buf)) > 0) {
			messageDigest.update(buf, 0, n);
		}
		byte hash[] = messageDigest.digest();
		Calendar cal = Calendar.getInstance();
		byte[] sh = sgnNew.getAuthenticatedAttributeBytes(hash, cal, null);

		Signature signature = Signature.getInstance("SHA1withRSA");
		signature.initSign(key);
		signature.update(sh);

		byte[] signed = signature.sign();
		System.out.println("Assinatura pelo Java:" + new String(Base64.encodeBase64(signed)));

		byte[] assinaturaExterna = hookAssinarExternamenteSHA1(sh);
		System.out.println("Assinatura externa:" + new String(Base64.encodeBase64(assinaturaExterna)));

		checarAssinaturaInternaExterna(signed, assinaturaExterna);
		if (assinaturaExterna.length > 0) {
			signed = assinaturaExterna;
		}

		sgnNew.setExternalDigest(signed, null, "RSA");
		byte[] encodedSig = sgnNew.getEncodedPKCS7(hash, cal);
		byte[] paddedSig = new byte[8192];
		System.arraycopy(encodedSig, 0, paddedSig, 0, encodedSig.length);

		PdfDictionary dic2 = new PdfDictionary();
		dic2.put(PdfName.CONTENTS, new PdfString(paddedSig).setHexWriting(true));

		appearance.close(dic2);

		FileOutputStream fos = new FileOutputStream(AssinaturaTestUtil.getPathTempDirFile(NOME_PDF_DE_TESTE_001 + "-assinado-sha1.pdf"));
		os.writeTo(fos);
		fos.flush();
		fos.close();
	}

}
