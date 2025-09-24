package br.jus.stf.estf.decisao.support.action.handlers;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.gov.stf.estf.entidade.julgamento.JulgamentoProcesso;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoAmbienteConstante;
import br.gov.stf.estf.entidade.julgamento.TipoSituacaoProcessoSessao;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.julgamento.model.service.JulgamentoProcessoService;
import br.gov.stf.estf.processostf.model.service.PreListaJulgamentoService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.support.security.Principal;
import br.jus.stf.estf.decisao.support.util.NestedRuntimeException;

@Component
public class CheckDestaquePeloMinistroHandler implements ActionConditionHandler<CheckDestaquePeloMinistro> {

	@Autowired
	JulgamentoProcessoService julgamentoProcessoService;
	
	@Autowired
	PreListaJulgamentoService preListaJulgamentoService;

	@Override
	public <T> boolean matches(CheckDestaquePeloMinistro annotation, Set<T> resources, Class<T> resourceClass,
			Map<?, ?> options) {
		if (resourceClass.isAssignableFrom(ObjetoIncidenteDto.class)) {
			for (T t : resources) {
				ObjetoIncidenteDto objetoIncidenteDto = (ObjetoIncidenteDto) t;
				if (objetoIncidenteDto.getId() != null) {
					Processo oi = new Processo();
					oi.setId(objetoIncidenteDto.getId());

					try {
						if (getMinistro() != null) {
							JulgamentoProcesso jp = julgamentoProcessoService.pesquisaUltimoJulgamentoProcesso(oi, getMinistro(), TipoSituacaoProcessoSessao.DESTAQUE, false);
							if (jp != null 
									&& jp.getProcessoListaJulgamento() != null
									&& TipoAmbienteConstante.VIRTUAL.getSigla().equals(jp.getSessao().getTipoAmbiente()))
								return true;
						}
					} catch (ServiceException e) {
						throw new NestedRuntimeException(e);
					}
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
	public Class<CheckDestaquePeloMinistro> getAnnotation() {
		return CheckDestaquePeloMinistro.class;
	}

}
