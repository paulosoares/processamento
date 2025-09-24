package br.jus.stf.estf.decisao.support.action.support;

import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.jus.stf.estf.decisao.objetoincidente.service.ObjetoIncidenteService;
import br.jus.stf.estf.decisao.support.security.Principal;

/**
 * Classe de suporte à execução das ações. Fornece métodos utilitários
 * e templates (Template Method) para execução das ações.
 * 
 * @author Rodrigo Barreiros
 * @see 24.05.2010 
 * @param <T> o tipo de recurso da ação
 */
public class ActionSupport<T> implements ActionInterface<T> {

	protected static final String MENSAGEM_ERRO_EXECUCAO_ACAO = "[%s]";

	protected static final String MENSAGEM_ERRO_NAO_ESPECIFICADO = "Erro não especificado. Contate a Informática no ramal 3416.";

	protected final Log logger = LogFactory.getLog(getClass());

	private ActionDefinition actionDefinition;
	private Set<T> resources;
	private boolean warning;
	private boolean information;
	private boolean error;
	private Map<Object, Object> options;
	private boolean refresh;
	private boolean actionFrame = false;
	
	@Autowired
	protected ObjetoIncidenteService objetoIncidenteService;

	/**
	 * Construtor default. Popula o objeto de definições com os dados
	 * da ação.
	 */
	public ActionSupport() {
		Action metadada = this.getClass().getAnnotation(Action.class);
		actionDefinition = new ActionDefinition();
		actionDefinition.setReport(metadada.report());
		actionDefinition.setName(metadada.name());
		actionDefinition.setFacet(metadada.facet());
		actionDefinition.setHeight(metadada.height());
		actionDefinition.setWidth(metadada.width());
		actionDefinition.setView(metadada.view());
		actionDefinition.setId(metadada.id());
	}

	/**
	 * Template Method para execução de ações em um contexto "protegidos", com tratamento de erros, 
	 * envio de informação e controle de navegação.
	 * 
	 * @param callback o callback com o código da ação
	 */
	public void execute(ActionCallback<T> callback) {
		for (T resource : getResources()) {
			try {
				callback.doInAction(resource);
			} catch (Exception e) {
				logger.error(String.format(MENSAGEM_ERRO_EXECUCAO_ACAO, resource.toString()), e);
				addError(String.format(MENSAGEM_ERRO_EXECUCAO_ACAO + ": %s ", resource.toString(), getMensagemDeErroPadrao(e)));
			}
		}
		if (!hasMessages()) {
			sendToConfirmation();
		} else {
			sendToErrors();
		}
	}

	protected String getMensagemDeErroPadrao(Exception e) {
		if (e.getMessage() != null) {
			return e.getMessage();
		}
		return MENSAGEM_ERRO_NAO_ESPECIFICADO;
	}

	/**
	 * @see br.jus.stf.estf.decisao.support.action.support.ActionInterface#getResourceClass()
	 */
	@SuppressWarnings("unchecked")
	public Class<T> getResourceClass() {
		ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
		return (Class) parameterizedType.getActualTypeArguments()[0];
	}

