package br.gov.stf.estf.entidade.publicacao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name = "VW_FERIADOS", schema = "SRH2")
public class Feriado extends ESTFBaseEntity<String> {

	private String id;
	private String dia;
	private String descricao;
	private String horarioInicio;
	private String horarioFim;
	
	public static final String RECESSO_FORENSE = "RECESSO FORENSE";
	public static final String RECESSO_REGIMENTAL = "RECESSO REGIMENTAL";
	
	@Id
	@Column(name = "MES_ANO")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "DIA")
	public String getDia() {
		return dia;
	}
	public void setDia(String dia) {
		this.dia = dia;
	}
	
	@Column(name = "DESCRICAO")	
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Column(name = "HORARIO_INICIO")
	public String getHorarioInicio() {
		return horarioInicio;
	}
	public void setHorarioInicio(String horarioInicio) {
		this.horarioInicio = horarioInicio;
	}
	
	@Column(name = "HORARIO_FIM")
	public String getHorarioFim() {
		return horarioFim;
	}
	public void setHorarioFim(String horarioFim) {
		this.horarioFim = horarioFim;
	}
	
}
