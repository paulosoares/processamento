/**
 * 
 */
package br.jus.stf.estf.decisao.pesquisa.domain;

import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.jus.stf.estf.decisao.support.query.Dto;

/**
 * @author Paulo.Estevao
 * @since 08.07.2011
 */
public class TipoTextoDto implements Dto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5850955284566349563L;

	private boolean selected;
	private TipoTexto tipoTexto;
	
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
		if (tipoTexto != null) {
			return tipoTexto.getCodigo();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see br.jus.stf.estf.decisao.support.query.Dto#isFake()
	 */
	@Override
	public boolean isFake() {
		return false;
	}

	public TipoTexto getTipoTexto() {
		return tipoTexto;
	}

	public void setTipoTexto(TipoTexto tipoTexto) {
		this.tipoTexto = tipoTexto;
	}

}
