/**
 * 
 */
package br.gov.stf.estf.jurisdicionado.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.jurisdicionado.Jurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.util.JurisdicionadoResult;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * @author Paulo.Estevao
 * @since 04.07.2011
 */
public interface JurisdicionadoDao extends GenericDao<Jurisdicionado, Long> {

	List<Jurisdicionado> pesquisar(String sugestaoNome) throws DaoException;
	
	List<Jurisdicionado> pesquisar(Long idJurisdicionado, String oab, String idUf) throws DaoException;

	public List<JurisdicionadoResult> pesquisarResult(Object value) throws DaoException;
	
	public List<JurisdicionadoResult> pesquisarResultCadastro(Object value) throws DaoException;


	public JurisdicionadoResult pesquisarResultPorId(Long id) throws DaoException;

	public List<JurisdicionadoResult> recuperarJurisdicionadoDosEmprestimosNaoDevolvidos(Object value) throws DaoException;

	public List<JurisdicionadoResult> pesquisarResultEntidadeGovernamental(Object value, List<Long>listaSeqObjetosIncidentes)  throws DaoException;

	public JurisdicionadoResult pesquisarResultEntidadeGovernamentalPorId(Long id) throws DaoException;

	public List<JurisdicionadoResult> pesquisarResultJurisdicionadoIncidente(List<Long>listaSeqObjetosIncidentes)  throws DaoException;

}
