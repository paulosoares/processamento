package br.jus.stf.estf.decisao.texto.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.gov.stf.estf.entidade.documento.tipofase.TipoTransicaoFaseTexto;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.objetoincidente.service.ObjetoIncidenteService;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
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
/**
 * Classe que permite o cancelamento de assinaturas
 * @author Demetrius.Jube
 *
 */
@Action(id = "cancelarAssinaturaActionFacesBean", name = "Cancelar Assinatura", view = "/acoes/texto/transicao/executar.xhtml", height = 215, width = 500)
@Restrict({ActionIdentification.CANCELAR_ASSINATURA})
@States({ FaseTexto.ASSINADO})
@RequiresResources(Mode.Many)
@CheckMinisterId
@CheckRestrictions
public class CancelarAssinaturaActionFacesBean extends
		AbstractAlterarFaseDoTextoActionFacesBean<TextoDto> {
	
	@Qualifier("objetoIncidenteServiceLocal")
	@Autowired
	private ObjetoIncidenteService objetoIncidenteService;

	@Override
	protected TipoTransicaoFaseTexto getDestino() {
		return TipoTransicaoFaseTexto.CANCELAR_ASSINATURA;
	}
	
	@Override
	protected String getErrorTitle() {
		return "Não foi possível cancelar a assinatura dos textos abaixo:";
	}
	
	@Override
	public void validateAndExecute() {
		List<TextoDto> listaEmentasComAcordaoSelecionado = new ArrayList<TextoDto>();
		for (TextoDto texto : getResources()) {
			adicionaInformacoesDeTextosIguais(texto, getResources());
			adicionaInformacoesDeEmentaAcordao(texto, getResources(), listaEmentasComAcordaoSelecionado);
		}
		
		// Remove e inclui as ementas para que elas sejam posicionadas ao final 
		// do conjunto, de forma que os acórdãos sejam processados sempre antes
		// das ementas, nos casos em que ementa e acórdão do mesmo processo
		// estejam tendo suas assinaturas canceladas.
		getResources().removeAll(listaEmentasComAcordaoSelecionado);
		getResources().addAll(listaEmentasComAcordaoSelecionado);
		
		defineFluxoExecucao();
	}
	
	private void adicionaInformacoesDeEmentaAcordao(TextoDto texto,
			Set<TextoDto> resources, List<TextoDto> listaEmentasComAcordaoSelecionado) {
		if (texto.getTipoTexto().equals(TipoTexto.EMENTA)) {
			try {
				TextoDto acordao = textoService.recuperarAcordao(ObjetoIncidenteDto.valueOf(objetoIncidenteService.recuperarObjetoIncidentePorId(texto.getIdObjetoIncidente())));
				if (acordao != null && FaseTexto.fasesComTextoAssinado.contains(acordao.getFase())) {
					if (!getResources().contains(acordao) ) {
						textosInvalidos.add(texto);
						addError("O texto " + texto.toString() + " não pode ter a assinatura cancelada pois o Acórdão está na fase " + acordao.getFase() + ".");
					} else {
						listaEmentasComAcordaoSelecionado.add(texto);
					}
				}
				
			} catch (ServiceException e) {
				addError(e.getMessage());
			}
		}
		
	}

	@Override
	protected void doExecute(TextoDto texto) throws Exception {
		try {
			textoService.cancelarAssinatura(texto, getDestino(), textosProcessados, getObservacao(), getResponsavel());
		} catch (TextoBloqueadoException e) {
			logger.warn(String.format(MENSAGEM_ERRO_EXECUCAO_ACAO, texto.toString()), e);
			addError(String.format(MENSAGEM_ERRO_EXECUCAO_ACAO + ": %s ", texto.toString(), getMensagemDeErroPadrao(e)));
		} catch (Exception e) {
			throw e;
		}
	}


}
