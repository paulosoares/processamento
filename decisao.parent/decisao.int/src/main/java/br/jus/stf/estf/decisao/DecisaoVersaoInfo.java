package br.jus.stf.estf.decisao;

import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.Date;

/**
 * Modelo usado para trafegar informações da versão de um documento
 * 
 * @author LeonardoD
 * 
 */
public class DecisaoVersaoInfo extends DocDecisaoId implements Serializable {

	private static final long serialVersionUID = 4351864493410488480L;

	private final String descricaoFaseTexto;
	private final Date dataInclusao;

	/**
	 * 
	 * @param id
	 *            identificador do texto
	 * @param dataInclusao
	 * @param descricaoFaseTexto
	 *            Descricao da fase em que o texto se encontra
	 */
	public DecisaoVersaoInfo(Date dataInclusao, String descricaoFaseTexto) {
		this.dataInclusao = dataInclusao;
		this.descricaoFaseTexto = descricaoFaseTexto;
	}

	public Date getDataInclusao() {
		return dataInclusao;
	}

	public String getDescricaoFaseTexto() {
		return descricaoFaseTexto;
	}


	public String toString() {
		return this.getClass().getSimpleName() + "@" + this.getNome() + " idTexto: " + this.getSeqTexto() + ", idFase: "
				+ getSeqFaseTextoProcesso() + " - " + descricaoFaseTexto;
	}

	public String toURI() throws URISyntaxException {
		return StfOfficeDecisaoURI.criarURI("decisao", DocDecisaoId.ACAO_ABRIR_DOCUMENTO, this);
	}

}
