package br.jus.stf.estf.decisao.assinatura.demo;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.util.Calendar;

import org.apache.commons.codec.binary.Base64;
import org.junit.Ignore;
import org.junit.Test;

import br.jus.stf.estf.decisao.assinatura.AssinaturaTestUtil;

import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfLiteral;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPKCS7;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSigGenericPKCS;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;

@Ignore
public class AssinaturaPdfSHA1withRSAWincerSignedDemoTest extends AbstractAssinaturaDemoTest {

	@Test
	public void testGerarPdfAssinadoSHA1WincerSignedComCadeia() throws Exception {
		PdfReader reader = getPdfReader();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrivateKey key = AssinaturaTestUtil.getPrivateKeyPessoa001();
		Certificate[] chain = recuperarCadeia();

		PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');
		PdfSignatureAppearance appearance = stamper.getSignatureAppearance();

		appearance.setCrypto(null, chain, null, PdfSignatureAppearance.WINCER_SIGNED);

		appearance.setSignDate(Calendar.getInstance());
		appearance.setReason("MOTIVO");

		appearance.setExternalDigest(new byte[256], new byte[20], "RSA");
		appearance.preClose();

		MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
		byte buf[] = new byte[8192];
		int n;
		InputStream inp = appearance.getRangeStream();
		while ((n = inp.read(buf)) > 0) {
			messageDigest.update(buf, 0, n);
		}
		byte hash[] = messageDigest.digest();

		PdfSigGenericPKCS sg = appearance.getSigStandard();
		PdfLiteral slit = (PdfLiteral) sg.get(PdfName.CONTENTS);
		byte[] outc = new byte[(slit.getPosLength() - 2) / 2];
		PdfPKCS7 sig = sg.getSigner();

		Signature signature = Signature.getInstance("SHA1withRSA");
		signature.initSign(key);
		signature.update(hash);

		byte[] signed = signature.sign();
		System.out.println("Assinatura pelo Java:" + new String(Base64.encodeBase64(signed)));

		byte[] assinaturaExterna = hookAssinarExternamenteSHA1(hash);
		System.out.println("Assinatura externa:" + new String(Base64.encodeBase64(assinaturaExterna)));

		checarAssinaturaInternaExterna(signed, assinaturaExterna);
		if (assinaturaExterna.length > 0) {
			signed = assinaturaExterna;
		}

		sig.setExternalDigest(signed, hash, "RSA");

		PdfDictionary dic = new PdfDictionary();
		byte[] ssig = sig.getEncodedPKCS7();
		System.arraycopy(ssig, 0, outc, 0, ssig.length);
		dic.put(PdfName.CONTENTS, new PdfString(outc).setHexWriting(true));
		appearance.close(dic);

		FileOutputStream fos = new FileOutputStream(AssinaturaTestUtil.getPathTempDirFile(NOME_PDF_DE_TESTE_001 + "-assinado-sha1ws.pdf"));
		os.writeTo(fos);
		fos.flush();
		fos.close();
	}

}
