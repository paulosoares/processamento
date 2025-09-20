package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.Agrupador;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamento;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface PreListaJulgamentoDao extends GenericDao<PreListaJulgamento, Long> {
	
	public PreListaJulgamento recuperarPorListaJulgamento(ListaJulgamento listaJulgamento) throws DaoException;

	public PreListaJulgamento recuperarPreListaPorCategoria(Agrupador agrupador) throws DaoException;

	public List<PreListaJulgamento> listarPreListasJulgamentoDoSetor(Setor setor, Boolean ordenarPorId) throws DaoException;
	
	public Long getProximoSequencialParaNomeLista(Integer ano, Long codMinistro, Boolean avulso) throws DaoException;

}
