package br.jus.stf.estf.decisao.texto.web;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.entidade.documento.TipoTexto;
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
import br.jus.stf.estf.decisao.texto.support.SituacaoDoTextoParaPublicacao;
import br.jus.stf.estf.decisao.texto.support.TextoComSituacaoDaPublicacaoVO;

@Action(id = "suspenderPublicacaoActionFacesBean", name = "Suspender Publicação", view = "/acoes/texto/publicacao/suspender.xhtml", height=250, width=500)
@Restrict({ActionIdentification.SUSPENDER_PUBLICACAO})
@States({ FaseTexto.LIBERADO_PUBLICACAO })
@RequiresResources(Mode.Many)
@CheckMinisterId
//@CheckNotForIdTipoTexto({TipoTexto.CODIGO_DESPACHO, TipoTexto.CODIGO_DECISAO_MONOCRATICA})
public class SuspenderPublicacaoActionFacesBean extends AbstractAlterarFaseDoTextoActionFacesBean<TextoDto> {

	@Autowired
	private ReferendarDecisaoService referendarDecisaoService;
	
	private Set<TextoDto> listaTextosValidos = new HashSet<TextoDto>();
	private Boolean existemTextosNaoSeraoSuspensos;
	private Boolean existeTextoValido;
	private Boolean existeTextoNaoProcessado;
	private Boolean existePublicacaoEmAta;

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

	public Boolean getExistemTextosNaoSeraoSuspensos() {
		return existemTextosNaoSeraoSuspensos;
	}

	public void setExistemTextosNaoSeraoSuspensos(Boolean existemTextosNaoSeraoSuspensos) {
		this.existemTextosNaoSeraoSuspensos = existemTextosNaoSeraoSuspensos;
	}
	
	public Boolean getExistePublicacaoEmAta() {
		return existePublicacaoEmAta;
	}

	public void setExistePublicacaoEmAta(Boolean existePublicacaoEmAta) {
		this.existePublicacaoEmAta = existePublicacaoEmAta;
	}

	@Override
	protected TipoTransicaoFaseTexto getDestino() {
		return TipoTransicaoFaseTexto.SUSPENDER_PUBLICACAO;
	}

	@Override
	public void validateAndExecute() {
		List<TextoComSituacaoDaPublicacaoVO> listaDeSituacoes = textoService.consultarSituacoesDePublicacaoDosTextos(getResources());
		for (TextoComSituacaoDaPublicacaoVO textoComSituacaoDaPublicacaoVO : listaDeSituacoes) {
			if ( isTextoPermitido( textoComSituacaoDaPublicacaoVO ) ) {
				listaTextosValidos.add(TextoDto.valueOf(textoComSituacaoDaPublicacaoVO.getTexto()));
			} else {
				if ( ( textoComSituacaoDaPublicacaoVO.getSituacaoDaPublicacaoDoTexto().equals( SituacaoDoTextoParaPublicacao.ATA_DE_PUBLICACAO ) ||
					   textoComSituacaoDaPublicacaoVO.getSituacaoDaPublicacaoDoTexto().equals( SituacaoDoTextoParaPublicacao.PUBLICADO_NO_DJ ) ) &&
					   ( textoComSituacaoDaPublicacaoVO.getTexto().getTipoTexto().equals( TipoTexto.DESPACHO ) || 
					     textoComSituacaoDaPublicacaoVO.getTexto().getTipoTexto().equals( TipoTexto.DECISAO_MONOCRATICA ) ||
					     textoComSituacaoDaPublicacaoVO.getTexto().getTipoTexto().equals( TipoTexto.EMENTA ) || 
					     textoComSituacaoDaPublicacaoVO.getTexto().getTipoTexto().equals( TipoTexto.ACORDAO ) ||
					     textoComSituacaoDaPublicacaoVO.getTexto().getTipoTexto().equals( TipoTexto.RELATORIO ) ||
					     textoComSituacaoDaPublicacaoVO.getTexto().getTipoTexto().equals( TipoTexto.VOTO ) ) ) {
					existePublicacaoEmAta = true;
				}
				textosInvalidos.add( TextoDto.valueOf( textoComSituacaoDaPublicacaoVO.getTexto() ) );
				adicionaMensagemDeTextoNaoLiberado( textoComSituacaoDaPublicacaoVO );
			}
		}
		verificaTextosIguais(listaTextosValidos);
		
		if(textosInvalidos != null && textosInvalidos.size() > 0) {
			existePublicacaoEmAta = true;
		}
		
		
		defineFluxoExecucao();
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
		existemTextosNaoSeraoSuspensos = true;

	}

	protected boolean isTextoPermitido(TextoComSituacaoDaPublicacaoVO textoComSituacaoDaPublicacaoVO) {
		return textoComSituacaoDaPublicacaoVO.getSituacaoDaPublicacaoDoTexto().isPermiteSuspensaoPublicacao();
	}

	@Override
	protected void doExecute(TextoDto texto) throws Exception {
		try {
			Collection<String> mensagensDeTextosProcessados = textoService
					.suspenderPublicacao(texto, textosProcessados, getObservacao(), getPrincipal(), getResponsavel());
			for (String mensagem : mensagensDeTextosProcessados) {
				addInformation(mensagem);
			}
			existeTextoValido = true;
			
//			desfazerReferendarDecisaoMonocratica(texto, getPrincipal().getUsuario());
		} catch (Exception e) {
			existeTextoNaoProcessado = true;
			throw e;
		}
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
	public void sendToErrors() {
		getDefinition().setFacet("final");
		getDefinition().setHeight(calcularAlturaTela());
		getDefinition().setWidth(500);
		cleanMessages();
	}
	
	@Override
	public void sendToInformations() {
		getDefinition().setFacet("warnings");
		getDefinition().setHeight(300);
		getDefinition().setWidth(500);
		cleanMessages();
	}
	
	private int calcularAlturaTela() {
		int tamanho = 250;
		if(existeTextoValido != null && existeTextoValido && existeTextoNaoProcessado != null && existeTextoNaoProcessado) {
			tamanho = 350;
		}
		
		return tamanho;
	}
	
}
