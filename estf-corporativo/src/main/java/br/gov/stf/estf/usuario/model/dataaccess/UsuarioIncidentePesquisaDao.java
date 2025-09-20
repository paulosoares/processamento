package br.gov.stf.estf.usuario.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.usuario.UsuarioIncidentePesquisa;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface UsuarioIncidentePesquisaDao extends GenericDao<UsuarioIncidentePesquisa, UsuarioIncidentePesquisa.UsuarioIncidentePesquisaId> {

	void apagarListaExportacao(String usuario);

	List<UsuarioIncidentePesquisa> pesquisarPorUsuario(String usuario)	throws DaoException;


}
