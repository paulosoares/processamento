package br.jus.stf.estf.decisao.support.action.support;

import java.lang.annotation.Annotation;
import java.text.Collator;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.jus.stf.estf.decisao.pesquisa.domain.AllResourcesDto;
import br.jus.stf.estf.decisao.pesquisa.web.PrincipalFacesBean;
import br.jus.stf.estf.decisao.support.action.handlers.ActionConditionHandler;
import br.jus.stf.estf.decisao.support.query.Selectable;

/**
 * Controller utilizado para definir a lista de a��es que ser� apresentada em fun��o do tipo de recurso
 * e dos diversos handlers (seguran�a e etc).
 * 
 * <p>Os controller�s s�o de escopo "prototype" o que significa que ser�o criados sempre que
 * forem solicitadas.
 * 
 * <p>Ser�o mantidos em mapa de controller�s na classe {@link PrincipalFacesBean}. Esse
 * mapa armazena um controller por tipo de dado. Como os controller s�o mantidos
 * no escopo do {@link PrincipalFacesBean}, seu ciclo de vida est� relacionado
 * ao ciclo de vida dele.
 * 
 * @author Rodrigo.Barreiros
 * @since 18.05.2010
 * 
 * @param <T> o tipo de recurso do controller
 */
@Component("actionController")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ActionController<T extends Selectable> implements InitializingBean {

	private Log logger = LogFactory.getLog(ActionController.class);

	/**
	 * Armazena uma lista de ordanada de todas a��es definidas para a entidade (resourceClass) em quest�o.
	 */
	private Set<ActionInterface<T>> allActions = new TreeSet<ActionInterface<T>>(new Comparator<ActionInterface<T>>() {
		public int compare(ActionInterface<T> a1, ActionInterface<T> a2) {
			return Collator.getInstance().compare(a1.getDefinition().getId(), a2.getDefinition().getId());
		}
	});

	/** 
	 * Armazenda todos os handlers declarados no contexto do projetos. O mapa de handler armazenar� um
	 * par Anota��o/Handler. Ex.: RequiresResources/RequiresResourcesHandler
	 */
	private Map<Class<?>, ActionConditionHandler<?>> handlers = new HashMap<Class<?>, ActionConditionHandler<?>>();

	/**
	 * Op��o s�o par�metros adicionais que podemos enviar aos handlers.
	 */
	private Map<Object, Object> options = new HashMap<Object, Object>();

	/**
	 * Cada ActionController manipula a��es para um dado tipo de recurso.
	 */
	private Class<T> resourceClass;

	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * Construtor default. Seta o tipo de recurso do controller.
	 * 
	 * @param resourceClass o tipo de recurso
	 */
	public ActionController(Class<T> resourceClass) {
		this.resourceClass = resourceClass;
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void afterPropertiesSet() throws Exception {
		Map<String, ActionConditionHandler> beans = applicationContext.getBeansOfType(ActionConditionHandler.class);
		for (ActionConditionHandler handler : beans.values()) {
			handlers.put(handler.getAnnotation(), handler);
		}

		Map<String, ActionInterface<T>> actions = applicationContext.getBeansOfType(ActionInterface.class);
		for (ActionInterface<T> action : actions.values()) {
			if (resourceClass.equals(action.getResourceClass())
					|| AllResourcesDto.class.equals(action.getResourceClass())) {
				allActions.add(action);
			}
		}
	}

	/**
	 * Define a lista de a��es em fun��o dos recursos de entra e de diversos handlers 
	 * definidos para todos os tipo recursos.
	 * 
	 * @param resources os recursos de entrada
	 * @return a lista de a��es
	 */
	public List<ActionInterface<T>> getActions(Set<T> resources) {
		List<ActionInterface<T>> allowedActions = new LinkedList<ActionInterface<T>>();
		for (ActionInterface<T> action : allActions) {
			if (matches(action, resources)) {
				action.setResources(resources);
				allowedActions.add(action);
			}
		}
		return allowedActions;
	}

	/**
	 * Verifica se uma a��o de ser exibida considerados os recursos de entradas.
	 * 
	 * <p>A a��o e os recurso s�o submetidos aos handlers que definir�o se
	 * a a��o deve ser exibida ou n�o.
	 * 
	 * <p>Os handlers s�o definidos em fun��o das anota��es adicionadas
	 * � classe da a��o. A cada anota��o est� relacionada um
	 * handler.
	 * 
	 * @param action a a��o avaliada
	 * @param resources os recursos selecionados
	 * @return true, se a a��o deve ser lista, false, caso contr�rio
	 */
	private boolean matches(ActionInterface<T> action, Set<T> resources) {
		Annotation[] annotations = action.getClass().getAnnotations();
		for (Annotation annotation : annotations) {
			ActionConditionHandler<?> handler = handlers.get(annotation.annotationType());
			if (!matches(annotation, resources, handler)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Submete a a��o a um dado handler, informando a anota��o do handler e os recursos
	 * selecionados. O handler definir� se a a��o deve, ou n�o, ser listada.
	 * 
	 * @param <A> o tipo da anota��o
	 * @param a a anota��o
	 * @param resources os recursos selecionados
	 * @param handler o handler
	 * @return true, se aprovado, false, caso contr�rio
	 */
	private <A> boolean matches(Annotation a, Set<T> resources, ActionConditionHandler<A> handler) {
		if (resources == null) {
			return false;
		}
		if (handler != null) {
			return handler.matches(handler.getAnnotation().cast(a), resources, resourceClass, options);
		}
		return true;
	}

	/**
	 * Op��es poder�o ser passada aos handlers. Esse m�todo adiciona um op��o ao
	 * mapa de op��es.
	 * 
	 * @param key o identificador da op��o
	 * @param value o valor da op��o
	 */
	public void addOption(Object key, Object value) {
		options.put(key, value);
	}

	/**
	 * Tipo de recurso do controller. 
	 * 
	 * @return tipo de recurso
	 */
	public Class<T> getResourceClass() {
		return resourceClass;
	}

	public Map<Object, Object> getOptions() {
		return options;
	}
}
