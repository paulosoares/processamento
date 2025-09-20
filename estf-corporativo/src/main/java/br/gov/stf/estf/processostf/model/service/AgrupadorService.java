package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.Agrupador;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.processostf.model.dataaccess.AgrupadorDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface AgrupadorService extends GenericService<Agrupador, Long, AgrupadorDao> {

	List<Agrupador> recuperarCategoriasDoSetor(Long idSetor) throws ServiceException;
	List<Agrupador> recuperarCategoriasDoSetor(Long idSetor, String texto) throws ServiceException;
	String getCategoriaDoIncidente(Long idObjetoIncidente, Long long1) throws ServiceException;
	void removerCategoriaDoObjetoIncidente(Long idObjetoIncidente, Long idCategoria) throws ServiceException;
	void alterarCategoriaDoObjetoIncidente(Long idObjetoIncidente, Long idCategoriaAnterior, Long idCategoriaNova) throws ServiceException;
	boolean objetoIncidentePossuiCategorias(Long idObjetoIncidente) throws ServiceException;
	void categorizarIncidente(ObjetoIncidente<?> objetoIncidente, Agrupador result, Setor setor) throws ServiceException, DaoException;
	List<Agrupador> recuperarCategoriasDoObjetoIncidente(Long idObjetoIncidente) throws ServiceException;
	
	void inserirObjetoIncidenteNoAgrupador(Long idObjetoIncidente, Long idCategoriaNova) throws ServiceException;
	
	boolean objetoIncidentePossuiCategoriasNoSetor(Long idObjetoIncidente, Long idSetor) throws ServiceException;
}
