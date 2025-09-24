/**
 * 
 */
package br.jus.stf.estf.decisao.texto.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.jboss.seam.annotations.Out;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.springframework.context.ApplicationContext;

import br.gov.stf.estf.documento.model.service.AssinaturaDigitalService;
import br.gov.stf.estf.documento.model.service.impl.AssinaturaDigitalServiceImpl;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoDocumentoTexto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.gov.stf.estf.entidade.documento.tipofase.TipoTransicaoFaseTexto;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.assinadorweb.api.requisicao.DocumentoPDF;
import br.jus.stf.assinadorweb.api.util.PageRefresher;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.action.handlers.CheckMinisterId;
import br.jus.stf.estf.decisao.support.action.handlers.CheckRestrictions;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.handlers.States;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.util.ApplicationContextUtils;
import br.jus.stf.estf.decisao.texto.support.RequisicaoTesteAssinaturaTexto;
import br.jus.stf.estf.decisao.texto.support.TextoBloqueadoException;
import br.jus.stf.estf.decisao.texto.support.TextoWrapper;

/**
 * @author Paulo.Estevao
 * @since 24.08.2011
 */
@Action(id = "testarAssinaturaDigitalActionFacesBean", name = "Testar Assinatura Digital", view = "/acoes/texto/testarAssinatura.xhtml", height = 200, width = 500)
@Restrict({ ActionIdentification.TESTAR_ASSINATURA_DIGITAL })
@States({ FaseTexto.LIBERADO_ASSINATURA })
@RequiresResources(Mode.Many)
@CheckMinisterId
@CheckRestrictions
public class TestarAssinaturaDigitalActionFacesBean extends AbstractAlterarFaseDoTextoActionFacesBean<TextoDto> {

	private boolean existeTextoNaoSeraAssinado;
	private Boolean assinar = false;

	@Out(value = RequisicaoTesteAssinaturaTexto.REQUISICAO_ASSINADOR)
	private RequisicaoTesteAssinaturaTexto requestAssinador;

	private Boolean inserirTimbre;

	public List<TextoDto> listaTextosValidos = new ArrayList<TextoDto>();

	public void setAssinar(Boolean assinar) {
		this.assinar = assinar;
	}

	public Boolean getAssinar() {
		return assinar;
	}

	public Boolean getInserirTimbre() {
		return inserirTimbre;
	}

	public void setInserirTimbre(Boolean inserirTimbre) {
		this.inserirTimbre = inserirTimbre;
	}

	public List<TextoDto> getListaTextosValidos() {
		return listaTextosValidos;
	}

	public void setListaTextosValidos(List<TextoDto> listaTextosValidos) {
		this.listaTextosValidos = listaTextosValidos;
	}

	/**
	 * @see br.jus.stf.estf.decisao.texto.web.AbstractAlterarFaseDoTextoActionFacesBean#getDestino()
	 */
	@Override
	protected TipoTransicaoFaseTexto getDestino() {
		return TipoTransicaoFaseTexto.ASSINAR_DIGITALMENTE;
	}

	private void adicionaTextoQueNaoSeraAssinado(Texto texto, String motivo) {
		FacesMessages.instance().add(Severity.WARN, String.format("%s: %s", texto.getIdentificacaoCompleta(), motivo));
		existeTextoNaoSeraAssinado = true;

	}

	public boolean getExistemTextosNaoSeraoAssinados() {
		return existeTextoNaoSeraAssinado;
	}

	public boolean getExistemTextosIguais() {
		return textosIguaisAdicionados != null && textosIguaisAdicionados.size() > 0;
	}

