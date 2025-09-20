package br.gov.stf.estf.entidade.processostf.enuns;

import br.gov.stf.estf.entidade.julgamento.TipoSituacaoProcessoSessao;

/**
 * Enum para representar um conceito genérico de situação de julgamento com
 * valores binários de Julgado e Não Julgado.
 * 
 * Esse conceito é mais genérico do que TipoSituacaoProcessoSessao e ListaJulgamento.julgado,
 * pois englobará os anteriores mas também outras situações em que o incidente é considerado como
 * julgado.
 * 
 * Para TipoSituacaoProcessoSessao, "não julgado" e "suspenso" corresponderá a 
 * SituacaoJulgamentoJulgadoOuNao.NAO_JULGADO, equanto "julgado" corresponderá a
 * SituacaoJulgamentoJulgadoOuNao.JULGADO.
 * 
 * No caso de incidentes julgados via lista de julgamento, será julgado se a lista correspondente
 * tiver sido julgada.
 * 
 * @author Tomas.Godoi
 *
 */
public enum SituacaoIncidenteJulgadoOuNao {

	JULGADO("J", "Julgado"), NAO_JULGADO("NJ", "Não Julgado");

	private String sigla;

	private String descricao;

	private SituacaoIncidenteJulgadoOuNao(String sigla, String descricao) {
		this.sigla = sigla;
		this.descricao = descricao;
	}

	public String getSigla() {
		return sigla;
	}

	public String getDescricao() {
		return descricao;
	}

	public static TipoSituacaoProcessoSessao valueOfSigla(String sigla) {
		for (TipoSituacaoProcessoSessao situacao : TipoSituacaoProcessoSessao.values()) {
			if (situacao.getSigla().equals(sigla)) {
				return situacao;
			}
		}

		return null;
	}

}
