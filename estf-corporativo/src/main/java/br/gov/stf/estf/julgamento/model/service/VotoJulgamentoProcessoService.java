package br.gov.stf.estf.julgamento.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.julgamento.JulgamentoProcesso;
import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.VotoJulgamentoProcesso;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.julgamento.model.dataaccess.VotoJulgamentoProcessoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface VotoJulgamentoProcessoService extends
		GenericService<VotoJulgamentoProcesso, Long, VotoJulgamentoProcessoDao> {

	public Long getProximaOrdemVoto(JulgamentoProcesso julgamentoProcesso)
			throws ServiceException;

	public boolean temVotoMinistroProcesso(ObjetoIncidente objetoIncidente, Ministro ministro) throws ServiceException;

	public List<VotoJulgamentoProcesso> listarRascunhosDoMinistroNaLista(Ministro ministro, ListaJulgamento listaJulgamento) throws ServiceException;

}
