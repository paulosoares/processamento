package br.jus.stf.estf.decisao.support.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.GrantedAuthority;
import org.springframework.stereotype.Component;

import br.gov.stf.estf.entidade.usuario.Perfil;
import br.gov.stf.estf.entidade.usuario.Transacao;
import br.gov.stf.estf.usuario.model.service.TransacaoService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.util.NestedRuntimeException;
/**
 * Classe que permite checar se um determinado usuário tem permissão para acessar uma ação.
 * @author Demetrius.Jube
 *
 */
@Component("permissionChecker")
public class PermissionChecker {

	public static final String SIGLA_SISTEMA_ESTF_DECISAO = "ESTFDECISAO";
	public static final String PERFIL_PREFIXO = "RS_ESTFDECISAO-";
	
	private Map<Integer, List<String>> roles = new HashMap<Integer, List<String>>();

	@Autowired
	private TransacaoService transacaoService;
	
	/**
	 * Verifica se uma autenticação possui permissão para acessar uma determinada ação.
	 * @param principal
	 * @param actionIdentification
	 * @return
	 */
	public boolean hasPermission(Principal principal, ActionIdentification actionIdentification) {
		// Utiliza as permissões do Principal ao invés das permissões do Authentication, pois 
		// é realizado um filtro dos perfis no momento da autenticação (vide AuthenticationProvider)
    	List<GrantedAuthority> authorities = principal.getAuthorities();
    	
    	// Se o usuário possui algum perfil necessário à ação, liberar! Caso contrário, negar.
    	for (GrantedAuthority authority : authorities) {
    		// Recuperando role, descartando prefixo...
    		String role = authority.getAuthority().substring(PERFIL_PREFIXO.length());
    		if (getRoles(actionIdentification.getCodigo()).contains(role)) {
				return true;
			}
		}
		return false;
    }
    
    /**
     * Recupera os perfis necessários a execução de uma transação, que para o caso em questão é
     * o mesmo que uma ação.
     * 
     * <p>Os perfis de cada transação (ação) será armazenado em um mapa de perfils (ação:perfis).
     * 
     * @param actionNumber o identificador da transação
     * @return a lista de perfis
     */
    private List<String> getRoles(Integer actionNumber) {
    	if (!roles.containsKey(actionNumber)) {
    		try {
		    	Transacao transacao = transacaoService.pesquisarTransacao(SIGLA_SISTEMA_ESTF_DECISAO, actionNumber);
		    	List<String> perfis = new ArrayList<String>();
		    	if (transacao != null && transacao.getPerfis() != null) {
			    	for (Perfil perfil : transacao.getPerfis()) {
			    		perfis.add(perfil.getDescricao());
					}
		    	}
		    	roles.put(actionNumber, perfis);
			} catch (ServiceException e) {
				throw new NestedRuntimeException(e);
			}
    	}
    	return roles.get(actionNumber);
    }

}
