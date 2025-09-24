package br.jus.stf.estf.decisao.texto.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import br.gov.stf.estf.entidade.documento.Texto.TipoRestricao;
import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.gov.stf.estf.entidade.usuario.GrupoUsuario;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.pesquisa.domain.TipoPermissaoTexto;
import br.jus.stf.estf.decisao.support.security.Principal;

public class TextoRestrictionChecker {

	public static Boolean isNotAllowedFaseTransition(TextoDto texto, Principal principal) {
		// Quando a restrição for N, deve ser permitido realizar transição de fase
		return Boolean.TRUE.equals(texto.isJulgamentoDigital()) || isRestrictedToUser(texto, principal) && !TipoRestricao.N.name().equals(texto.getTipoRestricao());
	}
	
	/**
	 * Texto está restrito ao usuário?
	 * @param texto a ser analisado
	 * @param principal que deseja ver o texto
	 * @return true quando o texto está restrito e, falso, caso contrário
	 */
	public static Boolean isRestrictedToUser(TextoDto texto, Principal principal){
		
		if (texto == null || principal == null || principal.getUsuario() == null)
			return true;
		
		boolean setorRestringeTextoAoResponsavel = principal.isSetorRestringeTextoAoResponsavel();
		boolean textoEmElaboracao = FaseTexto.EM_ELABORACAO.equals(texto.getFase());
		TipoPermissaoTexto tipoPermissaoTexto = texto.getTipoPermissaoTexto();
		String idResponsavelTexto = texto.getIdResponsavel();
		String idUsuarioLogado = principal.getUsuario().getId();
		TipoRestricao tipoRestricao = TipoRestricao.valueOf(texto.getTipoRestricao());

		Set<GrupoUsuario> gruposEgabDoUsuario = principal.getGruposEgabDoUsuario();
		List<String> idGruposEgabUsuario = new ArrayList<String>();
		
		if (gruposEgabDoUsuario != null) {
			for (GrupoUsuario grupo : gruposEgabDoUsuario) 
				idGruposEgabUsuario.add(grupo.getId().toString());
		}
		
		
		/**********************************************************************************/
		
		// Se o texto é de repercussão geral, então é sempre restrito
		if (tipoRestricao == TipoRestricao.N)
			return true;
		
		/**********************************************************************************/
		
		// Se o usuário é administrador, ministro e master
		if (principal.isMaster() || principal.isMinistroDecisao() || principal.isAdministrador())
			if (principal.getMinistro() != null && principal.getMinistro().getId().equals(texto.getIdMinistro())) // e o usuario está no gabinete do ministro do texto
				return false; // então o texto não é restrito
		
		/**********************************************************************************/
		
		// Se o setor possui restrição de texto ao responsavel (usuário ou grupo) e o texto está em elaboração, então restringe o texto ao responsável (usuário ou grupo do egab)
		if (setorRestringeTextoAoResponsavel && textoEmElaboracao) {
			
			// Verifica se o usuário logado é o responsável pelo texto
			if (idUsuarioLogado.equals(idResponsavelTexto)) 
				return false; // Se for, não restringe o texto
			
			// Se o usuário está em um grupo do egab que é o grupo responsável pelo texto 
			if (TipoPermissaoTexto.GRUPO.equals(tipoPermissaoTexto) && idGruposEgabUsuario.contains(idResponsavelTexto))// ou se o usuário está no mesmo grupo do grupo responsável pelo texto
				return false; // Se for, não restringe o texto
			
			// O setor tem restrição para textos e o texto está em elaboração, 
			// porém o usuário não é o responsável e nem está no grupo do egab que é o responsável pelo texto, 
			// então deve restringir.
			return true;
		}
		
		/**********************************************************************************/
		
		// Se a restrição é U, então restringe o texto ao responsável ou criador 
		if (tipoRestricao == TipoRestricao.U)  {
			String idUsuarioCriadorDoTexto = (texto.getIdUsuarioInclusao() != null)?texto.getIdUsuarioInclusao():"";
			if (idUsuarioLogado.equalsIgnoreCase(idUsuarioCriadorDoTexto) || idUsuarioLogado.equalsIgnoreCase(idResponsavelTexto))
				return false; 	
		}
		
		/**********************************************************************************/
		
		// Se a restrição é S, então o ministro do texto deve ser o ministro do setor do usuário logado
		if (tipoRestricao == TipoRestricao.S)
			if (principal.getMinistro().getId().equals(texto.getIdMinistro()))
				return false;
		
		/**********************************************************************************/
		
		// Se a restrição é P (Pública), então o texto não deve ser restrito
		if (tipoRestricao == TipoRestricao.P)
			return false;		
		
		/**********************************************************************************/
		
		// Se não caiu em nenhuma regra anterior, então restringe o texto
		return true;
	}
	
	public static Boolean isRestricted(TextoDto texto, Principal principal){
		//nova verificação para saber se o cadeado do texto deverá ser exibido
		//de acordo com as novas regras se o gabinete selecionar que os textos
		//deverão ser restritos a um usuário ou a um grupo pré-selecionado do EGAB
		//o texto deverá estar na fase de elaboração e ser público já que se houver
		//alguma restrição cairá nas verificações abaixo

		// Texto nulo, então mostra o cadeado
		if (texto != null) {
		
			// Tipo restrição nula, então não mostra o cadeado
			if (texto.getTipoRestricao() == null)
				return false;
			
			
			// Se o texto é público 
			if (TipoRestricao.P.name().equals(texto.getTipoRestricao())){
				// E o gabinete não habilitou a restrição ou o texto não está em elaboração, então o sistema não deve mostrar o cadeado
				/*
				  						  Restringe     Não Restringe
					Em elaboração    		true			false
					Não elaboração    		false			false					
					
					Restringe e está em elaboração        	true
					Restringe e não está em elaboração		false
					Não restringe e está em elaboração  	false
					Não restringe e não está em elaboração 	false
				 */
				if (!principal.isSetorRestringeTextoAoResponsavel() || !FaseTexto.EM_ELABORACAO.equals(texto.getFase()))   
					return false; // Não exibe o cadeado
			}
		}
		// Se não for público, mostra o cadeado
		return true;
	}
}
		
