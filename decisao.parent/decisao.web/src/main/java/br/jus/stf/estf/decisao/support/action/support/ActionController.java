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
 * Controller utilizado para definir a lista de ações que será apresentada em função do tipo de recurso
 * e dos diversos handlers (segurança e etc).
 * 
 * <p>Os controller´s são de escopo "prototype" o que significa que serão criados sempre que
 * forem solicitadas.
 * 
 * <p>Serão mantidos em mapa de controller´s na classe {@link PrincipalFacesBean}. Esse
 * mapa armazena um controller por tipo de dado. Como os controller são mantidos
 * no escopo do {@link PrincipalFacesBean}, seu ciclo de vida está relacionado
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
	 * Armazena uma lista de ordanada de todas ações definidas para a entidade (resourceClass) em questão.
	 */
	private Set<ActionInterface<T>> allActions = new TreeSet<ActionInterface<T>>(new Comparator<ActionInterface<T>>() {
		public int compare(ActionInterface<T> a1, ActionInterface<T> a2) {
			return Collator.getInstance().compare(a1.getDefinition().getId(), a2.getDefinition().getId());
		}
	});

	/** 
	 * Armazenda todos os handlers declarados no contexto do projetos. O mapa de handler armazenará um
	 * par Anotação/Handler. Ex.: RequiresResources/RequiresResourcesHandler
	 */
	private Map<Class<?>, ActionConditionHandler<?>> handlers = new HashMap<Class<?>, ActionConditionHandler<?>>();

	/**
	 * Opção são parâmetros adicionais que podemos enviar aos handlers.
	 */
	private Map<Object, Object> options = new HashMap<Object, Object>();

	/**
	 * Cada ActionController manipula ações para um dado tipo de recurso.
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
	 * Define a lista de ações em função dos recursos de entra e de diversos handlers 
	 * definidos para todos os tipo recursos.
	 * 
	 * @param resources os recursos de entrada
	 * @return a lista de ações
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
	 * Verifica se uma ação de ser exibida considerados os recursos de entradas.
	 * 
	 * <p>A ação e os recurso são submetidos aos handlers que definirão se
	 * a ação deve ser exibida ou não.
	 * 
	 * <p>Os handlers são definidos em função das anotações adicionadas
	 * à classe da ação. A cada anotação está relacionada um
	 * handler.
	 * 
	 * @param action a ação avaliada
	 * @param resources os recursos selecionados
	 * @return true, se a ação deve ser lista, false, caso contrário
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
	 * Submete a ação a um dado handler, informando a anotação do handler e os recursos
	 * selecionados. O handler definirá se a ação deve, ou não, ser listada.
	 * 
	 * @param <A> o tipo da anotação
	 * @param a a anotação
	 * @param resources os recursos selecionados
	 * @param handler o handler
	 * @return true, se aprovado, false, caso contrário
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
	 * Opções poderão ser passada aos handlers. Esse método adiciona um opção ao
	 * mapa de opções.
	 * 
	 * @param key o identificador da opção
	 * @param value o valor da opção
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
