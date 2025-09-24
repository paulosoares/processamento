package br.jus.stf.estf.decisao.documento.web;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import br.jus.stf.estf.decisao.documento.service.AssinaturaDocumentoService;
import br.jus.stf.estf.decisao.documento.service.impl.AssinaturaExternaDocumentoServiceImpl;
import br.jus.stf.estf.decisao.documento.support.assinador.RefAssinaturaExterna;
import br.jus.stf.estf.decisao.mobile.assinatura.support.AssinaturaDocumentoDto;
import br.jus.stf.estf.decisao.mobile.assinatura.support.AssinaturaExternaDocumentoDto;
import br.jus.stf.estf.decisao.pesquisa.domain.ComunicacaoDto;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;

@Name("assinarExternamenteDocumentosFacesBean")
@Scope(ScopeType.CONVERSATION)
public class AssinarExternamenteDocumentosFacesBean extends AbstractAssinarDocumentosBean {

	private List<String> errorMessages = new ArrayList<String>();
	private List<String> warningMessages = new ArrayList<String>();

	@In("#{assinaturaExternaDocumentoService}")
	private AssinaturaExternaDocumentoServiceImpl assinaturaExternaDocumentoService;

	@Override
	protected AssinaturaDocumentoService getAssinaturaDocumentoService() {
		return assinaturaExternaDocumentoService;
	}

	public AssinaturaExternaDocumentoDto preAssinarTextos(List<Long> ids, String[] cadeia) {
		limpar();
		RefAssinaturaExterna refAssinatura = null;
		try {
			checarPermissao(permissaoAssinarDigitalmente);
			List<TextoDto> textos = getAssinaturaDocumentoService().recuperarTextosParaAssinar(ids, textosNaoAssinados);
			preencherMensagensTextosNaoAssinados();
			if (textos.size() == 1) {
				refAssinatura = assinaturaExternaDocumentoService.preAssinarTexto(textos.get(0), cadeia);
				quantidadeTextosAssinados = textos.size(); // Se não ocorreu erro, assinou todos.
				enviouAlgumParaOAssinador = true;
			} else if (textos.size() > 0) {
				addError("É permitido assinar apenas um texto por vez.");
			} else {
				// Mostrar mensagem dizendo que não há textos para assinar.
				if (textosNaoAssinados.size() == 0) {
					addError("Não há nenhum texto para assinar.");
				}
			}
		} catch (Exception e) {
			addError(e.getMessage());
			logger.error(e);
		}
		return buildDto(refAssinatura);
	}

	private AssinaturaExternaDocumentoDto buildDto(RefAssinaturaExterna ref) {
		AssinaturaExternaDocumentoDto adDto = new AssinaturaExternaDocumentoDto();
		adDto.setQuantidadeTextosAssinados(quantidadeTextosAssinados);
		adDto.setQuantidadeComunicacoesAssinadas(quantidadeComunicacoesAssinadas);
		adDto.setErrors(errorMessages);
		adDto.setWarnings(warningMessages);
		if (ref != null) {
			adDto.setHash(ref.getHash());
			adDto.setId(ref.getId());
		}
		return adDto;
	}

	public AssinaturaDocumentoDto posAssinarTextos(List<Long> ids, String idContexto, String assinatura) {
		limpar();
		try {
			checarPermissao(permissaoAssinarDigitalmente);
			List<TextoDto> textos = getAssinaturaDocumentoService().recuperarTextosParaAssinar(ids, textosNaoAssinados);
			preencherMensagensTextosNaoAssinados();
			if (textos.size() == 1) {
				assinaturaExternaDocumentoService.posAssinarTexto(textos.get(0), idContexto, assinatura);
				quantidadeTextosAssinados = textos.size(); // Se não ocorreu erro, assinou todos.
				enviouAlgumParaOAssinador = true;
			} else if (textos.size() > 0) {
				addError("É permitido assinar apenas um texto por vez.");
			} else {
				// Mostrar mensagem dizendo que não há textos para assinar.
				if (textosNaoAssinados.size() == 0) {
					addError("Não há nenhum texto para assinar.");
				}
			}
		} catch (Exception e) {
			addError(e.getMessage());
			logger.error(e);
		}
		return buildDto();
	}

	private AssinaturaDocumentoDto buildDto() {
		AssinaturaDocumentoDto adDto = new AssinaturaDocumentoDto();
		adDto.setQuantidadeTextosAssinados(quantidadeTextosAssinados);
		adDto.setQuantidadeComunicacoesAssinadas(quantidadeComunicacoesAssinadas);
		adDto.setErrors(errorMessages);
		adDto.setWarnings(warningMessages);
		return adDto;
	}

	public AssinaturaExternaDocumentoDto preAssinarComunicacoes(List<Long> ids, String[] cadeia) {
		limpar();
		RefAssinaturaExterna refAssinatura = null;
		try {
			checarPermissao(permissaoAssinarDigitalmente);
			List<ComunicacaoDto> comunicacoes = getAssinaturaDocumentoService().recuperarComunicacoesParaAssinar(ids, comunicacoesNaoAssinadas);
			preencherMensagensComunicacoesNaoAssinadas();
			if (comunicacoes.size() == 1) {
				refAssinatura = assinaturaExternaDocumentoService.preAssinarComunicacao(comunicacoes.get(0), cadeia);
				quantidadeComunicacoesAssinadas = comunicacoes.size(); // Se não ocorreu erro, assinou todos.
				enviouAlgumParaOAssinador = true;
			} else if (comunicacoes.size() > 0) {
				addError("É permitido assinar apenas uma comunicação por vez.");
			} else {
				// Mostrar mensagem dizendo que não há comunicações para assinar.
				if (comunicacoesNaoAssinadas.size() == 0) {
					addError("Não há nenhuma comunicação para assinar.");
				}
			}
		} catch (Exception e) {
			addError(e.getMessage());
			logger.error(e);
		}
		return buildDto(refAssinatura);
	}

	public AssinaturaDocumentoDto posAssinarComunicacoes(List<Long> ids, String idContexto, String assinatura) {
		limpar();
		try {
			checarPermissao(permissaoAssinarDigitalmente);
			List<ComunicacaoDto> comunicacoes = getAssinaturaDocumentoService().recuperarComunicacoesParaAssinar(ids, comunicacoesNaoAssinadas);
			preencherMensagensComunicacoesNaoAssinadas();
			if (comunicacoes.size() == 1) {
				assinaturaExternaDocumentoService.posAssinarComunicacao(comunicacoes.get(0), idContexto, assinatura);
				quantidadeComunicacoesAssinadas = comunicacoes.size(); // Se não ocorreu erro, assinou todos.
				enviouAlgumParaOAssinador = true;
			} else if (comunicacoes.size() > 0) {
				addError("É permitido assinar apenas uma comunicação por vez.");
			} else {
				// Mostrar mensagem dizendo que não há comunicações para assinar.
				if (comunicacoesNaoAssinadas.size() == 0) {
					addError("Não há nenhuma comunicação para assinar.");
				}
			}
		} catch (Exception e) {
			addError(e.getMessage());
			logger.error(e);
		}
		return buildDto();
	}

	@Override
	protected void limpar() {
		errorMessages.clear();
		warningMessages.clear();
		super.limpar();
	}

	@Override
	protected void addError(String error) {
		errorMessages.add(error);
	}

	@Override
	public void addWarning(String warning) {
		warningMessages.add(warning);
	}
	
}
