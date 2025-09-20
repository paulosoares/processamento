package br.gov.stf.estf.documento.model.util;

import org.hibernate.criterion.Restrictions;

import br.gov.stf.estf.entidade.documento.TipoSituacaoPeca;
import br.gov.stf.framework.model.util.DynamicRestriction;

public class PecaProcessoEletronicoDynamicRestriction extends DynamicRestriction {

	public static final String ALIAS_PECA_PROCESSO_ELETRONICO = "ppe";

	public void setSequencialObjetoIncidente(Long sequencialObjetoIncidente) {
		if (sequencialObjetoIncidente != null && sequencialObjetoIncidente.intValue() > 0) {
			addCondition(Restrictions.eq(ALIAS_PECA_PROCESSO_ELETRONICO + ".objetoIncidente.id", sequencialObjetoIncidente));
		}
	}

	public void setTipoPecaProcesso(Long tipoPecaProcesso) {
		if (tipoPecaProcesso != null && tipoPecaProcesso.intValue() > 0) {
			addCondition(Restrictions.eq(ALIAS_PECA_PROCESSO_ELETRONICO + ".tipoPecaProcessoEletronico", tipoPecaProcesso));
		}
	}

	public void setSituacaoPeca(TipoSituacaoPeca... situacoes) {
		if (situacoes != null) {
			addCondition(Restrictions.in(ALIAS_PECA_PROCESSO_ELETRONICO + ".tipoSituacaoPeca", situacoes));
		}
	}

	public void setPossuiArquivoEletronico(Boolean possuiArquivoEletronico) {
		if (possuiArquivoEletronico != null) {
			if (possuiArquivoEletronico) {
				addCondition(Restrictions.isNotEmpty(ALIAS_PECA_PROCESSO_ELETRONICO + ".arquivoProcessoEletronico"));
			} else {
				addCondition(Restrictions.isEmpty(ALIAS_PECA_PROCESSO_ELETRONICO + ".arquivoProcessoEletronico"));
			}
		}
	}
}
