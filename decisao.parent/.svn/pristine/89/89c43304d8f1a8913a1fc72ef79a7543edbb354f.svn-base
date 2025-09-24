package br.jus.stf.estf.decisao.support.action.handlers;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.ministro.model.service.MinistroService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.support.security.Principal;
import br.jus.stf.estf.decisao.support.util.NestedRuntimeException;


@Component
public class CheckRelatorOrRevisorIdHandler implements ActionConditionHandler<CheckRelatorOrRevisorId> {
	
	@Autowired
	private MinistroService ministroService;
	
	@Override
	public <T> boolean matches(CheckRelatorOrRevisorId annotation, Set<T> resources, Class<T> resourceClass, Map<?, ?> options) {
		try {
			if (resourceClass.isAssignableFrom(ObjetoIncidenteDto.class)) {
				for (T t : resources) {
					ObjetoIncidenteDto objetoIncidenteDto = (ObjetoIncidenteDto) t;
					if (objetoIncidenteDto != null) {
						// Recuperar revisor
						Ministro revisor = ministroService.recuperarMinistroRevisorIncidente(objetoIncidenteDto.getId());
						
						if ((objetoIncidenteDto.getIdRelator() != null && getMinistro() != null && objetoIncidenteDto.getIdRelator().equals(getMinistro().getId()))
								|| (revisor != null && getMinistro() != null && revisor.getId().equals(getMinistro().getId()))) {
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
		} catch (ServiceException e) {
			throw new NestedRuntimeException(e);
		}
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
	public Class<CheckRelatorOrRevisorId> getAnnotation() {
		return CheckRelatorOrRevisorId.class;
	}

}
