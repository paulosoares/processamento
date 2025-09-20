package br.gov.stf.estf.publicacao.model.util;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProtocoloVO {
	public int getObjetoIncidente() {
		return objetoIncidente;
	}
	public void setObjetoIncidente(int objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}
	public ProtocoloVO(ResultSet rs) throws SQLException {
		this.objetoIncidente = rs.getInt("seq_objeto_incidente");
	}
	private int objetoIncidente;
}
