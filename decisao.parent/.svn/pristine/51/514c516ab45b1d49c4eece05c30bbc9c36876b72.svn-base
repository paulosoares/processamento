/**
 * 
 */
package br.jus.stf.estf.decisao.texto.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.documento.model.service.ListaTextosService;
import br.gov.stf.estf.entidade.documento.ListaTextos;
import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.pesquisa.domain.ListaTextosDto;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.handlers.States;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;

/**
 * @author Paulo.Estevao
 * @since 25.08.2010
 */
@Action(id="alterarListaTextosActionFacesBean", name="Alterar Lista", view="/acoes/texto/alterarLista.xhtml", height=210, width=500)
@Restrict({ActionIdentification.EDITAR_LISTA_DE_TEXTOS})
@RequiresResources(Mode.One)
@States({ FaseTexto.EM_ELABORACAO, FaseTexto.EM_REVISAO, FaseTexto.REVISADO, FaseTexto.LIBERADO_ASSINATURA, FaseTexto.ASSINADO, FaseTexto.LIBERADO_PUBLICACAO, FaseTexto.PUBLICADO, FaseTexto.JUNTADO})
public class AlterarListaTextosActionFacesBean extends ActionSupport<ListaTextosDto> {

	@Autowired
	private ListaTextosService listaTextosService;
	
	private String nomeLista;
	
	private boolean ativa;
	
	private ListaTextos listaTextos;
	
	@Override
	public void load() {
		// Recuperando e setando novos dados da lista...
		try {
			listaTextos = listaTextosService.recuperarPorId(getResources().iterator().next().getId());
			nomeLista = listaTextos.getNome();
			ativa = listaTextos.getAtiva();
		} catch (ServiceException e) {
			addError(e.getMessage());
			logger.error(e.getMessage(), e);
			sendToErrors();
		}
	}
	
	public void execute() {
		try {
			if (nomeLista == null || nomeLista.trim().length() == 0 ) {
				// Enviando mensagem padrão...
				addInformation("Por favor, informe o novo nome da lista.");
			} else {
				List<ListaTextos> listas = listaTextosService.pesquisarListaTextos(nomeLista, Boolean.TRUE, getSetorMinistro().getId());
				
				if (listas != null && listas.size() > 0) {
					addInformation("Já existe uma lista para este setor com este nome. Tente criar uma lista com um nome diferente.");
					sendToInformations();
				} else {
					listaTextos.setNome(nomeLista);
					listaTextos.setAtiva(ativa);
					// Persistindo a lista...
					listaTextosService.alterar(listaTextos);
					sendToConfirmation();
				}
			}
		} catch (ServiceException e) {
			addError(e.getMessage());
			logger.error(e.getMessage(), e);
			sendToErrors();
		}
	}

	public String getNomeNovaLista() {
		return nomeLista;
	}

	public void setNomeNovaLista(String nomeLista) {
		this.nomeLista = nomeLista;
	}

	public boolean isAtiva() {
		return ativa;
	}

	public void setAtiva(boolean ativa) {
		this.ativa = ativa;
	}
	
	public ListaTextos getLista() {
		return listaTextos;
	}
	
	@Override
	public String getErrorTitle() {
		return "Erro ao alterar lista de textos";
	}
	
	public void voltar() {
    	getDefinition().setFacet("principal");
    }
}
