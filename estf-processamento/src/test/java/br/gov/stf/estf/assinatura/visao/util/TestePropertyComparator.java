package br.gov.stf.estf.assinatura.visao.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.time.StopWatch;
import org.junit.Ignore;

@Ignore
public class TestePropertyComparator {

	public static void main(String[] args) {
		List<ObjetoTeste> lista = new ArrayList<ObjetoTeste>();
		final int QUANTIDADE_ELEMENTOS = 500;

		System.out.println("Adicionando " + QUANTIDADE_ELEMENTOS + " elementos à lista");
		for (int i = 0; i < QUANTIDADE_ELEMENTOS; i++) {
			lista.add(new ObjetoTeste("Objeto " + RandomUtils.nextInt(QUANTIDADE_ELEMENTOS)));
		}

		System.out.println("Lista antes da ordenação: " + lista);

		StopWatch cronometro = new StopWatch();
		cronometro.start();
		Collections.sort(lista, new PropertyComparator<ObjetoTeste>(TipoOrdenacao.DESCENDENTE, "nome"));
		cronometro.stop();

		System.out.println("Lista após ordenação: " + lista);
		System.out.println("Tempo gasto para ordenar os " + lista.size() + " elementos: " + cronometro.getTime() + " milisegundos");
	}
}