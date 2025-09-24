/**
 * 
 */
package br.jus.stf.estf.decisao.objetoincidente.web;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import br.gov.stf.estf.entidade.processostf.Agrupador;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamento;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamentoMotivoAlteracao;
import br.gov.stf.estf.processostf.model.service.AgrupadorService;
import br.gov.stf.estf.processostf.model.service.PreListaJulgamentoObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.PreListaJulgamentoService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.objetoincidente.service.ObjetoIncidenteService;
import br.jus.stf.estf.decisao.pesquisa.domain.AgrupadorLocal;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;
import br.jus.stf.estf.decisao.support.service.UsuarioLogadoService;
import br.jus.stf.estf.decisao.texto.service.TextoService;

/**
 * @author Gabriel Teles
 * @since 01.09.2015
 */
@Action(id = "categorizarProcessoActionFacesBean", name = "Categorizar Processos", view = "/acoes/objetoincidente/categorizarProcesso.xhtml", width=750)
@Restrict({ ActionIdentification.CATEGORIZAR_PROCESSOS })
@RequiresResources(Mode.Many)
public class CategorizarProcessoActionFacesBean extends ActionSupport<ObjetoIncidenteDto> {
	
	protected static final String MSG_ERRO_NAO_FOI_POSSIVEL_CARREGAR_CAT_GABINETE = "Não foi possível carregar as categorias deste gabinete.";
	protected static final String MSG_ERRO_CATEGORIA_NAO_SELECIONADA = "Você não selecionou nenhuma categoria.";
	protected static final String MSG_ERRO_APENAS_UMA_CATEGORIA = "Só é permitido salvar uma categoria por incidente";

	private List<AgrupadorLocal> categoriasRestantes = new ArrayList<AgrupadorLocal>();
	private List<AgrupadorLocal> categoriasAdicionadas = new ArrayList<AgrupadorLocal>();
	
	/**
	 * @var boolean temCategoria Indica o objeto Incidesnte tem agrupador associados  
	 */
	private boolean temCategoria = false;
	
	private List<ObjetoIncidente<?>> objetosIncidentes = new ArrayList<ObjetoIncidente<?>>();

	@Autowired
	private UsuarioLogadoService usuarioLogadoService;	
	
	@Autowired
	private AgrupadorService agrupadorService;

	@Autowired
	private TextoService textoService;
	
	@Qualifier("objetoIncidenteServiceLocal")
	@Autowired
	private ObjetoIncidenteService objetoIncidenteService;
	
	@Autowired
	private PreListaJulgamentoObjetoIncidenteService preListaJulgamentoObjetoIncidenteService;
	
	@Autowired PreListaJulgamentoService preListaJulgamentoService;
	
	
	@Override
	public void load() {		
		List<AgrupadorLocal> categoriasRestantes;
		List<AgrupadorLocal> categoriasAdicionadas;
		try {
			List<ObjetoIncidente<?>> objetosIncidentes = getListaObjetosIncidentes();
			this.setObjetosIncidentes(objetosIncidentes);
			categoriasRestantes = this.getAgrupadoresLocaisCategoriasRestantes();
			this.setCategoriasRestantes(categoriasRestantes);
			categoriasAdicionadas = this.getAgrupadoresLocaisCategoriasAdicionadas();
			this.setCategoriasAdicionadas(categoriasAdicionadas);
		} catch (ServiceException e) {
			addError(e.toString());
			sendToErrors();
		}
	}
	
	public boolean isTemCategoria() {
		return temCategoria;
	}
	
	public void setTemCategoria(boolean temCategoria) {
		this.temCategoria = temCategoria;
	}	
	
	public List<ObjetoIncidente<?>> getObjetosIncidentes() {
		return objetosIncidentes;
	}

	public void setObjetosIncidentes(List<ObjetoIncidente<?>> objetosIncidentes) {
		this.objetosIncidentes = objetosIncidentes;
	}
	
	/**
	 * Recupera todos os objetos incidentes
	 * @return List<ObjetoIncidente<?>> 
	 */
	protected List<ObjetoIncidente<?>> getListaObjetosIncidentes() {
		List<ObjetoIncidente<?>> objetosIncidentes = new ArrayList<ObjetoIncidente<?>>();
		for (ObjetoIncidenteDto oiDto : getResources()) {
			ObjetoIncidente<?> objetoIncidente = objetoIncidenteService.recuperarObjetoIncidentePorId(oiDto.getId());
			objetosIncidentes.add(objetoIncidente);
		}
		return objetosIncidentes;
	}	
	
	protected List<AgrupadorLocal> getAgrupadoresLocaisCategoriasRestantes() throws ServiceException{
		long idSetor = this.getSetorUsuario();		
		List<Agrupador> categoriasRestantes = agrupadorService.recuperarCategoriasDoSetor(idSetor);
		
		List<AgrupadorLocal> retorno = new ArrayList<AgrupadorLocal>();
				
		for (Agrupador categoriaRestante : categoriasRestantes) {
			AgrupadorLocal agrupadorLocal = new AgrupadorLocal(categoriaRestante);
			retorno.add(agrupadorLocal);
		}
		
		return retorno;
	}
	
