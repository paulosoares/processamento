package br.gov.stf.estf.tarefa.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.tarefa.TipoSituacaoTarefa;
import br.gov.stf.estf.tarefa.model.dataaccess.TipoSituacaoTarefaDao;
import br.gov.stf.estf.tarefa.model.service.TipoSituacaoTarefaService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("tipoSituacaoTarefaService")
public class TipoSituacaoTarefaServiceImpl
extends GenericServiceImpl<TipoSituacaoTarefa, Long, TipoSituacaoTarefaDao> 
implements TipoSituacaoTarefaService {

	public TipoSituacaoTarefaServiceImpl(TipoSituacaoTarefaDao dao) {
		super(dao);
	}

	public List<TipoSituacaoTarefa> pesquisarTipoSituacaoTarefa(Long Id,String Descricao,
			Long idSetor,Boolean ativo,Boolean semSetor)
			throws ServiceException {

		List<TipoSituacaoTarefa> situacoes = null;
		try {
			situacoes = dao.pesquisarTipoSituacaoTarefa(Id, Descricao, idSetor, ativo, semSetor);
		}
		catch( DaoException e ) {
			throw new ServiceException(e);
		}

		return situacoes;
	}


	public Boolean persistirTipoSituacaoTarefa(TipoSituacaoTarefa tipoSituacaoTarefa) 
	throws ServiceException {

		Boolean alterado = null;
		try {

			alterado = dao.persistirTipoSituacaoTarefa(tipoSituacaoTarefa);

		}
		catch( DaoException e ) {
			throw new ServiceException(e);
		}
		return alterado;
	}

	public Boolean excluirTipoSituacaoTarefa(TipoSituacaoTarefa tipoSituacaoTarefa) 
	throws ServiceException {

		Boolean alterado = null;
		try {

			alterado = dao.excluirTipoSituacaoTarefa(tipoSituacaoTarefa);

		}
		catch( DaoException e ) {
			throw new ServiceException(e);
		}
		return alterado;
	}

	public TipoSituacaoTarefa recuperarTipoSituacaoTarefa(Long id)throws ServiceException {

		try {

			return dao.recuperarTipoSituacaoTarefa(id);

		}
		catch( DaoException e ) {
			throw new ServiceException(e);
		}
		
	}


}