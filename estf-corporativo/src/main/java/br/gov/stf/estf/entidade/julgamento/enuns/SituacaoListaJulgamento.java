package br.gov.stf.estf.entidade.julgamento.enuns;

import br.gov.stf.framework.util.GenericEnum;

public class SituacaoListaJulgamento extends GenericEnum<String, SituacaoListaJulgamento> {

	private static final long serialVersionUID = -9003331015766030150L;
	
	public static final SituacaoListaJulgamento EMAIL_ENVIADO = new SituacaoListaJulgamento("E", "E-mail enviado");
	public static final SituacaoListaJulgamento EMAIL_NAO_ENVIADO = new SituacaoListaJulgamento("N", "E-mail não enviado");
	public static final SituacaoListaJulgamento LISTA_MODIFICADA = new SituacaoListaJulgamento("M", "Lista Modificada");
	
	private String descricao;

	SituacaoListaJulgamento(String codigo, String descricao) {
		super(codigo);
		this.descricao = descricao;
	}

	SituacaoListaJulgamento(String codigo) {
		super(codigo);
	}

	public String getDescricao() {
		return descricao;
	}

	public static SituacaoListaJulgamento valueOf(String codigo) {
		return valueOf(SituacaoListaJulgamento.class, codigo);
	}

	public static SituacaoListaJulgamento[] values() {
		return values(new SituacaoListaJulgamento[0], SituacaoListaJulgamento.class);
	}
}
