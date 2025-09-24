package br.jus.stf.estf.decisao.documento.support.assinador;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLClassLoader;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.cert.CRL;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;

import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.assinadorweb.config.STFAssinadorConfig;
import br.jus.stf.assinadorweb.config.STFAssinadorConfigBuilder;
import br.jus.stf.assinadorweb.exception.ConfiguracaoAssinadorNaoEncontradaException;
import br.jus.stf.assinadorweb.web.crl.RevogadorServidor;
import br.jus.stf.estf.decisao.documento.support.assinador.exception.AssinaturaExternaException;
import br.jus.stf.estf.decisao.documento.support.assinador.exception.ValidacaoCertificadoException;

public class ValidadorCertificado {

	protected Log log = Logging.getLog(ValidadorCertificado.class);

	private static final String TYPE_PKIX = "PKIX";
	private static final String BOUNCYCASTLE_PROVIDER = "BC";

	private static final String MENSAGEM_CERTIFICADO_NAO_ENCONTRADO = "Certificado do usuário não encontrado.";
	
	private static final String[] CERTIFICADOS_ICP_BRASIL = { "ICP-BRASIL_V0.cer", "ICP-BRASIL_V1.cer", "ICP-BRASIL_V2.cer", "ICP-BRASIL_V3.cer", "ICP-BRASIL_V5.cer" };
	
	protected LinkedList<X509Certificate> cadeiaCompleta;
	protected X509Certificate certificadoUsuario;
	protected X509Certificate certificadoRaiz;

	private CRL[] crl;

	private RevogadorServidor revogador;

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	private ValidadorCertificado(String[] cadeiaStr) throws ServiceException {
		try {
			STFAssinadorConfig config = STFAssinadorConfigBuilder.build();
			this.revogador = new RevogadorServidor(config.getServidorCarimbadorRevogador(), config.getPortaRevogador());
			setCadeiaCompleta(cadeiaStr);
		} catch (ConfiguracaoAssinadorNaoEncontradaException e) {
			throw new ServiceException("Erro ao configurar o revogador.", e);
		}

	}

	public static ValidadorCertificado build(String[] cadeiaStr) throws ServiceException {
		return new ValidadorCertificado(cadeiaStr);
	}

	/**
	 * Método que realiza o processamento do método abaixo do assinador desktop:
	 * 
	 * AbstractAssinador.validarCertificado()
	 * 
	 * @throws ValidacaoCertificadoException
	 * @throws AssinaturaExternaException
	 */
	public void validarCertificado() throws ValidacaoCertificadoException, AssinaturaExternaException {
		log.info("Validando certificado");
		carregaDadosCertificado();

		verificaCertificadoExpirado();

		verificaCertificadoHabilitado();

//		if (isVerificarCRL()) {
//			verificaCertificadoRevogado();
//		}

		verificaCertificadoCadeiaICPBrasil();

		log.info("Certifica válido");
	}

	/**
	 * No Assinador Desktop, o servidor Revogador é consultado para saber se
	 * vai verificar ou não CRL. Entretanto, pelo menos nos testes que realizei,
	 * esse servidor está retornando false por alguma razão. Como no Assinador Móvel
	 * a chave fica mais vulnerável a roubo, decidi para esse assinador sempre verificar
	 * as CRLs.
	 * 
	 * @return
	 */
	private boolean isVerificarCRL() {
		// revogador.isVerificarCRL()
		return false;
		
	}
	
	/**
	 * Método que realiza o processamento do método abaixo do assinador desktop:
	 * 
	 * AbstractAssinador.carregaDadosCertificado()
	 * 
	 * @throws ValidacaoCertificadoException
	 */
	private void carregaDadosCertificado() throws ValidacaoCertificadoException {
		log.info("Carregando informações do certificado...");
		try {
			certificadoUsuario = cadeiaCompleta.getFirst();
			certificadoRaiz = cadeiaCompleta.removeLast();
		} catch (Exception e) {
			throw new ValidacaoCertificadoException(MENSAGEM_CERTIFICADO_NAO_ENCONTRADO, e);
		}
		log.info("Certificado carregado!");
	}

