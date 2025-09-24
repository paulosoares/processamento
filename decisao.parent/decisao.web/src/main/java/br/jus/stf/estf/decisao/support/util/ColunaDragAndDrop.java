package br.jus.stf.estf.decisao.support.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.gov.stf.estf.entidade.processostf.PreListaJulgamentoMotivoAlteracao;

public class ColunaDragAndDrop<T> {
	private Long id;
	private String nome;
	private boolean ordenacaoNumerica;
	private HashMap<T, ObjetoDragAndDrop<T>> objetos;
	private boolean todosRevisados;
	
	public ColunaDragAndDrop(Long id, String nome, boolean ordenacaoNumerica) {
		this.id = id;
		this.nome = nome;
		this.ordenacaoNumerica = ordenacaoNumerica;
		this.objetos = new HashMap<T, ObjetoDragAndDrop<T>>();
	}
	
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getObjetoNome(T objeto) {
		return this.objetos.get(objeto).getNome();
	}

	public void add(T objeto, String nome, Boolean revisado, PreListaJulgamentoMotivoAlteracao motivo, String revisor, Date dataRevisao, String observacao) {
		ObjetoDragAndDrop<T> odnd = new ObjetoDragAndDrop<T>(nome, this, objeto, revisado, motivo, revisor, dataRevisao, observacao);
		this.objetos.put(objeto, odnd);
		this.atualizarCheckboxRevisao();
	}
	
	public void remove(T objeto) {
		this.objetos.remove(objeto);
	}
	
	public boolean getTemObjetos() {
		return !this.objetos.isEmpty();
	}
	
	public void atualizarCheckboxRevisao() {
		if (this.getObjetos().isEmpty()) { 
			this.setTodosRevisados(false);
		} else {
			this.setTodosRevisados(true);
			for (ObjetoDragAndDrop<T> objeto : this.getObjetos()) {
				if (!objeto.getRevisado()) {
					this.setTodosRevisados(false);
					break;
				}
			}
		}
	}
	
	public boolean getTodosRevisados() {
		return this.todosRevisados;
	}
	
	public void setTodosRevisados(boolean todosRevisados) {
		this.todosRevisados = todosRevisados;
	}

	public List<ObjetoDragAndDrop<T>> getObjetos() {
		List<ObjetoDragAndDrop<T>> result = new ArrayList<ObjetoDragAndDrop<T>>(this.objetos.values());
		
		if (result != null)
			if (ordenacaoNumerica)
				Collections.sort(result, getOrdenacaoNumericaComparator());
			else
				Collections.sort(result, getOrdenacaoAlfaNumericaComparator());
		
		return result;
	}
	
	public Set<T> getInstancias() {
		Set<T> listaInstancias = new HashSet<T>();
		
		for(ObjetoDragAndDrop<T> instancia : getObjetos()) 
			listaInstancias.add(instancia.getInstancia());
		
		return listaInstancias;
	}

	private Comparator<ObjetoDragAndDrop<T>> getOrdenacaoNumericaComparator() {	
		return new Comparator<ObjetoDragAndDrop<T>>() {
			@Override
			public int compare(ObjetoDragAndDrop<T> o1, ObjetoDragAndDrop<T> o2) {
				Long desc1 = Long.parseLong(o1.getNome().replaceAll("[^0-9]", ""));
				Long desc2 = Long.parseLong(o2.getNome().replaceAll("[^0-9]", ""));
				
				int comparacao = desc1.compareTo(desc2);
				
				if (comparacao != 0) 
					return comparacao;
				else
					return o1.getNome().compareTo(o2.getNome());
			}
		};
	}

	private Comparator<ObjetoDragAndDrop<T>> getOrdenacaoAlfaNumericaComparator() {
		return new Comparator<ObjetoDragAndDrop<T>>() {
			@Override
			public int compare(ObjetoDragAndDrop<T> o1, ObjetoDragAndDrop<T> o2) {
				return o1.getNome().compareTo(o2.getNome());
			}
		};
	}

	public boolean isOrdenacaoNumerica() {
		return ordenacaoNumerica;
	}

	public void setOrdenacaoNumerica(boolean ordenacaoNumerica) {
		this.ordenacaoNumerica = ordenacaoNumerica;
	}

	public void setObjetos(HashMap<T, ObjetoDragAndDrop<T>> objetos) {
		this.objetos = objetos;
	}
}