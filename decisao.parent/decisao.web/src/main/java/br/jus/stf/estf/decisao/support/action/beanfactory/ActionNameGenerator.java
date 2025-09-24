package br.jus.stf.estf.decisao.support.action.beanfactory;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;

import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionController;
import br.jus.stf.estf.decisao.support.util.NestedRuntimeException;

/**
 * Gerador de nomes para ações (Spring Beans), que são criadas de modo customizado.
 * 
 * @author Rodrigo.Barreiros
 * @since 26.05.2010
 * 
 * @see /WEB-INF/applicationContext.xml
 * @see ActionController
 */
public class ActionNameGenerator implements BeanNameGenerator {

	/**
	 * @see org.springframework.beans.factory.support.BeanNameGenerator#generateBeanName(org.springframework.beans.factory.config.BeanDefinition, org.springframework.beans.factory.support.BeanDefinitionRegistry)
	 */
	@Override
	public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
		try {
			return Class.forName(definition.getBeanClassName()).getAnnotation(Action.class).id();
		} catch (ClassNotFoundException e) {
			throw new NestedRuntimeException(e);
		}
	}

}
