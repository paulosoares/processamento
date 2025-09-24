package br.jus.stf.estf.decisao.support.controller.context;

import br.jus.stf.estf.decisao.support.action.support.ActionController;
import br.jus.stf.estf.decisao.support.controller.faces.datamodel.DataModel;

/**
 * Representa o contexto de apresentação de resultados. Pode ser um contexto de 
 * apresentação de detalhes de um objeto ou o contexto de apresentação de 
 * uma lista de resultados.
 * 
 * <p>Contextos são formados pelo controlador de ações, o data model 
 * (informações para apresentação), o status do paginador, a página
 * de apresentação de resultados e o tipo de contexto.
 * 
 * @author Rodrigo Barreiros
 * @since 03.05.2010
 */
@SuppressWarnings("unchecked")
public class Context {
	
	public enum ContextType {LIST, VIEW}
	
	private ActionController actionController; 
	private DataModel result;
	private int pageIndex;
	private String page;
	private ContextType type;

	public Context(DataModel result) {
		this.result = result;
	}
	
	public void setActionController(ActionController actionController) {
		this.actionController = actionController;
	}
	
	public ActionController getActionController() {
		return actionController;
	}
	
	public void setResult(DataModel result) {
		this.result = result;
	}
	
	public DataModel getResult() {
		return result;
	}
	
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	
	public int getPageIndex() {
		return pageIndex;
	}
	
	public void setPage(String page) {
		this.page = page;
	}

	public String getPage() {
		return page;
	}
	
	public void setType(ContextType type) {
		this.type = type;
	}
	
	public ContextType getType() {
		return type;
	}
	
}
