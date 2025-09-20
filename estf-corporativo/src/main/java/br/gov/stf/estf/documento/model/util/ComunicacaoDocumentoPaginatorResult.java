package br.gov.stf.estf.documento.model.util;

import java.util.List;

public class ComunicacaoDocumentoPaginatorResult {

	private List<ComunicacaoDocumentoResult> lista;
	private Integer totalResultSet;
	
	public List<ComunicacaoDocumentoResult> getLista() {
		return lista;
	}
	
	public void setLista(List<ComunicacaoDocumentoResult> listaProcessoLoteVinculados) {
		this.lista = listaProcessoLoteVinculados;
	}
	
	public Integer getTotalResultSet() {
		return totalResultSet;
	}
	
	public void setTotalResultSet(Integer totalResultSet) {
		this.totalResultSet = totalResultSet;
	}

    
}
