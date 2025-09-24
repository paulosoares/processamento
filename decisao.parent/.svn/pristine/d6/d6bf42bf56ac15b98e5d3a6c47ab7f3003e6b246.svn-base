package br.jus.stf.estf.decisao.handlers;

import org.apache.commons.lang.StringUtils;

public enum AlinhamentoParagrafo {

	CENTRALIZADO("center"), JUSTIFICADO("justify"), ESQUERDA("start"), DIREITA("end");
	private AlinhamentoParagrafo(String chaveAlinhamento) {
		this.chaveAlinhamento = chaveAlinhamento;
	}

	private final String chaveAlinhamento;

	public String getChaveAlinhamento() {
		return chaveAlinhamento;
	}

	public static AlinhamentoParagrafo getAlinhamento(String chave) {
		for (AlinhamentoParagrafo alinhamento : values()) {
			if (StringUtils.equals(alinhamento.chaveAlinhamento, chave)) {
				return alinhamento;
			}
		}
		return null;
	}

}