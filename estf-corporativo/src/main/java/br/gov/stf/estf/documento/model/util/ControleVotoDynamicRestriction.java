package br.gov.stf.estf.documento.model.util;

import java.util.Date;

import org.hibernate.criterion.Restrictions;

import br.gov.stf.estf.entidade.documento.TipoSituacaoTexto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.framework.model.util.DynamicRestriction;

public class ControleVotoDynamicRestriction extends DynamicRestriction {

	public static final String ALIAS_CONTROLE_VOTO = "cv";

	public void setDataSessao(Date dataSessao) {
		if (dataSessao != null) {
			addCondition(Restrictions.eq(ALIAS_CONTROLE_VOTO + ".dataSessao", dataSessao));
		}
	}

	public void setIdTexto(Long idTexto) {
		if (idTexto != null) {
			addCondition(Restrictions.eq(ALIAS_CONTROLE_VOTO + ".texto.id", idTexto));
		}
	}

	public void setTipoSituacaoTexto(TipoSituacaoTexto tipoSituacaoTexto) {
		if (tipoSituacaoTexto != null) {
			addCondition(Restrictions.eq(ALIAS_CONTROLE_VOTO +	".tipoSituacaoTexto", tipoSituacaoTexto));
		}

	}

	public void setTipoTexto(TipoTexto tipoTexto) {
		if (tipoTexto != null) {
			//addCondition(Restrictions.eq("t.tipoTexto", tipoTexto), ALIAS_CONTROLE_VOTO + ".texto", "t");
			addCondition(Restrictions.eq(ALIAS_CONTROLE_VOTO +	".tipoTexto", tipoTexto));
		}

	}
	
	public void setSequencialObjetoIncidente(Long sequencialObjetoIncidente) {
		if (sequencialObjetoIncidente != null) {
			addCondition(Restrictions.eq(ALIAS_CONTROLE_VOTO + ".objetoIncidente.id", sequencialObjetoIncidente));
		}
	}

	public void setCodigoDoMinistro(Long codigoDoMinistro, boolean incluirPresidencia) {
		if (codigoDoMinistro != null) {
			if (incluirPresidencia) {
				addCondition(Restrictions.in(ALIAS_CONTROLE_VOTO + ".ministro.id", new Long[] { codigoDoMinistro,
						Ministro.COD_MINISTRO_PRESIDENTE }));
			} else {
				addCondition(Restrictions.eq(ALIAS_CONTROLE_VOTO + ".ministro.id", codigoDoMinistro));
			}
		}

	}
}