	protected List<AgrupadorLocal> getAgrupadoresLocaisCategoriasAdicionadas() throws ServiceException{
		//List<ObjetoIncidente<?>> objetosIncidentes = getListaObjetosIncidentes();
		List<AgrupadorLocal> categoriasAdicionadas = getCategoriasAdicionadasEmCatSelecionada(objetosIncidentes);
		
		int qtdObjetosIncidentes = objetosIncidentes.size();
		List<AgrupadorLocal> retorno = getCategoriasAdicionadasEmTodosObjetosIncidentes(qtdObjetosIncidentes,categoriasAdicionadas);
		
		return retorno;
	}

	protected List<AgrupadorLocal> getCategoriasAdicionadasEmTodosObjetosIncidentes(int qtdObjetosIncidentes
			                                                                       ,List<AgrupadorLocal> categoriasAdicionadas) {
		List<AgrupadorLocal> retorno = new ArrayList<AgrupadorLocal>();
		for (AgrupadorLocal categoria : categoriasAdicionadas){
			int qtdObjetosIncidentesCategoria = categoria.getQtdObjIncidentes();
			if(qtdObjetosIncidentes == qtdObjetosIncidentesCategoria ){
				retorno.add(categoria);
			}
		}
		return retorno;
	}

	protected List<AgrupadorLocal> getCategoriasAdicionadasEmCatSelecionada(List<ObjetoIncidente<?>> objetosIncidentes) throws ServiceException {
		List<AgrupadorLocal> categoriasAdicionadas = new ArrayList<AgrupadorLocal>();		
		for (ObjetoIncidente<?> objetoIncidente : objetosIncidentes) {
			List<Agrupador> listaCategoriasObjetosIncidentes = agrupadorService.recuperarCategoriasDoObjetoIncidente(objetoIncidente.getId());
			for (Agrupador categoriaRestante : listaCategoriasObjetosIncidentes) {
				this.setTemCategoria(true);
				AgrupadorLocal agrupadorLocal = new AgrupadorLocal(categoriaRestante);
				int categoriaNaoIncluida = categoriasAdicionadas.indexOf(agrupadorLocal);
				if(categoriaNaoIncluida < 0){
					agrupadorLocal.setQtdObjIncidentes(1);
					categoriasAdicionadas.add(agrupadorLocal);
				}else{
					int qtdObjIncidentes = categoriasAdicionadas.get(categoriaNaoIncluida).getQtdObjIncidentes();
					categoriasAdicionadas.get(categoriaNaoIncluida).setQtdObjIncidentes(qtdObjIncidentes+1);
				}
			}
		}
		return categoriasAdicionadas;
	}	
	
	protected long getSetorUsuario() throws ServiceException{		
		Long setorId = -1L;
		if (usuarioLogadoService.getMinistro() != null && usuarioLogadoService.getMinistro().getSetor() != null) {
			setorId = usuarioLogadoService.getMinistro().getSetor().getId();
		}else{
			throw new ServiceException(MSG_ERRO_NAO_FOI_POSSIVEL_CARREGAR_CAT_GABINETE);
		}
		return setorId;
	}

	/**
	 * Salva a configuração de categorização
	 */
	public void salvarCategorizacao() throws ServiceException {
		if (categoriasAdicionadas.isEmpty() && (temCategoria == false) ) {
			addError(MSG_ERRO_CATEGORIA_NAO_SELECIONADA);
		}else if(categoriasAdicionadas.size() > 1){
			addError(MSG_ERRO_APENAS_UMA_CATEGORIA);
		}else{				
			//List<ObjetoIncidente<?>> objetosIncidentes = getListaObjetosIncidentes();			
			for (ObjetoIncidente<?> objetoIncidente : objetosIncidentes) {
				Long idObjetoIncidente = objetoIncidente.getId();
				
				List<Agrupador> objetoIncidenteListaCategorias = agrupadorService.recuperarCategoriasDoObjetoIncidente(idObjetoIncidente);
				salvarCategorizacaoExcluir(idObjetoIncidente,objetoIncidenteListaCategorias);
				
				objetoIncidenteListaCategorias = agrupadorService.recuperarCategoriasDoObjetoIncidente(idObjetoIncidente);
				if(objetoIncidenteListaCategorias.isEmpty() && (categoriasAdicionadas.size() < 2)){				
					salvarCategorizacaoInclusao(idObjetoIncidente,objetoIncidenteListaCategorias);
					this.inserirObjetoIncidenteemPreListaJulgamento(objetoIncidente);
				}else{
					addError(MSG_ERRO_APENAS_UMA_CATEGORIA);
				}
				
			}
		}//fim else
		
		// Exibe mensagens
		if (hasMessages()) {
			sendToErrors();
		} else {
			sendToConfirmation();
		}
	}

