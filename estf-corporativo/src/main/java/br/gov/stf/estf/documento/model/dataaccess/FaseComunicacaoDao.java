package br.gov.stf.estf.documento.model.dataaccess;

import br.gov.stf.estf.entidade.documento.FaseComunicacao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface FaseComunicacaoDao  extends GenericDao<FaseComunicacao, Long> {
	
	public FaseComunicacao pesquisarFaseAtual(Long idComunicacao) throws DaoException;
	
	public void incluirFase(FaseComunicacao faseComunicacao) throws DaoException;
	
}