	/**
	 * Recupera o usuário autenticado. Esse usuário é encapsulado em um objeto Principal que contém
	 * as credenciais do usuário.
	 * 
	 * @return o principal
	 */
	protected Principal getPrincipal() {
		return (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	/**
	 * Recupera o usuário autenticado.
	 * 
	 * @return o usuário logado
	 */
	public Usuario getUsuario() {
		return getPrincipal().getUsuario();
	}

	/**
	 * Recupera o ministro cujo o gabinete o usuário está lotado.
	 * 
	 * @return o ministro do usuário
	 */
	public Ministro getMinistro() {
		return getPrincipal().getMinistro();
	}
	
	/**
	 * Recupera o setor do ministro.
	 * 
	 * @return
	 */
	public Setor getSetorMinistro() {
		return objetoIncidenteService.consultarSetorPeloId(getMinistro().getSetor().getId());
	}
	
	public void addWarning(String warning){
		FacesMessages.instance().add(Severity.WARN, warning);
		this.warning = true;
	}

	/**
	 * Envia informação para apresentação pelo mecanismo de 
	 * mensagens.
	 * 
	 * @param information a informação
	 */
	public void addInformation(String information) {
		FacesMessages.instance().add(Severity.INFO, information);
		this.information = true;
	}

	/**
	 * Envia informação para apresentação pelo mecanismo de 
	 * mensagens. Essa informação será específica para 
	 * um componente visual.
	 * 
	 * @param id o identificador do componente
	 * @param information a informação
	 */
	public void addInformation(String id, String information) {
		FacesMessages.instance().addToControl(id, information);
	}

	/**
	 * Envia um mensagem de erro para apresentação pelo mecanismo de 
	 * mensagens.
	 * 
	 * @param error a mensagem de erro
	 */
	public void addError(String error) {
		getFacesMessages().add(Severity.ERROR, error);
		this.error = true;
	}

	/**
	 * Verifica se há mensagens no controle de mensagens.
	 * 
	 * @return true, se sim, false, caso contrário
	 */
	public boolean hasMessages() {
		return information || warning || error;
	}
	
	public boolean hasInformations() {
		return information;
	}
	
	public boolean hasWarnings() {
		return warning;
	}
	
	public boolean hasErrors() {
		return error;
	}
	
	protected void cleanMessages(){
		information = false;
		warning = false;
		error = false;
	}
	
	protected void cleanInformations() {
		information = false;
	}
	
	protected void cleanWarnings() {
		warning = false;
	}
	
	protected void cleanErrors() {
		error = false;
	}

	/**
	 * Redireciona para a tela de mensagem de sucesso.
	 */
	public void sendToConfirmation() {
		actionDefinition.setFacet("success");
		actionDefinition.setHeight(115);
		cleanMessages();
	}

	/**
	 * Redireciona para a tela de informações.
	 */
public void sendToInformations() {
		actionDefinition.setFacet("warnings");
		actionDefinition.setHeight(215);
		cleanInformations();
	}

	/**
	 * Redireciona para a tela de erros.
	 */
	public void sendToErrors() {
		actionDefinition.setFacet("errors");
		actionDefinition.setHeight(215);
		actionDefinition.setErrorTitle(getErrorTitle());
		cleanErrors();
	}


	/**
	 * Recupera o controlador de mensagens.
	 * 
	 * @return o controlador
	 */
	public FacesMessages getFacesMessages() {
		return FacesMessages.instance();
	}

	/**
	 * @see br.jus.stf.estf.decisao.support.action.support.ActionInterface#getDefinition()
	 */
	@Override
	public ActionDefinition getDefinition() {
		return actionDefinition;
	}

	/**
	 * @see br.jus.stf.estf.decisao.support.action.support.ActionInterface#setResources(java.util.Set)
	 */
	public void setResources(Set<T> resources) {
		this.resources = resources;
	}

	/**
	 * @see br.jus.stf.estf.decisao.support.action.support.ActionInterface#getResources()
	 */
	public Set<T> getResources() {
		return resources;
	}

	/**
	 * @see br.jus.stf.estf.decisao.support.action.support.ActionInterface#load()
	 */
	@Override
	public void load() {
	}

	/**
	 * Permite que o título da tela de erro seja customizada, caso necessário.
	 * @return
	 */
	protected String getErrorTitle() {
		return "Não foi possível aplicar a operação para os registros listados abaixo.";
	}

	@Override
	public void setOptions(Map<Object, Object> options) {
		this.options = options;
	}
	
	public Map<Object, Object> getOptions() {
		return options;
	}

	public boolean isRefresh() {
		return refresh;
	}

	public void setRefresh(boolean refresh) {
		this.refresh = refresh;
	}
	
	public boolean isActionFrame() {
		return actionFrame;
	}

	public void setActionFrame(boolean actionFrame) {
		this.actionFrame = actionFrame;
	}	

}
