/**
 * 
 */
package br.jus.stf.estf.decisao.objetoincidente.service.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.ministro.model.service.MinistroService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.objetoincidente.service.ObjetoIncidenteService;
import br.jus.stf.estf.decisao.objetoincidente.service.RelatorioProcessoService;
import br.jus.stf.estf.decisao.objetoincidente.support.ProcessoReport;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;

/**
 * @author Paulo.Estevao
 * @since 29.09.2010
 */
@Service("relatorioProcessoService")
public class RelatorioProcessoServiceImpl implements RelatorioProcessoService {

	@Qualifier("objetoIncidenteServiceLocal")
	@Autowired
	private ObjetoIncidenteService objetoIncidenteService;
	
	@Autowired
	private MinistroService ministroService;

	/* (non-Javadoc)
	 * @see br.jus.stf.estf.decisao.objetoincidente.service.RelatorioProcessoService#recuperaProcessoReport(java.util.Collection)
	 */
	@Override
	public List<ProcessoReport> recuperaProcessoReport(Collection<ObjetoIncidenteDto> listaProcessos)
			throws ServiceException {
		List<ProcessoReport> listaProcessoReport = new LinkedList<ProcessoReport>();

		for (ObjetoIncidenteDto oi : listaProcessos) {
			ObjetoIncidente<?> processoRecarregado = objetoIncidenteService.recuperarObjetoIncidentePorId(oi.getId());
			Ministro ministro = ministroService.recuperarPorId(oi.getIdRelator());
			listaProcessoReport.add(new ProcessoReport(processoRecarregado,	ministro));
		}

		return listaProcessoReport;
	}
}
