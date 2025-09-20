package br.gov.stf.estf.documento.model.util;

import br.gov.stf.estf.entidade.documento.TipoSituacaoPeca;
import br.gov.stf.framework.model.util.query.DynamicQuery;
import br.gov.stf.framework.model.util.query.SQLCondition;
import br.gov.stf.framework.model.util.query.SQLConnective;

public class PecaProcessoEletronicoDoTextoDynamicQuery extends DynamicQuery {

	public PecaProcessoEletronicoDoTextoDynamicQuery() {
		insereCondicaoTextual("pe.id = ae.pecaProcessoEletronico.id", SQLConnective.AND);
		insereCondicaoTextual("doc.id = ae.documentoEletronicoView.id", SQLConnective.AND);
		insereCondicaoTextual("dt.documentoEletronicoView.id = doc.id", SQLConnective.AND);
		insereCondicaoTextual("tx.id = dt.texto.id", SQLConnective.AND);
	}

	public void setSequencialObjetoIncidente(Long sequencialObjetoIncidente) {
		if (sequencialObjetoIncidente != null) {
			insereCondicao("pe.objetoIncidente.id", sequencialObjetoIncidente);
		}
	}

	public void setSequencialArquivoEletronico(Long sequencialArquivoEletronico) {
		if (sequencialArquivoEletronico != null) {
			insereCondicao("tx.arquivoEletronico.id", sequencialArquivoEletronico);
		}
	}

	public void setRetirarPecasExcluidas(Boolean retirar) {
		if (retirar != null && retirar) {
			insereCondicao("pe.tipoSituacaoPeca", TipoSituacaoPeca.EXCLUIDA.getCodigo(), SQLCondition.DIFERENTE);
		}
	}

}
