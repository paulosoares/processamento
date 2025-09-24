package br.jus.stf.estf.decisao.texto.web;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.gov.stf.estf.entidade.documento.tipofase.TipoTransicaoFaseTexto;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.TipoConfidencialidade;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.api.ReferendarDecisaoResultadoDto;
import br.jus.stf.estf.decisao.api.ReferendarDecisaoResultadoDto.TipoMensagem;
import br.jus.stf.estf.decisao.objetoincidente.service.ReferendarDecisaoService;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.action.handlers.CheckMinisterId;
import br.jus.stf.estf.decisao.support.action.handlers.CheckRestrictions;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.handlers.States;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.texto.support.TextoBloqueadoException;
import br.jus.stf.estf.decisao.texto.support.TextoComSituacaoDaPublicacaoVO;
import edu.emory.mathcs.backport.java.util.TreeSet;


/**
 * @author Rodrigo Barreiros
 */
@Action(id = "liberarParaPublicacaoActionFacesBean", name = "Liberar para Publicação", view = "/acoes/texto/publicacao/liberar.xhtml", height = 170, width = 500)
@Restrict({ActionIdentification.LIBERAR_PARA_PUBLICACAO})
@States({ FaseTexto.ASSINADO, FaseTexto.JUNTADO })
@RequiresResources(Mode.Many)
@CheckMinisterId
@CheckRestrictions
public class LiberarParaPublicacaoActionFacesBean extends AbstractAlterarFaseDoTextoActionFacesBean<TextoDto> {

	private static final String MENSAGEM_TEXTO_PROCESSO_OCULTO = "Este texto é vinculado a processo oculto e, como tal, não pode ser liberado para publicação. Para publicá-lo, é necessário despacho do Min. Relator determinando à Secretaria Judiciária que retire sua condição de oculto.";
	private static final String MENSAGEM_TEXTO_PROCESSO_SIGILOSO = "Este texto é vinculado a processo sigiloso e, como tal, não pode ser liberado para publicação. Para publicá-lo, é necessário despacho do Min. Relator determinando à Secretaria Judiciária que retire sua condição de sigilo.";
	private Boolean rtj;
	private Boolean existemTextosNaoSeraoLiberados;
	public Set<TextoDto> listaTextosValidos = new HashSet<TextoDto>();
	private Boolean existeTextoValido = false;
	private Boolean existeTextoNaoProcessado = false;
	private Boolean existeTextoDeProcessoEmSegredoDeJustica = false;
	private Boolean exibirTextoSigilosoOculto = false;
	private Boolean exibirMensagemReferendo = false;
	
//	@Autowired
//	private ReferendarDecisaoService referendarDecisaoService;
	
	@Override
	public void load() {
		for (TextoDto textoDto : getResources()) {
			Texto texto = textoService.recuperarTextoPorId(textoDto.getId());
			if (isTextoProcessoOculto(texto)) {
				exibirTextoSigilosoOculto = true;
			}
			
//			if (TipoTexto.DECISAO_MONOCRATICA.equals(texto.getTipoTexto()))
//				exibirMensagemReferendo = true;
		}
	}
	
	public Boolean getExisteTextoValido() {
		return existeTextoValido;
	}

	public void setExisteTextoValido(Boolean existeTextoValido) {
		this.existeTextoValido = existeTextoValido;
	}

	public Boolean getExisteTextoNaoProcessado() {
		return existeTextoNaoProcessado;
	}

	public void setExisteTextoNaoProcessado(Boolean existeTextoNaoProcessado) {
		this.existeTextoNaoProcessado = existeTextoNaoProcessado;
	}

	public Boolean getExistemTextosNaoSeraoLiberados() {
		return existemTextosNaoSeraoLiberados;
	}

	public void setExistemTextosNaoSeraoLiberados(Boolean existemTextosNaoSeraoLiberados) {
		this.existemTextosNaoSeraoLiberados = existemTextosNaoSeraoLiberados;
	}

	public Boolean getRtj() {
		return rtj;
	}

	public void setRtj(Boolean rtf) {
		this.rtj = rtf;
	}

