package br.gov.stf.estf.publicacao.model.dataaccess;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.processostf.IncidenteDistribuicao;
import br.gov.stf.estf.entidade.publicacao.AtaDistribuicao;
import br.gov.stf.estf.entidade.publicacao.TipoSessao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;


/**
 * 
 * @author alvaro.silva
 *
 */

public interface AtaDistribuicaoDao 
extends GenericDao <AtaDistribuicao, Long> {
	
	public AtaDistribuicao recuperar(Integer numero, TipoSessao tipoSessao, Date dataComposicaoParcial) 
	throws DaoException;
	
	public List<IncidenteDistribuicao> pesquisarIncidenteDistribuicao(
			AtaDistribuicao ataDistribuicao, Boolean recuperarOcultos)
	throws DaoException;

}