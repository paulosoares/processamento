package br.gov.stf.estf.entidade.documento;

import br.gov.stf.framework.util.GenericEnum;

public class TipoSituacaoTexto extends GenericEnum<Long,TipoSituacaoTexto> {
	public static final TipoSituacaoTexto ATIVO_NO_CONTROLE_DE_VOTOS = new TipoSituacaoTexto(1, "Ativo no Controle de Votos");
	public static final TipoSituacaoTexto CANCELADO = new TipoSituacaoTexto(2, "Cancelado");
	public static final TipoSituacaoTexto REVISADO = new TipoSituacaoTexto(3, "Revisado");
		
	private final String descricao;
	private TipoSituacaoTexto(Long codigo) {
		this(codigo, "Tipo Situacao Texto " + codigo);
	}
	private TipoSituacaoTexto(long codigo, String descricao) {
		super(codigo);
		this.descricao = descricao;
	}
	public String getDescricao() {
		return descricao;
	}
	public static TipoSituacaoTexto valueOf(Long codigo) {
		return valueOf(TipoSituacaoTexto.class,codigo);
	}
	
	public static TipoSituacaoTexto[] values() {
		return values(new TipoSituacaoTexto[0], TipoSituacaoTexto.class);
	}
}
