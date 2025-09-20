package br.gov.stf.estf.processostf.model.util;

import java.util.ArrayList;
import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;

public class ContainerGuiaProcessos {

	private Guia guia;

	private List<ProcessoEAndamentoProcesso> processosEAndamentosProcessos = new ArrayList<ContainerGuiaProcessos.ProcessoEAndamentoProcesso>();

	public ContainerGuiaProcessos(Long codigoOrigem,
			Integer tipoOrgaoOrigem, Origem destino, Integer tipoOrgaoDestino) {
		this.guia = new Guia();
		Guia.GuiaId guiaId = new Guia.GuiaId();

		guiaId.setCodigoOrgaoOrigem(codigoOrigem);

		guia.setId(guiaId);
		guia.setTipoOrgaoOrigem(tipoOrgaoOrigem);
		guia.setCodigoOrgaoDestino(destino.getId());
		guia.setTipoOrgaoDestino(tipoOrgaoDestino);
	}

	public class ProcessoEAndamentoProcesso {
		private Processo processo;
		private AndamentoProcessoInfo andamentoProcessoInfo;
		private AndamentoProcesso andamentoProcesso;
		private ObjetoIncidente<?> incidenteSelecionado;

		public ProcessoEAndamentoProcesso(Processo processo,
				AndamentoProcessoInfo andamentoProcessoInfo,
				AndamentoProcesso andamentoProcesso,
				ObjetoIncidente<?> incidenteSelecionado) {
			this.processo = processo;
			this.andamentoProcessoInfo = andamentoProcessoInfo;
			this.andamentoProcesso = andamentoProcesso;
			this.incidenteSelecionado = incidenteSelecionado;
		}

		public Processo getProcesso() {
			return processo;
		}

		public void setProcesso(Processo processo) {
			this.processo = processo;
		}

		public AndamentoProcessoInfo getAndamentoProcessoInfo() {
			return andamentoProcessoInfo;
		}

		public void setAndamentoProcessoInfo(
				AndamentoProcessoInfo andamentoProcessoInfo) {
			this.andamentoProcessoInfo = andamentoProcessoInfo;
		}

		public ObjetoIncidente<?> getIncidenteSelecionado() {
			return incidenteSelecionado;
		}

		public void setIncidenteSelecionado(
				ObjetoIncidente<?> incidenteSelecionado) {
			this.incidenteSelecionado = incidenteSelecionado;
		}

		public AndamentoProcesso getAndamentoProcesso() {
			return andamentoProcesso;
		}

		public void setAndamentoProcesso(AndamentoProcesso andamentoProcesso) {
			this.andamentoProcesso = andamentoProcesso;
		}

	}

	public void addProcessoEAndamentoProcesso(Processo processo,
			AndamentoProcessoInfo andamentoProcessoInfo,
			AndamentoProcesso andamentoProcesso,
			ObjetoIncidente<?> incidenteSelecionado) {
		this.processosEAndamentosProcessos.add(new ProcessoEAndamentoProcesso(
				processo, andamentoProcessoInfo, andamentoProcesso,
				incidenteSelecionado));
	}

	public Guia getGuia() {
		return guia;
	}

	public void setGuia(Guia guia) {
		this.guia = guia;
	}

	public List<ProcessoEAndamentoProcesso> getProcessosEAndamentosProcessos() {
		return processosEAndamentosProcessos;
	}

	public void setProcessosEAndamentosProcessos(
			List<ProcessoEAndamentoProcesso> processosEAndamentosProcessos) {
		this.processosEAndamentosProcessos = processosEAndamentosProcessos;
	}

}
