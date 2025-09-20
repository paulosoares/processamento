package br.gov.stf.estf.entidade.documento;

/**
 * Flag que indica o tipo da assinatura digital realizada.
 * 
 * Padrão é a assinatura realizada normalmente no assinador desktop.
 * Móvel é a assinatura realizada pelo aplicativo Assinador Móvel.
 * 
 * @author Tomas.Godoi
 *
 */
public enum FlagTipoAssinatura {

	PADRAO("P", "Assinatura Padrão"), MOVEL("M", "Assinatura Móvel");

	private String codigo;
	private String descricao;

	FlagTipoAssinatura(String codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static FlagTipoAssinatura valueOfCodigo(String codigo) {
		for (FlagTipoAssinatura tipo : FlagTipoAssinatura.values()) {
			if (tipo.getCodigo().equals(codigo)) {
				return tipo;
			}
		}
		
		return null;
	}

}
