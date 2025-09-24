package br.jus.stf.estf.decisao.support.action.handlers;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.gov.stf.estf.documento.model.service.TextoService;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.query.Dto;
import br.jus.stf.estf.decisao.support.security.Principal;
import br.jus.stf.estf.decisao.support.util.NestedRuntimeException;


/**
 * Trata a��es marcadas com {@link CheckMinisterId}
 * 
 * <p>
 * Executa valida��o de seguran�a para determinar se uma a��o pode ser
 * executada pelo usu�rio de acordo com o c�digo do ministro que ele est� logado e de um determinado texto.
 * 
 * <p>
 *
 * @author Rodrigo Lisboa
 * @since 13.08.2010
 * 
 */
@Component
public class CheckMinisterIdHandler implements ActionConditionHandler<CheckMinisterId> {
	
	@Autowired
	private TextoService textoService;
	

	@Override
	public <T> boolean matches(CheckMinisterId annotation, Set<T> resources,
			Class<T> resourceClass, Map<?, ?> options) {
		
		try {
		
			if (resourceClass.isAssignableFrom(TextoDto.class)) {
				for (T t : resources) {
					TextoDto textoDto = (TextoDto) t;
					
					if (textoDto != null) {
						if (textoDto.isFake()) {
							return false;
						}
						
						Texto texto = textoService.recuperarPorId(((Dto) t).getId());
						
						// verifica se o c�digo do ministro do usu�rio logado e do texto s�o os mesmos
						if (texto != null && texto.getMinistro() != null && texto.getMinistro().getId().longValue() > 0 
								&& texto.getMinistro().equals(getMinistro())) {
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
	 * Recupera o usu�rio autenticado. Esse usu�rio � encapsulado em um objeto
	 * Principal que cont�m as credenciais do usu�rio.
	 * 
	 * @return o principal
	 */
	private Principal getPrincipal() {
		return (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	/**
	 * Recupera o ministro cujo o gabinete o usu�rio est� lotado.
	 * 
	 * @return o ministro do usu�rio
	 */
	public Ministro getMinistro() {
		return getPrincipal().getMinistro();
	}	

	@Override
	public Class<CheckMinisterId> getAnnotation() {
		return CheckMinisterId.class;
	}

}
