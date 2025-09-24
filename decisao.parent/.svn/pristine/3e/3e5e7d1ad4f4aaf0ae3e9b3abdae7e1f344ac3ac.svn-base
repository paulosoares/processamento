package br.jus.stf.estf.decisao.documento.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.service.AssinaturaDigitalService;
import br.gov.stf.estf.documento.model.service.impl.AssinaturaDigitalServiceImpl;
import br.gov.stf.estf.entidade.documento.DocumentoComunicacao;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.TipoDocumentoTexto;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.assinadorweb.api.requisicao.DocumentoPDF;
import br.jus.stf.assinadorweb.api.requisicao.RequisicaoJnlpAssinador;
import br.jus.stf.assinadorweb.api.util.PageRefresher;
import br.jus.stf.estf.decisao.comunicacao.support.ComunicacaoWrapper;
import br.jus.stf.estf.decisao.comunicacao.support.RequisicaoAssinaturaComunicacao;
import br.jus.stf.estf.decisao.documento.support.DocumentoWrapper;
import br.jus.stf.estf.decisao.documento.support.RequisicaoAssinaturaDocumento;
import br.jus.stf.estf.decisao.pesquisa.domain.ComunicacaoDto;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.util.ApplicationContextUtils;
import br.jus.stf.estf.decisao.texto.support.RequisicaoAssinaturaTexto;
import br.jus.stf.estf.decisao.texto.support.TextoWrapper;

/**
 * Serviço que realiza a assinatura digital de documentos (textos ou comunicações).
 * Para isso, utiliza o mecanismo do applet assinador.
 * 
 * @author Tomas.Godoi
 * 
 */
@Service("assinaturaDigitalDocumentoService")
public class AssinaturaDigitalDocumentoServiceImpl extends AbstractAssinaturaDocumentoService {

	private RequisicaoAssinaturaComunicacao requestAssinadorComunicacao;

	private RequisicaoAssinaturaTexto requestAssinadorTexto;

	private RequisicaoAssinaturaDocumento requestAssinadorDocumento;

	@Override
	public void assinarTextosAutomaticamente(List<TextoDto> textos) throws ServiceException {
		ApplicationContext applicationContext = ApplicationContextUtils.getApplicationContext();
		System.out.println("[Início da execução]: " + new Date());

		// Montando requisição para componente de assinatura...
		requestAssinadorTexto = new RequisicaoAssinaturaTexto();

		List<DocumentoPDF<TextoWrapper>> documentos = montarWrappersTexto(textos, applicationContext, TextoWrapper.class);
		requestAssinadorTexto.setDocumentos(documentos);
		requestAssinadorTexto.setPageRefresher((PageRefresher) applicationContext.getBean("refreshController"));

		// Setando requisição como parâmetro do request...
		setRequestValue(requestAssinadorTexto);
		forward();
	}

	@SuppressWarnings("unchecked")
	private <T extends DocumentoWrapper> List<DocumentoPDF<T>> montarWrappersTexto(List<TextoDto> textos, ApplicationContext applicationContext, Class<T> clazz) throws ServiceException{
		TipoDocumentoTexto tipoDocumentoTexto = null;
		tipoDocumentoTexto = textoService.recuperarTipoDocumentoTextoPorId(TipoDocumentoTexto.COD_TIPO_DOCUMENTO_TEXTO_PADRAO);
		List<DocumentoPDF<T>> documentos = new ArrayList<DocumentoPDF<T>>(textos.size());
		for (TextoDto texto : textos) {
			Long sequencialDoDocumento = textoService.recuperarSequencialDoDocumentoEletronico(texto);
			String hashValidacao = documentoEletronicoService.gerarHashValidacao(sequencialDoDocumento);
			
			TextoWrapper textoWrapper = new TextoWrapper(applicationContext, tipoDocumentoTexto, texto, getInserirTimbreTexto(), sequencialDoDocumento, hashValidacao,
					getUsuario().getId(), getObservacaoTexto());
			documentos.add(new DocumentoPDF<T>(AssinaturaDigitalServiceImpl.getRodapeAssinaturaDigital(hashValidacao), textoWrapper.getNome(), (T) textoWrapper));
		}
		return documentos;
	}

	/**
	 * Padrão vazio
	 * 
	 * @return
	 */
	private String getObservacaoTexto() {
		return "";
	}

	/**
	 * Padrão false
	 * 
	 * @return
	 */
	private Boolean getInserirTimbreTexto() {				
		try {
			return configuracaoSistemaService.isGerarTimbreAssinatura();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			return false;
		}
	}

