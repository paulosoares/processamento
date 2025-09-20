package br.gov.stf.estf.documento.model.dataaccess;


import java.util.List;

import br.gov.stf.estf.entidade.documento.ControleVista;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ControleVistaDao extends GenericDao<ControleVista, Long> { 
	public List<ControleVista> recuperar(String siglaClasseProcessual, Long numeroProcesso,	Long codigoMinistro) throws DaoException;	
	public List<ControleVista> recuperar(String siglaClasseProcessual, Long numeroProcesso) throws DaoException;
	public List<ControleVista> recuperar(Long objetoIncidente) throws DaoException;
	public List<ControleVista> listarVistasVencidas() throws DaoException;
}
