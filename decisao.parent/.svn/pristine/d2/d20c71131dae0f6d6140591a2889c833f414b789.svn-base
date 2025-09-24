package br.jus.stf.estf.decisao.documento.support.assinador;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CRL;
import java.security.cert.CRLException;
import java.security.cert.Certificate;
import java.security.cert.X509CRL;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.TSAClient;

import br.jus.stf.assinadorweb.api.carimbo.Carimbo;
import br.jus.stf.assinadorweb.api.exception.AssinadorException;
import br.jus.stf.assinadorweb.config.STFAssinadorConfig;
import br.jus.stf.assinadorweb.config.STFAssinadorConfigBuilder;
import br.jus.stf.assinadorweb.web.carimbo.CarimbadorSerproServidor;
import br.jus.stf.estf.decisao.documento.support.assinador.exception.AssinaturaExternaException;
import br.jus.stf.estf.decisao.documento.support.assinador.timestamp.STFTSAClient;

public abstract class AssinadorPorPartes {

	protected Log log = Logging.getLog(getClass());

	private static final String EXTENSAO_PDF = ".pdf";

	protected boolean hasTSA;
	protected boolean hasOCSP;

	public AssinadorPorPartes(boolean hasTSA) {
		this.hasTSA = hasTSA;
		this.hasOCSP = false;
	}

	protected abstract byte[] preGerarHashes(Certificate[] cadeia, CRL[] crls, ContextoAssinatura ca, PdfStamper stamper, PdfSignatureAppearance appearance)
			throws IOException, DocumentException, AssinaturaExternaException;

	public abstract byte[] prepararHashParaAssinaturaExterna(byte[] dataToSign);

	public byte[] preAssinar(Certificate[] cadeia, CRL[] crls, byte[] pdf, String reason, ContextoAssinatura ca) throws AssinaturaExternaException {
		try {
			PdfReader reader = new PdfReader(pdf);
			File arquivoTemporario = criaArquivoTemporarioParaPdfAssinado(ca);

			PdfStamper stamper = PdfStamper.createSignature(reader, null, '\0', arquivoTemporario);
			PdfSignatureAppearance appearance = stamper.getSignatureAppearance();

			appearance.setSignDate(Calendar.getInstance());
			
			appearance.setReason(reason);

			byte[] hashParaAssinar = preGerarHashes(cadeia, crls, ca, stamper, appearance);

			return hashParaAssinar;
		} catch (IOException e) {
			throw new AssinaturaExternaException("Erro ao ler PDF.", e);
		} catch (DocumentException e) {
			throw new AssinaturaExternaException("Erro ao carregar PDF.", e);
		}
	}

	protected abstract void posAssinarImpl(ContextoAssinatura ca, PdfSignatureAppearance appearance, byte[] primeiroHash, String assinatura)
			throws AssinaturaExternaException;

