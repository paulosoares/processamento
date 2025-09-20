package br.gov.stf.estf.publicacao.model.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import br.gov.stf.estf.publicacao.model.util.Partes.Parte;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Ministros", propOrder = { "ministro" })
public class Ministros {

	@XmlElement(required = true)
	protected List<Ministros.Ministro> ministro;

	public List<Ministros.Ministro> getMinistro() {
		if (ministro == null) {
			ministro = new ArrayList<Ministros.Ministro>();
		}
		return this.ministro;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "")
	public static class Ministro extends Parte {

	}

}
