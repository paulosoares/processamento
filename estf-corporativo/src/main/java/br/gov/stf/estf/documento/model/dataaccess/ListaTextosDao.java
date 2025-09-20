package br.gov.stf.estf.documento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.documento.ListaTextos;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ListaTextosDao extends GenericDao<ListaTextos, Long> {

	public ListaTextos recuperarPorNome(String nome) throws DaoException;

	public List<ListaTextos> pesquisarListaTextos(String nome, Boolean ativo, Long idSetor) throws DaoException;

	public List<ListaTextos> pesquisarListasDoTexto(Texto texto) throws DaoException;

}
