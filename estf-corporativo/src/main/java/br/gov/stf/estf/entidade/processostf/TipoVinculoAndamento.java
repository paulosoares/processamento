package br.gov.stf.estf.entidade.processostf;

import br.gov.stf.framework.util.GenericEnum;


public class TipoVinculoAndamento extends GenericEnum<String, TipoVinculoAndamento>{
		
	private static final long serialVersionUID = -521695515414522430L;
	public static final TipoVinculoAndamento RELACIONADO = new TipoVinculoAndamento("R", "Relacionado");
	public static final TipoVinculoAndamento GERADO = new TipoVinculoAndamento("G", "Gerado");
	private final String descricao;

	private TipoVinculoAndamento(String sigla) {
		this(sigla, "Tipo Vínculo Andamento: " + sigla);
	}

	private TipoVinculoAndamento(String sigla, String descricao) {
		super(sigla);
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static TipoVinculoAndamento valueOf(String tipoMeioProcesso) {
		return valueOf(TipoVinculoAndamento.class, tipoMeioProcesso);
	}

	public static TipoVinculoAndamento[] values() {
		return values(new TipoVinculoAndamento[0], TipoVinculoAndamento.class);
	}

}
