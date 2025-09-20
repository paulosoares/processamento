package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.jurisdicionado.Jurisdicionado;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProcessoInteresse;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ProcessoInteresseDao extends GenericDao<ProcessoInteresse, Long> {
	public List<ProcessoInteresse> recuperarProcessosInteresse(Jurisdicionado advogado) throws DaoException;
	public ProcessoInteresse recuperarProcessosInteresse(Jurisdicionado advogado, Processo processo) throws DaoException;
	public Boolean existeMovimentada(Jurisdicionado advogado) throws DaoException;

}
