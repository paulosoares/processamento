package br.jus.stf.estf.decisao.documento.support.assinador.timestamp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import br.jus.stf.estf.decisao.documento.support.assinador.exception.AssinaturaExternaException;
import br.jus.stf.estf.decisao.documento.support.assinador.timestamp.config.CarimbadorUtil;

import com.itextpdf.text.pdf.PdfPKCS7;
import com.itextpdf.text.pdf.TSAClient;

public class STFTSAClient implements TSAClient {

	private static final Logger log = Logger.getLogger(STFTSAClient.class);

	private GeradorSelo geradorSelo;
	private List<ServidorCarimbo> servidores;

	public static STFTSAClient buildTSAClient() {
		try {
			STFTSAClient tsaClient = new STFTSAClient();
			return tsaClient;
		} catch (AssinaturaExternaException e) {
			log.error("Erro ao construir o TSAClient.", e);
			return null; // Retorna null, pois não conseguiu criar o TSAClient.
		}
	}

	private STFTSAClient() throws AssinaturaExternaException {
		geradorSelo = new GeradorSelo();
		try {
			servidores = detectarServidoresCompativeis(CarimbadorUtil.getServidores());
		} catch (ParserConfigurationException e) {
			throw new AssinaturaExternaException("Erro ao carregar servidores de carimbo de tempo.", e);
		} catch (SAXException e) {
			throw new AssinaturaExternaException("Erro ao carregar servidores de carimbo de tempo.", e);
		} catch (IOException e) {
			throw new AssinaturaExternaException("Erro ao carregar servidores de carimbo de tempo.", e);
		}
	}

	private List<ServidorCarimbo> detectarServidoresCompativeis(List<ServidorCarimbo> servidores) throws AssinaturaExternaException {
		List<ServidorCarimbo> servs = new ArrayList<ServidorCarimbo>();
		for (ServidorCarimbo sc : servidores) {
			if (sc.getNome().equals("BRY")) {
				servs.add(sc);
			}
		}
		if (servs.size() == 0) {
			throw new AssinaturaExternaException("Não foi encontrada carimbadora compatível.");
		} else {
			return servs;
		}
	}

	@Override
	public int getTokenSizeEstimate() {
		return 4096;
	}

	@Override
	public byte[] getTimeStampToken(PdfPKCS7 caller, byte[] imprint) throws Exception {
		for (ServidorCarimbo sc : servidores) {
			try {
				byte[] carimbo = geradorSelo.gerarCarimboTempo(sc, imprint);
				return carimbo;
			} catch (Exception e) {
				log.error("Erro ao carimbar.", e);
			}
		}
		return null;
	}

}
