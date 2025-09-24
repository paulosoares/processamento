package br.jus.stf.estf.decisao.tmp;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.commons.codec.binary.Base64;
import org.junit.Ignore;
import org.junit.Test;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfLiteral;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPKCS7;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSigGenericPKCS;
import com.itextpdf.text.pdf.PdfSignature;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;

@Ignore
public class AssinarPdfTest {

	@Test
	public void assinarPdfTeste1() throws Exception {
		PdfReader reader = getPdfReader();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrivateKey key = getPrivateKey();
		Certificate[] chain = getCertificateChain();

		PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');
		PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
		appearance.setCrypto(null, chain, null, PdfSignatureAppearance.SELF_SIGNED);
		appearance.setReason("External hash example");
		appearance.setLocation("Foobar");
		appearance.setVisibleSignature(new Rectangle(72, 732, 144, 780), 1, "sig");
		appearance.setExternalDigest(new byte[10000], null, "RSA");
		appearance.preClose();
		Signature signature = Signature.getInstance("SHA1withRSA");
		signature.initSign(key);
		byte buf[] = new byte[8192];
		int n;
		InputStream inp = appearance.getRangeStream();
		while ((n = inp.read(buf)) > 0) {
			signature.update(buf, 0, n);
		}
		PdfPKCS7 sig = appearance.getSigStandard().getSigner();
		sig.setExternalDigest(signature.sign(), null, "RSA");
		PdfDictionary dic = new PdfDictionary();
		dic.put(PdfName.CONTENTS, new PdfString(sig.getEncodedPKCS1()).setHexWriting(true));
		appearance.close(dic);

		FileOutputStream fos = new FileOutputStream("C:\\tmp\\assinarPdfTeste1.pdf");
		os.writeTo(fos);
		fos.flush();
		fos.close();
	}

	@Test
	public void assinarPdfTeste2() throws Exception {
		PdfReader reader = getPdfReader();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrivateKey key = getPrivateKey();
		Certificate[] chain = getCertificateChain();

		PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');
		PdfSignatureAppearance appearance = stamper.getSignatureAppearance();

		boolean sign = true;

		appearance.setCrypto(key, chain, null, PdfSignatureAppearance.WINCER_SIGNED);
		appearance.setExternalDigest(null, new byte[20], null);
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
		if (sign) {
			Signature signature = Signature.getInstance("SHA1withRSA");
			signature.initSign(key);
			signature.update(hash);
			sig.setExternalDigest(signature.sign(), hash, "RSA");
		} else
			sig.setExternalDigest(null, hash, null);
		PdfDictionary dic = new PdfDictionary();
		byte[] ssig = sig.getEncodedPKCS7();
		System.arraycopy(ssig, 0, outc, 0, ssig.length);
		dic.put(PdfName.CONTENTS, new PdfString(outc).setHexWriting(true));
		appearance.close(dic);

		FileOutputStream fos = new FileOutputStream("C:\\tmp\\assinarPdfTeste2.pdf");
		os.writeTo(fos);
		fos.flush();
		fos.close();
	}

	@Test
	public void assinarPdfTeste3() throws Exception {
		PdfReader reader = getPdfReader();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrivateKey key = getPrivateKey();
		Certificate[] chain = getCertificateChain();

		PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');
		PdfSignatureAppearance appearance = stamper.getSignatureAppearance();

		boolean sign = true;

		appearance.setCrypto(null, chain, null, PdfSignatureAppearance.WINCER_SIGNED);
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
		if (sign) {
			Signature signature = Signature.getInstance("SHA1withRSA");
			signature.initSign(key);
			signature.update(hash);
			sig.setExternalDigest(signature.sign(), hash, "RSA");
		} else
			sig.setExternalDigest(null, hash, null);
		PdfDictionary dic = new PdfDictionary();
		byte[] ssig = sig.getEncodedPKCS7();
		System.arraycopy(ssig, 0, outc, 0, ssig.length);
		dic.put(PdfName.CONTENTS, new PdfString(outc).setHexWriting(true));
		appearance.close(dic);

		FileOutputStream fos = new FileOutputStream("C:\\tmp\\assinarPdfTeste3.pdf");
		os.writeTo(fos);
		fos.flush();
		fos.close();
	}

