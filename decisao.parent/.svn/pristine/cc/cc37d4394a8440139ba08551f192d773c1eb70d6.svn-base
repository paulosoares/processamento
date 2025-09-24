package br.jus.stf.estf.decisao.documento.support.assinador;

import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.util.encoders.Base64;
import org.junit.Ignore;
import org.junit.Test;

import br.jus.stf.estf.decisao.assinatura.demo.AbstractAssinaturaDemoTest;

@Ignore
public class ValidadorCertificadoTest extends AbstractAssinaturaDemoTest {

	@Test
	public void testValidarCertificado() throws Exception {
		Certificate[] cadeia = recuperarCadeia();
		String[] cadeiaStr = certToStr(cadeia);
		ValidadorCertificado vc = ValidadorCertificado.build(cadeiaStr);
		vc.validarCertificado();
	}

	private String[] certToStr(Certificate[] cadeia) throws CertificateEncodingException {
		List<String> certs = new ArrayList<String>();
		for (Certificate c : cadeia) {
			String certStr = new String(Base64.encode(c.getEncoded()));
			certs.add(certStr);
		}
		return certs.toArray(new String[0]);
	}

}
