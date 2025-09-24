/**
 * 
 */
package br.jus.stf.estf.decisao.pesquisa.domain;

import br.jus.stf.estf.decisao.support.query.Dto;

/**
 * @author Paulo.Estevao
 * @since 21.06.2011
 */
public class MinistroDto implements Dto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8418530884974361672L;
	
	private boolean selected;
	private Long id;
	private String nome;
	
	/* (non-Javadoc)
	 * @see br.jus.stf.estf.decisao.support.query.Selectable#isSelected()
	 */
	@Override
	public boolean isSelected() {
		return selected;
	}

	/* (non-Javadoc)
	 * @see br.jus.stf.estf.decisao.support.query.Selectable#setSelected(boolean)
	 */
	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/* (non-Javadoc)
	 * @see br.jus.stf.estf.decisao.support.query.Dto#getId()
	 */
	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see br.jus.stf.estf.decisao.support.query.Dto#isFake()
	 */
	@Override
	public boolean isFake() {
		return false;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