	public void validateAndExecute() {
		try {
			Set<TextoDto> textos = getResources();
			for (TextoDto texto : textos) {
				if (isTextoValidoParaAssinar(texto)) {
					List<Texto> textosIguais = recuperaTextosIguaisParaTransicaoDeFase(texto);
					// possui textos iguais
					if (textosIguais != null && textosIguais.size() > 0) {
						textosIguais.add(0, textoService.recuperarTextoPorId(texto.getId()));
						for (Texto ti : textosIguais) {
							TextoDto textoIgualDto = TextoDto.valueOf(ti);
							// Adiciona a mensagem mesmo que o texto tenha sido
							// selecionado.
							if (!textosIguaisAdicionados.contains(textoIgualDto)) {
								verificaTextoValidoParaAssinar(ti);
								adicionaMensagemTextoParaAssinar(ti);
								textosIguaisAdicionados.add(textoIgualDto);
							}
						}
					}
				}
			}
			if (hasMessages()) {
				sendToInformations();
				setAssinar(false);
			} else {
				setAssinar(true);
			}
		} catch (Exception e) {
			addError(e.getMessage());
			sendToErrors();
		}
	}

	@Override
	public void sendToInformations() {
		getDefinition().setFacet("confirmacao");
		getDefinition().setHeight(defineAlturaDaTela());
		cleanMessages();
	}

	private int defineAlturaDaTela() {
		int tamanho = 250;
		if (getExistemTextosIguais() && getExistemTextosNaoSeraoAssinados()) {
			// Duplica a altura da tela caso vá mostrar os dois casos.
			tamanho *= 2;
		}
		return tamanho;
	}

	public void executaTesteDeAssinaturaTextos() {
		try {
			testarAssinaturaTextos(getListaTextosValidos());
		} catch (Exception e) {
			logger.error("Erro ao montar a requisição de teste de assinatura digital!", e);
			addError(String.format("Erro ao montar a requisição de teste de assinatura digital: %s ",
					getMensagemDeErroPadrao(e)));
		}

		setRefresh(true);

		if (!hasMessages()) {
			sendToConfirmation();
		} else {
			sendToErrors();
		}
	}

