package br.gov.stf.estf.entidade.documento;

import br.gov.stf.framework.util.GenericEnum;

public class TipoSituacaoDocumento extends GenericEnum<Long, TipoSituacaoDocumento> {
	public static final TipoSituacaoDocumento REJEITADO_MINISTRO = new TipoSituacaoDocumento(1, "Rejeitado pelo Ministro", "RJ");
	public static final TipoSituacaoDocumento REVISADO = new TipoSituacaoDocumento(2, "Revisado", "RV");
	public static final TipoSituacaoDocumento ASSINADO_DIGITALMENTE = new TipoSituacaoDocumento(3, "Assinado Digitalmente", "AS");
	public static final TipoSituacaoDocumento GERADO = new TipoSituacaoDocumento(4, "Gerado", "GR");
	public static final TipoSituacaoDocumento LIBERADO_PARA_REVISAO = new TipoSituacaoDocumento(5, "Liberado para Revisão", "LR");
	public static final TipoSituacaoDocumento REJEITADO_PELO_REVISOR = new TipoSituacaoDocumento(6, "Rejeitado pelo Revisor","RR");
	public static final TipoSituacaoDocumento JUNTADO = new TipoSituacaoDocumento(11, "Juntado", "JN");
	public static final TipoSituacaoDocumento ASSINADO_MANUALMENTE = new TipoSituacaoDocumento(7, "Assinado Manualmente", "AM");
	public static final TipoSituacaoDocumento SOMENTE_REVISADO = new TipoSituacaoDocumento(10, "Somente Revisado", "SR");
	public static final TipoSituacaoDocumento SOLICITADO_GERACAO_PDF = new TipoSituacaoDocumento(8, "Solicitado Geração de PDF","SG");
	public static final TipoSituacaoDocumento CANCELADO_PELO_MINISTRO = new TipoSituacaoDocumento(9, "Cancelado pelo Ministro","CM");
	public static final TipoSituacaoDocumento CANCELADO_AUTOMATICAMENTE = new TipoSituacaoDocumento(12,"Cancelado Automaticamente", "CA");

	private final String descricao;
	private final String sigla;

	private TipoSituacaoDocumento(Long codigo) {
		this(codigo, "TIPO_SITUACAO_DOCUMENTO_" + codigo, "TSD" + codigo);
	}

	private TipoSituacaoDocumento(long codigo, String descricao, String sigla) {
		super(codigo);
		this.descricao = descricao;
		this.sigla = sigla;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getSigla() {
		return sigla;
	}

	public static TipoSituacaoDocumento valueOf(Long codigo) {
		return valueOf(TipoSituacaoDocumento.class, codigo);
	}

	public static TipoSituacaoDocumento[] values() {
		return values(new TipoSituacaoDocumento[0], TipoSituacaoDocumento.class);
	}

	public static TipoSituacaoDocumento getBySigla(String sigla) {
		for (TipoSituacaoDocumento elem : values()) {
			if (elem.sigla.equals(sigla)) {
				return elem;
			}
		}
		throw new RuntimeException("Nao existe elemento com a sigla: " + sigla);
	}
}
