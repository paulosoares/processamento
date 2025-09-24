package br.jus.stf.estf.decisao.texto.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.action.handlers.CheckIdTipoTexto;
import br.jus.stf.estf.decisao.support.action.handlers.CheckMinisterId;
import br.jus.stf.estf.decisao.support.action.handlers.CheckRestrictions;
import br.jus.stf.estf.decisao.support.action.handlers.CheckVisualizacaoAntecipadaSelecionados;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.handlers.States;
import br.jus.stf.estf.decisao.support.action.handlers.CheckVisualizacaoAntecipadaSelecionados.BooleanMode;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionInterface;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;
import br.jus.stf.estf.decisao.texto.service.TextoService;

@Action(id="liberarVisualizacaoAtencipadaFacesBean", 
		name="Liberar Visualização Antecipada", 
		view="/acoes/texto/liberarVisualizacaoAntecipada.xhtml")
@Restrict({ActionIdentification.DISPONIBILIZAR_VOTO})
@RequiresResources(Mode.Many)
@CheckMinisterId
@CheckRestrictions
@States({FaseTexto.REVISADO,FaseTexto.LIBERADO_ASSINATURA, FaseTexto.ASSINADO, FaseTexto.LIBERADO_PUBLICACAO, FaseTexto.PUBLICADO, FaseTexto.JUNTADO})
@CheckIdTipoTexto({TipoTexto.CODIGO_VOTO_VISTA, TipoTexto.CODIGO_VOTO_VOGAL,TipoTexto.CODIGO_EMENTA, TipoTexto.CODIGO_RELATORIO, TipoTexto.CODIGO_VOTO })
@CheckVisualizacaoAntecipadaSelecionados(BooleanMode.NAO)
public class LiberarVisualizacaoAtencipadaFacesBean extends ActionSupport<TextoDto> implements ActionInterface<TextoDto>  {

	@Qualifier("textoServiceLocal") 
	@Autowired 
	private TextoService textoService;
	
	public void execute() {
		try {
			textoService.liberarVisualizacaoAntecipada(getResources(), true);
		} catch (ServiceException e) {
			logger.error("Erro ao liberar visualização antecipada!", e);
			addError(String.format("Erro ao liberar visualização antecipada: %s ", getMensagemDeErroPadrao(e)));
		}
		setRefresh(true);
		sendToConfirmation();
		if (!hasMessages()) {
			sendToConfirmation();
		} else {
			sendToErrors();
		}
    }
	
    public void voltar() {
    	getDefinition().setFacet("principal");
    }
    
}
