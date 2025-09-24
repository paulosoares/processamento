package br.jus.stf.estf.decisao.support.action.handlers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.security.PermissionChecker;
import br.jus.stf.estf.decisao.support.security.Principal;

/**
 * Trata ação marcadas com {@link Restrict}
 * 
 * <p>Executa validações de segurança para determinar se uma ação pode ser executada
 * pelo usuário corrente.
 * 
 * @author Rodrigo Barreiros
 * @since 24.05.2010
 * 
 * @see ActionIdentification
 */
@Component
public class RestrictHandler implements ActionConditionHandler<Restrict> {
	
	

	@Autowired
	private PermissionChecker permissionChecker;
	
	/**
	 * @see br.jus.stf.estf.decisao.support.action.handlers.ActionConditionHandler#matches(java.lang.Object, java.util.Set, java.lang.Class, java.util.Map)
	 */
	@Override
	public <T> boolean matches(Restrict annotation, Set<T> resources, Class<T> resourceClass, Map<?, ?> options) {
		List<ActionIdentification> transations = Arrays.asList(annotation.value());
		for (ActionIdentification transation : transations) {
			if (!permissionChecker.hasPermission((Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal(), transation)) {
				return false;
			}
		}
		return true;
		
//		return permissionChecker.hasPermission((Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal(), annotation.value());
    }
    
	/**
	 * @see br.jus.stf.estf.decisao.support.action.handlers.ActionConditionHandler#getAnnotation()
	 */
	@Override
	public Class<Restrict> getAnnotation() {
		return Restrict.class;
	}

}
