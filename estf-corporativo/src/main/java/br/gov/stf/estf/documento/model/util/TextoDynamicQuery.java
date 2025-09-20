package br.gov.stf.estf.documento.model.util;

import java.util.ArrayList;

import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.framework.model.util.query.DynamicQuery;
import br.gov.stf.framework.model.util.query.SQLConnective;

public class TextoDynamicQuery extends DynamicQuery {

	public static final String ALIAS_TEXTO = "t";
	public static final String ALIAS_DOCUMENTO_TEXTO = "dt";
	private static final String ALIAS_DESLOCA_PROCESSO = "dp";

	public void setIdObjetoIncidente(Long idObjetoIncidente) {
		if (idObjetoIncidente != null) {
			insereCondicao(ALIAS_TEXTO + ".objetoIncidente.id", idObjetoIncidente);
		}
	}

	public void setTipoTexto(TipoTexto tipoTexto) {
		if (tipoTexto != null) {
			insereCondicao(ALIAS_TEXTO + ".tipoTexto", tipoTexto.getCodigo());
		}
	}

	public void setTipoDocumentoTexto(Long tipoDocumentoTexto) {
		if (tipoDocumentoTexto != null) {
			registraJoinDocumentoTexto();
			insereCondicao(ALIAS_DOCUMENTO_TEXTO + ".tipoDocumentoTexto", tipoDocumentoTexto);
		}
	}

	private void registraJoinDocumentoTexto() {
		if (!getJoinsUtilizados().containsKey(ALIAS_DOCUMENTO_TEXTO)) {
			registraJoin(ALIAS_DOCUMENTO_TEXTO, DocumentoTexto.class, ALIAS_TEXTO + ".id = " + ALIAS_DOCUMENTO_TEXTO
					+ ".texto.id");
		}
	}

	public void setTipoSituacaoDocumento(TipoSituacaoDocumento... tiposSituacaoDocumento) {
		ArrayList<Long> ids = new ArrayList<Long>();
		for (TipoSituacaoDocumento tipoSituacaoDocumento : tiposSituacaoDocumento) {
			ids.add(tipoSituacaoDocumento.getCodigo());
		}
		if (tiposSituacaoDocumento != null && tiposSituacaoDocumento.length > 0) {
			registraJoinDocumentoTexto();
			insereCondicaoIn(ALIAS_DOCUMENTO_TEXTO + ".tipoSituacaoDocumento", SQLConnective.AND, ids.toArray());
		}
	}

	public void setOrgaoDestino(Long codigoOrgaoDestino) {
		if (codigoOrgaoDestino != null) {
			registraJoinDeslocaProcesso();
			insereCondicao(ALIAS_DESLOCA_PROCESSO + ".codigoOrgaoDestino", codigoOrgaoDestino);
			insereCondicao(ALIAS_DESLOCA_PROCESSO + ".ultimoDeslocamento", 'S');
		}

	}

	private void registraJoinDeslocaProcesso() {
		registraJoin(ALIAS_DESLOCA_PROCESSO, DeslocaProcesso.class, ALIAS_TEXTO + ".objetoIncidente.principal.id="
				+ ALIAS_DESLOCA_PROCESSO + ".id.processo.id");
	}

}
