/**
 * 
 */
package br.jus.stf.estf.decisao.comunicacao.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.jboss.seam.annotations.Out;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import br.gov.stf.estf.documento.model.service.AssinaturaDigitalService;
import br.gov.stf.estf.documento.model.service.DocumentoComunicacaoService;
import br.gov.stf.estf.documento.model.service.DocumentoEletronicoService;
import br.gov.stf.estf.documento.model.service.impl.AssinaturaDigitalServiceImpl;
import br.gov.stf.estf.entidade.documento.DocumentoComunicacao;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.assinadorweb.api.requisicao.DocumentoPDF;
import br.jus.stf.assinadorweb.api.util.PageRefresher;
import br.jus.stf.estf.decisao.comunicacao.support.ComunicacaoWrapper;
import br.jus.stf.estf.decisao.comunicacao.support.RequisicaoTesteAssinaturaComunicacao;
import br.jus.stf.estf.decisao.pesquisa.domain.ComunicacaoDto;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionInterface;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;
import br.jus.stf.estf.decisao.support.util.ApplicationContextUtils;
import br.jus.stf.estf.decisao.texto.support.RequisicaoTesteAssinaturaTexto;

/**
 * @author Paulo.Estevao
 * @since 13.05.2011
 */
@Action(id = "testarAssinaturaDigitalComunicacoesActionFacesBean", 
		name = "Testar Assinatura Digital", view = "/acoes/comunicacao/assinar.xhtml", height = 200, width = 500)
@Restrict({ActionIdentification.TESTAR_ASSINATURA_DIGITAL_DE_COMUNICACOES})
@RequiresResources(Mode.Many)
public class TestarAssinaturaDigitalComunicacoesActionFacesBean extends
		ActionSupport<ComunicacaoDto> implements
		ActionInterface<ComunicacaoDto> {
	
	@Out(value = RequisicaoTesteAssinaturaComunicacao.REQUISICAO_ASSINADOR)
	private RequisicaoTesteAssinaturaComunicacao requestAssinador;
	
	@Autowired
	private DocumentoComunicacaoService documentoComunicacaoService;
	
	@Autowired
	private DocumentoEletronicoService documentoEletronicoService;

	public void execute() {
		try {
			assinarComunicacoes(getResources());
		} catch (ServiceException e) {
			addError(e.getMessage());
			logger.error(e);
		}
		setRefresh(true);
	}
	
	private void assinarComunicacoes(Collection<ComunicacaoDto> comunicacoes) throws ServiceException {
		ApplicationContext applicationContext = ApplicationContextUtils.getApplicationContext();
		logger.info("[Início da execução]: " + new Date());

		// Montando requisição para componente de assinatura...
		requestAssinador = new RequisicaoTesteAssinaturaComunicacao();

		List<DocumentoPDF<ComunicacaoWrapper>> documentos = new ArrayList<DocumentoPDF<ComunicacaoWrapper>>(comunicacoes.size());
		for (ComunicacaoDto comunicacaoDto : comunicacoes) {
			
			DocumentoComunicacao documentoComunicacao = documentoComunicacaoService.recuperarPorId(comunicacaoDto.getIdDocumentoComunicacao());
			String hashValidacao = documentoEletronicoService.gerarHashValidacao(documentoComunicacao);
			ComunicacaoWrapper comunicacaoWrapper = new ComunicacaoWrapper(applicationContext, comunicacaoDto, getUsuario().getId(), hashValidacao);
			String rodape = null;
			if (!comunicacaoDto.getDescricaoStatusDocumento().equals(DocumentoEletronico.SIGLA_DESCRICAO_STATUS_ASSINADO)
					&& !comunicacaoDto.getDescricaoStatusDocumento().equals(DocumentoEletronico.SIGLA_DESCRICAO_STATUS_AGUARDANDO)) {
				rodape = AssinaturaDigitalServiceImpl.getRodapeAssinaturaDigital(hashValidacao);
			}
			documentos.add(new DocumentoPDF<ComunicacaoWrapper>(rodape, comunicacaoWrapper.getNome(), comunicacaoWrapper));
		}
		requestAssinador.setDocumentos(documentos);
		requestAssinador.setPageRefresher((PageRefresher) applicationContext.getBean("refreshController"));

		// Setando requisição como parâmetro do request...
		setRequestValue(requestAssinador);
		forward();

	}
	
	/**
	 * Seta uma requisição para assinatura como parâmentro da requisição Http (HttpServletRequest).
	 */
	private void setRequestValue(RequisicaoTesteAssinaturaComunicacao requisicao) {
		HttpServletRequest request = (HttpServletRequest) javax.faces.context.FacesContext.getCurrentInstance()
				.getExternalContext().getRequest();
		request.setAttribute(RequisicaoTesteAssinaturaTexto.REQUISICAO_ASSINADOR, requisicao);
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
}
