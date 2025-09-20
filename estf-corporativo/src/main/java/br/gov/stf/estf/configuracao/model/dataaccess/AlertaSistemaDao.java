package br.gov.stf.estf.configuracao.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.configuracao.AlertaSistema;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface AlertaSistemaDao extends GenericDao<AlertaSistema, Long> {
	
	public List<AlertaSistema> recuperarValor(String siglaSistema, String chave) throws DaoException;

}
