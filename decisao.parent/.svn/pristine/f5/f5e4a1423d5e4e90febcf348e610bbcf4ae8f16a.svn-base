package br.jus.stf.estf.decisao.pesquisa.web.texto;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import br.gov.stf.estf.entidade.usuario.Usuario;
import br.jus.stf.estf.decisao.support.security.UserFacesBean;

/**
 * Bean para o carregamento dos possíveis criadores de
 * texto de um gabinete.
 * 
 * @author Tomas.Godoi
 *
 */
@Name("criadorFacesBean")
@Scope(ScopeType.SESSION)
public class CriadorFacesBean {

	@In("#{userFacesBean}")
	private UserFacesBean userFacesBean;
	
	public List<Usuario> search(Object suggest) {
		List<Usuario> sugestoes = new ArrayList<Usuario>();
		
		sugestoes.addAll(userFacesBean.search(suggest));
		
		return sugestoes;
	}
	
}
