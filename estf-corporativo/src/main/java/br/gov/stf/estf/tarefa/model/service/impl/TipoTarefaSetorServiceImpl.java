package br.gov.stf.estf.tarefa.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.tarefa.ConfiguracaoTipoTarefaSetor;
import br.gov.stf.estf.entidade.tarefa.TipoAtribuicaoTarefa;
import br.gov.stf.estf.entidade.tarefa.TipoCampoTarefa;
import br.gov.stf.estf.entidade.tarefa.TipoCampoTarefaValor;
import br.gov.stf.estf.entidade.tarefa.TipoTarefaSetor;
import br.gov.stf.estf.tarefa.model.dataaccess.TipoTarefaSetorDao;
import br.gov.stf.estf.tarefa.model.service.TipoTarefaSetorService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("tipoTarefaSetorService")
public class TipoTarefaSetorServiceImpl
extends GenericServiceImpl<TipoTarefaSetor, Long, TipoTarefaSetorDao> 
implements TipoTarefaSetorService {

	
	public TipoTarefaSetorServiceImpl(TipoTarefaSetorDao dao) {
		super(dao);
	}

	public TipoTarefaSetor recuperarTipoTarefaSetor(Long id) throws ServiceException {
		try {
			return dao.recuperarTipoTarefaSetor(id);
		}
		catch( DaoException e ) {
			throw new ServiceException(e);
		}
	}
	
	public TipoCampoTarefa recuperarTipoCampoTarefa(Long id) throws ServiceException {
		try {
			return dao.recuperarTipoCampoTarefa(id);
		}
		catch( DaoException e ) {
			throw new ServiceException(e);
		}
	}
	public List<TipoTarefaSetor> pesquisarTipoTarefaSetor(Long id,String descricao,Long idSetor, Boolean ativo) throws ServiceException {
		try {
			return dao.pesquisarTipoTarefaSetor(id, descricao ,idSetor, ativo);
		}
		catch( DaoException e ) {
			throw new ServiceException(e);
		}
	}

	public List<TipoTarefaSetor> pesquisarTipoTarefaSetor(Long id,String descricao,Long idSetor, Boolean ativo, Boolean cargaProgramada) throws ServiceException {
		try {
			return dao.pesquisarTipoTarefaSetor(id, descricao ,idSetor, ativo, cargaProgramada);
		}
		catch( DaoException e ) {
			throw new ServiceException(e);
		}
	}
	public Boolean persistirTipoTarefaSetor(TipoTarefaSetor tipoTarefaSetor) 
	throws ServiceException {

		Boolean alterado = null;
		try {
			validarTipoTarefaSetor(tipoTarefaSetor);
			alterado = dao.persistirTipoTarefaSetor(tipoTarefaSetor);

		}
		catch( DaoException e ) {
			throw new ServiceException(e);
		}
		return alterado;
	}

	public Boolean excluirTipoTarefaSetor(TipoTarefaSetor tipoTarefaSetor) 
	throws ServiceException {

		Boolean alterado = null;
		try {

			alterado = dao.excluirTipoTarefaSetor(tipoTarefaSetor);

		}
		catch( DaoException e ) {
			throw new ServiceException(e);
		}
		return alterado;
	}


	public void validarTipoTarefaSetor(TipoTarefaSetor tipoTarefaSetor) throws ServiceException{
		try{
			if(tipoTarefaSetor == null){
				throw new ServiceException("Objeto nulo TipoStatusSetor");
			}else if(tipoTarefaSetor.getDescricao()==null||tipoTarefaSetor.getDescricao().equals("")){
				throw new ServiceException("A descrição deve ser informada.");
			}
			if(tipoTarefaSetor.getAtivo()==null){
				tipoTarefaSetor.setAtivo(Boolean.TRUE);
			}
			tipoTarefaSetor.setDescricao(tipoTarefaSetor.getDescricao().toUpperCase());						

			if(tipoTarefaSetor.getId()==null){
				if(dao.verificarUnicidade(tipoTarefaSetor.getDescricao(),tipoTarefaSetor.getSetor().getId())){
					throw new ServiceException("Já existe um status cadastrado com a descrição "+tipoTarefaSetor.getDescricao()+".");
				}
			}

		}catch (ServiceException e) {
			throw e; 
		}
		catch(DaoException ex ){
			throw new ServiceException(ex);
		}
	}

	public TipoAtribuicaoTarefa recuperarTipoAtribuicaoTarefa(Long id,String sigla,String descricao) throws ServiceException{
		try {
			return dao.recuperarTipoAtribuicaoTarefa(id, sigla, descricao);
		}
		catch( DaoException e ) {
			throw new ServiceException(e);
		}
	}
	public List<TipoAtribuicaoTarefa> pesquisarTipoAtribuicaoTarefa(Long id,String sigla,String descricao) throws ServiceException{
		try {
			return dao.pesquisarTipoAtribuicaoTarefa(id, sigla, descricao);
		}
		catch( DaoException e ) {
			throw new ServiceException(e);
		}
	}

	public ConfiguracaoTipoTarefaSetor recuperarConfiguracaoTipoTarefaSetor(Long id,String sigla,String descricao) throws ServiceException{
		try {
			return dao.recuperarConfiguracaoTipoTarefaSetor(id, sigla, descricao);
		}
		catch( DaoException e ) {
			throw new ServiceException(e);
		}
	}
	public List<ConfiguracaoTipoTarefaSetor> pesquisarConfiguracaoTipoTarefaSetor(Long id,String sigla,String descricao) throws ServiceException{
		try {
			return dao.pesquisarConfiguracaoTipoTarefaSetor(id, sigla, descricao);
		}
		catch( DaoException e ) {
			throw new ServiceException(e);
		}
	}
	
	public List<TipoCampoTarefaValor> pesquisarTipoCampoTarefaValor(Long id, Long idTipoCampoTarefa)throws ServiceException{
		try {
			return dao.pesquisarTipoCampoTarefaValor(id, idTipoCampoTarefa);
		}
		catch( DaoException e ) {
			throw new ServiceException(e);
		}
	}

}