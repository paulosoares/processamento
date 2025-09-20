/**
 * 
 */
package br.gov.stf.estf.processostf.model.util;

import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.processostf.Agendamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;

/**
 * @author Paulo.Estevao
 * @since 30.05.2011
 */
public class AgendamentoObjetoIncidenteResult {

	private Agendamento agendamento;
	private ObjetoIncidente<?> objetoIncidente;
	private Processo processo;
	private Integer codigoCapitulo;
	private Sessao sessao;
	
	public AgendamentoObjetoIncidenteResult(Agendamento agendamento, ObjetoIncidente<?> objetoIncidente, Processo processo, Integer codigoCapitulo, Sessao sessao) {
		this.agendamento = agendamento;
		this.objetoIncidente = objetoIncidente;
		this.processo = processo;
		this.codigoCapitulo = codigoCapitulo;
		this.sessao = sessao;
	}	
	
	public Agendamento getAgendamento() {
		return agendamento;
	}

	public void setAgendamento(Agendamento agendamento) {
		this.agendamento = agendamento;
	}

	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo) {
		this.processo = processo;
	}

	public Integer getCodigoCapitulo() {
		return codigoCapitulo;
	}

	public void setCodigoCapitulo(Integer codigoCapitulo) {
		this.codigoCapitulo = codigoCapitulo;
	}

	public Sessao getSessao() {
		return sessao;
	}

	public void setSessao(Sessao sessao) {
		this.sessao = sessao;
	}
	
	
}
