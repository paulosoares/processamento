package br.gov.stf.estf.entidade.processostf;

/**
 * Coluna criada para atender o RE eletr�nico. Armazena o tipo de intima��o feita ao advogado. P - intima��o feita
 * pessoalmente; E - intima��o eletr�nica.
 * 
 * @author Bruno da Silva Abreu
 * @since 31/05/2008
 * 
 */
public enum TipoIntimacaoAdvogado {
	E("Eletronicamente", "E"),
	P("Pessoalmente", "P");
	

	private String	codigo;
	private String	descricao;


	private TipoIntimacaoAdvogado(String descricao, String codigo) {
		this.descricao = descricao;
		this.codigo = codigo;
	}


	public String getCodigo() {
		return this.codigo;
	}


	public String getDescricao() {
		return this.descricao;
	}


	public TipoIntimacaoAdvogado getTipoIntimacaoAdvogado(String codigo) {
		if (TipoIntimacaoAdvogado.E.getCodigo().equals(codigo)) {
			return TipoIntimacaoAdvogado.E;
		}

		return TipoIntimacaoAdvogado.P;
	}
}
