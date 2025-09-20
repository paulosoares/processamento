/**
 * 
 */
package br.gov.stf.estf.julgamento.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.julgamento.InformacaoPautaProcesso;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.SubtemaPauta;
import br.gov.stf.estf.julgamento.model.dataaccess.InformacaoPautaProcessoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * @author Paulo.Estevao
 * @since 29.06.2011
 */
public interface InformacaoPautaProcessoService extends
		GenericService<InformacaoPautaProcesso, Long, InformacaoPautaProcessoDao> {

	InformacaoPautaProcesso recuperar(ObjetoIncidente<?> objetoIncidente) throws ServiceException;
	
	List<ObjetoIncidente<?>> recuperarProcessosJulgamentoConjunto(ObjetoIncidente<?> objetoIncidente) throws ServiceException;
	List<ObjetoIncidente<?>> recuperarProcessosJulgamentoConjunto(ObjetoIncidente<?> objetoIncidente, boolean incluirProcessoAtual) throws ServiceException;
	List<ObjetoIncidente<?>> recuperarProcessosJulgamentoConjunto(Long listaJulgamentoConjunto) throws ServiceException;
	Long recuperarProximaSequenciaListaJulgamentoConjunto() throws ServiceException;
	public Long recuperarQtdProcessosSubTema(SubtemaPauta subTemaPauta) throws ServiceException;
	public void refresh(InformacaoPautaProcesso informacaoPautaProcesso) throws ServiceException;
}
