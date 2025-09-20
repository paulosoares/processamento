package br.gov.stf.estf.entidade.processostf;

import br.gov.stf.framework.util.GenericEnum;

/**
 * F = Físico E = Eletrônico
 */
public class TipoMeioProcesso extends GenericEnum<String, TipoMeioProcesso> {

	private static final long serialVersionUID = -1004177817957070732L;
	
	public static final TipoMeioProcesso ELETRONICO = new TipoMeioProcesso("E", "Eletrônico");
	public static final TipoMeioProcesso FISICO = new TipoMeioProcesso("F", "Físico");
	private final String descricao;

	private TipoMeioProcesso(String sigla) {
		this(sigla, "Tipo Meio Processo:" + sigla);
	}

	private TipoMeioProcesso(String sigla, String descricao) {
		super(sigla);
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static TipoMeioProcesso valueOf(String tipoMeioProcesso) {
		return valueOf(TipoMeioProcesso.class, tipoMeioProcesso);
	}

	public static TipoMeioProcesso[] values() {
		return values(new TipoMeioProcesso[0], TipoMeioProcesso.class);
	}

}
