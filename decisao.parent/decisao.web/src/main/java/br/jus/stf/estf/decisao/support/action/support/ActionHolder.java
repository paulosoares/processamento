package br.jus.stf.estf.decisao.support.action.support;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.springframework.context.ApplicationContext;

import br.jus.stf.estf.decisao.support.query.Selectable;
import br.jus.stf.estf.decisao.support.util.ApplicationContextUtils;

/**
 * Armazena a ação durante seu processamento.
 * 
 * @author Rodrigo.Barreiros
 * @see 20.05.2010
 *
 * @param <T> o tipo de recurso da ação
 */
@Name("actionHolder")
@Scope(ScopeType.SESSION)
public class ActionHolder<T extends Selectable> {

	private ApplicationContext context;

	private ActionInterface<T> action;

	private ActionController<? extends Selectable> actionController;

	/**
	 * Construtor default. Recupera e seta o Sprint Context que será utilizado
	 * para instanciação das ações.
	 */
	public ActionHolder() {
		this.context = ApplicationContextUtils.getApplicationContext();
	}

	/**
	 * Instancia e carrega a ação selecionada no menu de ações.
	 * 
	 * <p>Sempre devemos reinstanciar a ação para reinicar seu
	 * contexto.
	 * 
	 * @param action a ação informada
	 */
	@SuppressWarnings("unchecked")
	public void setAction(ActionInterface<T> action) {
		this.action = (ActionInterface<T>) context.getBean(action.getDefinition().getId());
		this.action.setResources(action.getResources());
		if (actionController != null) {
			this.action.setOptions(actionController.getOptions());
		}
		this.action.load();
	}

	/**
	 * Recupera a ação selecionada.
	 * 
	 * @return a ação selecionada
	 */
	public ActionInterface<?> getAction() {
		return action;
	}

	/**
	 * Limpa o context resetando a ação corrente.
	 */
	public void clean() {
		this.action = null;
	}

	public void setActionController(ActionController<? extends Selectable> actionController) {
		this.actionController = actionController;
	}
}
