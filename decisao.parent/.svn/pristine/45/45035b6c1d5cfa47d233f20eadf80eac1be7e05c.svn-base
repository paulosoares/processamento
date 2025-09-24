package br.jus.stf.estf.decisao.support.controller.faces.datamodel;

import java.util.Arrays;
import java.util.List;

import br.jus.stf.estf.decisao.support.query.Dto;

/**
 * Wrapper para {@link javax.faces.model.ListDataModel}. Utilizado para
 * estender a classe {@link DataModel} que oferece serviços que serão
 * necessários para seleção de recursos na tela.
 * 
 * @author Rodrigo.Barreiros
 * @see 18.05.2010
 *
 * @param <T> o tipo de objeto listado no data model
 */
public class ListDataModel<T extends Dto> extends DataModel<T> {

	private javax.faces.model.ListDataModel dataModelWrapper;

    public ListDataModel() {
        this.dataModelWrapper = new javax.faces.model.ListDataModel();
    }

    public ListDataModel(List<?> list) {
        this.dataModelWrapper = new javax.faces.model.ListDataModel(list);
    }

    @SuppressWarnings("unchecked")
    public ListDataModel(T entity) {
    	this(Arrays.asList(entity));
    }
    
    /**
     * @see javax.faces.model.DataModel#isRowAvailable()
     */
    public boolean isRowAvailable() {
    	return this.dataModelWrapper.isRowAvailable();
    }

    /**
     * @see javax.faces.model.DataModel#getRowCount()
     */
    public int getRowCount() {
    	return this.dataModelWrapper.getRowCount();
    }

    /**
     * @see javax.faces.model.DataModel#getRowData()
     */
    public Object getRowData() {
    	return this.dataModelWrapper.getRowData();
    }

    /**
     * @see javax.faces.model.DataModel#getRowIndex()
     */
    public int getRowIndex() {
    	return this.dataModelWrapper.getRowIndex();
    }

    /**
     * @see javax.faces.model.DataModel#setRowIndex(int)
     */
    public void setRowIndex(int rowIndex) {
    	this.dataModelWrapper.setRowIndex(rowIndex);
    }

    /**
     * @see javax.faces.model.DataModel#getWrappedData()
     */
    public Object getWrappedData() {
    	return this.dataModelWrapper.getWrappedData();
    }

    /**
     * @see javax.faces.model.DataModel#setWrappedData(java.lang.Object)
     */
    public void setWrappedData(Object data) {
    	this.dataModelWrapper.setWrappedData(data);
    }

    public void setDataModelWrapper(List<?> list) {
        this.dataModelWrapper = new javax.faces.model.ListDataModel(list);
    }
    
    
}
