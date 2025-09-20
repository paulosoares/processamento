package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.Agrupador;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface AgrupadorDao extends GenericDao<Agrupador, Long> {

	List<Agrupador> pesquisarPorSetor(Long idSetor) throws DaoException;
	List<Agrupador> pesquisarPorSetor(Long idSetor, String Texto) throws DaoException;
	
	void inserirObjetoIncidenteNoAgrupador(Long idObjetoIncidente, Long idCategoria) throws DaoException;
	
	void removerObjetoIncidenteDoAgrupador(Long idObjetoIncidente, Long idCategoria) throws DaoException;
	
	List<Agrupador> recuperarCategoriasDoObjetoIncidente(Long idObjetoIncidente) throws DaoException;
	
	List<Agrupador> recuperarCategoriasDoObjetoIncidenteNoSetor(Long idObjetoIncidente, Long idSetor) throws DaoException;
	
	String getCategoriaDoIncidente(Long idObjetoIncidente, Long idSetor) throws DaoException;
}
