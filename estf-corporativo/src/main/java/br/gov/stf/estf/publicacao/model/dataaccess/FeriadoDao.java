package br.gov.stf.estf.publicacao.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.publicacao.Feriado;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface FeriadoDao extends GenericDao<Feriado, String> {

	public List<Feriado> recuperar(String mesAno) throws DaoException;
	
	public List<Feriado> recuperar(String mesAno, String dia) throws DaoException;
	

}
