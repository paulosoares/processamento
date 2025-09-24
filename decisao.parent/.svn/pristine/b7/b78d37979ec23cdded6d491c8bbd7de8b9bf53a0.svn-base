package br.jus.stf.estf.decisao.documento.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;
import org.springframework.security.context.SecurityContextHolder;

import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.documento.service.AssinaturaDocumentoService;
import br.jus.stf.estf.decisao.documento.support.Documento;
import br.jus.stf.estf.decisao.documento.support.DocumentoNaoAssinadoDto;
import br.jus.stf.estf.decisao.pesquisa.domain.ComunicacaoDto;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.security.PermissionChecker;
import br.jus.stf.estf.decisao.support.security.Principal;
import br.jus.stf.estf.decisao.support.service.UsuarioLogadoService;

/**
 * Classe base para os beans JSF de assinatura de documentos automaticamente (apenas um passo).
 * 
 * @author Tomas.Godoi
 * 
 */
public abstract class AbstractAssinarDocumentosBean {

	@Logger
	protected Log logger;

	@In("#{usuarioLogadoService}")
	protected UsuarioLogadoService usuarioLogadoService;
	
	@In("#{permissionChecker}")
	protected PermissionChecker permissionChecker;

	protected List<DocumentoNaoAssinadoDto<TextoDto>> textosNaoAssinados = new ArrayList<DocumentoNaoAssinadoDto<TextoDto>>();
	protected List<DocumentoNaoAssinadoDto<ComunicacaoDto>> comunicacoesNaoAssinadas = new ArrayList<DocumentoNaoAssinadoDto<ComunicacaoDto>>();

	protected boolean errors;

	protected boolean warnings;

	protected boolean enviouAlgumParaOAssinador;
	
	protected int quantidadeTextosAssinados;
	protected int quantidadeComunicacoesAssinadas;
	
	protected final ActionIdentification permissaoAssinarDigitalmente = ActionIdentification.ASSINAR_DIGITALMENTE;
	protected final ActionIdentification permissaoAssinarDigitalmenteComunicacoes = ActionIdentification.ASSINAR_DIGITALMENTE_COMUNICACOES;

	public void assinarTextosAutomaticamente() {
		limpar();
		try {
			checarPermissao(permissaoAssinarDigitalmente);
			List<TextoDto> textos = getAssinaturaDocumentoService().recuperarTextosParaAssinar(textosNaoAssinados);
			preencherMensagensTextosNaoAssinados();
			if (textos.size() > 0) {
				getAssinaturaDocumentoService().assinarTextosAutomaticamente(textos);
				quantidadeTextosAssinados = textos.size(); // Se não ocorreu erro, assinou todos.
				enviouAlgumParaOAssinador = true;
			} else {
				// Mostrar mensagem dizendo que não há textos para assinar.
				if (textosNaoAssinados.size() == 0) {
					addError("Não há nenhum texto para assinar.");
				}
			}
		} catch (ServiceException e) {
			addError(e.getMessage());
			logger.error(e);
		}
	}

	public void assinarTextos(List<Long> ids) {
		limpar();
		try {
			checarPermissao(permissaoAssinarDigitalmente);
			List<TextoDto> textos = getAssinaturaDocumentoService().recuperarTextosParaAssinar(ids, textosNaoAssinados);
			preencherMensagensTextosNaoAssinados();
			if (textos.size() > 0) {
				getAssinaturaDocumentoService().assinarTextosAutomaticamente(textos);
				quantidadeTextosAssinados = textos.size(); // Se não ocorreu erro, assinou todos.
				enviouAlgumParaOAssinador = true;
			} else {
				// Mostrar mensagem dizendo que não há textos para assinar.
				if (textosNaoAssinados.size() == 0) {
					addError("Não há nenhum texto para assinar.");
				}
			}
		} catch (ServiceException e) {
			addError(e.getMessage());
			logger.error(e);
		}
	}
	
	protected abstract AssinaturaDocumentoService getAssinaturaDocumentoService();

	protected void preencherMensagensTextosNaoAssinados() {
		for (DocumentoNaoAssinadoDto<TextoDto> dna : textosNaoAssinados) {
			addWarning(String.format("%s: %s", dna.getDescricao(), dna.getMotivo()));
		}
	}

	public int getTotalTextosParaAssinar() {
		return getAssinaturaDocumentoService().recuperarTotalTextosParaAssinar();
	}

	public void assinarComunicacoesAutomaticamente() {
		limpar();
		try {
			checarPermissao(permissaoAssinarDigitalmenteComunicacoes);
			List<ComunicacaoDto> comunicacoes = getAssinaturaDocumentoService().recuperarComunicacoesParaAssinar(comunicacoesNaoAssinadas);
			preencherMensagensComunicacoesNaoAssinadas();
			if (comunicacoes.size() > 0) {
				getAssinaturaDocumentoService().assinarComunicacoesAutomaticamente(comunicacoes);
				quantidadeComunicacoesAssinadas = comunicacoes.size();
				enviouAlgumParaOAssinador = true;
			} else {
				if (comunicacoesNaoAssinadas.size() == 0) {
					// Mostrar mensagem dizendo que não há comunicações para assinar.
					addError("Não há nenhum expediente para assinar.");
				}
			}
		} catch (ServiceException e) {
			addError(e.getMessage());
			logger.error(e);
		}
	}

