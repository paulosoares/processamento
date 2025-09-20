package br.gov.stf.estf.publicacao.model.util;

import java.util.Date;

import org.hibernate.criterion.Restrictions;

import br.gov.stf.framework.model.util.DynamicRestriction;

public class ConteudoPublicacaoDynamicRestriction extends DynamicRestriction {

	public static final String ALIAS_CONTEUDO_PUBLICACAO = "cp";

	public void setCodigoCapitulo(Integer codigoCapitulo) {
		if (codigoCapitulo != null) {
			addCondition(Restrictions.eq(ALIAS_CONTEUDO_PUBLICACAO + ".codigoCapitulo", codigoCapitulo));
		}
	}

	public void setCodigoMateria(Integer codigoMateria) {
		if (codigoMateria != null) {
			addCondition(Restrictions.eq(ALIAS_CONTEUDO_PUBLICACAO + ".codigoMateria", codigoMateria));
		}
	}

	public void setDataCriacao(Date dataCriacao) {
		if (dataCriacao != null) {
			addCondition(Restrictions.eq(ALIAS_CONTEUDO_PUBLICACAO + ".dataCriacao", dataCriacao));
		}
	}

}