	private void testarAssinaturaTextos(Collection<TextoDto> textos) throws ServiceException {
		ApplicationContext applicationContext = ApplicationContextUtils.getApplicationContext();
		TipoDocumentoTexto tipoDocumentoTexto = null;
		System.out.println("[Início da execução]: " + new Date());
		tipoDocumentoTexto = textoService
				.recuperarTipoDocumentoTextoPorId(TipoDocumentoTexto.COD_TIPO_DOCUMENTO_TEXTO_PADRAO);

		// Montando requisição para componente de assinatura...
		requestAssinador = new RequisicaoTesteAssinaturaTexto();

		List<DocumentoPDF<TextoWrapper>> documentos = new ArrayList<DocumentoPDF<TextoWrapper>>(textos.size());
		for (TextoDto texto : textos) {
			Long sequencialDoDocumento = textoService.recuperarSequencialDoUltimoDocumentoEletronico() + 10000L;
			String hashValidacao = AssinaturaDigitalServiceImpl.gerarHashValidacao();
			TextoWrapper textoWrapper = new TextoWrapper(applicationContext, tipoDocumentoTexto, texto,
					getInserirTimbre(), sequencialDoDocumento, hashValidacao, getUsuario().getId(), getObservacao());
			documentos.add(new DocumentoPDF<TextoWrapper>(AssinaturaDigitalServiceImpl.getRodapeAssinaturaDigital(hashValidacao), textoWrapper
					.getNome(), textoWrapper));
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
	private void setRequestValue(RequisicaoTesteAssinaturaTexto requisicao) {
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

	@Override
	public boolean hasMessages() {
		return super.hasMessages() || existeTextoNaoSeraAssinado;
	}

	/**
	 * Adiciona mensagem de texto igual.
	 * @param ti
	 */
	private void adicionaMensagemTextoParaAssinar(Texto ti) {
		addInformation(ti.getIdentificacaoCompleta());
	}

	/**
	 * Método que verifica se o texto é válido para assinatura. Se for, adiciona o texto à lista
	 * de textos para assinatura. Caso contrário, inclui o texto na lista de textos não liberados.
	 * @param texto
	 * @return
	 */
	private boolean isTextoValidoParaAssinar(TextoDto textoDto) {
		Texto texto = textoService.recuperarTextoPorId(textoDto.getId());
		return verificaTextoValidoParaAssinar(texto);
	}

	private boolean verificaTextoValidoParaAssinar(Texto texto) {
		if (!getListaTextosValidos().contains(texto)) {
			String mensagemDeErro = adicionaTextoParaAssinatura(texto);
			if (mensagemDeErro != null) {
				adicionaTextoQueNaoSeraAssinado(texto, mensagemDeErro);
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * Método que verifica se o texto é válido para assinatura. Caso haja algum problema, retorna uma 
	 * mensagem de texto contendo o erro. Caso não haja, retorna null. 
	 * @param texto O texto para assinatura
	 * @return A mensagem de erro caso haja algum; null se não houver erro.
	 * @throws ServiceException
	 */
	private String adicionaTextoParaAssinatura(Texto texto) {
		try {
			textoService.verificaTextoBloqueado(texto);
			if (texto.getTipoTexto().equals(TipoTexto.ACORDAO)) {
				Texto ementa = verificaEmentaHabilitadaParaAssinatura(texto);
				if (ementa.getTipoFaseTextoDocumento().equals(FaseTexto.LIBERADO_ASSINATURA)) {
					adicionaTextoValido(ementa);
				}
			} else if (texto.getTipoTexto().equals(TipoTexto.DECISAO_SOBRE_REPERCURSAO_GERAL)) {
				Texto ementaRepGeral = verificaEmentaRepercussaoGeralHabilitadaParaAssinatura(texto);
				if (ementaRepGeral.getTipoFaseTextoDocumento().equals(FaseTexto.LIBERADO_ASSINATURA)) {
					adicionaTextoValido(ementaRepGeral);
				}
			}
			adicionaTextoValido(texto);
			return null;
		} catch (ServiceException e) {
			return e.getMessage();
		} catch (TextoBloqueadoException e) {
			return e.getMessage();
		}
	}

	/**
	 * Adiciona um texto válido, verificando se o mesmo já não se encontra na lista, 
	 * evitando a assinatura duplicada.
	 * @param texto
	 */
	private void adicionaTextoValido(Texto texto) {
		TextoDto textoDto = TextoDto.valueOf(texto);
		if (!getListaTextosValidos().contains(textoDto)) {
			getListaTextosValidos().add(textoDto);
		}
	}

	/**
	 * Verifica se o acórdão possui uma ementa gerada, e se ela está em uma fase maior ou igual a Liberado Para Assinatura.  
	 * @param texto
	 * @return
	 * @throws ServiceException
	 */
	protected Texto verificaEmentaHabilitadaParaAssinatura(Texto texto) throws ServiceException {
		Texto ementa = textoService.recuperarEmenta(texto, getMinistro());
		if (ementa == null) {
			throw new ServiceException(
					"O acórdão não poderá ser assinado pois não existe ementa gerada para o processo!");
		}
		if (isFaseMenorLiberadoParaAssinatura(ementa.getTipoFaseTextoDocumento())) {
			throw new ServiceException(
					"O acordão não poderá ser assinado pois a ementa não foi liberada para assinatura!");
		}
		return ementa;
	}

	/**
	 * Verifica se a decisão sobre repercussão geral possui uma ementa sobre repercussão geral gerada, e se ela está em uma fase maior ou igual a Liberado Para Assinatura.  
	 * @param texto
	 * @return
	 * @throws ServiceException
	 */
	protected Texto verificaEmentaRepercussaoGeralHabilitadaParaAssinatura(Texto texto) throws ServiceException {
		Texto ementaRepGeral = textoService.recuperarEmentaRepercussaoGeral(texto, getMinistro());
		if (ementaRepGeral == null) {
			throw new ServiceException(
					"A decisão sobre repercussão geral não poderá ser assinada pois não existe ementa sobre repercussão gerada para o processo!");
		}
		if (isFaseMenorLiberadoParaAssinatura(ementaRepGeral.getTipoFaseTextoDocumento())) {
			throw new ServiceException(
					"A decisão sobre repercussão geral não poderá ser assinada pois a ementa sobre repercussão não foi liberada para assinatura!");
		}
		return ementaRepGeral;
	}

	private boolean isFaseMenorLiberadoParaAssinatura(FaseTexto fase) {
		return fase.compareTo(FaseTexto.LIBERADO_ASSINATURA) < 0;
	}

	public void voltar() {
		getDefinition().setFacet("principal");
		getDefinition().setHeight(200);
	}

}
