package br.gov.stf.estf.publicacao.model.util;

import org.hibernate.criterion.Restrictions;

import br.gov.stf.framework.model.util.DynamicRestriction;

public class ProcessoEmPautaDynamicRestriction extends DynamicRestriction {

	public static final String ALIAS_PROCESSO_PUBLICADO = "pp";
	
	private Boolean recuperarOcultos;

	public void setSequencialObjetoIncidente(Long sequencialObjetoIncidente) {
		if (sequencialObjetoIncidente != null) {
			addCondition(Restrictions.eq(ALIAS_PROCESSO_PUBLICADO + ".objetoIncidente.id", sequencialObjetoIncidente));
		}
	}

	public void setCodigosDosCapitulos(Integer... capitulos) {
		if (capitulos != null && capitulos.length > 0) {
			addCondition(Restrictions.in(ALIAS_PROCESSO_PUBLICADO + ".codigoCapitulo", capitulos));
		}
	}

	public void setCodigoMateria(Integer codigoMateria) {
		if (codigoMateria != null) {
			addCondition(Restrictions.eq(ALIAS_PROCESSO_PUBLICADO + ".codigoMateria", codigoMateria));
		}
	}

	public Boolean getRecuperarOcultos() {
		return recuperarOcultos;
	}

	public void setRecuperarOcultos(Boolean recuperarOcultos) {
		this.recuperarOcultos = recuperarOcultos;
	}
}
