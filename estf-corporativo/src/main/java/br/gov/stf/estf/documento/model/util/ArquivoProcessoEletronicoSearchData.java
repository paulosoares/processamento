package br.gov.stf.estf.documento.model.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.gov.stf.estf.model.util.ESTFSearchData;

public class ArquivoProcessoEletronicoSearchData extends ESTFSearchData {

	private static final long serialVersionUID = -725533130266960297L;

	public ArquivoProcessoEletronicoSearchData() {
		this.situacoesDocumentoEletronicoIncluidas = new HashSet<String>();
		this.situacoesDocumentoEletronicoExcluidas = new HashSet<String>();
	}
	
	public String siglaClasseProcessual;
	public Long numeroProcessual;
	public Long numeroProtocolo;
	public Short anoProtocolo;
	public Long idDocumentoEletronico;
	public Boolean apenasDocumentosAssinados;

	// Ver classe: DocumentoEletronico
	public Set<String> situacoesDocumentoEletronicoIncluidas;
	public Set<String> situacoesDocumentoEletronicoExcluidas;
	
	public List<Long> listaIdObjetoIncidente;
}
