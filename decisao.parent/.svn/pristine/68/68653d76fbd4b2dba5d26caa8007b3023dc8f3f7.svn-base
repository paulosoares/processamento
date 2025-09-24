/**
 * 
 */
package br.jus.stf.estf.decisao.texto.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Agrupador;
import br.gov.stf.estf.processostf.model.service.AgrupadorService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.pesquisa.domain.AllResourcesDto;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;
import br.jus.stf.estf.decisao.support.service.UsuarioLogadoService;

/**
 * @author Fabricio.Rodrigues
 * @since 01.09.2015
 */
@Action(id = "gerenciarCategoriaActionFacesBean", name = "Gerenciar Categorias", view = "/acoes/categoria/gerenciarCategoria.xhtml")
@Restrict({ActionIdentification.GERENCIAR_CATEGORIAS})
public class GerenciarCategoriaActionFacesBean extends ActionSupport<AllResourcesDto> {
	
	protected static final String MSG_EXCLUSAO_SUCESSO = "Categoria excluída com sucesso.";

	protected static final String MSG_REGISTRO_SALVO_COM_SUCESSO = "Registro salvo com sucesso!";

	protected static final String MSG_EXISTE_CATEGORIA_COM_MESMO_NOME = "Já existe uma categoria com este nome. Tente criar uma categoria com um nome diferente.";

	@Autowired
	private AgrupadorService agrupadorService;
	
	@Autowired
	private UsuarioLogadoService usuarioLogadoService;
	
	private List<Agrupador> categorias;
	
	private Agrupador categoria;
	
	private final int pageSize = 10;
	private int pageIndex;
	
	public void execute() {
		if (categoria == null) 
			categoria = new Agrupador();
			
			categoria.setSetor(getSetorMinistro());
			
		try {
			if(categoria.getDescricao() == null || categoria.getDescricao().isEmpty()){
				addInformation("Por favor, informe o nome da categoria.");
			} else {
				
					try {
						salvar(categoria);
						addInformation(MSG_REGISTRO_SALVO_COM_SUCESSO);
						categoria = new Agrupador();			
					} catch (RuntimeException e) {						
						addError(MSG_EXISTE_CATEGORIA_COM_MESMO_NOME);
					}					

					carregarCategorias();
			}
		} catch (Exception e) {
			// Enviando mensagem padrão...
			e.printStackTrace();
			addError(e.getMessage());
			sendToErrors();
		}
		setRefresh(true);
	}
	
	protected void salvar(Agrupador categoria) throws ServiceException {
		List<Agrupador> registros = pesquisarCategoria(categoria);
		
		if (registros.size() > 0) {
			throw new RuntimeException(MSG_EXISTE_CATEGORIA_COM_MESMO_NOME);
		} else {
			agrupadorService.salvar(categoria);
		}
	}

	protected List<Agrupador> pesquisarCategoria(Agrupador categoria)
			throws ServiceException {
		List<Agrupador> registros = agrupadorService.recuperarCategoriasDoSetor(categoria.getSetor().getId(), categoria.getDescricao());
		return registros;
	}

	@Override
	protected String getErrorTitle() {
		return "Erro ao gerenciar categorias";
	}
	
	@Override
	public void load() {
		Ministro ministro = usuarioLogadoService.getMinistro();
		if (ministro == null) {
			addError("Usuário não está associado a um gabinete");
			sendToErrors();
			return;
		}
		
		pageIndex = 1;
		carregarCategorias();
		
		categoria = new Agrupador();
		categoria.setSetor(getSetorMinistro());

	}
	
	public void editarCategoria(Agrupador categoria){
		carregarCategorias();
		setCategoria(categoria);
	}
	
	public void excluirCategoria(Agrupador agrupador) {
		try {
			agrupadorService.excluir(agrupador);
			addInformation("Categoria excluída com sucesso.");
		} catch (ServiceException e) {
			e.printStackTrace();
			addWarning(e.getMessage());
			setRefresh(true);
		}
		carregarCategorias();
	}
	
	public void carregarCategorias() {
		try {
			categorias = recuperarCategorias();
		} catch (ServiceException e) {
			e.printStackTrace();
			addError(e.getMessage());
			sendToErrors();
		}
		setRefresh(true);
	}
	
	public List<Agrupador> recuperarCategorias() throws ServiceException {
		Ministro ministro = usuarioLogadoService.getMinistro();
		if (ministro != null) {
			return agrupadorService.recuperarCategoriasDoSetor(ministro.getSetor().getId());
		} else {
			return new ArrayList<Agrupador>();
		}
	}
	
	public void voltar() {
    	getDefinition().setFacet("principal");
    }
	
	public List<Agrupador> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Agrupador> categorias) {
		this.categorias = categorias;
	}
	
	public int getPageSize() {
		return pageSize;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	
	public Agrupador getCategoria() {
		return categoria;
	}

	public void setCategoria(Agrupador categoria) {
		this.categoria = categoria;
	}
	
}
