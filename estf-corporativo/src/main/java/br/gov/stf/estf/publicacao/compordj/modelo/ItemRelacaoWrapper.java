package br.gov.stf.estf.publicacao.compordj.modelo;

import java.util.List;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@SuppressWarnings("unchecked")
public class ItemRelacaoWrapper<T extends ESTFBaseEntity> {
	private Integer indice;
	private T wrappedObject;
	private List textos;
	private boolean isCabecaIguais;
	
	public Integer getIndice() {
		return indice;
	}
	public void setIndice(Integer indice) {
		this.indice = indice;
	}
	public T getWrappedObject() {
		return wrappedObject;
	}
	public void setWrappedObject(T wrappedObject) {
		this.wrappedObject = wrappedObject;
	}
	public List getTextos() {
		return textos;
	}
	public void setTextos(List textos) {
		this.textos = textos;
	}
	
	public ItemRelacaoWrapper(Integer indice, T wrappedObject,
			List textos) {
		this.indice = indice;
		this.wrappedObject = wrappedObject;
		this.textos = textos;
	}
	public boolean isCabecaIguais() {
		return isCabecaIguais;
	}
	public void setCabecaIguais(boolean isCabecaIguais) {
		this.isCabecaIguais = isCabecaIguais;
	}
	

}
