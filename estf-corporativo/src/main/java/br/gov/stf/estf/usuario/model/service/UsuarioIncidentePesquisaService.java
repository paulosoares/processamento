package br.gov.stf.estf.usuario.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.usuario.UsuarioIncidentePesquisa;
import br.gov.stf.estf.usuario.model.dataaccess.UsuarioIncidentePesquisaDao;
import br.gov.stf.framework.model.service.GenericService;

public interface UsuarioIncidentePesquisaService extends GenericService<UsuarioIncidentePesquisa, UsuarioIncidentePesquisa.UsuarioIncidentePesquisaId, UsuarioIncidentePesquisaDao> {
	
	public void apagarListaExportacao(String usuario);

	public List<UsuarioIncidentePesquisa> pesquisarPorUsuario(String usuario);
	
}
