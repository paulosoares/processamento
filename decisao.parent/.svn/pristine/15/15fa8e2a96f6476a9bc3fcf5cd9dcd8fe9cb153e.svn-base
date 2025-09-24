package br.jus.stf.estf.decisao.documento.support.assinador.algs;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CRL;
import java.security.cert.Certificate;

import org.apache.commons.codec.binary.Base64;

import br.jus.stf.estf.decisao.documento.support.assinador.AssinadorPorPartes;
import br.jus.stf.estf.decisao.documento.support.assinador.ContextoAssinatura;
import br.jus.stf.estf.decisao.documento.support.assinador.exception.AssinaturaExternaException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfLiteral;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPKCS7;
import com.itextpdf.text.pdf.PdfSigGenericPKCS;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;
import com.sun.star.uno.RuntimeException;

public class SHA1WincerSignedAssinadorPorPartes extends AssinadorPorPartes {

	public SHA1WincerSignedAssinadorPorPartes() {
		super(false);
	}

	@Override
	protected byte[] preGerarHashes(Certificate[] cadeia, CRL[] crls, ContextoAssinatura ca, PdfStamper stamper, PdfSignatureAppearance appearance)
			throws AssinaturaExternaException {
		try {
			appearance.setCrypto(null, cadeia, crls, PdfSignatureAppearance.WINCER_SIGNED);
			appearance.setExternalDigest(new byte[256], new byte[20], "RSA");

			appearance.preClose();

			MessageDigest md = MessageDigest.getInstance("SHA1");
			byte buf[] = new byte[8192];
			int n;
			InputStream inp = appearance.getRangeStream();
			while ((n = inp.read(buf)) > 0) {
				md.update(buf, 0, n);
			}
			byte primeiroHash[] = md.digest();

			ca.setAppearance(appearance);
			ca.setPrimeiroHash(primeiroHash);

			return primeiroHash;
		} catch (IOException e) {
			throw new AssinaturaExternaException("Erro ler pré-assinatura do PDF..", e);
		} catch (DocumentException e) {
			throw new AssinaturaExternaException("Erro ao inciar assinatura do PDF.", e);
		} catch (NoSuchAlgorithmException e) {
			throw new AssinaturaExternaException("Erro ao calcular hash do PDF.", e);
		}
	}

	@Override
	protected void posAssinarImpl(ContextoAssinatura ca, PdfSignatureAppearance appearance, byte[] primeiroHash, String assinatura)
			throws AssinaturaExternaException {
		try {
			PdfSigGenericPKCS sg = appearance.getSigStandard();
			PdfLiteral slit = (PdfLiteral) sg.get(PdfName.CONTENTS);
			byte[] outc = new byte[(slit.getPosLength() - 2) / 2];
			PdfPKCS7 sig = sg.getSigner();
			sig.setExternalDigest(Base64.decodeBase64(assinatura.getBytes()), primeiroHash, "RSA");

			PdfDictionary dic = new PdfDictionary();
			byte[] ssig = sig.getEncodedPKCS7();
			System.arraycopy(ssig, 0, outc, 0, ssig.length);
			dic.put(PdfName.CONTENTS, new PdfString(outc).setHexWriting(true));
			appearance.close(dic);
		} catch (IOException e) {
			throw new AssinaturaExternaException("Erro ao finalizar montagem do PDF.", e);
		} catch (DocumentException e) {
			throw new AssinaturaExternaException("Erro ao fechar PDF assinado.", e);
		}
	}

	@Override
	public byte[] prepararHashParaAssinaturaExterna(byte[] dataToSign) {
		return aplicarHashSHA1(dataToSign);
	}

	private byte[] aplicarHashSHA1(byte[] arr) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
			messageDigest.update(arr);
			byte hash[] = messageDigest.digest();
			return hash;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Algoritmo de hash SHA1 não encontrado.", e);
		}
	}

}