	private void setCadeiaCompleta(String[] cadeiaStr) {
		cadeiaCompleta = AssinadorUtil.gerarCadeia(cadeiaStr);
	}

	/**
	 * Método que realiza o processamento do método abaixo do assinador desktop:
	 * 
	 * AbstractAssinador.verificaCertificadoExpirado()
	 * 
	 * @throws ValidacaoCertificadoException
	 */
	private void verificaCertificadoExpirado() throws ValidacaoCertificadoException {
		log.info("Verificando se o certificado está expirado");
		try {
			certificadoUsuario.checkValidity();
		} catch (CertificateExpiredException e) {
			throw new ValidacaoCertificadoException("O certificado se encontra expirado!", e);
		} catch (CertificateNotYetValidException e) {
			throw new ValidacaoCertificadoException("O certificado não é váliddo!", e);
		}
		log.info("Certificado não expirado");
	}

	/**
	 * Método que realiza o processamento do método abaixo do assinador desktop:
	 * 
	 * AbstractAssinador.verificaCertificadoHabilitado()
	 * 
	 * @throws ValidacaoCertificadoException
	 */
	private void verificaCertificadoHabilitado() throws ValidacaoCertificadoException {
		log.info("Verificando se o certificado está habilitado para assinatura de documentos");
		if (!certificadoUsuario.getKeyUsage()[0]) {
			throw new ValidacaoCertificadoException("Certificado não habilitado para assinatura de documentos.");
		}
		log.info("Certificado habilitado para assinatura");
	}

	/**
	 * Método que realiza o processamento do método abaixo do assinador desktop:
	 * 
	 * AbstractAssinador.verificaCertificadoRevogado()
	 * 
	 * @throws ValidacaoCertificadoException
	 */
	private void verificaCertificadoRevogado() throws ValidacaoCertificadoException {
		log.info("Verificando se o certificado está revogado");

		crl = recuperarListaCertificadosRevogados();

		log.info("Lista de certificados revogados: " + crl);

		try {
			Set<TrustAnchor> trustedAnchors = new HashSet<TrustAnchor>();
			TrustAnchor trustedCert = new TrustAnchor(certificadoRaiz, null);
			trustedAnchors.add(trustedCert);
			CertificateFactory cf = getCertificateFactory();
			CertPath cp = cf.generateCertPath(cadeiaCompleta);
			PKIXParameters params = new PKIXParameters(trustedAnchors);
			params.setRevocationEnabled(true);
			CollectionCertStoreParameters csPrams = new CollectionCertStoreParameters(Arrays.asList(crl));
			CertStore cs = CertStore.getInstance("Collection", csPrams);
			List<CertStore> certStores = new ArrayList<CertStore>();
			certStores.add(cs);
			params.setCertStores(certStores);
			CertPathValidator cpv = CertPathValidator.getInstance(TYPE_PKIX, BOUNCYCASTLE_PROVIDER);
			cpv.validate(cp, params);
		} catch (CertPathValidatorException cpve) {
			throw new ValidacaoCertificadoException("Certificado revogado!", cpve);
		} catch (Exception e) {
			throw new ValidacaoCertificadoException("Não foi possível recuperar a lista de certificados revogados do certificado", e);
		}
		log.info("Certificado não revogado");
	}

	private CertificateFactory getCertificateFactory() throws CertificateException {
		return CertificateFactory.getInstance("X.509");
	}

	/**
	 * Método que realiza o processamento do método abaixo do assinador desktop:
	 * 
	 * AbstractAssinador.recuperarListaCertificadosRevogados()
	 * 
	 * @throws ValidacaoCertificadoException
	 */
	private CRL[] recuperarListaCertificadosRevogados() throws ValidacaoCertificadoException {
		log.info("Recuperando lista de certificados revogados de toda a cadeia de certificação");
		Set<CRL> crls = new HashSet<CRL>();
		try {
			CertificateFactory certFactory = getCertificateFactory();
			for (Certificate cer : cadeiaCompleta) {
				CRL certificateCRL = recuperaCRLCertificado(cer, certFactory);
				crls.add(certificateCRL);
			}
			log.info("Lista completa recuperada com sucesso");
			return crls.toArray(new CRL[0]);
		} catch (Exception e) {
			throw new ValidacaoCertificadoException("Não foi possível recuperar a lista de certificados revogados do certificado", e);
		}
	}

