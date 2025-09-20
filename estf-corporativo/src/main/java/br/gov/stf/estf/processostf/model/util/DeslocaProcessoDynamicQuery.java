package br.gov.stf.estf.processostf.model.util;

import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.framework.model.util.query.DynamicQuery;

public class DeslocaProcessoDynamicQuery extends DynamicQuery {

	public static final String ALIAS_DESLOCA_PROCESSO = "dp";

	public void setNumeroProcesso(Long numeroProcesso) {
		if (numeroProcesso != null && numeroProcesso != 0L) {
			insereCondicao(ALIAS_DESLOCA_PROCESSO + ".id.processo.numeroProcessual", numeroProcesso);
		}
	}

	public void setSiglaClasseProcessual(String siglaClasseProcessual) {
		if (siglaClasseProcessual != null && !siglaClasseProcessual.equals("")) {
			insereCondicao(ALIAS_DESLOCA_PROCESSO + ".id.processo.siglaClasseProcessual", siglaClasseProcessual);
		}
	}

	public void setUltimoDeslocamento(Boolean ultimoDeslocamento) {
		if (ultimoDeslocamento != null) {
			String condicao = "S";
			if (!ultimoDeslocamento) {
				condicao = "N";
			}
			insereCondicao(ALIAS_DESLOCA_PROCESSO + ".ultimoDeslocamento", condicao);
		}
	}

	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		if (objetoIncidente != null && objetoIncidente.getPrincipal() != null) {
			insereCondicao(ALIAS_DESLOCA_PROCESSO + ".id.processo.principal.id", objetoIncidente.getPrincipal().getId());
		}
	}
}
