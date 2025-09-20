/**
 * 
 */
package br.gov.stf.estf.julgamento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.julgamento.InformacaoPautaProcesso;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.SubtemaPauta;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * @author Paulo.Estevao
 * @since 29.06.2011
 */
public interface InformacaoPautaProcessoDao extends GenericDao<InformacaoPautaProcesso, Long> {

	InformacaoPautaProcesso recuperar(ObjetoIncidente<?> objetoIncidente) throws DaoException;

	List<ObjetoIncidente<?>> recuperarProcessosJulgamentoConjunto(
			Long seqListaJulgamentoConjunto) throws DaoException;

	Long recuperarProximaSequenceListaJulgamentoConjunto() throws DaoException;
	public Long recuperarQtdProcessosSubTema(SubtemaPauta subTemaPauta) throws DaoException;
	public void refresh(InformacaoPautaProcesso informacaoPautaProcesso) throws DaoException;
	
}
