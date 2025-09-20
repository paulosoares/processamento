package br.gov.stf.estf.processostf.model.dataaccess;

import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.AndamentoPeticao;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface AndamentoPeticaoDao extends GenericDao<AndamentoPeticao, Long> {
	
	public AndamentoPeticao pesquisar(Andamento andamento, Peticao peticao) throws DaoException;
	
	public Long recuperarUltimaSequencia(ObjetoIncidente objetoIncidente) throws DaoException;
}