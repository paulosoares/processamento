package br.gov.stf.estf.processostf.model.util;

import java.util.Collection;

import org.hibernate.criterion.Restrictions;

import br.gov.stf.framework.model.util.DynamicRestriction;

public class AgendamentoDynamicRestriction extends DynamicRestriction {

	public static final String ALIAS_AGENDAMENTO = "ag";

	public void setSequencialObjetoIncidente(Long sequencialObjetoIncidente) {
		if (sequencialObjetoIncidente != null) {
			addCondition(Restrictions.eq(ALIAS_AGENDAMENTO + ".id.objetoIncidente", sequencialObjetoIncidente));
		}
	}

	public void setCodigoDoMinistro(Long codigoDoMinistro) {
		if (codigoDoMinistro != null) {
			addCondition(Restrictions.eq(ALIAS_AGENDAMENTO + ".ministro.id", codigoDoMinistro));
		}
	}

	public void setCodigoMateria(Integer codigoMateria) {
		if (codigoMateria != null) {
			addCondition(Restrictions.eq(ALIAS_AGENDAMENTO + ".id.codigoMateria", codigoMateria));
		}
	}

	public void setSequenciaisObjetosIncidentes(Collection<Long> objetosIncidentes) {
		if (objetosIncidentes != null && objetosIncidentes.size() > 0) {
			addCondition(Restrictions.in(ALIAS_AGENDAMENTO + ".id.objetoIncidente", objetosIncidentes));
		}
	}

}
