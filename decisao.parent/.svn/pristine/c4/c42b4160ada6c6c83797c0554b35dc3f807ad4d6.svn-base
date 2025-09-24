package br.jus.stf.estf.decisao.support.action.handlers;

import java.util.Map;
import java.util.Set;

import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.gov.stf.estf.entidade.ministro.Ministro;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.support.security.Principal;


@Component
public class CheckRelatorIdHandler implements ActionConditionHandler<CheckRelatorId> {
	
	@Override
	public <T> boolean matches(CheckRelatorId annotation, Set<T> resources, Class<T> resourceClass, Map<?, ?> options) {
		if (resourceClass.isAssignableFrom(ObjetoIncidenteDto.class)) {
			for (T t : resources) {
				ObjetoIncidenteDto objetoIncidenteDto = (ObjetoIncidenteDto) t;
				if (objetoIncidenteDto != null) {
					if (objetoIncidenteDto.getIdRelator() != null && getMinistro() != null && objetoIncidenteDto.getIdRelator().equals(getMinistro().getId())) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		return false;
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
	 * Recupera o ministro cujo o gabinete o usuário está lotado.
	 * 
	 * @return o ministro do usuário
	 */
	public Ministro getMinistro() {
		return getPrincipal().getMinistro();
	}	

	@Override
	public Class<CheckRelatorId> getAnnotation() {
		return CheckRelatorId.class;
	}

}
