package br.jus.stf.estf.decisao.mobile.assinatura.service;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.pesquisa.domain.ComunicacaoDto;

@Name("comunicacaoParaAssinaturaFacesBean")
@Scope(ScopeType.CONVERSATION)
public class ComunicacaoParaAssinaturaService {

	@In("#{assinarContingencialmenteMobileService}")
	private AssinarContingencialmenteMobileService assinarContingencialmenteMobileService;

	public List<ComunicacaoDto> getComunicacoesParaAssinar(List<Long> ids) throws ServiceException {
		return assinarContingencialmenteMobileService.getComunicacoesParaAssinar(ids);
	}

	public boolean isComunicacaoLiberadaParaAssinaturaNoGabinete(Long id) throws ServiceException {
		return getComunicacoesParaAssinar(Arrays.asList(id)).size() > 0;
	}

}
