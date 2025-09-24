/**
 * 
 */
package br.jus.stf.estf.decisao.objetoincidente.service;

import java.util.List;
import java.util.Set;

import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.api.ReferendarDecisaoDto;
import br.jus.stf.estf.decisao.api.ReferendarDecisaoResultadoDto;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.security.Principal;

public interface ReferendarDecisaoService {
		public List<ReferendarDecisaoResultadoDto> referendarDecisao(List<ReferendarDecisaoDto> lista, Principal principal) throws ServiceException;
		public List<ReferendarDecisaoResultadoDto> desfazerReferendarDecisao(List<ReferendarDecisaoDto> lista, Principal principal) throws ServiceException;
		public ReferendarDecisaoResultadoDto referendarDecisao(ReferendarDecisaoDto referendarDecisaoDto, Principal principal) throws ServiceException;
		List<ReferendarDecisaoResultadoDto> referendarDecisoesMonocraticas(Set<TextoDto> textos, Principal principal) throws ServiceException;
		List<ReferendarDecisaoResultadoDto> desfazerReferendarDecisaoMonocratica(TextoDto texto, Principal principal) throws ServiceException;
}
