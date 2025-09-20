package br.gov.stf.estf.julgamento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.julgamento.JulgamentoProcesso;
import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.VotoJulgamentoProcesso;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface VotoJulgamentoProcessoDao extends GenericDao<VotoJulgamentoProcesso, Long> {

	public Long getProximaOrdemVoto(JulgamentoProcesso julgamentoProcesso) throws DaoException;

	public boolean temVotoMinistroProcesso(ObjetoIncidente objetoIncidente, Ministro ministro) throws DaoException;

	public List<VotoJulgamentoProcesso> listarRascunhosDoMinistroNaLista(Ministro ministro, ListaJulgamento listaJulgamento) throws DaoException;
}
