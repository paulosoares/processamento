package br.jus.stf.estf.decisao.documento.support.assinador.timestamp.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import br.jus.stf.estf.decisao.documento.support.assinador.timestamp.ServidorCarimbo;

public class CarimbadorUtil {
	private static final String CARIMBADORES_XML = "carimbadores.xml";

	private static List<ServidorCarimbo> servidores;

	public static List<ServidorCarimbo> getServidores() throws ParserConfigurationException, SAXException, IOException {
		if (servidores == null) {
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			InputStream is = CarimbadorUtil.class.getClassLoader().getResourceAsStream(CARIMBADORES_XML);
			CarimbadoresXMLHandler handler = new CarimbadoresXMLHandler();
			parser.parse(is, handler);
			servidores = handler.getServidores();
			if (servidores == null) {
				throw new IOException("Nenhum servidor encontrado");
			}
		}
		return servidores;
	}
}
