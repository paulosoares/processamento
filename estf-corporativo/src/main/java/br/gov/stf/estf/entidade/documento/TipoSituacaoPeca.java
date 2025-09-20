package br.gov.stf.estf.entidade.documento;

import br.gov.stf.framework.util.GenericEnum;

public class TipoSituacaoPeca extends GenericEnum<Long, TipoSituacaoPeca> {

	private static final long serialVersionUID = -5523748647473358222L;

	public static final TipoSituacaoPeca EXCLUIDA = new TipoSituacaoPeca(1, "Excluída");
	public static final TipoSituacaoPeca PENDENTE = new TipoSituacaoPeca(2, "Pendente");
	public static final TipoSituacaoPeca JUNTADA = new TipoSituacaoPeca(3, "Juntada");
	public static final TipoSituacaoPeca MEMORIAL = new TipoSituacaoPeca(4, "Memorial");

	private final String descricao;

	private TipoSituacaoPeca(Long codigo) {
		this(codigo, "Tipo Situacao Peca " + codigo);
	}

	private TipoSituacaoPeca(long codigo, String descricao) {
		super(codigo);
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static TipoSituacaoPeca valueOf(Long codigo) {
		return valueOf(TipoSituacaoPeca.class, codigo);
	}

	public static TipoSituacaoPeca[] values() {
		return values(new TipoSituacaoPeca[0], TipoSituacaoPeca.class);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof TipoSituacaoPeca)) {
			return false;
		} else {
			return this.compareTo((TipoSituacaoPeca) obj) == 0;
		}
	}
}
