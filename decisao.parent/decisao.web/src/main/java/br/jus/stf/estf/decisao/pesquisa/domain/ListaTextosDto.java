package br.jus.stf.estf.decisao.pesquisa.domain;

import br.jus.stf.estf.decisao.support.query.Dto;

/**
 * DTO para os dados de Listas de Textos retornados na pesquisa avançada.
 * 
 * <p>O objetivo é tornar a pesquisa mais eficiente, retornando somente 
 * os dados utilizados na apresentação do resultado.
 * 
 * @author Rodrigo Barreiros
 * @since 30.04.2010
 */
public class ListaTextosDto implements Dto {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String nome;
    private boolean selected;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object other) {
        if (!(other instanceof ListaTextosDto)) return false;
        ListaTextosDto castOther = (ListaTextosDto) other;
        return this.getId().equals(castOther.getId());
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return getId().hashCode();
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("Lista [%s].", nome);
    }

	@Override
	public boolean isFake() {
		return false;
	}

}