	@Override
	public void validateAndExecute() {
		List<TextoComSituacaoDaPublicacaoVO> listaDeSituacoes = textoService.consultarSituacoesDePublicacaoDosTextos(getResources());
		for (TextoComSituacaoDaPublicacaoVO textoComSituacaoDaPublicacaoVO : listaDeSituacoes) {
			if (isTextoProcessoOculto(textoComSituacaoDaPublicacaoVO.getTexto())){
				
				if(!verificiarPermissaoEspecialParaPublicacao(textoComSituacaoDaPublicacaoVO.getTexto())) {
					adicionaMensagemDeTextoDeProcessoOcultoNaoLiberado(textoComSituacaoDaPublicacaoVO.getTexto());
					textosInvalidos.add(TextoDto.valueOf(textoComSituacaoDaPublicacaoVO.getTexto()));
				}
			} else if (isTextoPermitido(textoComSituacaoDaPublicacaoVO)) {
				listaTextosValidos.add(TextoDto.valueOf(textoComSituacaoDaPublicacaoVO.getTexto()));
			} else {
				adicionaMensagemDeTextoNaoLiberado(textoComSituacaoDaPublicacaoVO);
				textosInvalidos.add(TextoDto.valueOf(textoComSituacaoDaPublicacaoVO.getTexto()));
			}
		}
		verificaTextosIguais(listaTextosValidos);
		verificaTextosProcessosSegredoJustica(listaTextosValidos, textosIguaisAdicionados);
		if(textosInvalidos != null && textosInvalidos.size() > 0) {
			existeTextoNaoProcessado = true;
		}
		
		defineFluxoExecucao();
	}

//	private void referendarDecisoesMonocraticas(Set<TextoDto> textos) {
//		try {
//			List<ReferendarDecisaoResultadoDto> resultados = referendarDecisaoService.referendarDecisoesMonocraticas(textos, getPrincipal());
//
//			for (ReferendarDecisaoResultadoDto resultado : resultados) {
//				ObjetoIncidente<?> oi = objetoIncidenteService.recuperarObjetoIncidentePorId(resultado.getObjetoIncidenteId());
//				TipoMensagem tipoMensagem = resultado.getTipoMensagem();
//
//				String mensagemFormatada = String.format("[%s]: %s", oi.getIdentificacao(), resultado.getMensagem());
//
//				if (TipoMensagem.SUCESSO.equals(tipoMensagem))
//					addInformation(mensagemFormatada);
//
//				if (TipoMensagem.FALHA.equals(tipoMensagem))
//					addError(mensagemFormatada);
//			}
//
//		} catch (ServiceException e) {
//			addError(e.getMessage());
//		}
//	}

	//Quem tem o perfil: "Liberar acordao para publicacao em processo sigiloso" pode liberar
	//textos sigilogos para publicacao.
	private boolean verificiarPermissaoEspecialParaPublicacao(Texto texto) {
		return (textoService.hasPerfilLiberarAcordaoParaPublicacaoProcessoSigiloso());
	}
	
	private void verificaTextosProcessosSegredoJustica(Set<TextoDto> listaTextosValidos,
			Set<TextoDto> textosIguaisAdicionados) {
		Set<TextoDto> todosTextos = new HashSet<TextoDto>();
		todosTextos.addAll(listaTextosValidos);
		todosTextos.addAll(textosIguaisAdicionados);
		for (TextoDto textoDto : todosTextos) {
			Texto texto = textoService.recuperarTextoPorId(textoDto.getId());
			if (isTextoProcessoSegredoJustica(texto)) {
				existeTextoDeProcessoEmSegredoDeJustica = true;
			}
		}
	}
	
	/**
	 * Método que avalia se a ação deve ser executada imediatamente, ou se há alguma informação que deve ser mostrada antes.*/
	protected void defineFluxoExecucao() {
		// Remove os textos inválidos para executar a transição.
		getResources().removeAll( textosInvalidos );
		if (getResources().size() > 0) {
			if (hasInformations()) {
				sendToInformations();
			} else if(existeTextoDeProcessoEmSegredoDeJustica) {
				sendToConfirmacaoPeca();
			} else {
				execute();
			}
		} else {
			setRefresh(true);
			sendToErrors();
		}
	}

	private void adicionaMensagemDeTextoDeProcessoOcultoNaoLiberado(Texto texto) {
		if (TipoConfidencialidade.OCULTO.equals(texto.getObjetoIncidente().getTipoConfidencialidade()))
				FacesMessages.instance().add(Severity.WARN,String.format("[%s]: %s", texto.getIdentificacaoCompleta(),MENSAGEM_TEXTO_PROCESSO_OCULTO));
		
		if (TipoConfidencialidade.SIGILOSO.equals(texto.getObjetoIncidente().getTipoConfidencialidade()))
				FacesMessages.instance().add(Severity.WARN,String.format("[%s]: %s", texto.getIdentificacaoCompleta(),MENSAGEM_TEXTO_PROCESSO_SIGILOSO));
		existemTextosNaoSeraoLiberados = true;
	}

	private boolean isTextoProcessoOculto(Texto texto) {
		return TipoConfidencialidade.OCULTO.equals(texto.getObjetoIncidente().getTipoConfidencialidade()) || TipoConfidencialidade.SIGILOSO.equals(texto.getObjetoIncidente().getTipoConfidencialidade());
	}

	private boolean isTextoProcessoSegredoJustica(Texto texto) {
		return TipoConfidencialidade.SEGREDO_JUSTICA.equals(texto.getObjetoIncidente().getTipoConfidencialidade());
	}

	private void verificaTextosIguais(Set<TextoDto> textosValidos) {
		for (TextoDto textoDto : textosValidos) {
			adicionaInformacoesDeTextosIguais(textoDto, textosValidos);
		}

	}

