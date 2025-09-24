/**
 * 
 */
package br.jus.stf.estf.decisao.objetoincidente.service;

import java.util.Collection;
import java.util.List;

import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.objetoincidente.support.ProcessoReport;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;

/**
 * @author Paulo.Estevao
 * @since 29.09.2010
 */
public interface RelatorioProcessoService {
		List<ProcessoReport> recuperaProcessoReport(Collection<ObjetoIncidenteDto> listaProcessos) throws ServiceException;
}
