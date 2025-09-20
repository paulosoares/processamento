package br.gov.stf.estf.documento.model.util;

import br.gov.stf.estf.documento.model.service.enums.TipoDeAcessoDoDocumento;
import br.gov.stf.framework.model.util.query.DynamicQuery;
import br.gov.stf.framework.model.util.query.SQLCondition;
import br.gov.stf.framework.model.util.query.SQLConnective;

public class PecaProcessoEletronicoPendenteVisualizacaoDynamicQuery extends DynamicQuery {

	public PecaProcessoEletronicoPendenteVisualizacaoDynamicQuery() {
		insereCondicaoTextual("ppe.id = ape.pecaProcessoEletronico.id", SQLConnective.AND);
		insereCondicaoTextual("doc.id = ape.documentoEletronicoView.id", SQLConnective.AND);
		insereCondicao("doc.tipoAcesso", TipoDeAcessoDoDocumento.INTERNO.getChave(), SQLCondition.IGUAL);
	}

	public void setSequencialObjetoIncidente(Long sequencialObjetoIncidente) {
		if (sequencialObjetoIncidente != null) {
			insereCondicao("ppe.objetoIncidente.id", sequencialObjetoIncidente);
		}
	}

}