	private void adicionaMensagemDeTextoNaoLiberado(TextoComSituacaoDaPublicacaoVO textoComSituacaoDaPublicacaoVO) {
		FacesMessages.instance().add(
				Severity.WARN,
				String.format("[%s]: %s", textoComSituacaoDaPublicacaoVO.getTexto().getIdentificacaoCompleta(),
						textoComSituacaoDaPublicacaoVO.getSituacaoDaPublicacaoDoTexto().getDescricao()));
		existemTextosNaoSeraoLiberados = true;

	}

	protected boolean isTextoPermitido(TextoComSituacaoDaPublicacaoVO textoComSituacaoDaPublicacaoVO) {
		return textoComSituacaoDaPublicacaoVO.getSituacaoDaPublicacaoDoTexto().isPermiteLiberacaoParaPublicacao();
	}
	
	public void confirmarLiberacao() {
		if (existeTextoDeProcessoEmSegredoDeJustica) {
			sendToConfirmacaoPeca();
		} else {
			execute();
		}
	}

	private void sendToConfirmacaoPeca() {
		getDefinition().setFacet("confirmacaoPeca");
	}
	
	public void confirmarLiberacaoAcessoPeca() {
		sendToConfirmacaoTexto();
	}
	
	private void sendToConfirmacaoTexto() {
		getDefinition().setFacet("confirmacaoTexto");
	}

	/**
	 * @see br.jus.stf.estf.decisao.texto.web.AbstractAlterarFaseDoTextoActionFacesBean#doExecute(br.jus.stf.estf.decisao.pesquisa.domain.TextoDto)
	 */
	@Override
	protected void doExecute(TextoDto texto) throws Exception {
		try {
			Collection<String> mensagensDeTextosProcessados = textoService.liberarParaPublicacao(texto,
					textosProcessados, getRtj(), getPrincipal(), getObservacao(), getResponsavel());
			for (String mensagem : mensagensDeTextosProcessados) {
				addInformation(mensagem);
			}
			existeTextoValido = true;
			
//			Set<TextoDto> textos = new TreeSet();
//			textos.add(texto);
//			referendarDecisoesMonocraticas(textos);
		} catch (TextoBloqueadoException e) {
			existeTextoNaoProcessado = true;
			logger.warn(String.format(MENSAGEM_ERRO_EXECUCAO_ACAO, texto.toString()), e);
			addError(String.format(MENSAGEM_ERRO_EXECUCAO_ACAO + ": %s ", texto.toString(), getMensagemDeErroPadrao(e)));
		} catch (Exception e) {
			existeTextoNaoProcessado = true;
			logger.warn(String.format(MENSAGEM_ERRO_EXECUCAO_ACAO, texto.toString()), e);
			addError(String.format(MENSAGEM_ERRO_EXECUCAO_ACAO + ": %s ", texto.toString(), getMensagemDeErroPadrao(e)));
		}
	}

	@Override
	public void sendToInformations() {
		getDefinition().setFacet("warnings");
		getDefinition().setHeight(defineTamanhoDaTela());
		getDefinition().setWidth(500);
		cleanMessages();
	}
	private int defineTamanhoDaTela() {
		int tamanho = 300;
		if (textosIguaisAdicionados.size() > 0 && textosInvalidos.size() >0){
			//Duplica a altura da tela caso as duas mensagens sejam exibidas.
			tamanho *= 2;
		}
		return tamanho;
	}

	@Override
	public void sendToErrors() {
		getDefinition().setFacet("final");
		getDefinition().setHeight(defineAlturaDaTelaSucessoErro());
		getDefinition().setWidth(500);
		cleanMessages();
	}
	
	private int defineAlturaDaTelaSucessoErro() {
		int tamanho = 250;
		if (existeTextoNaoProcessado != null && existeTextoNaoProcessado && existeTextoValido != null && existeTextoValido){
			//Duplica a altura da tela caso as duas mensagens sejam exibidas.
			tamanho = 350;
		}
		return tamanho;
	}	

	/**
	 * @see br.jus.stf.estf.decisao.texto.web.AbstractAlterarFaseDoTextoActionFacesBean#getDestino()
	 */
	@Override
	protected TipoTransicaoFaseTexto getDestino() {
		return TipoTransicaoFaseTexto.LIBERAR_PARA_PUBLICACAO;
	}

	/**
	 * Método que retorna para a página principal.
	 */
	public void voltar() {
		getDefinition().setFacet("principal");
	
	}

	public Boolean getExibirTextoSigilosoOculto() {
		return exibirTextoSigilosoOculto;
	}

	public void setExibirTextoSigilosoOculto(Boolean exibirTextoSigilosoOculto) {
		this.exibirTextoSigilosoOculto = exibirTextoSigilosoOculto;
	}

	public Boolean getExibirMensagemReferendo() {
		return exibirMensagemReferendo;
	}

	public void setExibirMensagemReferendo(Boolean exibirMensagemReferendo) {
		this.exibirMensagemReferendo = exibirMensagemReferendo;
	}

}
