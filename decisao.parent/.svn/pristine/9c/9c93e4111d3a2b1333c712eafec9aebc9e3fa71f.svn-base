package br.jus.stf.estf.decisao.texto.web;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.documento.model.service.exception.NaoExisteDocumentoAssinadoException;
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
import br.jus.stf.estf.decisao.support.action.handlers.CheckIdTipoTexto;
import br.jus.stf.estf.decisao.support.action.handlers.CheckMinisterId;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.handlers.States;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.texto.service.TextoService;
import br.jus.stf.estf.decisao.texto.support.TextoBloqueadoException;
import edu.emory.mathcs.backport.java.util.TreeSet;

/**
 * Junta as peças de cada texto selecionado.
 * 
 * @author Rodrigo Barreiros
 * @since 22.07.2010
 */
@Action(id="juntarPecasActionFacesBean", name="Juntar Peças", view="/acoes/texto/juntarPecas.xhtml", height=150, width=500)
@States({FaseTexto.ASSINADO})
@Restrict({ActionIdentification.JUNTAR_PECAS})
@RequiresResources(Mode.Many)
@CheckMinisterId
@CheckIdTipoTexto({TipoTexto.CODIGO_DESPACHO, TipoTexto.CODIGO_DECISAO_MONOCRATICA})
public class JuntarPecasActionFacesBean extends AbstractAlterarFaseDoTextoActionFacesBean<TextoDto> {
	
	@Autowired
	private TextoService textoService;
	
//	@Autowired
//	private ReferendarDecisaoService referendarDecisaoService;
//	
	private Boolean exibirMensagemReferendo = false;

	private final String MENSAGEM_VISUALIZACAO_PUBLICA_IMEDIATA = "Esta opção somente deve ser utilizada quando, "
			+ "para viabilizar o efetivo cumprimento da decisão, seu conteúdo tenha de permanecer com visualização "
			+ "apenas no ambiente interno da Corte. Cumprido o comando, a SEJ disponibilizará a consulta externa da peça.";
	
	private boolean disponibilizarNaInternet = true;
	private Boolean existeTextoDeProcessoEmSegredoDeJustica = false;	
	private Boolean existeTextoNaoProcessado;
	private Boolean existeTextoValido;
	
//	@Override
//	public void load() {
//		for (TextoDto textoDto : getResources())
//			if (TipoTexto.DECISAO_MONOCRATICA.equals(textoDto.getTipoTexto()))
//				exibirMensagemReferendo = true;
//	}
	
	public void validar() {
		if (!disponibilizarNaInternet) {
			addInformation(MENSAGEM_VISUALIZACAO_PUBLICA_IMEDIATA);
			getDefinition().setFacet("confirmacao");
			cleanMessages();
		} else {
			validateAndExecute();
		}
	}
	