	public byte[] posAssinar(ContextoAssinatura ca, String assinatura) throws AssinaturaExternaException {
		InputStream is = null;
		try {
			PdfSignatureAppearance appearance = ca.getAppearance();
			byte primeiroHash[] = ca.getPrimeiroHash();

			posAssinarImpl(ca, appearance, primeiroHash, assinatura);
			is = new FileInputStream(appearance.getTempFile());
			byte[] pdfAssinado = IOUtils.toByteArray(is);

			return pdfAssinado;
		} catch (IOException e) {
			throw new AssinaturaExternaException("Erro ao finalizar montagem do PDF.", e);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	/**
	 * Extrai a assinatura mais recente do Pdf, caso exista mais de uma.
	 * 
	 * @param pdfAssinado
	 * @return
	 * @throws AssinadorException
	 */
	public byte[] extrairAssinatura(byte[] pdfAssinado) throws AssinaturaExternaException {
		log.info("Extraindo assinatura do PDF");
		try {
			AcroFields campos = new PdfReader(pdfAssinado).getAcroFields();
			byte[] assinatura = identificaAssinaturaMaisRecente(campos);
			if (assinatura.length == 0) {
				throw new Exception("O conteúdo da assinatura está vazio!");
			}
			log.info("Assinatura encontrada: " + assinatura.length + " bytes");
			return assinatura;
		} catch (Exception e) {
			throw new AssinaturaExternaException("Erro ao extrair assinatura do PDF assinado.", e);
		}
	}

	private byte[] identificaAssinaturaMaisRecente(AcroFields campos) {
		byte[] assinatura = null;
		PdfString dataEscolhida = null;
		List<String> nomesCampos = campos.getSignatureNames();
		for (String idAssinatura : nomesCampos) {
			log.info("Assinatura encontrada: " + idAssinatura);
			PdfDictionary dicionarioPDF = campos.getSignatureDictionary(idAssinatura);
			// O item PdfName.M traz a data em que o documento foi assinado
			PdfString dataAssinatura = (PdfString) dicionarioPDF.get(PdfName.M);
			if (isDataEscolhidaMenorQueAssinatura(dataEscolhida, dataAssinatura)) {
				dataEscolhida = dataAssinatura;
				assinatura = recuperaDadosDaAssinatura(dicionarioPDF);
			}
		}
		return assinatura;
	}

	private byte[] recuperaDadosDaAssinatura(PdfDictionary dicionarioPDF) {
		PdfString conteudo = (PdfString) dicionarioPDF.get(PdfName.CONTENTS);
		return conteudo.getBytes();
	}

	private boolean isDataEscolhidaMenorQueAssinatura(PdfString dataEscolhida, PdfString dataAssinatura) {
		log.info("Data da assinatura: " + dataAssinatura.toString());
		if (dataEscolhida == null) {
			return true;
		}
		return dataEscolhida.toString().compareTo(dataAssinatura.toString()) < 0;
	}

	public Carimbo carimbar(byte[] assinatura) throws AssinaturaExternaException {
		try {
			STFAssinadorConfig stfAssinadorConfig = STFAssinadorConfigBuilder.build();
			
			String servico = stfAssinadorConfig.getServidorCarimbadorSerpro();
			
			CarimbadorSerproServidor carimbador = new CarimbadorSerproServidor(servico);
			log.info("Recuperando carimbo de tempo");
			Carimbo carimbo = carimbador.carimbar(assinatura);
			log.info("Pdf carimbado: " + carimbo.getPdfCarimbado().length + " bytes");
			return carimbo;
		} catch (Exception e) {
			throw new AssinaturaExternaException("Erro ao carimbar assinatura.", e);
		}
	}

	private File criaArquivoTemporarioParaPdfAssinado(ContextoAssinatura ca) throws AssinaturaExternaException {
		try {
			File arquivoTemporario = File.createTempFile(ca.getIdContexto(), EXTENSAO_PDF);
			return arquivoTemporario;
		} catch (IOException e) {
			throw new AssinaturaExternaException("Eror ao criar arquivo temporário para assinatura.", e);
		}
	}

	protected int calcularTamanhoEstimado(CRL[] crls) throws CRLException {
		int tamanhoEstimado = 8192;

		if (crls != null && crls.length > 0) {
			for (CRL crl : crls) {
				X509CRL xCrl = (X509CRL) crl;
				tamanhoEstimado += xCrl.getEncoded().length + 10;
			}
		}

		if (hasOCSP) {
			tamanhoEstimado += 4192;
		}

		if (hasTSA) {
			tamanhoEstimado += 4192;
		}

		return tamanhoEstimado;
	}

	protected TSAClient getTSAClient() {
		if (hasTSA) {
			return getSTFTSAClient();
		} else {
			return null;
		}
	}

	private TSAClient getSTFTSAClient() {
		TSAClient tsaClient = STFTSAClient.buildTSAClient();
		return tsaClient;
	}

}
