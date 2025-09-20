package br.gov.stf.estf.processostf.model.dataaccess;

// default package
// Generated 25/03/2008 10:35:05 by Hibernate Tools 3.1.0.beta5
import java.util.List;

import br.gov.stf.estf.entidade.processostf.Classe;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * DAO interface for domain model class Classe.
 * 
 * @see .Classe
 * @author Hibernate Tools
 */

public interface ClasseDao extends GenericDao<Classe, String> {

	public List pesquisarClasseProcessual() throws DaoException;

	public List pesquisarClasseAntiga() throws DaoException;
	
	public Classe pesquisarClassePorSigla(String siglaClasse) throws DaoException;

	public List<Classe> pesquisarClassesAtivas() throws DaoException; 

}
