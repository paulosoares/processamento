package br.gov.stf.estf.expedicao.visao.vo;

import static br.gov.stf.estf.expedicao.visao.BeanListaRemessa.BARRA;

/**
 *
 * @author roberio.fernandes
 */
public class NumeroAnoVo {

	private final Long numero;
	private final Integer ano;

	public NumeroAnoVo(String numeros) {
		String[] dadosLista = numeros.split(BARRA);
		Long numeroAux = null;
		Integer anoAux = null;
		if (dadosLista.length == 2) {
			String numeroLista = dadosLista[0].trim();
			String anoLista = dadosLista[1].trim();
			String numeroListaNumero = numeroLista.replaceAll("[^0-9]+", "");
			String anoListaNumero = anoLista.replaceAll("[^0-9]+", "");
			if (numeroLista.equals(numeroListaNumero) && anoLista.equals(anoListaNumero)) {
				numeroAux = Long.parseLong(numeroLista);
				anoAux = Integer.parseInt(anoLista);
				if (numeroAux < 1 || anoAux < 1000 || anoAux > 9999) {
					numeroAux = null;
					anoAux = null;
				}
			}
		}
		numero = numeroAux;
		ano = anoAux;
	}

	public Long getNumero() {
		return numero;
	}

	public Integer getAno() {
		return ano;
	}

	@Override
	public String toString() {
		return numero + BARRA + ano;
	}
}