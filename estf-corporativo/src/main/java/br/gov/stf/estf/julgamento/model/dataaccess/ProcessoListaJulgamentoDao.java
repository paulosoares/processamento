package br.gov.stf.estf.julgamento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.ProcessoListaJulgamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ProcessoListaJulgamentoDao extends GenericDao<ProcessoListaJulgamento, Long> {

	public ProcessoListaJulgamento recuperar(ObjetoIncidente<?> objetoIncidente) throws DaoException;

	public ProcessoListaJulgamento recuperarProcessoListaJulgamento(ObjetoIncidente<?> incidente, ListaJulgamento listaJulgamento) throws DaoException;

	public List<ProcessoListaJulgamento> listarProcessos(ListaJulgamento listaJulgamento) throws DaoException;
}
