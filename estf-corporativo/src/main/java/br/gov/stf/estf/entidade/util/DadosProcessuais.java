package br.gov.stf.estf.entidade.util;

import br.gov.stf.estf.entidade.processostf.Classe;
import br.gov.stf.estf.entidade.processostf.TipoIncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.TipoRecurso;

public class DadosProcessuais {

	private Classe classeProcessual;

	private Long numeroProcesso;

	private TipoRecurso tipoRecurso;

	private TipoIncidenteJulgamento tipoJulgamento;

	public Classe getClasseProcessual() {
		return classeProcessual;
	}

	public void setClasseProcessual(Classe classeProcessual) {
		this.classeProcessual = classeProcessual;
	}

	public Long getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(Long numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public TipoRecurso getTipoRecurso() {
		return tipoRecurso;
	}

	public void setTipoRecurso(TipoRecurso tipoRecurso) {
		this.tipoRecurso = tipoRecurso;
	}

	public TipoIncidenteJulgamento getTipoJulgamento() {
		return tipoJulgamento;
	}

	public void setTipoJulgamento(TipoIncidenteJulgamento tipoJulgamento) {
		this.tipoJulgamento = tipoJulgamento;
	}
}
