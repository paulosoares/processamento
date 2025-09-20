package br.gov.stf.estf.usuario.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.usuario.UsuarioIncidentePesquisa;
import br.gov.stf.estf.usuario.model.dataaccess.UsuarioIncidentePesquisaDao;
import br.gov.stf.estf.usuario.model.service.UsuarioIncidentePesquisaService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("usuarioIncidentePesquisaService")
public class UsuarioIncidentePesquisaServiceImpl extends
		GenericServiceImpl<UsuarioIncidentePesquisa, UsuarioIncidentePesquisa.UsuarioIncidentePesquisaId, UsuarioIncidentePesquisaDao> implements UsuarioIncidentePesquisaService {

	public UsuarioIncidentePesquisaServiceImpl(UsuarioIncidentePesquisaDao dao) {
		super(dao);
	}
	
	@Override
	public List<UsuarioIncidentePesquisa> pesquisarPorUsuario(String usuario){
		try {
			return dao.pesquisarPorUsuario(usuario);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void apagarListaExportacao(String usuario) {
		dao.apagarListaExportacao(usuario);		
	}
	
}