	protected void inserirObjetoIncidenteemPreListaJulgamento(ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		PreListaJulgamento preListaJulgamento = (categoriasAdicionadas.size() == 1) ? preListaJulgamentoService.recuperarPreListaPorCategoria(categoriasAdicionadas.get(0).getAgrupador()) : null;								
		preListaJulgamentoObjetoIncidenteService.inserirObjetoIncidenteemPreListaJulgamento(
					preListaJulgamento == null? null: preListaJulgamento.getId() , objetoIncidente, PreListaJulgamentoMotivoAlteracao.AUTOMATICA);
	}	
	
	protected void salvarCategorizacaoInclusao(Long idObjetoIncidente
			                                  ,List<Agrupador> objetoIncidenteListaCategorias) throws ServiceException {
		for (AgrupadorLocal agrupLocalAdicional : categoriasAdicionadas) {
			Long idCatAdic = agrupLocalAdicional.getAgrupador().getId();
			boolean categoriaNovaObjetoIncidente = !objetoIncidenteListaCategorias.contains(agrupLocalAdicional.getAgrupador());
			if(categoriaNovaObjetoIncidente){
				agrupadorService.inserirObjetoIncidenteNoAgrupador(idObjetoIncidente, idCatAdic);
			}
		}
	}

	protected void salvarCategorizacaoExcluir(Long idObjetoIncidente
											 ,List<Agrupador> objetoIncidenteListaCategorias) throws ServiceException {
		for (Agrupador catObjIncidente : objetoIncidenteListaCategorias) {
			Long idCatObjIncidente = catObjIncidente.getId();
			AgrupadorLocal agrupadorLocal = new AgrupadorLocal(catObjIncidente);
			boolean categoriaRemovida = categoriasRestantes.contains(agrupadorLocal);
			if(categoriaRemovida){
				agrupadorService.removerCategoriaDoObjetoIncidente(idObjetoIncidente, idCatObjIncidente);
			}
		}
	}

	/**
	 * Seleciona as categorias que foram adicionadas aos processos atuais
	 */
	public void adicionarSelecionados() {
		List<AgrupadorLocal> remover = new ArrayList<AgrupadorLocal>();
		
		for (AgrupadorLocal categoria : this.getCategoriasRestantes()) {
			if (categoria.isSelected()) {
				remover.add(categoria);
				categoriasAdicionadas.add(categoria);
				categoria.setSelected(false);
			}
		}
		
		for (AgrupadorLocal categoria : remover) {
			categoriasRestantes.remove(categoria);
		}
	}
	
	/**
	 * Remove as categorias selecionadas dos processos atuais
	 */
	public void removerSelecionados() {
		List<AgrupadorLocal> remover = new ArrayList<AgrupadorLocal>();
		
		for (AgrupadorLocal categoria : this.getCategoriasAdicionadas()) {
			if (categoria.isSelected()) {
				remover.add(categoria);
				categoriasRestantes.add(categoria);
				categoria.setSelected(false);
			}
		}
		
		for (AgrupadorLocal categoria : remover) {
			categoriasAdicionadas.remove(categoria);
		}
	}
	
	/**
	 * Seleciona as categorias que não foram selecionadas para os processo atuais
	 * 
	 * @return List<CategoriaDto> Categorias não selecionadas
	 */
	public List<AgrupadorLocal> getCategoriasRestantes() {
		// Remove as categorias selecionadas
		for (AgrupadorLocal categoria : this.getCategoriasAdicionadas()) {
			categoriasRestantes.remove(categoria);
		}
		
		return categoriasRestantes;
	}
	
	public void setCategoriasRestantes(List<AgrupadorLocal> categoriasRestantes) {
		this.categoriasRestantes = categoriasRestantes;
	}	
	
	/**
	 * Seleciona as categorias que foram selecionadas para os processo atuais
	 * 
	 * @return List<CategoriaDto> Categorias selecionadas
	 */
	public List<AgrupadorLocal> getCategoriasAdicionadas() {
		return this.categoriasAdicionadas;
	}

	public void setCategoriasAdicionadas(List<AgrupadorLocal> categoriasAdicionadas) {
		this.categoriasAdicionadas = categoriasAdicionadas;
	}	
	
	/**
	 * Marca todas as categorias não adicionadas
	 */
	public void marcarTodasRestantes() {
		this._selecionarTodas(this.getCategoriasRestantes());
	}
	
	/**
	 * Marca todas as categorias adicionadas
	 */
	public void marcarTodasAdicionadas() {
		this._selecionarTodas(this.getCategoriasAdicionadas());
	}
	
	/**
	 * Seleciona todas as categorias de uma lista
	 * @param List<CategoriaDto> lista Lista que deve ser marcada como selecionada
	 */
	private void _selecionarTodas(List<AgrupadorLocal> lista) {
		for (AgrupadorLocal categoria : lista) {
			categoria.setSelected(true);
		}
	}
}
