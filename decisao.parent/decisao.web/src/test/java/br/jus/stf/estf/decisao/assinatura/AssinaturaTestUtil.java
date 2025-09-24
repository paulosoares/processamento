package br.jus.stf.estf.decisao.assinatura;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CRL;
import java.security.cert.CRLException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.List;

public class AssinaturaTestUtil {

	private AssinaturaTestUtil() {

	}

	public static InputStream getInputStreamFromClasspath(String fileName) {
		return AssinaturaTestUtil.class.getClassLoader().getResourceAsStream("assinador/" + fileName);
	}

	public static String getPathTempDirFile(String fileName) {
		return "C:\\tmp\\" + fileName;
	}

	private static Certificate getCertificateFromFile(String fileName) throws CertificateException {
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		InputStream is = getInputStreamFromClasspath(fileName);
		if (is != null) {
			return cf.generateCertificate(is);
		} else {
			throw new RuntimeException("Arquivo de certificado não encontrado.");
		}
	}

	private static CRL getCRLFromFile(String fileName) throws CertificateException, CRLException {
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		InputStream is = getInputStreamFromClasspath(fileName);
		if (is != null) {
			return cf.generateCRL(is);
		} else {
			throw new RuntimeException("Arquivo de CRL não encontrado.");
		}
	}
	
	private static PrivateKey getPrivateKeyFromFile(String fileName, char[] certPassword, char[] keyPassword) {
		try {
			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
			ks.load(getInputStreamFromClasspath(fileName), certPassword);
			String alias = (String) ks.aliases().nextElement();
			PrivateKey pk = (PrivateKey) ks.getKey(alias, keyPassword);
			return pk;
		} catch (KeyStoreException e) {
			throw new RuntimeException("Erro ao recuperar chave privada de arquivo.", e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Erro ao recuperar chave privada de arquivo.", e);
		} catch (CertificateException e) {
			throw new RuntimeException("Erro ao recuperar chave privada de arquivo.", e);
		} catch (IOException e) {
			throw new RuntimeException("Erro ao recuperar chave privada de arquivo.", e);
		} catch (UnrecoverableKeyException e) {
			throw new RuntimeException("Erro ao recuperar chave privada de arquivo.", e);
		}
	}

	public static Certificate getCertificateACRaiz() throws CertificateException {
		return getCertificateFromFile("acrdev2v1.crt");
	}

	public static Certificate getCertificateACIntermediaria() throws CertificateException {
		return getCertificateFromFile("acsdv1.crt");
	}

	public static Certificate getCertificateACEmissora() throws CertificateException {
		return getCertificateFromFile("acdv1.crt");
	}

	public static Certificate getCertificateTesteUnitario001() throws CertificateException {
		return getCertificateFromFile("tu001.crt");
	}

	public static PrivateKey getPrivateKeyTesteUnitario001() {
		return getPrivateKeyFromFile("tu001.jks", "changeit".toCharArray(), "password".toCharArray());
	}

	public static Certificate getCertificatePessoa001() throws CertificateException {
		return getCertificateFromFile("tomas-01.crt");
	}

	public static PrivateKey getPrivateKeyPessoa001() {
		return getPrivateKeyFromFile("tomas-01.jks", "changeit".toCharArray(), "password".toCharArray());
	}

	public static CRL[] getCrls() {
		List<CRL> crls = new ArrayList<CRL>();
		try {
			crls.add(getCRLFromFile("crl/LCRacdecv1.crl"));
			crls.add(getCRLFromFile("crl/LCRacintsupdecv1.crl"));
			crls.add(getCRLFromFile("crl/LCRacraizv1.crl"));
			return crls.toArray(new CRL[0]);
		} catch (CertificateException e) {
			throw new RuntimeException("Erro ao recuperar CRL.", e);
		} catch (CRLException e) {
			throw new RuntimeException("Erro ao recuperar CRL.", e);
		}
	}
	
}
