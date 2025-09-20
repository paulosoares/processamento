package br.gov.stf.estf.processostf.model.dataaccess;

import br.gov.stf.estf.entidade.processostf.AndamentoProcessoComunicacao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface AndamentoProcessoComunicacaoDao extends GenericDao<AndamentoProcessoComunicacao, Long> {

	AndamentoProcessoComunicacao recuperarPorAndamento(Long idAndamento) throws DaoException;
}