	public void assinarComunicacoes(List<Long> ids) {
		limpar();
		try {
			checarPermissao(permissaoAssinarDigitalmenteComunicacoes);
			List<ComunicacaoDto> comunicacoes = getAssinaturaDocumentoService().recuperarComunicacoesParaAssinar(ids, comunicacoesNaoAssinadas);
			preencherMensagensComunicacoesNaoAssinadas();
			if (comunicacoes.size() > 0) {
				getAssinaturaDocumentoService().assinarComunicacoesAutomaticamente(comunicacoes);
				quantidadeComunicacoesAssinadas = comunicacoes.size();
				enviouAlgumParaOAssinador = true;
			} else {
				if (comunicacoesNaoAssinadas.size() == 0) {
					// Mostrar mensagem dizendo que não há comunicações para assinar.
					addError("Não há nenhum expediente para assinar.");
				}
			}
		} catch (ServiceException e) {
			addError(e.getMessage());
			logger.error(e);
		}
	}
	
	/**
	 * Hoje no sistema não existe regra de comunicações que não podem ser assinadas. Entretanto,
	 * se algum dia essa regra existir, será de responsabilidade do código que a valide adicionar
	 * a descrição da comunicação.
	 */
	protected void preencherMensagensComunicacoesNaoAssinadas() {
		for (DocumentoNaoAssinadoDto<ComunicacaoDto> dna : comunicacoesNaoAssinadas) {
			addWarning(String.format("%s: %s", dna.getDescricao(), dna.getMotivo()));
		}
	}

	public int getTotalComunicacoesParaAssinar() throws ServiceException {
		return getAssinaturaDocumentoService().recuperarTotalComunicacoesParaAssinar();
	}

	public void assinarDocumentosAutomaticamente() {
		limpar();
		try {
			checarPermissao(permissaoAssinarDigitalmente);
			checarPermissao(permissaoAssinarDigitalmenteComunicacoes);
			List<TextoDto> textos = getAssinaturaDocumentoService().recuperarTextosParaAssinar(textosNaoAssinados);
			List<ComunicacaoDto> comunicacoes = getAssinaturaDocumentoService().recuperarComunicacoesParaAssinar(comunicacoesNaoAssinadas);
			preencherMensagensTextosNaoAssinados();
			preencherMensagensComunicacoesNaoAssinadas();
			if (comunicacoes.size() > 0 || textos.size() > 0) {
				getAssinaturaDocumentoService().assinarDocumentosAutomaticamente(textos, comunicacoes);
				quantidadeTextosAssinados = textos.size(); // Se não ocorreu erro, assinou todos.
				quantidadeComunicacoesAssinadas = comunicacoes.size();
				enviouAlgumParaOAssinador = true;
			} else {
				if (textosNaoAssinados.size() == 0 && comunicacoesNaoAssinadas.size() == 0) {
					// Mostrar mensagem dizendo que não há texto ou comunicação para assinar.
					addError("Não há nenhum texto ou expediente para assinar.");
				}
			}
		} catch (ServiceException e) {
			addError(e.getMessage());
			logger.error(e);
		}
	}

	protected void limpar() {
		quantidadeTextosAssinados = 0;
		warnings = false;
		errors = false;
		textosNaoAssinados = new ArrayList<DocumentoNaoAssinadoDto<TextoDto>>();
		comunicacoesNaoAssinadas = new ArrayList<DocumentoNaoAssinadoDto<ComunicacaoDto>>();
	}

	public List<DocumentoNaoAssinadoDto<? extends Documento>> getDocumentosNaoAssinados() {
		if (textosNaoAssinados.size() == 0 && comunicacoesNaoAssinadas.size() == 0) {
			return Collections.emptyList();
		}
		List<DocumentoNaoAssinadoDto<? extends Documento>> docs = new ArrayList<DocumentoNaoAssinadoDto<? extends Documento>>();
		docs.addAll(textosNaoAssinados);
		docs.addAll(comunicacoesNaoAssinadas);
		return docs;
	}

	protected void checarPermissao(ActionIdentification ai) throws ServiceException {
		if (!permissionChecker.hasPermission(usuarioLogadoService.getPrincipal(), ai)) {
			throw new ServiceException("Usuário não tem permissão para executar a ação.");
		}
	}
	
	protected void addError(String error) {
		getFacesMessages().add(Severity.ERROR, error);
		this.errors = true;
	}

	public void addWarning(String warning) {
		getFacesMessages().add(Severity.WARN, warning);
		this.warnings = true;
	}

	public boolean hasErrors() {
		return errors;
	}

	public boolean hasWarnings() {
		return warnings;
	}

	public boolean isShowModalMessages() {
		return errors || warnings;
	}

	public boolean isEnviouAlgumParaOAssinador() {
		return enviouAlgumParaOAssinador;
	}

	/**
	 * Recupera o controlador de mensagens.
	 * 
	 * @return o controlador
	 */
	private FacesMessages getFacesMessages() {
		return FacesMessages.instance();
	}

	protected Principal getPrincipal() {
		return (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	/**
	 * Recupera o usuário autenticado.
	 * 
	 * @return o usuário logado
	 */
	public Usuario getUsuario() {
		return getPrincipal().getUsuario();
	}
	
}