	/**
	 * Método que realiza o processamento do método abaixo do assinador desktop:
	 * 
	 * AbstractAssinador.recuperaCRLCertificado()
	 * 
	 * @throws Exception
	 */
	private CRL recuperaCRLCertificado(Certificate certificado, CertificateFactory certFactory) throws Exception {
		X509Certificate x509 = (X509Certificate) certificado;
		List<String> urls = getPontoDistribuicaoCRL(x509);
		log.info(MessageFormat.format("Recuperando lista de certificados revogados para o certificado: {0}", certificado));
		byte[] bytesCert = revogador.recuperarListaCertificadosRevogados(urls);
		if (bytesCert == null) {
			throw new ValidacaoCertificadoException(MessageFormat.format("Não foi possível recuperar a lista de certificados revogados do certificado: {0}",
					certificado));
		}
		log.info(MessageFormat.format("Lista recuperada com sucesso para o certificado: {0}", certificado));
		return certFactory.generateCRL(new ByteArrayInputStream(bytesCert));

	}

	/**
	 * Método que realiza o processamento do método abaixo do assinador desktop:
	 * 
	 * AbstractAssinador.getPontoDistribuicaoCRL()
	 * 
	 * @throws IOException
	 * @throws ValidacaoCertificadoException
	 */
	@SuppressWarnings("deprecation")
	private List<String> getPontoDistribuicaoCRL(X509Certificate cert) throws IOException, ValidacaoCertificadoException {
		log.info("Recuperando ponto de distribuicao da LCR para o certificado");
		byte bytes[] = cert.getExtensionValue(X509Extensions.CRLDistributionPoints.getId());

		ASN1InputStream asn1is = new ASN1InputStream(new ByteArrayInputStream(bytes));
		ASN1OctetString asn1os = ASN1OctetString.getInstance(asn1is.readObject());

		asn1is = new ASN1InputStream(new ByteArrayInputStream(asn1os.getOctets()));

		DERObject derObject = asn1is.readObject();
		List<String> listaURLs = recuperarListaUrl(derObject);
		log.info("Ponto de distribuicao recuperado!");
		return listaURLs;
	}

	/**
	 * Método que realiza o processamento do método abaixo do assinador desktop:
	 * 
	 * AbstractAssinador.recuperarListaUrl()
	 * 
	 * @throws ValidacaoCertificadoException
	 */
	private List<String> recuperarListaUrl(DERObject derObject) throws ValidacaoCertificadoException {
		if (derObject instanceof DERSequence) {
			List<String> retorno = new LinkedList<String>();
			DERSequence seq = (DERSequence) derObject;
			Enumeration<?> enumeracao = seq.getObjects();

			while (enumeracao.hasMoreElements()) {
				DERObject nestedObj = (DERObject) enumeracao.nextElement();
				List<String> appo = recuperarListaUrl(nestedObj);

				if (appo != null) {
					retorno.addAll(appo);
				}
			}

			return retorno;
		}

		if (derObject instanceof DERTaggedObject) {
			DERTaggedObject derTag = (DERTaggedObject) derObject;

			if (derTag.isExplicit() && !derTag.isEmpty()) {
				DERObject nestedObj = derTag.getObject();
				List<String> retorno = recuperarListaUrl(nestedObj);

				return retorno;
			} else {
				if (derTag.getObject() instanceof DEROctetString) {
					DEROctetString derOct = (DEROctetString) derTag.getObject();
					String val = new String(derOct.getOctets());
					List<String> retorno = new LinkedList<String>();
					retorno.add(val);

					return retorno;
				} else {
					DERObject nestedObj = derTag.getObject();
					List<String> retorno = recuperarListaUrl(nestedObj);

					return retorno;
				}
			}
		}

		return null;
	}

