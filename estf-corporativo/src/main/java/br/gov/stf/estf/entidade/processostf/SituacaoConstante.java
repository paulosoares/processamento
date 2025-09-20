package br.gov.stf.estf.entidade.processostf;

import br.gov.stf.framework.util.GenericEnum;


/*
 * Indica a situa��o da peti��o eletr�nica: Peticionada (Q) ou N�o Peticionada(U).
 */
public class SituacaoConstante  extends GenericEnum<String, SituacaoConstante> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -194637409996295857L;
	public static final SituacaoConstante PETICIONADA = new SituacaoConstante("Q", "Peticionada");
	public static final SituacaoConstante NAO_PETICIONADA = new SituacaoConstante("U", "N�o Peticionada");
	
	private final String descricao;

	private SituacaoConstante(String sigla) {
		this(sigla, "Tipo Situa��o:" + sigla);
	}

	private SituacaoConstante(String sigla, String descricao) {
		super(sigla);
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static SituacaoConstante valueOf(String SituacaoConstante) {
		return valueOf(SituacaoConstante.class, SituacaoConstante);
	}

	public static SituacaoConstante[] values() {
		return values(new SituacaoConstante[0], SituacaoConstante.class);
	}

}
