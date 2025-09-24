package br.jus.stf.estf.decisao.mobile.assinatura.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.springframework.security.BadCredentialsException;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.providers.ldap.authenticator.BindAuthenticator;

import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.documento.service.AssinaturaDocumentoService;
import br.jus.stf.estf.decisao.documento.support.DocumentoNaoAssinadoDto;
import br.jus.stf.estf.decisao.documento.web.AbstractAssinarDocumentosBean;
import br.jus.stf.estf.decisao.mobile.assinatura.support.AssinaturaDocumentoDto;
import br.jus.stf.estf.decisao.mobile.assinatura.support.DocumentoDto;
import br.jus.stf.estf.decisao.pesquisa.domain.ComunicacaoDto;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;

/**
 * Bean JSF (Seam Component) para assinatura contingencial de documentos
 * (textos ou comunicações) automaticamente (em apenas um passo).
 * 
 * @author Tomas.Godoi
 * 
 */
@Name("assinarContingencialmenteMobileService")
@Scope(ScopeType.CONVERSATION)
public class AssinarContingencialmenteMobileService extends AbstractAssinarDocumentosBean {

	@In("#{assinaturaContingencialDocumentoService}")
	private AssinaturaDocumentoService assinaturaDocumentoService;

	@In("#{textoFacesBeanMobile}")
	private TextoMobileService textoFacesBeanMobile;

	@In("#{bindAuthenticator}")
	private BindAuthenticator bindAuthenticator;

	@In("#{comunicacaoFacesBeanMobile}")
	private ComunicacaoMobileService comunicacaoFacesBeanMobile;

	private List<String> errors = new ArrayList<String>();
	private List<String> warnings = new ArrayList<String>();
	private boolean erroImpeditivo = false;

	@Override
	protected AssinaturaDocumentoService getAssinaturaDocumentoService() {
		return assinaturaDocumentoService;
	}

	public AssinaturaDocumentoDto assinarDocumentosAutomaticamenteComAutenticacao(String senha) {
		try {
			verificarAutenticacao(senha);
			super.assinarDocumentosAutomaticamente();
		} catch (ServiceException e) {
			logger.error(e);
			addError(e.getMessage());
		} catch (Exception e) {
			logger.error(e);
			addError("Erro ao assinar documentos.");
		}
		return buildDto();
	}

	public AssinaturaDocumentoDto assinarTextosComAutenticacao(List<Long> ids, String senha) {
		try {
			verificarAutenticacao(senha);
			assinarTextos(ids);
		} catch (ServiceException e) {
			logger.error(e);
			addError(e.getMessage());
		} catch (Exception e) {
			logger.error(e);
			addError("Erro ao assinar textos.");
		}
		return buildDto();
	}

	public AssinaturaDocumentoDto assinarComunicacoesAutomaticamenteComAutenticacao(String senha) {
		try {
			verificarAutenticacao(senha);
			super.assinarComunicacoesAutomaticamente();
		} catch (ServiceException e) {
			logger.error(e);
			addError(e.getMessage());
		} catch (Exception e) {
			logger.error(e);
			addError("Erro ao assinar comunicações.");
		}
		return buildDto();
	}

	public AssinaturaDocumentoDto assinarComunicacoesComAutenticacao(List<Long> ids, String senha) {
		try {
			verificarAutenticacao(senha);
			assinarComunicacoes(ids);
		} catch (ServiceException e) {
			logger.error(e);
			addError(e.getMessage());
		} catch (Exception e) {
			logger.error(e);
			addError("Erro ao assinar comunicações.");
		}
		return buildDto();
	}

	public List<DocumentoDto<?>> getDocumentosParaAssinar() throws ServiceException {
		List<DocumentoDto<?>> docsParaAssinar = new ArrayList<DocumentoDto<?>>();

		docsParaAssinar.addAll(getTextosParaAssinar());
		docsParaAssinar.addAll(getComunicacoesParaAssinar());

		return docsParaAssinar;
	}

	/**
	 * Recupera os textos cujos ids são passados, aplicando as regras de se esse texto está
	 * liberado para assinatura e pertence ao gabinete do usuário logado.
	 * 
	 * @param ids
	 * @param textosNaoAssinar
	 * @return
	 * @throws ServiceException
	 */
	public List<TextoDto> getTextosParaAssinar(List<Long> ids, List<DocumentoNaoAssinadoDto<TextoDto>> textosNaoAssinar) throws ServiceException {
		List<TextoDto> textosAssinar = getAssinaturaDocumentoService().recuperarTextosParaAssinar(ids, textosNaoAssinar);
		return textosAssinar;
	}

	/**
	 * Recupera os textos cujos ids são passados, aplicando as regras de se esse texto está
	 * liberado para assinatura e pertence ao gabinete do usuário logado.
	 * 
	 * @param ids
	 * @return
	 * @throws ServiceException
	 */
	public List<DocumentoDto<TextoDto>> getTextosParaAssinar(List<Long> ids) throws ServiceException {
		List<DocumentoDto<TextoDto>> textosAssinar = getAssinaturaDocumentoService().recuperarTextosParaAssinarMantendoNaoPermitidos(ids);
		return textosAssinar;
	}

