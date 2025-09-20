package br.gov.stf.estf.processostf.model.dataaccess;


import java.util.List;

import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface PeticaoDao extends GenericDao<Peticao, Long> {

	public Peticao recuperarPeticao(Long numero, Short ano) throws DaoException;

	public Peticao recuperarPeticao(Long idObjetoIndicente) throws DaoException;

	public Long persistirPeticao(Peticao peticao)throws DaoException;

	public Peticao recuperarPeticaoProcesso( Long numeroPeticao, 
													 Short anoPeticao, 
													 String siglaProcessual, 
													 Long numeroProcessual, 
													 Short codRecurso,
													 Boolean flgJuntado
	) throws DaoException; 	
	
	public List<ObjetoIncidente<?>> recuperarListaObjetoPeloObjetoIncidentePrincipal(Long idObjetoIncidente) throws DaoException;
	
	List<Peticao> recuperarPeticoes(Long numero, Short ano) throws DaoException;
	Boolean isPendenteDigitalizacao(Peticao peticao) throws DaoException;
	Boolean isRemessaIndevida(Peticao peticao) throws DaoException;
	
}
