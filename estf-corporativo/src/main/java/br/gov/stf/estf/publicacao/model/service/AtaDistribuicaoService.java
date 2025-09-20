package br.gov.stf.estf.publicacao.model.service;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.processostf.IncidenteDistribuicao;
import br.gov.stf.estf.entidade.publicacao.AtaDistribuicao;
import br.gov.stf.estf.entidade.publicacao.TipoSessao;
import br.gov.stf.estf.publicacao.model.dataaccess.AtaDistribuicaoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;


/**
 * 
 * @author alvaro.silva
 *
 */

public interface AtaDistribuicaoService extends GenericService <AtaDistribuicao, Long, AtaDistribuicaoDao> {

	public AtaDistribuicao recuperar(Integer numero, TipoSessao tipoSessao, Date dataComposicaoParcial) 
	throws ServiceException; 

	public List<IncidenteDistribuicao> pesquisarIncidenteDistribuicao(AtaDistribuicao ataDistribuicao, Boolean recuperarOcultos)  
	throws ServiceException;
}