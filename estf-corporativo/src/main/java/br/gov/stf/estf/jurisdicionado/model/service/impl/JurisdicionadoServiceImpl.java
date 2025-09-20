/**
 * 
 */
package br.gov.stf.estf.jurisdicionado.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.jurisdicionado.EnderecoJurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.IdentificacaoPessoa;
import br.gov.stf.estf.entidade.jurisdicionado.Jurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.ObjetoIncidenteEnderecoJurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.util.JurisdicionadoResult;
import br.gov.stf.estf.jurisdicionado.model.dataaccess.JurisdicionadoDao;
import br.gov.stf.estf.jurisdicionado.model.service.EnderecoJurisdicionadoService;
import br.gov.stf.estf.jurisdicionado.model.service.IdentificacaoPessoaService;
import br.gov.stf.estf.jurisdicionado.model.service.JurisdicionadoService;
import br.gov.stf.estf.jurisdicionado.model.service.ObjetoIncidenteEnderecoJurisdicionadoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Paulo.Estevao
 * @since 04.07.2011
 */
@Service("jurisdicionadoService")
public class JurisdicionadoServiceImpl extends
		GenericServiceImpl<Jurisdicionado, Long, JurisdicionadoDao> implements JurisdicionadoService {

	private final EnderecoJurisdicionadoService enderecoJurisdicionadoService;
	private final ObjetoIncidenteEnderecoJurisdicionadoService objetoIncidenteEnderecoJurisdicionadoService;
	private final IdentificacaoPessoaService identificacaoPessoaService;

	public JurisdicionadoServiceImpl( JurisdicionadoDao dao
									, EnderecoJurisdicionadoService enderecoJurisdicionadoService
									, ObjetoIncidenteEnderecoJurisdicionadoService objetoIncidenteEnderecoJurisdicionadoService
									,IdentificacaoPessoaService identificacaoPessoaService) {
		super(dao);
		this.enderecoJurisdicionadoService = enderecoJurisdicionadoService;
		this.objetoIncidenteEnderecoJurisdicionadoService = objetoIncidenteEnderecoJurisdicionadoService;
		this.identificacaoPessoaService = identificacaoPessoaService;
	}
	
	@Override
	public List<JurisdicionadoResult> recuperarJurisdicionadoDosEmprestimosNaoDevolvidos(Object value) throws ServiceException {
		try {
			return dao.recuperarJurisdicionadoDosEmprestimosNaoDevolvidos(value);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public JurisdicionadoResult pesquisarResultEntidadeGovernamentalPorId(Long id) throws ServiceException {
		try {
			return dao.pesquisarResultEntidadeGovernamentalPorId(id);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	
	@Override
	public JurisdicionadoResult pesquisarResultPorId(Long id) throws ServiceException {
		try {
			return dao.pesquisarResultPorId(id);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public List<JurisdicionadoResult> pesquisarResultCadastro(Object value) throws ServiceException {
		try {
			return dao.pesquisarResultCadastro(value);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public List<JurisdicionadoResult> pesquisarResult(Object value) throws ServiceException {
		try {
			return dao.pesquisarResult(value);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public List<JurisdicionadoResult> pesquisarResultEntidadeGovernamental(Object value, List<Long>listaSeqObjetosIncidentes) throws ServiceException {
		try {
			return dao.pesquisarResultEntidadeGovernamental(value, listaSeqObjetosIncidentes);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<JurisdicionadoResult> pesquisarResultJurisdicionadoIncidente(List<Long>listaSeqObjetosIncidentes)  throws ServiceException {
		try {
			return dao.pesquisarResultJurisdicionadoIncidente(listaSeqObjetosIncidentes);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	
	
	@Override
	public List<Jurisdicionado> pesquisar(String sugestaoNome)
			throws ServiceException {
		try {
			return dao.pesquisar(sugestaoNome);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Jurisdicionado> pesquisar(Long idJurisdicionado, String oab, String idUf) throws ServiceException {
		try { 
			return dao.pesquisar(idJurisdicionado, oab, idUf);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public void excluirEnderecos(List<EnderecoJurisdicionado> enderecos) throws ServiceException{
		for (EnderecoJurisdicionado endereco: enderecos) {
			// obter e remover os filhos
			List<ObjetoIncidenteEnderecoJurisdicionado> oiEnderecos = 
					objetoIncidenteEnderecoJurisdicionadoService.recuperarEnderecos(endereco.getId());
			objetoIncidenteEnderecoJurisdicionadoService.excluirTodos(oiEnderecos);
			enderecoJurisdicionadoService.excluir(endereco);
		}
	}
	
	public void excluirIdentificadores(List<IdentificacaoPessoa> identificadores) throws ServiceException{
		for (IdentificacaoPessoa identificacaoPessoa : identificadores) {
			identificacaoPessoaService.excluir(identificacaoPessoa);
		}
	}
	
	@Override
	public void alterarJurusidicionado(List<EnderecoJurisdicionado> enderecosParaExclusao,Jurisdicionado jurisdicionado) throws ServiceException {
		try {
			excluirEnderecos(enderecosParaExclusao);
			dao.alterar(jurisdicionado);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public void alterarJurusidicionado(List<EnderecoJurisdicionado> enderecosParaExclusao,List<IdentificacaoPessoa> identificadoresParaExclusao, Jurisdicionado jurisdicionado) throws ServiceException {
		try {
			excluirEnderecos(enderecosParaExclusao);
			excluirIdentificadores(identificadoresParaExclusao);
			dao.alterar(jurisdicionado);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}
