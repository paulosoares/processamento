/**
 * 
 */
package br.jus.stf.estf.decisao.texto.service.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.ministro.model.service.MinistroService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.security.Principal;
import br.jus.stf.estf.decisao.texto.service.RelatorioTextoService;
import br.jus.stf.estf.decisao.texto.service.TextoService;
import br.jus.stf.estf.decisao.texto.support.TextoReport;

/**
 * @author Paulo.Estevao
 * @since 01.09.2010
 */
@Service("relatorioTextoService")
public class RelatorioTextoServiceImpl implements RelatorioTextoService {

	private static final Log logger = LogFactory.getLog(RelatorioTextoServiceImpl.class);
	
	@Qualifier("textoServiceLocal")
	@Autowired
	private TextoService textoService;
	
	@Autowired
	private MinistroService ministroService;

	public List<TextoReport> recuperaTextoReport(Collection<TextoDto> listaTextos) throws ServiceException {
		
		List<TextoReport> listaTextoReport = new LinkedList<TextoReport>();
		
		int i = 0;
		
		for(TextoDto texto : listaTextos){
			Texto textoRecarregado = textoService.recuperarTextoPorId(texto.getId());
			
			if (textoRecarregado != null && !textoRecarregado.getMinistro().getSetor().getId().equals(getPrincipal().getMinistro().getSetor().getId()))
				textoRecarregado.setObservacao(null);
			
			Ministro ministro = ministroService.recuperarPorId(texto.getIdMinistro());
			listaTextoReport.add(new TextoReport(ministro, textoRecarregado));
			logger.trace("Texto: " + ++i);			
		}
		
		return listaTextoReport;
	}
	
	private Principal getPrincipal() {
		return (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
