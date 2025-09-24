package br.jus.stf.estf.decisao.support.controller.faces.datamodel;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import br.jus.stf.estf.decisao.support.query.Dto;

/**
 * Abstração que deverá ser utilizda por todos os DataModel´s utilizados para apresentar 
 * resultados produzidos pela pesquisa avançada.
 * 
 * <p>Fornece serviços de seleção dos objetos presentes no data model.
 * 
 * @author Rodrigo Barreiros
 * @see 18.05.2010
 *
 * @param <T>
 */
public abstract class DataModel<T extends Dto> extends javax.faces.model.DataModel {
	
    private Set<T> selecteds;
    
    /**
     * Altera a seleção do recursos, incluindo o recurso se ele não estava na lista
     * de selecionados ou removendo o recurso se ele já estava na lista.
     * 
     * @param resource o recurso informado
     */
    public void changeSelection(T resource) {
        if (getSelecteds().contains(resource) && !resource.isSelected()) {
        	getSelecteds().remove(resource);
            return;
        }
        if (!getSelecteds().contains(resource) && resource.isSelected()) {
        	getSelecteds().add(resource);
        }
    }
    
    /**
     * Adiciona ou retira todos da lista de selecionados. Todos serão
     * selecionados se pelo menos um não estiver selecionado. Todos
     * serão deselecionados se todos estiverem selecionados.
     */
    @SuppressWarnings("unchecked")
	public void selectAll() {
    	List<T> all = (List<T>) getWrappedData();
    	boolean check = !allChecked();
    	for (T entity : all) {
    		if (entity.getId() != null) {
	        	entity.setSelected(check);
	        	if (check) {
	        		getSelecteds().add(entity);
	        	} else {
	            	getSelecteds().remove(entity);
	        	}
    		}
		}
    }
    
    /**
     * Recupera os ID's dos registros selecionados.
     * 
     * @return os ID's em uma string separada por virgula.
     */
    public String getSelectedsId() {
    	List<Long> ids = new ArrayList<Long>(getSelecteds().size());
    	for (T t : selecteds) {
			ids.add(t.getId());
		}
    	return ids.toString();
    }
    
    /**
     * Verifica se todos os registros estão selecionados.
     * 
     * @return true, se todos estão selecionados, false, caso contrário
     */
    @SuppressWarnings("unchecked")
    public boolean allChecked() {
    	for (T dto : (List<T>) getWrappedData()) {
    		if (dto.getId() == null) {
    			continue;
    		}
    		if (!dto.isSelected()) {
    			return false;
    		}
    	}
    	return true;
    }
    
    /**
     * Recupera o recurso dado seu index na lista de recursos.
     * 
     * @param index o index do registro
     * @return o registro
     */
    @SuppressWarnings("unchecked")
    public Dto getRowData(int index) {
    	return ((List<T>) getWrappedData()).get(index);
    }
    
    /**
     * Retorna a lista de recursos selecionados na tela.
     * 
     * @return a lista de recursos
     */
    public Set<T> getSelecteds() {
    	if (selecteds == null) {
    		selecteds = new LinkedHashSet<T>();
    	}
		return selecteds;
	}

    /**
     * Seleciona um recurso dados recursos, removendo todos os outros
     * recursos da lista de selecionados.
     * 
     * <p>Remover os outros recursos da lista de selecionados é
     * necessário porque quando o usuário estiver paginando
     * entre os recursos, só pode haver um recurso selecionado,
     * caso contrário o mecanismo de ação não funcionaria
     * apropriadamente.
     * 
     * @param entity o recurso que será selecionado
     * @return o recurso selecionado
     */
    public T select(T entity) {
    	unselectAll();
    	entity.setSelected(true);
    	getSelecteds().add(entity);
    	return entity;
    }
    
    /**
     * Remove todos os recursos da lista de selecionados.
     */
    public void unselectAll() {
    	for (T selectable : getSelecteds()) {
			selectable.setSelected(false);
		}
    	getSelecteds().clear();
    }
    
    /**
     * Reseta o conteudo do datamodel. Deve ser implementado 
     * pelas subclasses. Por default, desmarca os registros que estavam selecionados.
     */
    public void reset() {
    	getSelecteds().clear();
    }
}
