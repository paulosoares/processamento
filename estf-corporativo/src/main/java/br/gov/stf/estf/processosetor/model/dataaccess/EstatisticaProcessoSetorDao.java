package br.gov.stf.estf.processosetor.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.processosetor.EstatisticaProcessoSetorSecao;
import br.gov.stf.framework.model.dataaccess.DaoException;

public interface EstatisticaProcessoSetorDao {

	public List<EstatisticaProcessoSetorSecao> gerarEstatisticaProcessoSetorSecao(
			Long idSetor) throws DaoException;
	
	public List<EstatisticaProcessoSetorSecao> gerarEstatisticaProcessoFaseSetor(
			Long idSetor , Boolean status ) throws DaoException;
}
