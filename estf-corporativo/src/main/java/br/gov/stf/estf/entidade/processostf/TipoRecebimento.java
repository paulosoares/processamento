package br.gov.stf.estf.entidade.processostf;

import br.gov.stf.framework.util.GenericEnum;

/**
 * F-Fax; P-PETIÇÃO ELETRÔNICA; C-Correio; E-E-mail; B-BALCÃO; D-DOCUMENTO
 * (processo) ELETRÔNICO. A-Protocolo Avançado G-Petição Eletrônica com
 * Certificação Digital S- Setor do STF I- Indeterminado
 */
public class TipoRecebimento extends GenericEnum<String, TipoRecebimento> {

	private static final long serialVersionUID = -4319484517374210028L;
	
	public static final TipoRecebimento FAX = new TipoRecebimento("F", "Fax");
	public static final TipoRecebimento PETICAO_ELETRONICA = new TipoRecebimento("P", "Petição Eletrônica");
	public static final TipoRecebimento CORREIO = new TipoRecebimento("C", "Correio");
	public static final TipoRecebimento EMAIL = new TipoRecebimento("E", "E-mail");
	public static final TipoRecebimento BALCAO = new TipoRecebimento("B", "Balcão");
	public static final TipoRecebimento DOCUMENTO_ELETRONICO = new TipoRecebimento("D", "Documento (processo) Eletrônico");
	public static final TipoRecebimento PROTOCOLO_AVANCADO = new TipoRecebimento("A", "Protocolo Avançado");
	public static final TipoRecebimento PETICAO_ELETRONICA_CERTIFICACAO_DIGITAL = new TipoRecebimento("G",
			"Petição Eletrônica com Certificação Digital");
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
