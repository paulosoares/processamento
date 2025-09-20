package br.gov.stf.estf.documento.model.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.ListaTextosDao;
import br.gov.stf.estf.documento.model.service.ListaTextosService;
import br.gov.stf.estf.documento.model.service.TextoListaTextoService;
import br.gov.stf.estf.entidade.documento.ListaTextos;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TextoListaTexto;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("listaTextosService")
public class ListaTextosServiceImpl extends GenericServiceImpl<ListaTextos, Long, ListaTextosDao> implements ListaTextosService {
	
	@Autowired
	TextoListaTextoService textoListaTextoService;

	public ListaTextosServiceImpl(ListaTextosDao dao) {
		super(dao);
	}

	public ListaTextos recuperarPorNome(String nome) throws ServiceException {
		try {
			return dao.recuperarPorNome(nome);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public List<ListaTextos> pesquisarListaTextos(String nome, Boolean ativo, Long idSetor) throws ServiceException {
		try {
			return dao.pesquisarListaTextos(nome, ativo, idSetor);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<ListaTextos> pesquisarListasDoTexto(Texto texto) throws ServiceException {
		try {
			return dao.pesquisarListasDoTexto(texto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public void excluirTextoDasListas(Texto texto) throws ServiceException {
		try {
			List<ListaTextos> listasDeTexto = dao.pesquisarListasDoTexto(texto);
			for (ListaTextos listaTexto : listasDeTexto) {
				TextoListaTexto textoLista = textoListaTextoService.recuperar(listaTexto, texto);	
				textoListaTextoService.excluir(textoLista);
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}
