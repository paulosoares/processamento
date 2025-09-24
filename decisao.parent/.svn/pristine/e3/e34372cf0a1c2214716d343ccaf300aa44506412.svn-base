package br.jus.stf.estf.decisao.pesquisa.domain;

import br.gov.stf.estf.entidade.processostf.Agrupador;

/**
 * DTO para os dados de Categorias
 * 
 * @author Gabriel Teles
 * @since 01.09.2015
 */
public class AgrupadorLocal {
	
	public AgrupadorLocal(Agrupador agrupador) {
		super();
		this.agrupador = agrupador;
	}

	private static final long serialVersionUID = 1L;

    private boolean selected;
    
    private boolean listaAssociada;
    
    private int qtdObjIncidentes;

	public int getQtdObjIncidentes() {
		return qtdObjIncidentes;
	}

	public void setQtdObjIncidentes(int qtdObjIncidentes) {
		this.qtdObjIncidentes = qtdObjIncidentes;
	}

	private Agrupador agrupador;
    
	
    public Agrupador getAgrupador() {
		return agrupador;
	}

	public void setAgrupador(Agrupador agrupador) {
		this.agrupador = agrupador;
	}

	public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    public boolean getListaAssociada(){
    	return listaAssociada;
    }
    
    public void setListaAssociada(boolean hasLista){
    	this.listaAssociada = hasLista;
    }
    
    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object other) {
        if (!(other instanceof AgrupadorLocal)) return false;
        AgrupadorLocal castOther = (AgrupadorLocal)other;
        return this.getAgrupador().getId().equals(castOther.getAgrupador().getId());
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return getAgrupador().getId().hashCode();
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("Categoria [%s].", this.getAgrupador().getDescricao());
    }

}
