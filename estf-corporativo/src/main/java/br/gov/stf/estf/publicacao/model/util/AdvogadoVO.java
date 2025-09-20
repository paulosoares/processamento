package br.gov.stf.estf.publicacao.model.util;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdvogadoVO {
	private int seqJurisdicionado;
	private String nome;
	private int objetoIncidente;
	private int processo;
	private int origem;
	private java.sql.Date dataComposicao;
	private int capitulo;
	private String tipoObjetoIncidente;
	
	public AdvogadoVO (ResultSet rs) throws SQLException {
		this.seqJurisdicionado = rs.getInt("seq_jurisdicionado");
		this.nome = rs.getString("nom_jurisdicionado");
		this.objetoIncidente = rs.getInt("seq_objeto_incidente");
		this.origem = rs.getInt("cod_origem");
		this.dataComposicao = rs.getDate("dat_composicao_parcial");
		this.processo = rs.getInt("seq_objeto_incidente_principal");
		this.capitulo = rs.getInt("cod_capitulo");
		this.tipoObjetoIncidente = rs.getString("tip_objeto_incidente");
	}

	public String getTipoObjetoIncidente() {
		return tipoObjetoIncidente;
	}


	public int getProcesso() {
		return processo;
	}


	public java.sql.Date getDataComposicao() {
		return dataComposicao;
	}


	public int getOrigem() {
		return origem;
	}


	public int getObjetoIncidente() {
		return objetoIncidente;
	}


	public int getSeqJurisdicionado() {
		return seqJurisdicionado;
	}


	public String getNome() {
		return nome;
	}

	
	public boolean isLancarIntimacaoEletronica () {
		boolean lancar = false;
		if ( origem>0 && capitulo>=2 && capitulo<=6 ) {
			lancar = true;
		}
		return lancar;
	}
	
}
