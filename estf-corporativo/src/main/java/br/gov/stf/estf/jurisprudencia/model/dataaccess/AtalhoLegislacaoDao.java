/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.jurisprudencia.AtalhoLegislacao;
import br.gov.stf.estf.entidade.jurisprudencia.LegislacaoIncidenteAnalise;
import br.gov.stf.estf.entidade.jurisprudencia.TipoLegislacao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * @author Paulo.Estevao
 * @since 11.08.2012
 */
public interface AtalhoLegislacaoDao extends GenericDao<AtalhoLegislacao, AtalhoLegislacao.AtalhoLegislacaoId> {

	AtalhoLegislacao recuperar(LegislacaoIncidenteAnalise legislacaoIncidenteAnalise) throws DaoException;

	List<AtalhoLegislacao> pesquisarAtalhosLegislacao(String sigla, Long ano) throws DaoException;

	AtalhoLegislacao recuperar(String numero, Long ano) throws DaoException;

	AtalhoLegislacao recuperar(String numero, Long ano, TipoLegislacao tipoNorma, TipoLegislacao tipoEscopoLegislacao) throws DaoException;

}
