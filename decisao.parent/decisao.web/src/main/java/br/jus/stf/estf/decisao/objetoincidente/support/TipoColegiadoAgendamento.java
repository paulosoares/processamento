package br.jus.stf.estf.decisao.objetoincidente.support;

import br.gov.stf.estf.entidade.publicacao.EstruturaPublicacao;

public enum TipoColegiadoAgendamento {
	PLENARIO("P", "Plenário"), 
	PT("1T", "Primeira Turma"), 
	ST("2T", "Segunda Turma");

	private String id;
	private String descricao;

	TipoColegiadoAgendamento(String id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public String getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}

	public static TipoColegiadoAgendamento getById(String id) {
		if ("TP".equals(id))
			return PLENARIO;
		
		for (TipoColegiadoAgendamento tipoColegiadoAgendamento : values()) {
			if (tipoColegiadoAgendamento.getId().equals(id)) {
				return tipoColegiadoAgendamento;
			}
		}
		return null;
	}

	public Integer getCodigoCapitulo() {
		if (PLENARIO.id.equals(id))
			return EstruturaPublicacao.COD_CAPITULO_PLENARIO;
		
		if (PT.id.equals(id))
			return EstruturaPublicacao.COD_CAPITULO_PRIMEIRA_TURMA;
		
		if (ST.id.equals(id))
			return EstruturaPublicacao.COD_CAPITULO_SEGUNDA_TURMA;
		
		return null;
	}

}
