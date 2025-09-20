package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.Assunto;
import br.gov.stf.estf.entidade.processostf.AssuntoProcesso;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface AssuntoProcessoDao  extends GenericDao <AssuntoProcesso, Long> {
	public List<AssuntoProcesso> pesquisar(String siglaClasseProcessual, Long numeroProcesso) throws DaoException;
	
	public void persistirAssuntoProcesso(AssuntoProcesso assuntoProcesso) throws DaoException;
	
	public Assunto recuperarAssuntoPrincipal(Processo processo) throws DaoException;
	
	public List<Assunto> recuperarListaAssuntosDoProcesso(Processo processo)throws DaoException;
}