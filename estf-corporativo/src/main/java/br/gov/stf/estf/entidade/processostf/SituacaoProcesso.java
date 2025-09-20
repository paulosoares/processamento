package br.gov.stf.estf.entidade.processostf;

import br.gov.stf.framework.util.GenericEnum;

/**
 * Z PROBLEMAS A Autuado C Conferido p/Distribuição P Protocolado D Distribuido
 * G Registrado ao Presidente R Redistribuido I Irregular E Errado X Chamada
 * p/Preparo M Remessa indevida
 */
public class SituacaoProcesso extends GenericEnum<String, SituacaoProcesso> {
	
	private static final long serialVersionUID = -8666263265980033644L;
	
	public static final SituacaoProcesso PROBLEMAS = new SituacaoProcesso("Z", "Problemas");
	public static final SituacaoProcesso AUTUADO = new SituacaoProcesso("A", "Autuado");
	public static final SituacaoProcesso CONFERIDO = new SituacaoProcesso("C", "Conferido p/ Distribuição");
	public static final SituacaoProcesso PROTOCOLADO = new SituacaoProcesso("P", "Protocolado");
	public static final SituacaoProcesso DISTRIBUIDO = new SituacaoProcesso("D", "Distribuído");
	public static final SituacaoProcesso REGISTRADO_PRESIDENTE = new SituacaoProcesso("G", "Registrado ao Presidente");
	public static final SituacaoProcesso REDISTRIBUIDO = new SituacaoProcesso("R", "Redistribuído");
	public static final SituacaoProcesso IRREGULAR = new SituacaoProcesso("I", "Irregular");
	public static final SituacaoProcesso ERRADO = new SituacaoProcesso("E", "Errado");
	public static final SituacaoProcesso CHAMADA = new SituacaoProcesso("X", "Chamada p/ preparo");
	public static final SituacaoProcesso REMESSA_INDEVIDA = new SituacaoProcesso("M", "Remessa indevida");
	public static final SituacaoProcesso CADASTRAMENTO = new SituacaoProcesso("L", "Em Cadastramento");
	public static final SituacaoProcesso PROCESSO_FINDO = new SituacaoProcesso("J", "Processo Findo");
	public static final SituacaoProcesso DETERMINADA_DEVOLUCAO = new SituacaoProcesso("K", "Determinada a Devolução");

	private final String descricao;

	private SituacaoProcesso(String sigla) {
		this(sigla, "Situação Processo:" + sigla);
	}

	private SituacaoProcesso(String sigla, String descricao) {
		super(sigla);
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public static SituacaoProcesso valueOf(String SituacaoProcesso) {
		return valueOf(SituacaoProcesso.class, SituacaoProcesso);
	}

	public static SituacaoProcesso[] values() {
		return values(new SituacaoProcesso[0], SituacaoProcesso.class);
	}
}
