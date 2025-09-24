package br.jus.stf.estf.decisao.support.action.support;


/**
 * Interface callback usada pelo método {@link ActionSupport#execute(ActionCallback)}.
 * 
 * <p>Usada para rodar ações dentro de um contexto protegido, com tratamento de 
 * erros, envio de mensagens e navegação de fluxo de tela.
 * 
 * @author Rodrigo.Barreiros
 * @since 24.05.2010
 *
 * @param <T> o tipo de recurso
 */
public interface ActionCallback<T> {
    
    /**
     * Chamado pelo método {@link ActionSupport#execute(ActionCallback)} para executar
     * ações em um contexto protegido.
     * 
     * @param resource o recurso que será utilizado pela ação
     * @throws Exception caso ocorra algum problema
     */
    void doInAction(T resource) throws Exception;

}
