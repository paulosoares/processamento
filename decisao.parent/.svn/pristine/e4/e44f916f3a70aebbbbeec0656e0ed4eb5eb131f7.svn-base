package br.jus.stf.estf.decisao.objetoincidente.support;

import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.TipoRecurso;
import br.gov.stf.estf.entidade.util.ObjetoIncidenteUtil;
import br.gov.stf.estf.publicacao.model.util.IConsultaDePautaDeJulgamento;

public class ProcessoPautaDeJulgamentoAdapter implements IConsultaDePautaDeJulgamento {
	private ObjetoIncidente<?> objetoIncidente;
	private Integer codigoDaMateria;
	private Long tipoJulgamento;

	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	private void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	public ProcessoPautaDeJulgamentoAdapter(ObjetoIncidente<?> objetoIncidente) {
		setObjetoIncidente(objetoIncidente);
	}

	public Long getCodigoRecurso() {
		TipoRecurso tipoRecurso = ObjetoIncidenteUtil.getTipoRecurso(objetoIncidente);
		return tipoRecurso.getId();
	}

	public Long getNumeroProcesso() {
		Processo processo = ObjetoIncidenteUtil.getProcesso(objetoIncidente);
		return processo.getNumeroProcessual();
	}

	public String getSiglaClasseProcessual() {
		Processo processo = ObjetoIncidenteUtil.getProcesso(objetoIncidente);
		return processo.getClasseProcessual().getId();
	}

	public void setCodigoDaMateria(Integer codigoDaMateria) {
		this.codigoDaMateria = codigoDaMateria;
	}

	public Integer getCodigoDaMateria() {
		return codigoDaMateria;
	}

	public void setTipoJulgamento(Long tipoJulgamento) {
		this.tipoJulgamento = tipoJulgamento;
	}

	public Long getTipoJulgamento() {
		return tipoJulgamento;
	}

	public Long getSequencialObjetoIncidente() {
		return objetoIncidente.getId();
	}

}
