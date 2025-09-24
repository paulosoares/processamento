package br.jus.stf.estf.decisao.documento.support.assinador.algs;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CRL;
import java.security.cert.CRLException;
import java.security.cert.Certificate;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.commons.codec.binary.Base64;

import br.jus.stf.estf.decisao.documento.support.assinador.AssinadorPorPartes;
import br.jus.stf.estf.decisao.documento.support.assinador.ContextoAssinatura;
import br.jus.stf.estf.decisao.documento.support.assinador.exception.AssinaturaExternaException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPKCS7;
import com.itextpdf.text.pdf.PdfSignature;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.TSAClient;
import com.sun.star.uno.RuntimeException;

public class SHA256DetachedAssinadorPorPartes extends AssinadorPorPartes {

	public SHA256DetachedAssinadorPorPartes(boolean hasTSA) {
		super(hasTSA);
	}

	@Override
	protected byte[] preGerarHashes(Certificate[] cadeia, CRL[] crls, ContextoAssinatura ca, PdfStamper stamper, PdfSignatureAppearance appearance)
			throws AssinaturaExternaException {
		try {
			int tamanhoEstimado = calcularTamanhoEstimado(crls);

			PdfSignature dic = new PdfSignature(PdfName.ADOBE_PPKLITE, PdfName.ADBE_PKCS7_DETACHED);
			dic.setReason(appearance.getReason());
			dic.setDate(new PdfDate(appearance.getSignDate()));
			appearance.setCryptoDictionary(dic);
			HashMap<PdfName, Integer> exc = new HashMap<PdfName, Integer>();
			exc.put(PdfName.CONTENTS, new Integer(tamanhoEstimado * 2 + 2));
			appearance.preClose(exc);

			PdfPKCS7 sgnNew = new PdfPKCS7(null, cadeia, crls, "SHA256", null, false);

			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			byte buf[] = new byte[8192];
			int n;
			InputStream inp = appearance.getRangeStream();
			while ((n = inp.read(buf)) > 0) {
				messageDigest.update(buf, 0, n);
			}
			byte primeiroHash[] = messageDigest.digest();
			Calendar cal = Calendar.getInstance();
			byte[] authAttrs = sgnNew.getAuthenticatedAttributeBytes(primeiroHash, cal, null);

			ca.setAppearance(appearance);
			ca.setPrimeiroHash(primeiroHash);
			ca.setPdfPKCS7(sgnNew);
			ca.setSignDate(cal);
			ca.setTamanhoEstimado(tamanhoEstimado);

			return authAttrs;
		} catch (IOException e) {
			throw new AssinaturaExternaException("Erro ler pré-assinatura do PDF..", e);
		} catch (DocumentException e) {
			throw new AssinaturaExternaException("Erro ao inciar assinatura do PDF.", e);
		} catch (NoSuchAlgorithmException e) {
			throw new AssinaturaExternaException("Erro ao calcular hash do PDF.", e);
		} catch (InvalidKeyException e) {
			throw new AssinaturaExternaException("Erro gerar assinatura PKCS7. Chave não encontrada.", e);
		} catch (NoSuchProviderException e) {
			throw new AssinaturaExternaException("Erro gerar assinatura PKCS7. Provedor não encontrado.", e);
		} catch (CRLException e) {
			throw new AssinaturaExternaException("Erro ao estimar tamanho da assinatura.", e);
		}
	}

	private byte[] aplicarHashSHA256(byte[] arr) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(arr);
			byte hash[] = messageDigest.digest();
			return hash;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Algoritmo de hash SHA-256 não encontrado.", e);
		}
	}

	@Override
	protected void posAssinarImpl(ContextoAssinatura ca, PdfSignatureAppearance appearance, byte[] primeiroHash, String assinatura)
			throws AssinaturaExternaException {
		try {
			int tamanhoEstimado = ca.getTamanhoEstimado();
			PdfPKCS7 sgnNew = ca.getPdfPKCS7();
			sgnNew.setExternalDigest(Base64.decodeBase64(assinatura.getBytes()), null, "RSA");

			TSAClient tsaClient = getTSAClient();

			byte[] encodedSig = sgnNew.getEncodedPKCS7(primeiroHash, ca.getSignDate(), tsaClient, null);
			byte[] paddedSig = new byte[tamanhoEstimado];
			System.arraycopy(encodedSig, 0, paddedSig, 0, encodedSig.length);

			PdfDictionary dic2 = new PdfDictionary();
			dic2.put(PdfName.CONTENTS, new PdfString(paddedSig).setHexWriting(true));

			appearance.close(dic2);
		} catch (IOException e) {
			throw new AssinaturaExternaException("Erro ao finalizar montagem do PDF.", e);
		} catch (DocumentException e) {
			throw new AssinaturaExternaException("Erro ao fechar PDF assinado.", e);
		}
	}

	@Override
	public byte[] prepararHashParaAssinaturaExterna(byte[] dataToSign) {
		return aplicarHashSHA256(dataToSign);
	}

}
