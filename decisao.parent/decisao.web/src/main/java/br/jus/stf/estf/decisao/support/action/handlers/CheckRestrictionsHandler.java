package br.jus.stf.estf.decisao.support.action.handlers;

import java.util.Map;
import java.util.Set;

import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.gov.stf.estf.entidade.documento.Texto.TipoRestricao;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.security.Principal;
import br.jus.stf.estf.decisao.texto.support.TextoRestrictionChecker;

/**
 * Trata ações marcadas com {@link CheckRestrictions}
 * 
 * <p>
 * Executa validações de segurança para determinar se uma ação pode ser
 * executada pelo usuário corrente, dados os recursos selecionados.
 * 
 * <p>
 * Para textos, devem ser consideradas as restrições de acesso.
 * 
 * @author Rodrigo Barreiros
 * @since 20.07.2010
 * 
 * @see TipoRestricao
 */
@Component
public class CheckRestrictionsHandler implements ActionConditionHandler<CheckRestrictions> {

	/**
	 * @see br.jus.stf.estf.decisao.support.action.handlers.ActionConditionHandler#matches(java.lang.Object,
	 *      java.util.Set, java.lang.Class, java.util.Map)
	 */
	@Override
	public <T> boolean matches(CheckRestrictions annotation, Set<T> resources, Class<T> resourceClass, Map<?, ?> options) {
		if (resourceClass.isAssignableFrom(TextoDto.class)) {
			for (T t : resources) {
				TextoDto textoDto = (TextoDto) t;
				if (textoDto.isFake()) {
					return false;
				}
				if (TextoRestrictionChecker.isNotAllowedFaseTransition(textoDto, getPrincipal())) {
					// Se chegou aqui, devemos negar acesso.
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Recupera o usuário autenticado. Esse usuário é encapsulado em um objeto
	 * Principal que contém as credenciais do usuário.
	 * 
	 * @return o principal
	 */
	private Principal getPrincipal() {
		return (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	/**
	 * @see br.jus.stf.estf.decisao.support.action.handlers.ActionConditionHandler#getAnnotation()
	 */
	@Override
	public Class<CheckRestrictions> getAnnotation() {
		return CheckRestrictions.class;
	}

}
