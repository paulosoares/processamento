package br.gov.stf.estf.publicacao.model.dataaccess;

import br.gov.stf.framework.model.dataaccess.DaoException;

public interface MontaParteDjDao {
	public Boolean recuperarRedatorAcordao (String siglaProcesso, Long numProcesso, Long codRecurso, String tipoJulgamento) throws DaoException;
}