	@Override
	public void assinarComunicacoesAutomaticamente(List<ComunicacaoDto> comunicacoes) throws ServiceException {
		ApplicationContext applicationContext = ApplicationContextUtils.getApplicationContext();
		logger.info("[Início da execução]: " + new Date());

		// Montando requisição para componente de assinatura...
		requestAssinadorComunicacao = new RequisicaoAssinaturaComunicacao();

		List<DocumentoPDF<ComunicacaoWrapper>> documentos = montarWrappersComunicacao(comunicacoes, applicationContext, ComunicacaoWrapper.class);
		requestAssinadorComunicacao.setDocumentos(documentos);
		requestAssinadorComunicacao.setPageRefresher((PageRefresher) applicationContext.getBean("refreshController"));

		// Setando requisição como parâmetro do request...
		setRequestValue(requestAssinadorComunicacao);
		forward();
	}

	@SuppressWarnings("unchecked")
	private <T extends DocumentoWrapper> List<DocumentoPDF<T>> montarWrappersComunicacao(List<ComunicacaoDto> comunicacoes,
			ApplicationContext applicationContext, Class<T> clazz) throws ServiceException {
		List<DocumentoPDF<T>> documentos = new ArrayList<DocumentoPDF<T>>(comunicacoes.size());
		for (ComunicacaoDto comunicacaoDto : comunicacoes) {
			
			DocumentoComunicacao documentoComunicacao = documentoComunicacaoService.recuperarPorId(comunicacaoDto.getIdDocumentoComunicacao());
			String hashValidacao = documentoEletronicoService.gerarHashValidacao(documentoComunicacao);
			
			ComunicacaoWrapper comunicacaoWrapper = new ComunicacaoWrapper(applicationContext, comunicacaoDto, getUsuario().getId(), hashValidacao);
			String rodape = null;
			if (!comunicacaoDto.getDescricaoStatusDocumento().equals(DocumentoEletronico.SIGLA_DESCRICAO_STATUS_ASSINADO)
					&& !comunicacaoDto.getDescricaoStatusDocumento().equals(DocumentoEletronico.SIGLA_DESCRICAO_STATUS_AGUARDANDO)) {
				rodape = AssinaturaDigitalServiceImpl.getRodapeAssinaturaDigital(hashValidacao);
			}
			documentos.add(new DocumentoPDF<T>(rodape, comunicacaoWrapper.getNome(), (T) comunicacaoWrapper));
		}
		return documentos;
	}

	@Override
	public void assinarDocumentosAutomaticamente(List<TextoDto> textos, List<ComunicacaoDto> comunicacoes) throws ServiceException {
		ApplicationContext applicationContext = ApplicationContextUtils.getApplicationContext();
		System.out.println("[Início da execução]: " + new Date());

		// Montando requisição para componente de assinatura...
		requestAssinadorDocumento = new RequisicaoAssinaturaDocumento();

		List<DocumentoPDF<DocumentoWrapper>> textoWrappers = montarWrappersTexto(textos, applicationContext, DocumentoWrapper.class);
		List<DocumentoPDF<DocumentoWrapper>> comunicacaoWrappers = montarWrappersComunicacao(comunicacoes, applicationContext, DocumentoWrapper.class);

		List<DocumentoPDF<DocumentoWrapper>> documentoWrappers = new ArrayList<DocumentoPDF<DocumentoWrapper>>();
		documentoWrappers.addAll(textoWrappers);
		documentoWrappers.addAll(comunicacaoWrappers);

		requestAssinadorDocumento.setDocumentos(documentoWrappers);
		requestAssinadorDocumento.setPageRefresher((PageRefresher) applicationContext.getBean("refreshController"));

		// Setando requisição como parâmetro do request...
		setRequestValue(requestAssinadorDocumento);
		forward();
	}

	/**
	 * Redireciona para o Servlet de Assinatura.
	 */
	private void forward() {
		javax.faces.context.FacesContext context = javax.faces.context.FacesContext.getCurrentInstance();
		ServletResponse response = (ServletResponse) context.getExternalContext().getResponse();
		ServletRequest request = (ServletRequest) context.getExternalContext().getRequest();
		try {
			// Por algum motivo o redirect usando o pages.xml (JSF) não
			// funcionou.
			// A alternativa foi usar o RequestDispatcher fazendo um forward
			// manual.
			request.getRequestDispatcher(AssinaturaDigitalService.PATH_ASSINADOR).forward(request, response);
			context.responseComplete();
		} catch (Exception e) {
			new RuntimeException(e);
		}
	}

	/**
	 * Seta uma requisição para assinatura como parâmentro da requisição Http (HttpServletRequest).
	 */
	private void setRequestValue(RequisicaoJnlpAssinador<?> requisicao) {
		HttpServletRequest request = (HttpServletRequest) javax.faces.context.FacesContext.getCurrentInstance().getExternalContext().getRequest();
		request.setAttribute(RequisicaoJnlpAssinador.REQUISICAO_ASSINADOR, requisicao);
	}

}