	public void validateAndExecute() {
		for (TextoDto texto : getResources()) {
			adicionaInformacoesDeTextosIguais(texto, getResources());
		}
		
		verificaTextosProcessosSegredoJustica(getResources(), textosIguaisAdicionados);
		defineFluxoExecucao();
	}
	
//	private void referendarDecisoesMonocraticas(Set<TextoDto> resources) {
//		try {
//			List<ReferendarDecisaoResultadoDto> resultados = referendarDecisaoService.referendarDecisoesMonocraticas(resources, getPrincipal());
//
//			if (resultados != null && !resultados.isEmpty()) {
//				for (ReferendarDecisaoResultadoDto resultado : resultados) {
//					ObjetoIncidente<?> oi = objetoIncidenteService.recuperarObjetoIncidentePorId(resultado.getObjetoIncidenteId());
//					TipoMensagem tipoMensagem = resultado.getTipoMensagem();
//
//					String mensagemFormatada = String.format("[%s]: %s", oi.getIdentificacao(), resultado.getMensagem());
//
//					if (TipoMensagem.SUCESSO.equals(tipoMensagem))
//						addInformation(mensagemFormatada);
//
//					if (TipoMensagem.FALHA.equals(tipoMensagem))
//						addError(mensagemFormatada);
//				}
//			}
//
//		} catch (ServiceException e) {
//			addError(e.getMessage());
//		}
//	}

	
	@Override
	protected void defineFluxoExecucao() {
		// Remove os textos inválidos para executar a transição.
		getResources().removeAll(textosInvalidos);
		if (getResources().size() > 0) {
			if (hasInformations()) {
				sendToInformations();
			} else if(existeTextoDeProcessoEmSegredoDeJustica) {
				sendToConfirmacaoPeca();
			} else {
				execute();
			}
		} else {
			sendToErrors();
		}
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
	
	private boolean isTextoProcessoSegredoJustica(Texto texto) {
		return TipoConfidencialidade.SEGREDO_JUSTICA.equals(texto.getObjetoIncidente().getTipoConfidencialidade());
	}
	
	public void confirmarJuntada() {
		if (existeTextoDeProcessoEmSegredoDeJustica) {
			sendToConfirmacaoPeca();
		} else {
			execute();
		}
	}

	private void sendToConfirmacaoPeca() {
		getDefinition().setFacet("confirmacaoPeca");
	}
	
	public void confirmarJuntadaAcessoPeca() {
		sendToConfirmacaoTexto();
	}
	
	private void sendToConfirmacaoTexto() {
		getDefinition().setFacet("confirmacaoTexto");
	}
	
	/**
	 * Junta as peças de cada texto selecionado.
	 */
	protected void doExecute(TextoDto texto) throws Exception {
		try {
			existeTextoNaoProcessado = true;
			Collection<String> mensagensDeTextosProcessados = textoService.juntarPecas(texto, textosProcessados, disponibilizarNaInternet, getPrincipal(), getObservacao(), getResponsavel());
			for (String mensagem : mensagensDeTextosProcessados) {
				addInformation(mensagem);
			}
			existeTextoValido = true;
			existeTextoNaoProcessado = false;
			
			Set<TextoDto> textos = new TreeSet();
			textos.add(texto);
			
//			referendarDecisoesMonocraticas(textos);
		} catch (NaoExisteDocumentoAssinadoException e) {
			logger.warn(String.format(MENSAGEM_ERRO_EXECUCAO_ACAO, texto.toString()), e);
			addError(String.format(MENSAGEM_ERRO_EXECUCAO_ACAO + ": %s ", texto.toString(), getMensagemDeErroPadrao(e)));
		} catch (TextoBloqueadoException e) {
			logger.warn(String.format(MENSAGEM_ERRO_EXECUCAO_ACAO, texto.toString()), e);
			addError(String.format(MENSAGEM_ERRO_EXECUCAO_ACAO + ": %s ", texto.toString(), getMensagemDeErroPadrao(e)));
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void voltar() {
		getDefinition().setFacet("principal");
	}
	
	@Override
	protected String getErrorTitle() {
		return "Não foi possível juntar os textos abaixo:";
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

	@Override
	protected TipoTransicaoFaseTexto getDestino() {
		return TipoTransicaoFaseTexto.JUNTAR;
	}

	public boolean isDisponibilizarNaInternet() {
		return disponibilizarNaInternet;
	}

	public void setDisponibilizarNaInternet(boolean disponibilizarNaInternet) {
		this.disponibilizarNaInternet = disponibilizarNaInternet;
	}
	
	public Boolean getExisteTextoNaoProcessado() {
		return existeTextoNaoProcessado;
	}
	
	public void setExisteTextoNaoProcessado(Boolean existeTextoNaoProcessado) {
		this.existeTextoNaoProcessado = existeTextoNaoProcessado;
	}
	
	public Boolean getExisteTextoValido() {
		return existeTextoValido;
	}
	
	public void setExisteTextoValido(Boolean existeTextoValido) {
		this.existeTextoValido = existeTextoValido;
	}
	
	public Boolean getExibirMensagemReferendo() {
		return exibirMensagemReferendo;
	}

	public void setExibirMensagemReferendo(Boolean exibirMensagemReferendo) {
		this.exibirMensagemReferendo = exibirMensagemReferendo;
	}
}
