package br.gov.stf.estf.documento.model.util;

import org.hibernate.criterion.Restrictions;

import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.framework.model.util.DynamicRestriction;

public class DadosDoTextoDynamicRestriction extends DynamicRestriction {

	public static final String ALIAS_TEXTOS = "t";

	public void setTipoDeTexto(TipoTexto tipoTexto) {
		if (tipoTexto != null) {
			addCondition(Restrictions.eq(ALIAS_TEXTOS + ".tipoTexto", tipoTexto));
		}
	}

	public void setSequencialObjetoIncidente(Long sequencialObjetoIncidente) {
		if (sequencialObjetoIncidente != null) {
			addCondition(Restrictions.eq(ALIAS_TEXTOS + ".objetoIncidente.id", sequencialObjetoIncidente));
		}
	}

	public void setCodigoDoMinistro(Long codigoDoMinistro, boolean inserirPesquisaNaPresidencia) {
		if (codigoDoMinistro != null) {
			if (inserirPesquisaNaPresidencia) {
				addCondition(Restrictions.in(ALIAS_TEXTOS + ".ministro.id", new Long[] { codigoDoMinistro,
						Ministro.COD_MINISTRO_PRESIDENTE }));
			} else {
				addCondition(Restrictions.eq(ALIAS_TEXTOS + ".ministro.id", codigoDoMinistro));
			}
		}
	}

	public void setSequencialDoArquivoDiferente(Long sequencialDoArquivo) {
		if (sequencialDoArquivo != null) {
			addCondition(Restrictions.ne(ALIAS_TEXTOS + ".arquivoEletronico.id", sequencialDoArquivo));
		}
	}

	public void setTiposDeTextoParaExcluir(TipoTexto... tiposDeTextoParaExcluir) {
		if (tiposDeTextoParaExcluir != null && tiposDeTextoParaExcluir.length > 0) {
			addCondition(Restrictions.not(Restrictions.in(ALIAS_TEXTOS + ".tipoTexto", tiposDeTextoParaExcluir)));
		}
	}

}
