package br.jus.stf.estf.decisao.documento.support.assinador;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;

public class AssinadorUtil {

	protected static Log logger = Logging.getLog(AssinadorUtil.class);
	
	private AssinadorUtil() {

	}

	protected static X509Certificate stringToCertificate(String certificateStr) {
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			X509Certificate cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(Base64.decodeBase64(certificateStr.getBytes())));
			return cert;
		} catch (CertificateException e) {
			logger.error("Erro ao converter String para Certificate", e);
		}
		
		return null;
	}

	protected static LinkedList<X509Certificate> gerarCadeia(String[] cadeia) {
		List<X509Certificate> certificados = new ArrayList<X509Certificate>();
		LinkedList<X509Certificate> cadeiaOrdenada = new LinkedList<X509Certificate>();
		
		X509Certificate certificadoUsuario = null;
		X509Certificate certificadoRaiz = null;
		
		for (String certStr : cadeia) {
			X509Certificate cert = stringToCertificate(certStr);
			certificados.add(cert);
			
			if (cert.getIssuerDN().getName().equals(cert.getSubjectDN().getName()))
				certificadoRaiz = cert;
			
			if (cert.getKeyUsage()[0])
				certificadoUsuario = cert;
		}
		
		cadeiaOrdenada.addFirst(certificadoUsuario);
		X509Certificate certificadoPai = getPai(certificados, certificadoUsuario);
		
		while(certificadoPai != certificadoRaiz) {
			cadeiaOrdenada.add(certificadoPai);
			certificadoPai = getPai(certificados, certificadoPai);
		}
		
		cadeiaOrdenada.add(certificadoRaiz);
		
		return cadeiaOrdenada;
	}

	private static X509Certificate getPai(List<X509Certificate> certificados, X509Certificate certificado) {
		for (X509Certificate c : certificados)
			if (c.getSubjectDN().getName().equals(certificado.getIssuerDN().getName()))
				return c;
		
		return null;
	}
}
