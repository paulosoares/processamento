package br.jus.stf.estf.decisao.support.action.handlers;

import java.util.Map;
import java.util.Set;

/**
 * Abstração para tratar ações e determinar se uma ação pode ou não ser exibida
 * dado o contexto de execução.
 * 
 * @author Rodrigo.Barreiros
 * @since 24.05.2010
 * 
 * @param <A> o tipo da anotação vinculada ao handler
 */
public interface ActionConditionHandler<A> {
    
	/**
	 * Verifica se uma dada ação pode ou não ser exibida ao usuário, dada a configuração 
	 * do handler (annotation), os recursos selecionados, o tipo de recurso e 
	 * outras opções que poderão ser utilizadas para decisão.
	 * 
	 * @param <T> o tipo de recurso
	 * @param annotation a anotação utilizada para configurar e indicar que esse
	 * handler deve tratar uma determinada ação
	 * @param resources o recursos selecionada na tela
	 * @param resourceClass o tipo de recurso selecionado
	 * @param options outras opções utilizadas para decisão
	 * 
	 * @return true, se a ação deve ser listada, false, caso contrário
	 */
	<T> boolean matches(A annotation, Set<T> resources, Class<T> resourceClass, Map<?, ?> options);
    
    /**
     * Retorna a anotação utilizada para configurar e indicar que esse
	 * handler deve tratar uma determinada ação.
	 * 
     * @return a anotação associada ao handler
     */
    Class<A> getAnnotation();

}
