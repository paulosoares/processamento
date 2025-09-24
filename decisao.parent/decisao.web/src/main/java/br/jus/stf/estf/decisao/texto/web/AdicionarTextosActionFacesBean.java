package br.jus.stf.estf.decisao.texto.web;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import br.gov.stf.estf.documento.model.service.ListaTextosService;
import br.gov.stf.estf.documento.model.service.TextoListaTextoService;
import br.gov.stf.estf.entidade.documento.ListaTextos;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TextoListaTexto;
import br.gov.stf.estf.entidade.documento.TextoListaTexto.TextoListaTextoId;
import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.action.handlers.CheckMinisterId;
import br.jus.stf.estf.decisao.support.action.handlers.CheckRestrictions;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.handlers.States;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionCallback;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionInterface;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;
import br.jus.stf.estf.decisao.texto.service.TextoService;

/**
 * @author Rodrigo Barreiros
 * @see 27.05.2010
 */
@Action(id="adicionarTextosActionFacesBean", 
		name="Adicionar Textos à Lista", 
		view="/acoes/texto/adicionarTextos.xhtml")
@Restrict({ActionIdentification.ADICIONAR_TEXTO_A_UMA_LISTA})
@RequiresResources(Mode.Many)
@CheckMinisterId
@CheckRestrictions
@States({ FaseTexto.EM_ELABORACAO, FaseTexto.EM_REVISAO, FaseTexto.REVISADO, FaseTexto.LIBERADO_ASSINATURA, FaseTexto.ASSINADO, FaseTexto.LIBERADO_PUBLICACAO, FaseTexto.PUBLICADO, FaseTexto.JUNTADO})
public class AdicionarTextosActionFacesBean extends ActionSupport<TextoDto> implements ActionInterface<TextoDto>  {

	@Autowired
	private ListaTextosService listaTextosService;
	
	@Autowired
	private TextoListaTextoService textoListaTextoService;
	
	@Qualifier("textoServiceLocal") 
	@Autowired 
	private TextoService textoService;
	
	private Long idListaTextos;
	
	private String nomeNovaListaTextos;

	
    /**
     * Executa as regras para adição de texto.
     */
	public void execute() {
    	try {
	    	ListaTextos listaTextos = null;
	    	
	    	// Se o ID da lista selecionada for -1, significa que o usuário selecionou a opções criar nova lista. Caso contrário, 
			// o usuário selecionou uma lista já existente. Neste caso, será informado o ID da lista selecionada.
	    	if(idListaTextos == -1L) {
	    		// Criação de uma nova lista.
	    		if(nomeNovaListaTextos != null && nomeNovaListaTextos.trim().length() > 0) {
	    			List<ListaTextos> listas = listaTextosService.pesquisarListaTextos(nomeNovaListaTextos, Boolean.TRUE, getSetorMinistro().getId());
					
					if (listas != null && listas.size() > 0) {
						addInformation("Já existe uma lista para este setor com este nome. Tente criar uma lista com um nome diferente.");
						sendToInformations();
						return;
					} else {
						listaTextos = new ListaTextos();
						listaTextos.setSetor(getSetorMinistro());
						listaTextos.setNome(nomeNovaListaTextos);
						listaTextos.setAtiva(true);
						listaTextos.setElementos(new LinkedHashSet<Texto>());
						listaTextos = listaTextosService.incluir(listaTextos);
						
						adicionarTextos(listaTextos);
						
					}
	    		} else {
	    			// Erro caso o usuário tente criar uma lista sem nome.
	    			addInformation("Por favor, informe o nome da nova lista.");
					sendToInformations();
	    			return;
	    		}
	    	} else {
	    		// Recupera a lista caso o usuário selecione uma lista existente. 
	    		listaTextos = listaTextosService.recuperarPorId(idListaTextos);
    			
	    		adicionarTextos(listaTextos);
	    		
	    	}
    	} catch(ServiceException e) {
    		addError(e.getMessage());
    		sendToErrors();
    	} catch(Exception e) {
			addError(e.getMessage());
			sendToErrors();
		}
    	
    	setRefresh(true);
    	
    }
    
	public void adicionarTextos(final ListaTextos listaTextos) {
    	execute(new ActionCallback<TextoDto>() {
    		public void doInAction(TextoDto dto) throws Exception {
    			Texto texto = textoService.recuperarTextoPorId(dto.getId());
    			
    			TextoListaTexto existente = textoListaTextoService.recuperar(listaTextos, texto);
    			if (existente != null) {
    				addError("[" + dto.toString() + "] - Texto já pertence à lista.");
    			} else {
    				TextoListaTexto textoListaTexto = new TextoListaTexto();
	    			TextoListaTextoId textoListaTextoId = new TextoListaTextoId();
	    			textoListaTextoId.setListaTexto(listaTextos);
	    			textoListaTextoId.setTexto(texto);
	    			textoListaTexto.setId(textoListaTextoId);
	    			
	    			textoListaTextoService.incluir(textoListaTexto);
    			}
    		}
    	});
    }
	
    public void voltar() {
    	getDefinition().setFacet("principal");
    }
    
    public List<SelectItem> getListasDisponiveis() throws ServiceException {
		List<SelectItem> itens = new LinkedList<SelectItem>();
		List<ListaTextos> listas = listaTextosService.pesquisarListaTextos(null, null, getSetorMinistro().getId());
		itens.add(new SelectItem(-1, "Nova Lista de Textos"));
		for (ListaTextos lista : listas) {
			itens.add(new SelectItem(lista.getId(), lista.getNome()));
		}
		idListaTextos = -1L;
		return itens;
	}

	public Long getIdListaTextos() {
		return idListaTextos;
	}

	public void setIdListaTextos(Long idListaTextos) {
		this.idListaTextos = idListaTextos;
	}

	public String getNomeNovaListaTextos() {
		return nomeNovaListaTextos;
	}

	public void setNomeNovaListaTextos(String nomeNovaListaTextos) {
		this.nomeNovaListaTextos = nomeNovaListaTextos;
	}
    
}
