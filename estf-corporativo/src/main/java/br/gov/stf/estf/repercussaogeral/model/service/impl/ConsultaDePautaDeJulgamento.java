package br.gov.stf.estf.repercussaogeral.model.service.impl;

import br.gov.stf.estf.publicacao.model.util.IConsultaDePautaDeJulgamento;

public class ConsultaDePautaDeJulgamento implements IConsultaDePautaDeJulgamento {

	private Long sequencialObjetoIncidente;
	private Long tipoJulgamento;
	private Integer codigoDaMateria;
	
	public ConsultaDePautaDeJulgamento(Long sequencialObjetoIncidente, Integer codigoDaMateria, Long tipoJulgamento) {
		super();
		this.sequencialObjetoIncidente = sequencialObjetoIncidente;
		this.codigoDaMateria = codigoDaMateria;
		this.tipoJulgamento = tipoJulgamento;
	}
	public Long getSequencialObjetoIncidente() {
		return sequencialObjetoIncidente;
	}
	public void setSequencialObjetoIncidente(Long sequencialObjetoIncidente) {
		this.sequencialObjetoIncidente = sequencialObjetoIncidente;
	}
	public Long getTipoJulgamento() {
		return tipoJulgamento;
	}
	public void setTipoJulgamento(Long tipoJulgamento) {
		this.tipoJulgamento = tipoJulgamento;
	}
	public Integer getCodigoDaMateria() {
		return codigoDaMateria;
	}
	public void setCodigoDaMateria(Integer codigoDaMateria) {
		this.codigoDaMateria = codigoDaMateria;
	}
}
