package br.jus.stf.estf.decisao;

import java.io.Serializable;
import java.util.Arrays;

public class ConfiguracaoTexto implements Serializable {
	private static final long serialVersionUID = -7537644353833764389L;
	
	private byte[] textoEstilo;
	private byte[] xmlMacro;
	private byte[] xmlAtalho;
	
	
	public byte[] getTextoEstilo() {
		return textoEstilo;
	}
	public void setTextoEstilo(byte[] textoEstilo) {
		if (textoEstilo != null) {
			this.textoEstilo = Arrays.copyOf(textoEstilo, textoEstilo.length);
		} else {
			this.textoEstilo = null;
		}
	}
	public byte[] getXmlMacro() {
		return xmlMacro;
	}
	public void setXmlMacro(byte[] xmlMacro) {
		if (xmlMacro != null) {
			this.xmlMacro = Arrays.copyOf(xmlMacro, xmlMacro.length);
		} else {
			this.xmlMacro = null;
		}
	}
	public byte[] getXmlAtalho() {
		return xmlAtalho;
	}
	public void setXmlAtalho(byte[] xmlAtalho) {
		if (xmlAtalho != null) {
			this.xmlAtalho = Arrays.copyOf(xmlAtalho, xmlAtalho.length);
		} else {
			this.xmlAtalho = null;
		}
	}
	
}
