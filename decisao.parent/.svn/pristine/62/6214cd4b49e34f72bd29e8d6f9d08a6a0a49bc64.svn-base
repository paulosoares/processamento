package br.jus.stf.estf.decisao;

import java.net.URISyntaxException;

public class DocAbrirDecisaoId extends DocDecisaoId {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1302852677941670896L;
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName()+"@"+this.getNome()+","+this.getSeqTexto();
	}

	@Override
	public String toURI() throws URISyntaxException {
		return StfOfficeDecisaoURI.criarURI("decisao",
				DocDecisaoId.ACAO_ABRIR_DOCUMENTO, this);
	}

}