	/**
	 * Método que realiza o processamento do método abaixo do assinador desktop:
	 * 
	 * AbstractAssinador.verificaCertificadoCadeiaICPBrasil()
	 * 
	 * @throws ValidacaoCertificadoException
	 */
	private void verificaCertificadoCadeiaICPBrasil() throws ValidacaoCertificadoException {
		log.info("Verificando se o certificado pertence a cadeia ICP-Brasil");
		try {
			Set<TrustAnchor> trustedAnchors = recuperaTrustedAnchorsICPBrasil();

			CertificateFactory cf = getCertificateFactory();
			CertPath cp = cf.generateCertPath(cadeiaCompleta);
			PKIXParameters params = new PKIXParameters(trustedAnchors);
			params.setRevocationEnabled(false);
			CertPathValidator cpv = CertPathValidator.getInstance(TYPE_PKIX, BOUNCYCASTLE_PROVIDER);
			cpv.validate(cp, params);
		} catch (CertPathValidatorException cpve) {
			throw new ValidacaoCertificadoException("Certificado não pertencente a cadeia da ICP-Brasil", cpve);
		} catch (Exception e) {
			throw new ValidacaoCertificadoException("Não foi possível validar a cadeia do certificado do usuário", e);
		}
		log.info("Cadeia do certificado válida");

	}

	/**
	 * Método que realiza o processamento do método abaixo do assinador desktop:
	 * 
	 * AbstractAssinador.recuperaTrustedAnchorsICPBrasil()
	 * 
	 * @throws ValidacaoCertificadoException
	 * @throws FileNotFoundException
	 * @throws CertificateException
	 * @throws NoSuchProviderException
	 */
	private Set<TrustAnchor> recuperaTrustedAnchorsICPBrasil() throws ValidacaoCertificadoException, FileNotFoundException, CertificateException, NoSuchProviderException {
		log.info("Carregando certificados da ICP-Brasil");
		List<InputStream> certificados = recuperaArquivosDosCertificadosICPBrasil();
		Set<TrustAnchor> trustedAnchors = new HashSet<TrustAnchor>();
		for (InputStream certificado : certificados) {
			TrustAnchor trustedCert = montaTrustAnchor(certificado);
			trustedAnchors.add(trustedCert);
		}
		return trustedAnchors;
	}

	/**
	 * Método que realiza o processamento do método abaixo do assinador desktop:
	 * 
	 * AbstractAssinador.recuperaArquivosDosCertificadosICPBrasil()
	 * 
	 * @throws ValidacaoCertificadoException
	 */
	private List<InputStream> recuperaArquivosDosCertificadosICPBrasil() throws ValidacaoCertificadoException {
		URLClassLoader classLoader = (URLClassLoader) this.getClass().getClassLoader();
		List<InputStream> certificados = new ArrayList<InputStream>();
		for (String certificadoIcpBrasil : CERTIFICADOS_ICP_BRASIL) {
			InputStream certificadoStream = classLoader.getResourceAsStream("certificados/" + certificadoIcpBrasil);
			if (certificadoStream != null) {
				certificados.add(certificadoStream);
			}
		}
		if (certificados.size() == 0) {
			throw new ValidacaoCertificadoException("Não foi possível localizar nenhum certificado raiz da ICP-Brasil!");
		}
		return certificados;
	}

	/**
	 * Método que realiza o processamento do método abaixo do assinador desktop:
	 * 
	 * AbstractAssinador.montaTrustAnchor()
	 * 
	 * @throws FileNotFoundException
	 * @throws CertificateException
	 * @throws NoSuchProviderException
	 */
	private TrustAnchor montaTrustAnchor(InputStream cert) throws FileNotFoundException, CertificateException, NoSuchProviderException {
		CertificateFactory fabricaCertificados = getCertificateFactory();
		X509Certificate certificadoICPBrasil = (X509Certificate) fabricaCertificados.generateCertificate(cert);
		return new TrustAnchor(certificadoICPBrasil, null);
	}

	public LinkedList<X509Certificate> getCadeiaCompleta() {
		return cadeiaCompleta;
	}

	public CRL[] getCrl() {
		return crl;
	}
	
}
