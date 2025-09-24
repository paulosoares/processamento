package br.jus.stf.estf.decisao.pesquisa.web.texto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import br.gov.stf.estf.entidade.usuario.GrupoUsuario;
import br.gov.stf.estf.entidade.usuario.Responsavel;
import br.gov.stf.estf.usuario.model.enuns.EnumTipoResposavelTexto;
import br.gov.stf.estf.usuario.model.service.UsuarioService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.support.security.UserFacesBean;
import br.jus.stf.estf.decisao.support.service.ConfiguracaoSistemaService;

@Name("responsavelFacesBean")
@Scope(ScopeType.SESSION)
public class ResponsavelFacesBean {
    
	@In("#{userFacesBean}")
	private UserFacesBean userFacesBean;
	
	@In("#{configuracaoSistemaServiceLocal}")
	private ConfiguracaoSistemaService configuracaoSistemaService;
	
	@In("#{usuarioService}")
	private UsuarioService usuarioService;
	
	@Logger
	private Log logger;
	
    public List<Responsavel> search(Object suggest) {
    	List<Responsavel> sugestoes = new ArrayList<Responsavel>();
    	
    	// Adiciona os grupos do egab vinculados ao setor do usuário caso o gabinete tenha habilitado a opção de ver grupos no campo responsável
		try {
				EnumTipoResposavelTexto opcao = configuracaoSistemaService.retornaOpcaoTiposResponsaveis();
				
				if (opcao.equals(EnumTipoResposavelTexto.GRUPO) || opcao.equals(EnumTipoResposavelTexto.AMBOS)) {
					Long idSetor = userFacesBean.getPrincipal().getMinistro() == null ? null : userFacesBean.getPrincipal().getMinistro().getSetor().getId();
					String nome = suggest.toString();
					List<GrupoUsuario> grupos = idSetor == null ? new ArrayList<GrupoUsuario>() : usuarioService.pesquisarGruposUsuario(idSetor, nome, true);
					Collections.sort(grupos, new ResponsavelComparator());
					sugestoes.addAll(grupos);
				}
				
				if (opcao.equals(EnumTipoResposavelTexto.USUARIO) || opcao.equals(EnumTipoResposavelTexto.AMBOS)) {
			    	// Adiciona todos os usuário conforme a lógica que já existe
			    	sugestoes.addAll(userFacesBean.search(suggest));
				}
				
			} catch (ServiceException e) {
				logger.error(e.getMessage(), e);
			}
    	
		
    	return sugestoes;
	}
	
	class ResponsavelComparator implements Comparator<Responsavel>{
		
		@Override
		public int compare(Responsavel o1, Responsavel o2) {
			
			if (o1 != null){
				if (o2 != null){
					return o1.getNome().compareTo(o2.getNome());
				}else{
					return -1;
				}
			} else if (o2 != null) {
				return 1;
			}
			
			return 0;
		};
	}
}	
