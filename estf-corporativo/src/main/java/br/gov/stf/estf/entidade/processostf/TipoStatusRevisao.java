package br.gov.stf.estf.entidade.processostf;

import br.gov.stf.framework.util.GenericEnum;

public class TipoStatusRevisao extends GenericEnum<String, TipoStatusRevisao> {

	private static final long serialVersionUID = 7193169964822626452L;

	public static final TipoStatusRevisao NAO_REVISADO = new TipoStatusRevisao("NR", "Não Revisado");
	public static final TipoStatusRevisao EM_REVISAO = new TipoStatusRevisao("ER", "Em Revisão");
	public static final TipoStatusRevisao REVISADO = new TipoStatusRevisao("RE", "Revisado");
	public static final TipoStatusRevisao AGUARDANDO_AUTORIZAÇÃO = new TipoStatusRevisao("AA", "Aguardando Autorização");
	public static final TipoStatusRevisao OCERIZADO = new TipoStatusRevisao("OC", "Ocerizado");

	private String descricao;

	TipoStatusRevisao(String codigo, String descricao) {
		super(codigo);
		this.descricao = descricao;
	}

	TipoStatusRevisao(String codigo) {
		super(codigo);
	}

	public String getDescricao() {
		return descricao;
	}

	public static TipoStatusRevisao valueOf(String codigo) {
		return valueOf(TipoStatusRevisao.class, codigo);
	}

	public static TipoStatusRevisao[] values() {
		return values(new TipoStatusRevisao[0], TipoStatusRevisao.class);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof TipoStatusRevisao)) {
			return false;
		} else {
			return this.compareTo((TipoStatusRevisao) obj) == 0;
		}
	}
}
