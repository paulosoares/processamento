package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.processostf.PrescricaoReu;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface PrescricaoReuDao extends GenericDao<PrescricaoReu, Long> {

	
	public List<PrescricaoReu> pesquisarProcessosPrescricao (Long idObjetoIncidente, Date dtPrescricaoInicial, Date dtPrescricaoFinal, 
			Long idMinistro, String codigoPena, Boolean filtroEmTramitacao) throws DaoException;
}
