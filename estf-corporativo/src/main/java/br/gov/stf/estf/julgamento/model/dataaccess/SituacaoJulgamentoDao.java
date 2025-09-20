package br.gov.stf.estf.julgamento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.julgamento.SituacaoJulgamento;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface SituacaoJulgamentoDao extends GenericDao<SituacaoJulgamento, Long> {
	
	List<SituacaoJulgamento> pesquisar(Long idJulgamentoProcesso)throws DaoException;
}
