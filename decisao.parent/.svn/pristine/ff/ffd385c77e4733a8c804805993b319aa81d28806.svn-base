package br.jus.stf.estf.decisao.texto.web;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.gov.stf.estf.entidade.documento.tipofase.TipoTransicaoFaseTexto;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.action.handlers.CheckMinisterId;
import br.jus.stf.estf.decisao.support.action.handlers.CheckNotForIdTipoTexto;
import br.jus.stf.estf.decisao.support.action.handlers.CheckRestrictions;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.handlers.States;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.gov.stf.estf.julgamento.model.service.JulgamentoProcessoService;
import br.gov.stf.estf.entidade.julgamento.JulgamentoProcesso;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoAmbienteConstante;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.objetoincidente.service.ObjetoIncidenteService;

/**
 * @author Rodrigo Barreiros
 * @see 27.05.2010
 */
@Action(id = "liberarParaAssinaturaActionFacesBean", name = "Liberar para Assinatura", view = "/acoes/texto/transicao/executar.xhtml", height = 215, width = 500)
@Restrict({ActionIdentification.LIBERAR_PARA_ASSINATURA})
@States({ FaseTexto.EM_ELABORACAO, FaseTexto.EM_REVISAO, FaseTexto.REVISADO })
@RequiresResources(Mode.Many)
@CheckMinisterId
@CheckRestrictions
@CheckNotForIdTipoTexto({TipoTexto.CODIGO_MINUTA, TipoTexto.CODIGO_MEMORIA_DE_CASO})
public class LiberarParaAssinaturaActionFacesBean extends AbstractAlterarFaseDoTextoActionFacesBean<TextoDto> {

	/**
	 * @see br.jus.stf.estf.decisao.texto.web.AbstractAlterarFaseDoTextoActionFacesBean#getDestino()
	 */
	@Autowired
	private JulgamentoProcessoService julgamentoProcessoService;
	
	@Autowired
	private ObjetoIncidenteService objetoIncidenteService;
	
	protected Set<String> processosSessaoVirtualAberta = new HashSet<String>();
	
	// ESSE TRECHO FOI COMENTADO - VER JUSTIFICATIVA EM  https://jira.stf.jus.br/jira/browse/DECISAO-2486
	/*@Override
	public void validateAndExecute() {

		boolean temTextoDeSessaoEmAndamento = false;
		try {
			for (TextoDto texto : getResources()) {
				if (texto.getTipoTexto().equals(TipoTexto.VOTO_VOGAL)) {
					ObjetoIncidente<?> oi = processoPossuiSessaoVirtualAberta(texto.getIdObjetoIncidente());
					if (oi != null) {
						//oi.get
						//processosSessaoVirtualAberta.add(texto.getProcesso());
						//adicionaInformacoesDeProcessosComSessaoEmAndamento(texto.getProcesso());
						processosSessaoVirtualAberta.add(oi.getIdentificacao());
						adicionaInformacoesDeProcessosComSessaoEmAndamento(oi.getIdentificacao());
						temTextoDeSessaoEmAndamento = true;
					}
				}
			}
		
			if (temTextoDeSessaoEmAndamento) {
				getDefinition().setFacet("votoVogal");
			} else {
				super.validateAndExecute();
			}
		} catch (ServiceException se) {
			
		}
		
	}

	@Override
	public void execute() {
		cleanMessages();
		super.execute();
	}
	*/
	
	@Override
	protected TipoTransicaoFaseTexto getDestino() {
		return TipoTransicaoFaseTexto.LIBERAR_PARA_ASSINATURA;
	}

	@Override
	protected String getErrorTitle() {
		return "Não foi possível liberar os textos abaixo para assinatura:";
	}
	
	protected ObjetoIncidente<?> processoPossuiSessaoVirtualAberta(Long idObjetoIncidente) throws ServiceException  {
		JulgamentoProcesso jp;
		
		List<ObjetoIncidente<?>> cadeia = objetoIncidenteService.recuperarObjetoIncidentePorId(idObjetoIncidente).getCadeia();
		 
		for (ObjetoIncidente<?> oi : cadeia) {
			jp = julgamentoProcessoService.pesquisaSessaoNaoFinalizada(oi, TipoAmbienteConstante.VIRTUAL);
			if (jp != null) {
				return jp.getObjetoIncidente();
			}
		}
		return null;
		
	}
	
	protected void adicionaInformacoesDeProcessosComSessaoEmAndamento(String processo) {
			addInformation(processo);
	}
	
	public Set<String> getProcessosSessaoVirtualAberta() {
		return processosSessaoVirtualAberta;
	}

	public void setProcessosSessaoVirtualAberta(Set<String> processosSessaoVirtualAberta) {
		this.processosSessaoVirtualAberta = processosSessaoVirtualAberta;
	}

}
