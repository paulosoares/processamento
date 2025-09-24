package br.jus.stf.estf.decisao.documento.support.assinador.timestamp.config;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.jus.stf.estf.decisao.documento.support.assinador.timestamp.ServidorCarimbo;

public class CarimbadoresXMLHandler extends DefaultHandler {
	private static final String TAG_SERVIDOR = "servidor";
	private static final String ATRIBUTO_NOME_SERVIDOR = "nome";
	private static final String ATRIBUTO_URL_SERVIDOR = "url";

	private List<ServidorCarimbo> servidores = null;

	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		if (TAG_SERVIDOR.equals(name)) {
			String nome = attributes.getValue(ATRIBUTO_NOME_SERVIDOR);
			String url = attributes.getValue(ATRIBUTO_URL_SERVIDOR);
			if (servidores == null) {
				servidores = new ArrayList<ServidorCarimbo>();
			}
			servidores.add(new ServidorCarimbo(nome, url));
		}
	}

	public List<ServidorCarimbo> getServidores() {
		return servidores;
	}
}
