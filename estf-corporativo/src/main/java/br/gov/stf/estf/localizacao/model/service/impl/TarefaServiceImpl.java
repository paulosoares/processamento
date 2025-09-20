package br.gov.stf.estf.localizacao.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.localizacao.SecaoSetor;
import br.gov.stf.estf.entidade.localizacao.Tarefa;
import br.gov.stf.estf.localizacao.model.dataaccess.TarefaDao;
import br.gov.stf.estf.localizacao.model.service.SecaoSetorService;
import br.gov.stf.estf.localizacao.model.service.TarefaService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("tarefaService")
public class TarefaServiceImpl extends GenericServiceImpl<Tarefa, Long, TarefaDao> implements TarefaService {
	private SecaoSetorService secaoSetorService;
	
	protected TarefaServiceImpl(TarefaDao dao) {
		super(dao);
	}


	public List<Tarefa> pesquisarTarefa(Long id, String descricao,
			Long idSecao, Long idSetor, boolean localizacaoIgual)
			throws ServiceException {
		List<Tarefa> Tarefas = null;

		try {
			Tarefas = dao.pesquisarTarefa(id, descricao, idSecao,
					idSetor, localizacaoIgual);

			return Tarefas;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public Boolean excluirTarefa(Tarefa tarefa) throws ServiceException {
		try {
			if (tarefa == null || tarefa.getId() == null) {
				throw new ServiceException("Objeto nulo tarefa");
			}
			List<SecaoSetor> listaSecaoSetor = secaoSetorService
					.pesquisarTarefaSecao(null, tarefa.getId(), null, null,
							null, null);
			if (listaSecaoSetor != null && listaSecaoSetor.size() > 0) {
				for (SecaoSetor secaoSetor : listaSecaoSetor) {
					secaoSetor.getTarefas().remove(tarefa);
					secaoSetorService.persistirSecaoSetor(secaoSetor);
				}
			}
			return dao.excluirTarefa(tarefa);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public Boolean persistirTarefa(Tarefa tarefa) throws ServiceException {
		try {
			validarTarefa(tarefa);
			return dao.persistirTarefa(tarefa);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

	}

	public void validarTarefa(Tarefa tarefa) throws ServiceException {
		try {
			if (tarefa == null) {
				throw new ServiceException("Objeto nulo Tarefa");
			}
			if (tarefa.getDescricao() == null
					|| tarefa.getDescricao().equals("")) {
				throw new ServiceException("A descrição deve ser informada.");
			}
			tarefa.setDescricao(tarefa.getDescricao().toUpperCase());
			if (tarefa.getId() == null) {
				List lista = dao.verificarUnicidade(null, tarefa
						.getDescricao());
				if (lista != null && lista.size() > 0) {
					throw new ServiceException(
							"Já existe uma tarefa cadastrada com a descrição informada.");
				}
			}

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

	}

	
}
