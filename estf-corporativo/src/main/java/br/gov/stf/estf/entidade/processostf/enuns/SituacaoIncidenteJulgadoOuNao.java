package br.gov.stf.estf.entidade.processostf.enuns;

import br.gov.stf.estf.entidade.julgamento.TipoSituacaoProcessoSessao;

/**
 * Enum para representar um conceito gen�rico de situa��o de julgamento com
 * valores bin�rios de Julgado e N�o Julgado.
 * 
 * Esse conceito � mais gen�rico do que TipoSituacaoProcessoSessao e ListaJulgamento.julgado,
 * pois englobar� os anteriores mas tamb�m outras situa��es em que o incidente � considerado como
 * julgado.
 * 
 * Para TipoSituacaoProcessoSessao, "n�o julgado" e "suspenso" corresponder� a 
 * SituacaoJulgamentoJulgadoOuNao.NAO_JULGADO, equanto "julgado" corresponder� a
 * SituacaoJulgamentoJulgadoOuNao.JULGADO.
 * 
 * No caso de incidentes julgados via lista de julgamento, ser� julgado se a lista correspondente
 * tiver sido julgada.
 * 
 * @author Tomas.Godoi
 *
 */
public enum SituacaoIncidenteJulgadoOuNao {

	JULGADO("J", "Julgado"), NAO_JULGADO("NJ", "N�o Julgado");

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
