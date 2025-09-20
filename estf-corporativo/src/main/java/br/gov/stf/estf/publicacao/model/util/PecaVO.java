package br.gov.stf.estf.publicacao.model.util;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PecaVO {
	private int seqPeca;
	private int situacao;
	
	public PecaVO (ResultSet rs) throws SQLException {
		this.seqPeca = rs.getInt("seq_peca_proc_eletronico");
		this.situacao = rs.getInt("seq_tipo_situacao_peca");
	}

	public int getSeqPeca() {
		return seqPeca;
	}

	public void setSeqPeca(int seqPeca) {
		this.seqPeca = seqPeca;
	}

	public int getSituacao() {
		return situacao;
	}

	public void setSituacao(int situacao) {
		this.situacao = situacao;
	}
	
	public boolean isAtualizarPeca () {
		boolean atualizar = false;
		if ( situacao==2 ) {
			atualizar = true;
		}
		return atualizar;
	}
}