	public List<DocumentoDto<TextoDto>> getTextosComDetalhesParaAssinar(List<Long> ids) throws ServiceException {
		List<DocumentoDto<TextoDto>> docsTextosParaAssinar = new ArrayList<DocumentoDto<TextoDto>>();
		List<DocumentoNaoAssinadoDto<TextoDto>> textosNaoAssinar = new ArrayList<DocumentoNaoAssinadoDto<TextoDto>>();
		List<TextoDto> textosAssinar = getTextosParaAssinar(ids, textosNaoAssinar);
		for (TextoDto cDto : textosAssinar) {
			String conteudo = textoFacesBeanMobile.loadConteudo(cDto);
			docsTextosParaAssinar.add(DocumentoDto.detalhadoFrom(cDto, conteudo));
		}
		for (DocumentoNaoAssinadoDto<TextoDto> dnad : textosNaoAssinar) {
			String conteudo = textoFacesBeanMobile.loadConteudo(dnad.getDocumento());
			docsTextosParaAssinar.add(DocumentoDto.detalhadoFromNaoAssinadoTexto(dnad, conteudo));
		}
		return docsTextosParaAssinar;
	}

	public List<DocumentoDto<TextoDto>> getTextosParaAssinar() throws ServiceException {
		return getAssinaturaDocumentoService().recuperarTextosParaAssinarMantendoNaoPermitidos();
	}

	/**
	 * Recupera as comunicações cujos ids são passados, aplicando as regras de se essa comunicação está
	 * liberada para assinatura e pertence ao gabinete do usuário logado.
	 * 
	 * @param ids
	 * @param comunicacoesNaoAssinar
	 * @return
	 * @throws ServiceException
	 */
	public List<ComunicacaoDto> getComunicacoesParaAssinar(List<Long> ids, List<DocumentoNaoAssinadoDto<ComunicacaoDto>> comunicacoesNaoAssinar)
			throws ServiceException {
		List<ComunicacaoDto> comunicacoesAssinar = getAssinaturaDocumentoService().recuperarComunicacoesParaAssinar(ids, comunicacoesNaoAssinar);
		return comunicacoesAssinar;
	}

	/**
	 * Recupera as comunicações cujos ids são passados, aplicando as regras de se essa comunicação está
	 * liberada para assinatura e pertence ao gabinete do usuário logado.
	 * 
	 * @param ids
	 * @return
	 * @throws ServiceException
	 */
	public List<ComunicacaoDto> getComunicacoesParaAssinar(List<Long> ids) throws ServiceException {
		return getComunicacoesParaAssinar(ids, new ArrayList<DocumentoNaoAssinadoDto<ComunicacaoDto>>());
	}

	public List<DocumentoDto<ComunicacaoDto>> getComunicacoesComDetalhesParaAssinar(List<Long> ids) throws ServiceException {
		List<DocumentoDto<ComunicacaoDto>> docsComunicacoesParaAssinar = new ArrayList<DocumentoDto<ComunicacaoDto>>();
		List<DocumentoNaoAssinadoDto<ComunicacaoDto>> comunicacoesNaoAssinar = new ArrayList<DocumentoNaoAssinadoDto<ComunicacaoDto>>();
		List<ComunicacaoDto> comunicacoesAssinar = getComunicacoesParaAssinar(ids, comunicacoesNaoAssinar);
		for (ComunicacaoDto cDto : comunicacoesAssinar) {
			docsComunicacoesParaAssinar.add(DocumentoDto.withPdfPageCountFrom(cDto, comunicacaoFacesBeanMobile.totalPaginasConteudo(cDto)));
		}
		for (DocumentoNaoAssinadoDto<ComunicacaoDto> dnad : comunicacoesNaoAssinar) {
			docsComunicacoesParaAssinar.add(DocumentoDto.fromNaoAssinadoComunicacao(dnad));
		}
		return docsComunicacoesParaAssinar;
	}

	public List<DocumentoDto<ComunicacaoDto>> getComunicacoesParaAssinar() throws ServiceException {
		return getAssinaturaDocumentoService().recuperarComunicacoesParaAssinarMantendoNaoPermitidosMobile();
	}

	private void verificarAutenticacao(String senha) throws ServiceException {
		if (StringUtils.isEmpty(senha)) {
			erroImpeditivo = true;
			throw new ServiceException("A senha é obrigatória.");
		}
		try {
			bindAuthenticator.authenticate(new UsernamePasswordAuthenticationToken(getUsuario().getId(), senha));
		} catch (BadCredentialsException e) {
			erroImpeditivo = true;
			throw new ServiceException("Senha inválida.");
		}
	}

	private AssinaturaDocumentoDto buildDto() {
		AssinaturaDocumentoDto adDto = new AssinaturaDocumentoDto();
		adDto.setQuantidadeTextosAssinados(quantidadeTextosAssinados);
		adDto.setQuantidadeComunicacoesAssinadas(quantidadeComunicacoesAssinadas);
		adDto.setErrors(errors);
		adDto.setWarnings(warnings);
		adDto.setErroImpeditivo(erroImpeditivo);
		return adDto;
	}

	@Override
	protected void limpar() {
		errors.clear();
		warnings.clear();
		erroImpeditivo = false;
		super.limpar();
	}

	@Override
	protected void addError(String error) {
		errors.add(error);
	}

	@Override
	public void addWarning(String warning) {
		warnings.add(warning);
	}

}
