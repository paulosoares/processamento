package br.gov.stf.estf.entidade.processostf;

import br.gov.stf.framework.util.GenericEnum;



/**
 * Quando a peticao é do tipo Reclamação, especifica se a reclamação é do tipo:
 *  *  Descumprimento da autoridade da decisao do STF (D) ou 
 *  *  Usurpação da Competência do STF (U)
 */
public class TipoReclamacao extends GenericEnum<String, TipoReclamacao> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2155145088995618570L;
	public static final TipoReclamacao DESCUMPRIMENTO = new TipoReclamacao("D", "Descumprimento da autoridade da decisao do STF");
	public static final TipoReclamacao USURPACAO = new TipoReclamacao("U", "Usurpação da Competência do STF");
	
	private final String descricao;

	private TipoReclamacao(String sigla) {
		this(sigla, "Tipo Reclamação:" + sigla);
	}

	private TipoReclamacao(String sigla, String descricao) {
		super(sigla);
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static TipoReclamacao valueOf(String TipoReclamacao) {
		return valueOf(TipoReclamacao.class, TipoReclamacao);
	}

	public static TipoReclamacao[] values() {
		return values(new TipoReclamacao[0], TipoReclamacao.class);
	}

}
