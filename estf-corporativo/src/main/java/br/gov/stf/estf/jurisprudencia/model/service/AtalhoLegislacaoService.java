/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.jurisprudencia.AtalhoLegislacao;
import br.gov.stf.estf.entidade.jurisprudencia.LegislacaoIncidenteAnalise;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.AtalhoLegislacaoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * @author Paulo.Estevao
 * @since 11.08.2012
 */
public interface AtalhoLegislacaoService extends GenericService<AtalhoLegislacao, AtalhoLegislacao.AtalhoLegislacaoId, AtalhoLegislacaoDao> {
	
	AtalhoLegislacao recuperar(LegislacaoIncidenteAnalise legislacaoIncidenteAnalise) throws ServiceException;

	List<AtalhoLegislacao> pesquisarAtalhosLegislacao(String sigla, Long ano) throws ServiceException;

	AtalhoLegislacao recuperar(String numero, Long ano) throws ServiceException;
	
	AtalhoLegislacao recuperar(String numero, Long ano, Long norma, Long ambito) throws ServiceException;

}
