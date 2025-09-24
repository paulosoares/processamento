package br.jus.stf.estf.decisao.support.action.support;

/**
 * Armazena as metainforma��es definidas na anota��o {@link Action}.
 * 
 * <p>Essas informa��es ser�o disponibilizadas por cada a��o via {@link ActionSupport#getDefinition()}
 * 
 * @author Rodrigo Barreiros
 * @since 08.06.2010
 */
public class ActionDefinition {

	private String facet;
	private String id;
	private String name;
	private int height;
	private int width;
	private String view;
	private boolean report;
	private String errorTitle;

	public String getFacet() {
		return facet;
	}

	public void setFacet(String facet) {
		this.facet = facet;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public boolean isReport() {
		return report;
	}

	public void setReport(boolean interactive) {
		this.report = interactive;
	}

	public String getErrorTitle() {
		return errorTitle;
	}

	public void setErrorTitle(String errorTitle) {
		this.errorTitle = errorTitle;
	}

}
