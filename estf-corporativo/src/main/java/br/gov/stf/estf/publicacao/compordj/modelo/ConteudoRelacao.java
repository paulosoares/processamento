package br.gov.stf.estf.publicacao.compordj.modelo;

import java.util.List;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@SuppressWarnings("unchecked")
public class ConteudoRelacao<T extends ESTFBaseEntity> extends Conteudo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1160893955645158165L;
	
	private List<ItemRelacaoWrapper<T>> relacao;

	public List<ItemRelacaoWrapper<T>> getRelacao() {
		return relacao;
	}

	public void setRelacao(List<ItemRelacaoWrapper<T>> relacao) {
		this.relacao = relacao;
	}

	public ConteudoRelacao(List<ItemRelacaoWrapper<T>> relacao) {
		super();
		this.relacao = relacao;
	}
	
	public ConteudoRelacao() {
		super();
	}
}
