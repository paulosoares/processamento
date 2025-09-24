package br.jus.stf.estf.decisao.support.controller.context;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.richfaces.component.html.HtmlDatascroller;

import br.jus.stf.estf.decisao.pesquisa.web.PrincipalFacesBean;

/**
 * Encapsula o Datascroller da página principal permitindo setar o
 * número da página do paginador. Necessário para recuperar e 
 * restaurar o status do datascroller em um dado momento.
 * 
 * @author Rodrigo Barreiros
 * @since 30.04.2010
 * 
 * @see PrincipalFacesBean#visualizar(javax.faces.model.DataModel, br.jus.stf.estf.decisao.pesquisa.domain.Selectable)
 * @see PrincipalFacesBean#pesquisar(Class)
 */
@Name("dataScroller")
@Scope(ScopeType.EVENT)
public class ContextDataScroller {
	
    private HtmlDatascroller wrapped;
    
    public void setWrapped(HtmlDatascroller wrapped) {
		this.wrapped = wrapped;
	}
    
    public HtmlDatascroller getWrapped() {
		return wrapped;
	}
    
    public void setPage(int pageIndex) {
    	wrapped.setPage(pageIndex);
    }
    
}