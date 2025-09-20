package br.gov.stf.estf.assinatura.visao.util;

import java.util.Comparator;

import br.gov.stf.estf.assinatura.visao.jsf.beans.ComunicacaoDocumento;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

/**
 * Comparador de CheckableDataTableRowWrapper que empacotem objetos ComunicacaoDocumento. A comparação será feita através da identificação processual da comunicação associada ao
 * objeto ComunicacaoDocumento.
 * 
 * @author thiago.miranda
 * @see Comunicacao#getIdentificacaoProcessual()
 */
public class IdentificaoProcessualComunicacaoTableRowComparator implements Comparator<CheckableDataTableRowWrapper> {

	private static final byte SIGLA_CLASSE_PROCESSO = 0;
	private static final byte NUMERO_PROCESSO = 1;
	private static final byte SIGLA_RECURSO = 2;

	@Override
	public int compare(CheckableDataTableRowWrapper comunicacaoTableRow1, CheckableDataTableRowWrapper comunicacaoTableRow2) {
		try {
			String idProcessual1 = ((ComunicacaoDocumento) comunicacaoTableRow1.getWrappedObject()).getComunicacao().getIdentificacaoProcessual();
			String idProcessual2 = ((ComunicacaoDocumento) comunicacaoTableRow2.getWrappedObject()).getComunicacao().getIdentificacaoProcessual();
	
			return compareIdsProcessuais(idProcessual1, idProcessual2);
		} catch (Exception e) {
			// TODO: handle exception
		}
				
		return 0;
	}

	private int compareIdsProcessuais(String idProcessual1, String idProcessual2) {
		String[] tokens1 = idProcessual1.split(" ");
		String[] tokens2 = idProcessual2.split(" ");
		int quantidadeTokens1 = tokens1.length;
		int quantidadeTokens2 = tokens2.length;

		int comparacao = tokens1[SIGLA_CLASSE_PROCESSO].compareTo(tokens2[SIGLA_CLASSE_PROCESSO]);

		if (comparacao == 0) {
			// mesma classe
			int numeroProcesso1 = Integer.parseInt(tokens1[NUMERO_PROCESSO].replaceAll("\\.", ""));
			int numeroProcesso2 = Integer.parseInt(tokens2[NUMERO_PROCESSO].replaceAll("\\.", ""));
			comparacao = numeroProcesso1 - numeroProcesso2;

			if (comparacao == 0) {
				// mesmo processo
				if (quantidadeTokens1 == 3 && quantidadeTokens2 == 3) {
					comparacao = tokens1[SIGLA_RECURSO].compareTo(tokens2[SIGLA_RECURSO]);
				} else if (quantidadeTokens1 == 3) {
					// recursos vem por último
					return 1;
				}
			}
		}

		return comparacao;
	}
}
