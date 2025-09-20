package br.gov.stf.estf.publicacao.model.util;

import org.hibernate.criterion.Restrictions;

import br.gov.stf.framework.model.util.DynamicRestriction;
import br.gov.stf.framework.util.SearchData;

public class DadosDePublicacaoDynamicRestriction extends DynamicRestriction {

	public static final String ALIAS_PROCESSO_PUBLICADO = "pp";
	public static final String ALIAS_CONTEUDO_PUBLICACAO = "cp";

	public void setClasseDoProcesso(String classeDoProcesso) {
		if (SearchData.stringNotEmpty(classeDoProcesso)) {
			addCondition(Restrictions.eq(ALIAS_PROCESSO_PUBLICADO
					+ ".processo.id.siglaClasseProcessual", classeDoProcesso));
		}
	}

	public void setNumeroDoProcesso(Long numeroDoProcesso) {
		if (numeroDoProcesso != null) {
			addCondition(Restrictions.eq(ALIAS_PROCESSO_PUBLICADO
					+ ".processo.id.numeroProcessual", numeroDoProcesso));
		}
	}

	public void setCodigoDoRecurso(Long codigoDoRecurso) {
		if (codigoDoRecurso != null) {
			addCondition(Restrictions.eq(ALIAS_PROCESSO_PUBLICADO
					+ ".tipoRecurso.id", codigoDoRecurso));
		}
	}

	public void setTipoDeJulgamento(String tipoDeJulgamento) {
		if (tipoDeJulgamento != null) {
			addCondition(Restrictions.eq(ALIAS_PROCESSO_PUBLICADO
					+ ".tipoJulgamento.id", tipoDeJulgamento));
		}
	}

	public void setSequencialDoArquivoEletronico(
			Long sequencialDoArquivoEletronico) {
		if (sequencialDoArquivoEletronico != null) {
			addCondition(Restrictions.eq(ALIAS_PROCESSO_PUBLICADO
					+ ".arquivoEletronicoTexto.id",
					sequencialDoArquivoEletronico));
		}
	}

	public void setMateriaCodigoCapitulo(Integer codigoCapituloMateria) {
		if (codigoCapituloMateria != null) {
			addCondition(Restrictions.eq(ALIAS_CONTEUDO_PUBLICACAO
					+ ".codigoCapitulo", codigoCapituloMateria),
					"conteudoPublicacao", ALIAS_CONTEUDO_PUBLICACAO);
		}
	}

	public void setProcessoCodigoCapitulo(Integer codigoCapituloProcesso) {
		if (codigoCapituloProcesso != null) {
			addCondition(Restrictions.eq(ALIAS_PROCESSO_PUBLICADO
					+ ".codigoCapitulo", codigoCapituloProcesso));
		}

	}

}
