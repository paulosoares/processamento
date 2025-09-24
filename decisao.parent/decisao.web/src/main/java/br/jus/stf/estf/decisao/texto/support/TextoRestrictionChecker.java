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
		// Quando a restri��o for N, deve ser permitido realizar transi��o de fase
		return Boolean.TRUE.equals(texto.isJulgamentoDigital()) || isRestrictedToUser(texto, principal) && !TipoRestricao.N.name().equals(texto.getTipoRestricao());
	}
	
	/**
	 * Texto est� restrito ao usu�rio?
	 * @param texto a ser analisado
	 * @param principal que deseja ver o texto
	 * @return true quando o texto est� restrito e, falso, caso contr�rio
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
		
		// Se o texto � de repercuss�o geral, ent�o � sempre restrito
		if (tipoRestricao == TipoRestricao.N)
			return true;
		
		/**********************************************************************************/
		
		// Se o usu�rio � administrador, ministro e master
		if (principal.isMaster() || principal.isMinistroDecisao() || principal.isAdministrador())
			if (principal.getMinistro() != null && principal.getMinistro().getId().equals(texto.getIdMinistro())) // e o usuario est� no gabinete do ministro do texto
				return false; // ent�o o texto n�o � restrito
		
		/**********************************************************************************/
		
		// Se o setor possui restri��o de texto ao responsavel (usu�rio ou grupo) e o texto est� em elabora��o, ent�o restringe o texto ao respons�vel (usu�rio ou grupo do egab)
		if (setorRestringeTextoAoResponsavel && textoEmElaboracao) {
			
			// Verifica se o usu�rio logado � o respons�vel pelo texto
			if (idUsuarioLogado.equals(idResponsavelTexto)) 
				return false; // Se for, n�o restringe o texto
			
			// Se o usu�rio est� em um grupo do egab que � o grupo respons�vel pelo texto 
			if (TipoPermissaoTexto.GRUPO.equals(tipoPermissaoTexto) && idGruposEgabUsuario.contains(idResponsavelTexto))// ou se o usu�rio est� no mesmo grupo do grupo respons�vel pelo texto
				return false; // Se for, n�o restringe o texto
			
			// O setor tem restri��o para textos e o texto est� em elabora��o, 
			// por�m o usu�rio n�o � o respons�vel e nem est� no grupo do egab que � o respons�vel pelo texto, 
			// ent�o deve restringir.
			return true;
		}
		
		/**********************************************************************************/
		
		// Se a restri��o � U, ent�o restringe o texto ao respons�vel ou criador 
		if (tipoRestricao == TipoRestricao.U)  {
			String idUsuarioCriadorDoTexto = (texto.getIdUsuarioInclusao() != null)?texto.getIdUsuarioInclusao():"";
			if (idUsuarioLogado.equalsIgnoreCase(idUsuarioCriadorDoTexto) || idUsuarioLogado.equalsIgnoreCase(idResponsavelTexto))
				return false; 	
		}
		
		/**********************************************************************************/
		
		// Se a restri��o � S, ent�o o ministro do texto deve ser o ministro do setor do usu�rio logado
		if (tipoRestricao == TipoRestricao.S)
			if (principal.getMinistro().getId().equals(texto.getIdMinistro()))
				return false;
		
		/**********************************************************************************/
		
		// Se a restri��o � P (P�blica), ent�o o texto n�o deve ser restrito
		if (tipoRestricao == TipoRestricao.P)
			return false;		
		
		/**********************************************************************************/
		
		// Se n�o caiu em nenhuma regra anterior, ent�o restringe o texto
		return true;
	}
	
	public static Boolean isRestricted(TextoDto texto, Principal principal){
		//nova verifica��o para saber se o cadeado do texto dever� ser exibido
		//de acordo com as novas regras se o gabinete selecionar que os textos
		//dever�o ser restritos a um usu�rio ou a um grupo pr�-selecionado do EGAB
		//o texto dever� estar na fase de elabora��o e ser p�blico j� que se houver
		//alguma restri��o cair� nas verifica��es abaixo

		// Texto nulo, ent�o mostra o cadeado
		if (texto != null) {
		
			// Tipo restri��o nula, ent�o n�o mostra o cadeado
			if (texto.getTipoRestricao() == null)
				return false;
			
			
			// Se o texto � p�blico 
			if (TipoRestricao.P.name().equals(texto.getTipoRestricao())){
				// E o gabinete n�o habilitou a restri��o ou o texto n�o est� em elabora��o, ent�o o sistema n�o deve mostrar o cadeado
				/*
				  						  Restringe     N�o Restringe
					Em elabora��o    		true			false
					N�o elabora��o    		false			false					
					
					Restringe e est� em elabora��o        	true
					Restringe e n�o est� em elabora��o		false
					N�o restringe e est� em elabora��o  	false
					N�o restringe e n�o est� em elabora��o 	false
				 */
				if (!principal.isSetorRestringeTextoAoResponsavel() || !FaseTexto.EM_ELABORACAO.equals(texto.getFase()))   
					return false; // N�o exibe o cadeado
			}
		}
		// Se n�o for p�blico, mostra o cadeado
		return true;
	}
}
		