	@SuppressWarnings("restriction")
	@Test
	public void assinarPdfTeste4() throws Exception {
		PdfReader reader = getPdfReader();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrivateKey key = getPrivateKey();
		Certificate[] chain = getCertificateChain();

		PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');
		PdfSignatureAppearance appearance = stamper.getSignatureAppearance();

		boolean sign = true;

		appearance.setCrypto(null, chain, null, PdfSignatureAppearance.WINCER_SIGNED);
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
		if (sign) {
			Signature signature = Signature.getInstance("SHA1withRSA");
			signature.initSign(key);
			signature.update(hash);
			System.out.println(new Base64().encode(hash));

			MessageDigest messageDigest2 = MessageDigest.getInstance("SHA1");
			messageDigest2.update(hash);
			byte hashNovo[] = messageDigest2.digest();

			System.out.println(new Base64().encode(hashNovo));

			byte[] signed = signature.sign();
			System.out.println(new Base64().encode(signed));
			// signed = simularAssinaturaDoHash(hash);
			sig.setExternalDigest(signed, hash, "RSA");
		} else
			sig.setExternalDigest(null, hash, null);
		PdfDictionary dic = new PdfDictionary();
		byte[] ssig = sig.getEncodedPKCS7();
		System.arraycopy(ssig, 0, outc, 0, ssig.length);
		dic.put(PdfName.CONTENTS, new PdfString(outc).setHexWriting(true));
		appearance.close(dic);

		FileOutputStream fos = new FileOutputStream("C:\\tmp\\assinarPdfTeste4.pdf");
		os.writeTo(fos);
		fos.flush();
		fos.close();
	}

	@Test
	public void assinarPdfTeste5() throws Exception {
		PdfReader reader = getPdfReader();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrivateKey key = getPrivateKey2();
		Certificate[] chain = getCertificateChain2();

		PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');
		PdfSignatureAppearance appearance = stamper.getSignatureAppearance();

		boolean sign = true;

		appearance.setCrypto(null, chain, null, PdfSignatureAppearance.WINCER_SIGNED);
		appearance.setSignDate(Calendar.getInstance());
		// appearance.setProvider(STFProvider.PROVIDER_NAME);
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
		if (sign) {
			Signature signature = Signature.getInstance("SHA1withRSA");
			signature.initSign(key);
			signature.update(hash);
			System.out.println(new Base64().encode(hash));

			MessageDigest messageDigest2 = MessageDigest.getInstance("SHA1");
			messageDigest2.update(hash);
			byte hashNovo[] = messageDigest2.digest();

			System.out.println(new Base64().encode(hashNovo));

			byte[] signed = signature.sign();
			System.out.println(new Base64().encode(signed));
			// signed = simularAssinaturaDoHash(hash);
			sig.setExternalDigest(signed, hash, "RSA");
		} else
			sig.setExternalDigest(null, hash, null);
		PdfDictionary dic = new PdfDictionary();
		byte[] ssig = sig.getEncodedPKCS7();
		System.arraycopy(ssig, 0, outc, 0, ssig.length);
		dic.put(PdfName.CONTENTS, new PdfString(outc).setHexWriting(true));
		appearance.close(dic);

		FileOutputStream fos = new FileOutputStream("C:\\tmp\\assinarPdfTeste5.pdf");
		os.writeTo(fos);
		fos.flush();
		fos.close();
	}

