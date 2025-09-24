/**
 * 
 */
package br.jus.stf.estf.decisao.texto.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.documento.model.service.ListaTextosService;
import br.gov.stf.estf.entidade.documento.ListaTextos;
import br.jus.stf.estf.decisao.pesquisa.domain.ListaTextosDto;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;

/**
 * @author Paulo.Estevao
 * @since 24.08.2010
 */
@Action(id="criarListaTextosActionFacesBean", name="Nova Lista de Textos", view="/acoes/texto/criarLista.xhtml", height=180, width=500)
@Restrict({ActionIdentification.NOVA_LISTA_DE_TEXTOS})
public class CriarListaTextosActionFacesBean extends ActionSupport<ListaTextosDto> {
	
	@Autowired
	private ListaTextosService listaTextosService;
	
	private String nomeNovaLista;

	public void execute() {
		try {
			if (nomeNovaLista == null || nomeNovaLista.trim().length() == 0 ) {
				// Enviando mensagem padrão...
				addInformation("Por favor, informe o nome da nova lista.");
			} else {
				List<ListaTextos> listas = listaTextosService.pesquisarListaTextos(nomeNovaLista, Boolean.TRUE, getSetorMinistro().getId());
				
				if (listas != null && listas.size() > 0) {
					addInformation("Já existe uma lista para este setor com este nome. Tente criar uma lista com um nome diferente.");
					sendToInformations();
				} else {
					// Instanciando e setando nome da nova lista...
					ListaTextos listaTextos = new ListaTextos();
					listaTextos = new ListaTextos();
		    		listaTextos.setSetor(getSetorMinistro());
		    		listaTextos.setNome(nomeNovaLista);
		    		listaTextos.setAtiva(true);
		    		
					// Persistindo a lista...
		    		listaTextosService.salvar(listaTextos);
		    		sendToConfirmation();
				}
			}
			setRefresh(true);
		} catch (Exception e) {
			// Enviando mensagem padrão...
			addError(e.getMessage());
			sendToErrors();
		}		
	}
	
	public String getNomeNovaLista() {
		return nomeNovaLista;
	}

	public void setNomeNovaLista(String nomeNovaLista) {
		this.nomeNovaLista = nomeNovaLista;
	}	
	
	@Override
	protected String getErrorTitle() {
		return "Erro ao criar lista de textos";
	}
	
	public void voltar() {
    	getDefinition().setFacet("principal");
    }
}
