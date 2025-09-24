package br.jus.stf.estf.decisao.support.action.beanfactory;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ScopeMetadata;
import org.springframework.context.annotation.ScopeMetadataResolver;
import org.springframework.context.annotation.ScopedProxyMode;


/**
 * ScopeResolver utilizado para criar as ações. Essas ações sempre são
 * "prototype", ou seja, são criadas sempre que forem solicitadas.
 * 
 * @author Rodrigo Barreiros
 * @since 24.05.2010
 * 
 * @see /WEB-INF/applicationContext.xml
 */
public class ActionScopeResolver implements ScopeMetadataResolver {
    
    private ScopeMetadata scope;

    /**
     * @see org.springframework.context.annotation.ScopeMetadataResolver#resolveScopeMetadata(org.springframework.beans.factory.config.BeanDefinition)
     */
    public ScopeMetadata resolveScopeMetadata(BeanDefinition definition) {
        if (scope == null) {
            scope = new ScopeMetadata();
            scope.setScopedProxyMode(ScopedProxyMode.NO);
            scope.setScopeName("prototype");
        }
        return scope;
    }

}
