/**
 * 
 */
package br.jus.stf.estf.decisao.texto.web;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.gov.stf.estf.entidade.documento.tipofase.TipoTransicaoFaseTexto;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.api.ReferendarDecisaoResultadoDto;
import br.jus.stf.estf.decisao.api.ReferendarDecisaoResultadoDto.TipoMensagem;
import br.jus.stf.estf.decisao.objetoincidente.service.ReferendarDecisaoService;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.action.handlers.CheckMinisterId;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.handlers.States;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.texto.service.TextoService;

/**
 * @author Paulo.Estevao
 * @since 04.10.2010
 */
@Action(id="desfazerJuntadaActionFacesBean", name="Desfazer Juntada", view = "/acoes/texto/transicao/executar.xhtml", height = 215, width = 500)
@Restrict({ActionIdentification.DESFAZER_JUNTADA})
@States({ FaseTexto.JUNTADO })
@RequiresResources(Mode.Many)
@CheckMinisterId
public class DesfazerJuntadaActionFacesBean extends AbstractAlterarFaseDoTextoActionFacesBean<TextoDto> {

	@Autowired
	private TextoService textoService;
	
	@Autowired
	private ReferendarDecisaoService referendarDecisaoService;
	
	@Override
	public void validateAndExecute() {
		cleanMessages();
		TextoDto textoDto = new TextoDto();

		try {
			Set<TextoDto> textos = getResources();
			for (TextoDto texto : textos) {
				textoDto = texto;
				textoService.desfazerJuntada(texto, textosProcessados, getObservacao(), getResponsavel());
//				desfazerReferendarDecisaoMonocratica(texto, getPrincipal().getUsuario());
			}

			if (hasErrors()) {
				sendToErrors();
			} else if (hasInformations()) {
				sendToInformations();
			} else { 
				sendToConfirmation();
			}
			
		} catch (Exception e) {
			logger.warn(String.format(MENSAGEM_ERRO_EXECUCAO_ACAO, textoDto.toString()), e);
			addError(String.format(MENSAGEM_ERRO_EXECUCAO_ACAO + ": %s ", textoDto.toString(), getMensagemDeErroPadrao(e)));
			sendToErrors();
		}
		
		setRefresh(true);
	}

//	private void desfazerReferendarDecisaoMonocratica(TextoDto texto, Usuario usuario) {
//		try {
//			List<ReferendarDecisaoResultadoDto> resultados = referendarDecisaoService.desfazerReferendarDecisaoMonocratica(texto, getPrincipal());
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

	@Override
	protected String getErrorTitle() {
		return "Não foi possível juntar os textos abaixo:";
	}

	@Override
	protected TipoTransicaoFaseTexto getDestino() {
		return TipoTransicaoFaseTexto.DESFAZER_JUNTADA;
	}

		
}
