package br.jus.stf.estf.decisao.objetoincidente.support;

import br.gov.stf.estf.entidade.processostf.Agendamento;


public enum TipoAgendamento {

	PAUTA("P", "Pauta", Agendamento.COD_MATERIA_AGENDAMENTO_PAUTA), INDICE("I", "Índice", Agendamento.COD_MATERIA_AGENDAMENTO_JULGAMENTO);
	private String id;
	private String descricao;
	private Integer codigoMateria;

	TipoAgendamento(String id, String descricao, Integer codigoMateria) {
		this.id = id;
		this.descricao = descricao;
		this.codigoMateria = codigoMateria;
	}

	public String getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public Integer getCodigoMateria() {
		return codigoMateria;
	}

	public static TipoAgendamento getById(String id) {
		for (TipoAgendamento tipoDeAgendamento : values()) {
			if (tipoDeAgendamento.getId().equals(id)) {
				return tipoDeAgendamento;
			}
		}
		return null;
	}
	
	public static TipoAgendamento getByCodigoMateria(Integer codigoMateria) {
		for (TipoAgendamento tipoDeAgendamento : values()) {
			if (tipoDeAgendamento.getCodigoMateria().equals(codigoMateria)) {
				return tipoDeAgendamento;
			}
		}
		return null;
	}

}
