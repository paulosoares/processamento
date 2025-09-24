package br.jus.stf.estf.decisao.support.action.support;

import java.util.Map;
import java.util.Set;

/**
 * Abstração que será utilizada para implementação das ações.
 * 
 * @author Rodrigo Barreiros
 * @since 24.05.2010
 *
 * @param <T> o tipo do recurso manipulado pela ação
 */
public interface ActionInterface<T> {
	
	/**
	 * Recupera as metainformações definidas para a ação na anotação {@link Action}
	 * 
	 * @return as metainformações da ação
	 */
	public ActionDefinition getDefinition();
    
    /**
     * Seta os recursos que foram selecionados na tela para manipulação pela ação.
     * 
     * @param resources os recursos para a ação
     */
    public void setResources(Set<T> resources);

    /**
     * Recupera os recursos submetidos à ação.
     * 
     * @return os recursos da ação
     */
    public Set<T> getResources();
    
    /**
     * Recupera o tipo dos recursos manipulados pela ação.
     * 
     * @return o tipo dos recursos
     */
    public Class<T> getResourceClass();
    
    /**
     * Chamado assim que a ação é carregada para execução.
     */
    public void load();
     
    /**
     * @param options
     */
    public void setOptions(Map<Object, Object> options);
    
    /**
     * Setar "true" se a action for apresentada em um iframe (default=false)
     * 
     * @param actionFrame
     */
    public void setActionFrame(boolean actionFrame);

}
