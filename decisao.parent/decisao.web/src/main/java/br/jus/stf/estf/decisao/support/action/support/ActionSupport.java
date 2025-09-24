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
 * Classe de suporte � execu��o das a��es. Fornece m�todos utilit�rios
 * e templates (Template Method) para execu��o das a��es.
 * 
 * @author Rodrigo Barreiros
 * @see 24.05.2010 
 * @param <T> o tipo de recurso da a��o
 */
public class ActionSupport<T> implements ActionInterface<T> {

	protected static final String MENSAGEM_ERRO_EXECUCAO_ACAO = "[%s]";

	protected static final String MENSAGEM_ERRO_NAO_ESPECIFICADO = "Erro n�o especificado. Contate a Inform�tica no ramal 3416.";

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
	 * Construtor default. Popula o objeto de defini��es com os dados
	 * da a��o.
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
	 * Template Method para execu��o de a��es em um contexto "protegidos", com tratamento de erros, 
	 * envio de informa��o e controle de navega��o.
	 * 
	 * @param callback o callback com o c�digo da a��o
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
	 * Recupera o usu�rio autenticado. Esse usu�rio � encapsulado em um objeto Principal que cont�m
	 * as credenciais do usu�rio.
	 * 
	 * @return o principal
	 */
	protected Principal getPrincipal() {
		return (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	/**
	 * Recupera o usu�rio autenticado.
	 * 
	 * @return o usu�rio logado
	 */
	public Usuario getUsuario() {
		return getPrincipal().getUsuario();
	}

	/**
	 * Recupera o ministro cujo o gabinete o usu�rio est� lotado.
	 * 
	 * @return o ministro do usu�rio
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
	 * Envia informa��o para apresenta��o pelo mecanismo de 
	 * mensagens.
	 * 
	 * @param information a informa��o
	 */
	public void addInformation(String information) {
		FacesMessages.instance().add(Severity.INFO, information);
		this.information = true;
	}

	/**
	 * Envia informa��o para apresenta��o pelo mecanismo de 
	 * mensagens. Essa informa��o ser� espec�fica para 
	 * um componente visual.
	 * 
	 * @param id o identificador do componente
	 * @param information a informa��o
	 */
	public void addInformation(String id, String information) {
		FacesMessages.instance().addToControl(id, information);
	}

	/**
	 * Envia um mensagem de erro para apresenta��o pelo mecanismo de 
	 * mensagens.
	 * 
	 * @param error a mensagem de erro
	 */
	public void addError(String error) {
		getFacesMessages().add(Severity.ERROR, error);
		this.error = true;
	}

	/**
	 * Verifica se h� mensagens no controle de mensagens.
	 * 
	 * @return true, se sim, false, caso contr�rio
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
	 * Redireciona para a tela de informa��es.
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
	 * Permite que o t�tulo da tela de erro seja customizada, caso necess�rio.
	 * @return
	 */
	protected String getErrorTitle() {
		return "N�o foi poss�vel aplicar a opera��o para os registros listados abaixo.";
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
