package br.jus.stf.estf.decisao.pesquisa.web.texto;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import br.gov.stf.estf.entidade.documento.ListaTextos;
import br.jus.stf.estf.decisao.pesquisa.domain.ListaTextosDto;
import br.jus.stf.estf.decisao.pesquisa.domain.Pesquisa;
import br.jus.stf.estf.decisao.pesquisa.service.PesquisaService;
import br.jus.stf.estf.decisao.support.controller.context.FacesBean;
import br.jus.stf.estf.decisao.support.controller.faces.datamodel.PagedList;
import br.jus.stf.estf.decisao.texto.service.TextoService;

/**
 * Bean JSF (Seam Component) para controle e tratamento de eventos de tela associados às Listas
 * de Textos. Usado pelo mecanismo de pesquisa para recuperação de informações.
 * 
 * <p>Implementação <code>FacesBean</code> para Lista de Textos.
 * 
 * @author Rodrigo.Barreiros
 * @since 30.04.2010
 */
@Name("listaTextosFacesBean")
@Scope(ScopeType.CONVERSATION)
public class ListaTextosFacesBean implements FacesBean<ListaTextosDto> {
	
	@In("#{textoServiceLocal}")
	private TextoService textoService;
	
	@In("#{pesquisaService}")
	private PesquisaService pesquisaService;
	
    /**
     * Pesquisa as listas de textos dado o nome ou parte dele.
     * 
     * @param suggest o nome da lista
     * 
     * @return as listas de textos
     */
    public List<ListaTextos> search(Object suggest) {
		return textoService.pesquisarListasTextos(suggest.toString());
	}
    
	/**
	 * @see br.jus.stf.estf.decisao.support.controller.context.FacesBean#search(br.jus.stf.estf.decisao.pesquisa.domain.Pesquisa, int, int)
	 */
	@Override
	public PagedList<ListaTextosDto> search(Pesquisa pesquisa, int first, int max) {
		pesquisa.setFirstResult(first);
		pesquisa.setMaxResults(max);
        
        return pesquisaService.pesquisarListasTextos(pesquisa);
    }

	/**
	 * Listas de Textos não são editadas pelo mecanismo de pesquisa. São manipuladas
	 * pelo mecanismo de ações.
	 */
	@Override
	public ListaTextosDto load(ListaTextosDto texto) {
		throw new IllegalStateException("Lista de Textos não é editável via pesquisa");
    }
	
}
