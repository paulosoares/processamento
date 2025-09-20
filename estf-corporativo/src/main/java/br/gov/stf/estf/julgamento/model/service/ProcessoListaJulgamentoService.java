/**
 * 
 */
package br.gov.stf.estf.julgamento.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.ProcessoListaJulgamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.julgamento.model.dataaccess.ProcessoListaJulgamentoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ProcessoListaJulgamentoService extends GenericService<ProcessoListaJulgamento, Long, ProcessoListaJulgamentoDao> {

	public ProcessoListaJulgamento recuperar(ObjetoIncidente<?> objetoIncidente) throws ServiceException;

	public ProcessoListaJulgamento recuperarProcessoListaJulgamento(ObjetoIncidente<?> incidente, ListaJulgamento listaJulgamento) throws ServiceException;
	
	public List<ProcessoListaJulgamento> listarProcessos(ListaJulgamento listaJulgamento) throws ServiceException;

	public void clonarManifestacoes(ListaJulgamento novaListaJulgamento, ListaJulgamento listaJulgamentoOriginal) throws ServiceException;
}
