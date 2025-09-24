package br.jus.stf.estf.decisao.mobile.assinatura.service;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.mobile.assinatura.support.DocumentoDto;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;

@Name("textoParaAssinaturaService")
@Scope(ScopeType.CONVERSATION)
public class TextoParaAssinaturaService {

	@In("#{assinarContingencialmenteMobileService}")
	private AssinarContingencialmenteMobileService assinarContingencialmenteMobileService;

	public List<DocumentoDto<TextoDto>> getTextosParaAssinar(List<Long> ids) throws ServiceException {
		return assinarContingencialmenteMobileService.getTextosParaAssinar(ids);
	}

	public boolean isTextoLiberadoParaAssinaturaNoGabinete(Long id) throws ServiceException {
		return getTextosParaAssinar(Arrays.asList(id)).size() > 0;
	}

}
