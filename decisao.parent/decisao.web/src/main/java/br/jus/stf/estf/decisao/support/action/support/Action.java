package br.jus.stf.estf.decisao.support.action.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indica que a classe anotada é uma Ação.
 * 
 * @author Rodrigo.Barreiros
 * @since 24.05.2010
 * 
 * @see ActionController
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Action {
    
    /**
     * O identificador da ação. A ação pode ser acessada na camada de apresentação usando
     * esse identificador
     * @return id da ação
     */
    String id();
    
    
    /**
     * O nome da ação. É esse nome que será apresentado no menu de ações
     * @return o nome da ação
     */
    String name();
    
    
    /**
     * A página facelets que será utilizada para entrada de informações 
     * necessárias à execução da ação
     * @return
     */
    String view() default "";
    
    
    /**
     * Indica que página (facet) deve ser exibida em um dado momento 
     * da execução da ação. Muitas ações utilizam várias páginas,
     * como em um wizard e precisa controlar página deve ser
     * apresentada e quando.
     * @return a página que será exibida
     */
    String facet() default "principal";
    
    /**
     * A página da ação é exibida em uma popup. Indica o tamanho da popup.
     * @return o tamanho da popup
     */
    int height() default 180;
    
    
    /**
     * A página da ação é exibida em uma popup. Indica a largura da popup.
     * @return a largura da popup
     */
    int width() default 500;
    
    /**
     * Indica se a ações exibirá algum relatório.
     * @return true, se exibir, false, caso contrário
     */
    boolean report() default false;

}
