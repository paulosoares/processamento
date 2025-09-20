package br.gov.stf.estf.assinatura.visao.util;

public enum TipoOrdenacao {

	ASCENDENTE((byte) 1), DESCENDENTE((byte) -1);

	private byte indice;

	TipoOrdenacao(byte indice) {
		this.indice = indice;
	}

	public byte getIndice() {
		return indice;
	}
}
