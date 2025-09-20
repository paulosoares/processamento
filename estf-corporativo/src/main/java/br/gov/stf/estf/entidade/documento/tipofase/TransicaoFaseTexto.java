package br.gov.stf.estf.entidade.documento.tipofase;

import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;

public class TransicaoFaseTexto {
	public final FaseTexto origem;
	public final FaseTexto destino;
	public final TipoSituacaoDocumento tipoSituacaoDocumento;
	public TransicaoFaseTexto(FaseTexto origem, FaseTexto destino, TipoSituacaoDocumento tipoSituacaoDocumento) {
		this.origem = origem;
		this.destino = destino;
		this.tipoSituacaoDocumento = tipoSituacaoDocumento;
	}	
	public String toString() {
		return String.format("[Transicao: %s -> %s (%s)",origem.getDescricao(), destino.getDescricao(),tipoSituacaoDocumento);
	}
}
