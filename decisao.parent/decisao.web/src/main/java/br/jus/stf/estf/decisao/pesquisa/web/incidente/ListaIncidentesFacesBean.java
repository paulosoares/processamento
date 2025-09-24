package br.jus.stf.estf.decisao.pesquisa.web.incidente;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import br.gov.stf.estf.entidade.processostf.ListaProcessos;
import br.jus.stf.estf.decisao.objetoincidente.service.ObjetoIncidenteService;
import br.jus.stf.estf.decisao.pesquisa.domain.ListaIncidentesDto;
import br.jus.stf.estf.decisao.pesquisa.domain.Pesquisa;
import br.jus.stf.estf.decisao.pesquisa.service.PesquisaService;
import br.jus.stf.estf.decisao.support.controller.context.FacesBean;
import br.jus.stf.estf.decisao.support.controller.faces.datamodel.PagedList;

/**
 * Bean JSF (Seam Component) para controle e tratamento de eventos de tela associados às Listas
 * de Objetos Incidente. Usado pelo mecanismo de pesquisa para recuperação de informações.
 * 
 * <p>Implementação <code>FacesBean</code> para Lista de Incidentes.
 * 
 * @author Rodrigo.Barreiros
 * @since 30.04.2010
 */
@Name("listaIncidentesFacesBean")
@Scope(ScopeType.CONVERSATION)
public class ListaIncidentesFacesBean implements FacesBean<ListaIncidentesDto> {
	
	@In("#{objetoIncidenteServiceLocal}")
	private ObjetoIncidenteService objetoIncidenteService;
	
	@In("#{pesquisaService}")
	private PesquisaService pesquisaService;
	
    /**
     * Pesquisa as listas de incidentes dado o nome ou parte dele.
     * 
     * @param suggest o nome da lista
     * 
     * @return as listas de incidentes
     */
    public List<ListaProcessos> search(Object suggest) {
		return objetoIncidenteService.pesquisarListasIncidentes(suggest.toString());
	}
    
	/**
	 * @see br.jus.stf.estf.decisao.support.controller.context.FacesBean#search(br.jus.stf.estf.decisao.pesquisa.domain.Pesquisa, int, int)
	 */
	@Override
	public PagedList<ListaIncidentesDto> search(Pesquisa pesquisa, int first, int max) {
		pesquisa.setFirstResult(first);
		pesquisa.setMaxResults(max);
        
        return pesquisaService.pesquisarListasIncidentes(pesquisa);
    }

	/**
	 * Listas de Incidentes não são editadas pelo mecanismo de pesquisa. São manipuladas
	 * pelo mecanismo de ações.
	 */
	@Override
	public ListaIncidentesDto load(ListaIncidentesDto entidade) {
		throw new IllegalStateException("Lista de Incidentes não é editável via pesquisa");
	}

}
