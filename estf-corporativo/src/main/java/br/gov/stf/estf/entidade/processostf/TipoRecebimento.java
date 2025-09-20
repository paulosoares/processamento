package br.gov.stf.estf.entidade.processostf;

import br.gov.stf.framework.util.GenericEnum;

/**
 * F-Fax; P-PETI��O ELETR�NICA; C-Correio; E-E-mail; B-BALC�O; D-DOCUMENTO
 * (processo) ELETR�NICO. A-Protocolo Avan�ado G-Peti��o Eletr�nica com
 * Certifica��o Digital S- Setor do STF I- Indeterminado
 */
public class TipoRecebimento extends GenericEnum<String, TipoRecebimento> {

	private static final long serialVersionUID = -4319484517374210028L;
	
	public static final TipoRecebimento FAX = new TipoRecebimento("F", "Fax");
	public static final TipoRecebimento PETICAO_ELETRONICA = new TipoRecebimento("P", "Peti��o Eletr�nica");
	public static final TipoRecebimento CORREIO = new TipoRecebimento("C", "Correio");
	public static final TipoRecebimento EMAIL = new TipoRecebimento("E", "E-mail");
	public static final TipoRecebimento BALCAO = new TipoRecebimento("B", "Balc�o");
	public static final TipoRecebimento DOCUMENTO_ELETRONICO = new TipoRecebimento("D", "Documento (processo) Eletr�nico");
	public static final TipoRecebimento PROTOCOLO_AVANCADO = new TipoRecebimento("A", "Protocolo Avan�ado");
	public static final TipoRecebimento PETICAO_ELETRONICA_CERTIFICACAO_DIGITAL = new TipoRecebimento("G",
			"Peti��o Eletr�nica com Certifica��o Digital");
	public static final TipoRecebimento SETOR_STF = new TipoRecebimento("S", "Setor do STF");
	public static final TipoRecebimento INDETERMINADO = new TipoRecebimento("I", "Indeterminado");

	private final String descricao;

	public TipoRecebimento(String sigla) {
		this(sigla, "Tipo Recebimento:" + sigla);
	}

	private TipoRecebimento(String sigla, String descricao) {
		super(sigla);
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static TipoRecebimento valueOf(String TipoRecebimento) {
		return valueOf(TipoRecebimento.class, TipoRecebimento);
	}

	public static TipoRecebimento[] values() {
		return values(new TipoRecebimento[0], TipoRecebimento.class);
	}

}
