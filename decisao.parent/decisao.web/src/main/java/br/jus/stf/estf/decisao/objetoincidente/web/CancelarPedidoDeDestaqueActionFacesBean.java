package br.jus.stf.estf.decisao.objetoincidente.web;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.entidade.julgamento.Colegiado.TipoColegiadoConstante;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoAmbienteConstante;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoSessaoConstante;
import br.gov.stf.estf.entidade.documento.TipoLiberacao;
import br.gov.stf.estf.entidade.julgamento.JulgamentoProcesso;
import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.ProcessoListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.TipoSituacaoProcessoSessao;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.julgamento.model.service.JulgamentoProcessoService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.support.action.handlers.CheckDestaquePeloMinistro;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;

@Action(id = "cancelarPedidoDeDestaqueActionFacesBean", name = "Cancelar Pedido de Destaque", view = "/acoes/objetoincidente/cancelarPedidoDeDestaque.xhtml", width = 650)
@Restrict({ ActionIdentification.CANCELAR_PEDIDO_DE_DESTAQUE })
@RequiresResources(Mode.One)
@CheckDestaquePeloMinistro
public class CancelarPedidoDeDestaqueActionFacesBean extends ActionSupport<ObjetoIncidenteDto> {

	@Autowired
	private JulgamentoProcessoService julgamentoProcessoService;

	private Boolean cancelarTodos = false;

	private ListaJulgamento listaJulgamento;

	private ObjetoIncidente<?> objetoIncidente;

	private List<ObjetoIncidente<?>> processos;

	private TipoColegiadoConstante colegiado;

	@Override
	public void load() {
		processos = new ArrayList<ObjetoIncidente<?>>();
		
		try {
			ObjetoIncidenteDto dto = getResources().iterator().next();

			Processo oi = new Processo();
			oi.setId(dto.getId());

			JulgamentoProcesso jp = julgamentoProcessoService.pesquisaUltimoJulgamentoProcesso(oi, getMinistro(), TipoSituacaoProcessoSessao.DESTAQUE, false);

			if (jp != null) {
				
				if (Boolean.TRUE.equals(jp.getExclusivoDigital())) {
					throw new ServiceException("Este serviço não pode ser usado em processos julgados no STF Digital.");
				}
				
				TipoAmbienteConstante tipoAmbiente = TipoAmbienteConstante.PRESENCIAL;
				TipoSessaoConstante tipoSessao = TipoSessaoConstante.ORDINARIA;
				TipoLiberacao tipoLiberacao = TipoLiberacao.SESSAO_PRESENCIAL_ORDINARIA;
				
				tipoAmbiente = TipoAmbienteConstante.valueOfSigla(jp.getSessao().getTipoAmbiente());
				tipoSessao = TipoSessaoConstante.valueOfSigla(jp.getSessao().getTipoSessao());
				
				if (TipoAmbienteConstante.VIRTUAL.equals(tipoAmbiente))
					if (TipoSessaoConstante.ORDINARIA.equals(tipoSessao))
						tipoLiberacao = TipoLiberacao.SESSAO_VIRTUAL_ORDINARIA;
					else
						tipoLiberacao = TipoLiberacao.SESSAO_VIRTUAL_EXTRAORDINARIA;
				else if (TipoAmbienteConstante.PRESENCIAL.equals(tipoAmbiente))
					if (TipoSessaoConstante.ORDINARIA.equals(tipoSessao))
						tipoLiberacao = TipoLiberacao.SESSAO_PRESENCIAL_ORDINARIA;
					else
						tipoLiberacao = TipoLiberacao.SESSAO_PRESENCIAL_EXTRAORDINARIA;
				
				if (!tipoLiberacao.isVirtual()) {
					objetoIncidente = jp.getObjetoIncidente();
					
					colegiado = TipoColegiadoConstante.valueOfSigla(jp.getSessao().getColegiado().getId());
					
					listaJulgamento = jp.getProcessoListaJulgamento().getListaJulgamento();
					
					List<ProcessoListaJulgamento> lplj = listaJulgamento.getProcessosListaJulgamento();
					
					for (ProcessoListaJulgamento plj : lplj)
						if (plj.getJulgamentoProcesso().isDestaque() && getPrincipal().getMinistro().equals(plj.getJulgamentoProcesso().getMinistroDestaque()))
							processos.add(objetoIncidenteService.deproxy(plj.getObjetoIncidente()));
				} else {
					throw new ServiceException("Não é mais permitido o cancelamento de pedido de destaque para sessões virtuais.");
				}
			}

		} catch (ServiceException e) {
			addError("Erro ao recuperar o último julgamento do processo: " + e.getMessage());
			logger.error("Erro ao recuperar o último julgamento do processo.", e);
			sendToErrors();
		}
	}

	public void execute() {
		try {
			if (cancelarTodos != null) {
				List<ObjetoIncidente<?>> listaDestaques = new ArrayList<ObjetoIncidente<?>>();

				if (cancelarTodos)
					listaDestaques = processos;
				else
					listaDestaques.add(objetoIncidente);

				objetoIncidenteService.cancelarPedidoDeDestaque(listaDestaques, listaJulgamento, getUsuario(), getMinistro());

				sendToConfirmation();
			}
		} catch (ServiceException e) {
			addError(e.getMessage());
			logger.error(e.getMessage(), e);
			sendToErrors();
		} catch (Exception e) {
			addError(e.getMessage());
			logger.error(e);
			sendToErrors();
			e.printStackTrace();
		}
		
		setRefresh(true);
	}

	public ListaJulgamento getListaJulgamento() {
		return listaJulgamento;
	}

	public void setListaJulgamento(ListaJulgamento listaJulgamento) {
		this.listaJulgamento = listaJulgamento;
	}

	public List<ObjetoIncidente<?>> getProcessos() {
		return processos;
	}

	public void setProcessos(List<ObjetoIncidente<?>> processos) {
		this.processos = processos;
	}

	public Boolean getCancelarTodos() {
		return cancelarTodos;
	}

	public void setCancelarTodos(Boolean cancelarTodos) {
		this.cancelarTodos = cancelarTodos;
	}

	public TipoColegiadoConstante getColegiado() {
		return colegiado;
	}

	public void setColegiado(TipoColegiadoConstante colegiado) {
		this.colegiado = colegiado;
	}

	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

}