	@Test
	public void assinarPdfTeste6() throws Exception {
		PdfReader reader = getPdfReader();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrivateKey key = getPrivateKey2();
		Certificate[] chain = getCertificateChain2();

		PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');
		PdfSignatureAppearance appearance = stamper.getSignatureAppearance();

		boolean sign = true;

		// appearance.setCrypto(null, chain, null, PdfSignatureAppearance.);
		appearance.setSignDate(Calendar.getInstance());
		appearance.setReason("MOTIVO");
		// appearance.setExternalDigest(new byte[256], new byte[32], "RSA");

		PdfSignature dic = new PdfSignature(PdfName.ADOBE_PPKLITE, PdfName.ADBE_PKCS7_DETACHED);
		dic.setReason(appearance.getReason());
		dic.setDate(new PdfDate(appearance.getSignDate()));
		appearance.setCryptoDictionary(dic);
		HashMap<PdfName, Integer> exc = new HashMap<PdfName, Integer>();
		exc.put(PdfName.CONTENTS, new Integer(8192 * 2 + 2));
		appearance.preClose(exc);

		PdfPKCS7 sgnNew = new PdfPKCS7(null, chain, null, "SHA256", null, false);

		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		byte buf[] = new byte[8192];
		int n;
		InputStream inp = appearance.getRangeStream();
		while ((n = inp.read(buf)) > 0) {
			messageDigest.update(buf, 0, n);
		}
		byte hash[] = messageDigest.digest();
		Calendar cal = Calendar.getInstance();
		byte[] sh = sgnNew.getAuthenticatedAttributeBytes(hash, cal, null);

		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initSign(key);
		signature.update(sh);

		byte[] signed = signature.sign();

		sgnNew.setExternalDigest(signed, null, "RSA");
		byte[] encodedSig = sgnNew.getEncodedPKCS7(hash, cal);
		byte[] paddedSig = new byte[8192];
		System.arraycopy(encodedSig, 0, paddedSig, 0, encodedSig.length);

		PdfDictionary dic2 = new PdfDictionary();
		dic2.put(PdfName.CONTENTS, new PdfString(paddedSig).setHexWriting(true));

		appearance.close(dic2);

		FileOutputStream fos = new FileOutputStream("C:\\tmp\\assinarPdfTeste6.pdf");
		os.writeTo(fos);
		fos.flush();
		fos.close();
	}

	private Certificate[] getCertificateChain() throws Exception {
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(new FileInputStream("C:\\tmp\\tomas-01.jks"), "changeit".toCharArray());
		String alias = (String) ks.aliases().nextElement();
		Certificate cert = ks.getCertificate(alias);
		return new Certificate[] { cert };
	}

	private Certificate[] getCertificateChain2() throws Exception {
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(new FileInputStream("C:\\Users\\tomas.godoi\\Desenv\\Seg\\Keystores\\tomas-full"), "changeit".toCharArray());
		Certificate cert[] = new Certificate[] { ks.getCertificate("tomas-acit"), ks.getCertificate("acit"), ks.getCertificate("acrt") };
		return cert;
	}

	private PrivateKey getPrivateKey() throws Exception {
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(new FileInputStream("C:\\tmp\\tomas-01.jks"), "changeit".toCharArray());
		String alias = (String) ks.aliases().nextElement();
		PrivateKey pk = (PrivateKey) ks.getKey(alias, "password".toCharArray());
		return pk;
	}

	private PrivateKey getPrivateKey2() throws Exception {
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(new FileInputStream("C:\\Users\\tomas.godoi\\Desenv\\Seg\\Keystores\\tomas-full"), "changeit".toCharArray());
		PrivateKey pk = (PrivateKey) ks.getKey("tomas", "changeit".toCharArray());
		return pk;
	}

	private PdfReader getPdfReader() throws Exception {
		return new PdfReader(new FileInputStream("C:\\tmp\\exemplo.pdf"));
	}

	private byte[] simularAssinaturaDoHash(byte[] hash) {
		String assinado = "yBU4Rte8v+THtuPIAnAUB7g/GzgYkTgdLvGb1396mLyArNRiRxf8NfdrPBkSNlzzXNEyY+TStfoRSHxQ4V5mMSQPxs+PYlMGCI5qXYaxUmBpBePRbhFsaIJN0HGOPUxUtPlcvlTGwGWJwa8d9I2iCbgrRoqoqYzWVcm3uiScn5L0OTCYN4rb/VrU/EkxNyWBguaOZrJrBwvG2i8pmFoXnxSj1K2eIdt3+KBk3eYT8bZv9OxDqaUjhziCdDnFSN/6H8bMgbO+0p6R9pHHNYvDw5xFTI0aMm1YZPqZ4aegxWE+uvhUPHEf6Jo1tv44qjLZn8Isr66MJ+m7stluHz2+8A==";
		byte[] byAssinado = Base64.decodeBase64(assinado.getBytes());
		return byAssinado;
	}

}
