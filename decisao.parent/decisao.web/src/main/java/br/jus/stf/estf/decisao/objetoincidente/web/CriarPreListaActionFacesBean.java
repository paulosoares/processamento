package br.jus.stf.estf.decisao.objetoincidente.web;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Agrupador;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamento;
import br.gov.stf.estf.processostf.model.service.AgrupadorService;
import br.gov.stf.estf.processostf.model.service.PreListaJulgamentoService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.pesquisa.domain.AgrupadorLocal;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;
import br.jus.stf.estf.decisao.support.service.UsuarioLogadoService;

/**
 * @author Gabriel.Bastos
 * @since 29.10.2015
 */
@Action(id = "criarPreListaActionFacesBean", name = "Gerenciar Pré-listas", view = "/acoes/objetoincidente/criarPreLista.xhtml")
@Restrict({ ActionIdentification.LIBERAR_PARA_JULGAMENTO })
@RequiresResources(Mode.One)
public class CriarPreListaActionFacesBean extends ActionSupport<PreListaJulgamento> {
	@Autowired
	private UsuarioLogadoService usuarioLogadoService;
	
	@Autowired
	private AgrupadorService agrupadorService;
	
	@Autowired
	private PreListaJulgamentoService preListaJulgamentoService;
	
	@In(value = "revisarListasFacesBean", create = true)
	private RevisarListasFacesBean revisarListasFacesBean;
	
	private List<AgrupadorLocal> categorias;
	
	PreListaJulgamento lista;

	public void load() {
		// Limpa as mensagens mostradas anteriormente.
		cleanMessages();
				
		Ministro ministro = usuarioLogadoService.getMinistro();
		if (ministro == null) {
			addError("Usuário não está associado a um gabinete");
			sendToErrors();
			return;
		}
		
		setRefresh(false);
		setActionFrame(false);
		
		recuperarCategorias();
		
		if (lista == null)
			lista = new PreListaJulgamento();
	}

	
	public void recuperarCategorias() {
		Ministro ministro = usuarioLogadoService.getMinistro();
		categorias = new ArrayList<AgrupadorLocal>();
		
		try {
			if (ministro != null) {
				List<Agrupador> listaTemporaria = agrupadorService.recuperarCategoriasDoSetor(ministro.getSetor().getId());
				for (Agrupador categoria : listaTemporaria) {
					AgrupadorLocal dto = new AgrupadorLocal(categoria);
					if(preListaJulgamentoService.recuperarPreListaPorCategoria(categoria) != null){
						dto.setListaAssociada(true);					
					}
					categorias.add(dto);					
				}
			}
		} catch (ServiceException e) {
			e.printStackTrace();
			addError(e.getMessage());
			sendToErrors();
		}
		
		setRefresh(true);
	}

	public void salvarPreLista() {
		PreListaJulgamento lista = this.lista;
		lista.setSetor(getSetorMinistro());
		
		if (lista.getNome() == null || lista.getNome().isEmpty())
			addError("O campo \"Nome da Lista\" é obrigatório.");
		
		if (hasErrors()) {
			sendToErrors();
		} else {
			List<Agrupador> listaCategorias = new ArrayList<Agrupador>();

			for (AgrupadorLocal categoria : categorias) {
				if (categoria.isSelected())
					listaCategorias.add(categoria.getAgrupador());
			}
			
			lista.setCategorias(listaCategorias);
			
			try {
				boolean atualizar = true;
				
				if (lista.getId() == null)
					atualizar = false;
				
				preListaJulgamentoService.salvar(lista);
				lista = preListaJulgamentoService.recuperarPorId(lista.getId());
				
				if (atualizar) {
					for(PreListaJulgamento listaJulgamento : getPreListas()){
						if(listaJulgamento.getId().equals(lista.getId())) 
							listaJulgamento.setNome(lista.getNome());
							listaJulgamento.setObjetosIncidentes(lista.getObjetosIncidentes());
						
					}
					getAgruparFacesBean().atualizarColuna(lista);
				} else {
					getAgruparFacesBean().initListas();
					getAgruparFacesBean().incluirColuna(lista);
					getAgruparFacesBean().atualizarColuna(lista);
				}
				
				this.lista = new PreListaJulgamento();
				
				preListaJulgamentoService.flushSession();
				
				recuperarCategorias();
				
				addInformation("Operação realizada com sucesso!");
			} catch (ServiceException e) {
				e.printStackTrace();
				addError("Não foi possível salvar a pré-lista.");
				sendToErrors();
			}
		}
	}	
	
	public PreListaJulgamento getLista() {
		return lista;
	}
	
	public List<AgrupadorLocal> getCategorias() {
		return categorias;
	}

	public List<PreListaJulgamento> getPreListas() {
		return getAgruparFacesBean().getPreListasJulgamento();
	}
	
	public void excluirPrelista(PreListaJulgamento preListaJulgamento) {
		try {
			preListaJulgamento = preListaJulgamentoService.recuperarPorId(preListaJulgamento.getId());
			
			if (preListaJulgamento.getObjetosIncidentes() != null && preListaJulgamento.getObjetosIncidentes().size() > 0 )
				throw new ServiceException("Antes de excluir uma pré-lista é necessário remover todos os processos dela.");

			preListaJulgamentoService.excluir(preListaJulgamento);
			getAgruparFacesBean().initListas();
			getAgruparFacesBean().removerColuna(preListaJulgamento);
			lista = new PreListaJulgamento();
			recuperarCategorias();
			addInformation("Operação realizada com sucesso!");
		} catch (ServiceException e) {
			addWarning(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void alterarPreLista(PreListaJulgamento preListaJulgamento) {
		try {
			lista = preListaJulgamentoService.recuperarPorId(preListaJulgamento.getId());
			
			recuperarCategorias();
			
			for(AgrupadorLocal agrupador : getCategorias()) {
				for(Agrupador categoria : lista.getCategorias()) {
					if (categoria.getId().equals(agrupador.getAgrupador().getId())) {
						agrupador.setSelected(true);
						agrupador.setListaAssociada(false);
					}
				}
			}			
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	public RevisarListasFacesBean getAgruparFacesBean() {
		RevisarListasFacesBean revisarListasFacesBean = (RevisarListasFacesBean)Component.getInstance(RevisarListasFacesBean.class, true);
		return revisarListasFacesBean;
	}

	public void setAgruparFacesBean(RevisarListasFacesBean revisarListasFacesBean) {
		this.revisarListasFacesBean = revisarListasFacesBean;
	}
}
