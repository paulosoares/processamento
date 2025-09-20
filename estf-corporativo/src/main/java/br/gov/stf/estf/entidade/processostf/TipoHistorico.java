package br.gov.stf.estf.entidade.processostf;

import br.gov.stf.framework.util.GenericEnum;

/**
 *Caso o histórico seja de Origens o valor ="O" Caso seja de Processo o valor =
 * "P"
 * 
 */
public class TipoHistorico extends GenericEnum<String, TipoHistorico> {

	public static final TipoHistorico ORIGEM = new TipoHistorico("O", "Origem");
	public static final TipoHistorico PROCESSO = new TipoHistorico("P", "Processo");

	private final String descricao;

	private TipoHistorico(String sigla) {
		this(sigla, "Tipo Histórico:" + sigla);
	}

	private TipoHistorico(String sigla, String descricao) {
		super(sigla);
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static TipoHistorico valueOf(String TipoHistorico) {
		return valueOf(TipoHistorico.class, TipoHistorico);
	}

	public static TipoHistorico[] values() {
		return values(new TipoHistorico[0], TipoHistorico.class);
	}
	
	public static TipoHistorico valueOfCodigo(String codigo) {
		if (codigo != null) {
			for (TipoHistorico tipo : values()) {
				if (codigo.equals(tipo.getCodigo())) {
					return tipo;
				}
			}
		}
		throw new RuntimeException("Nao existe TipoHistorico com codigo: " + codigo);
	}

}
