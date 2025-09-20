/**
 * 
 */
package br.gov.stf.estf.jurisdicionado.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.jurisdicionado.EnderecoJurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.IdentificacaoPessoa;
import br.gov.stf.estf.entidade.jurisdicionado.Jurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.util.JurisdicionadoResult;
import br.gov.stf.estf.jurisdicionado.model.dataaccess.JurisdicionadoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * @author Paulo.Estevao
 * @since 04.07.2011
 */
public interface JurisdicionadoService extends GenericService<Jurisdicionado, Long, JurisdicionadoDao> {

	List<Jurisdicionado> pesquisar(String sugestaoNome) throws ServiceException;
	
	List<Jurisdicionado> pesquisar(Long idJusrisdicionado, String oab, String uf) throws ServiceException;

	public List<JurisdicionadoResult> pesquisarResult(Object value) throws ServiceException;

	public JurisdicionadoResult pesquisarResultPorId(Long id) throws ServiceException;
	
	public List<JurisdicionadoResult> recuperarJurisdicionadoDosEmprestimosNaoDevolvidos(Object value) throws ServiceException;

	public List<JurisdicionadoResult> pesquisarResultEntidadeGovernamental(Object value, List<Long>listaSeqObjetosIncidentes)  throws ServiceException;

	public JurisdicionadoResult pesquisarResultEntidadeGovernamentalPorId(Long id) throws ServiceException;

	public List<JurisdicionadoResult> pesquisarResultJurisdicionadoIncidente(List<Long>listaSeqObjetosIncidentes)  throws ServiceException;
	
	public List<JurisdicionadoResult> pesquisarResultCadastro(Object value) throws ServiceException;
	
	public void excluirEnderecos(List<EnderecoJurisdicionado> enderecos) throws ServiceException;

	public void alterarJurusidicionado(List<EnderecoJurisdicionado> enderecosParaExclusao, Jurisdicionado jurisdicionado) throws ServiceException;

	public void alterarJurusidicionado(List<EnderecoJurisdicionado> enderecosParaExclusao,List<IdentificacaoPessoa> identificadoresParaExclusao, Jurisdicionado jurisdicionado) throws ServiceException;

}
