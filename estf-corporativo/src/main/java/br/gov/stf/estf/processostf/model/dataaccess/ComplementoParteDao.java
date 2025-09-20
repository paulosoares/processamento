package br.gov.stf.estf.processostf.model.dataaccess;


// default package
// Generated 18/03/2008 11:02:23 by Hibernate Tools 3.1.0.beta5
import java.util.List;

import br.gov.stf.estf.entidade.processostf.ComplementoParte;
import br.gov.stf.estf.entidade.processostf.ComplementoParte.ComplementoParteId;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;


/**
 * DAO interface for domain model class ComplementoParte.
 * @see .ComplementoParte
 * @author Hibernate Tools
 */

public interface ComplementoParteDao 
extends GenericDao <ComplementoParte, ComplementoParteId> {
	public List<ComplementoParte> pesquisar (Long codigoParte) throws DaoException;
}

