/**
 * 
 */
package br.jus.stf.estf.decisao.comunicacao.web;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.documento.model.service.DocumentoComunicacaoService;
import br.gov.stf.estf.entidade.documento.DocumentoComunicacao;
import br.jus.stf.estf.decisao.pesquisa.domain.ComunicacaoDto;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionCallback;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;

/**
 * @author Paulo.Estevao
 * @since 13.05.2011
 */
@Action(id = "devolverComunicacoesActionFacesBean", 
		name = "Devolver Comunicações", view = "/acoes/comunicacao/devolver.xhtml")
@Restrict({ActionIdentification.DEVOLVER_COMUNICACOES})
@RequiresResources(Mode.Many)
public class DevolverComunicacoesActionFacesBean extends
		ActionSupport<ComunicacaoDto> {
	
	@Autowired
	private DocumentoComunicacaoService documentoComunicacaoService;
	
	private String observacao;
	
	public void execute() {
		execute(new ActionCallback<ComunicacaoDto>() {
			public void doInAction(ComunicacaoDto comunicacao) throws Exception {
				doExecute(comunicacao);
			}
		});
		setRefresh(true);
	}
	
	private void doExecute(ComunicacaoDto comunicacao) throws Exception {
		try {
			DocumentoComunicacao documentoComunicacao = documentoComunicacaoService.recuperarPorId(comunicacao.getIdDocumentoComunicacao());
			documentoComunicacaoService.devolverDocumentoeSTFDecisao(documentoComunicacao, getObservacao(), getUsuario().getId());
		} catch (Exception e) {
			addError(e.getMessage());
			logger.error(e.getMessage(), e);
		}
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
}
