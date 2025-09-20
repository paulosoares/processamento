package br.gov.stf.estf.entidade.documento.tipofase;

import java.util.HashMap;
import java.util.Map;

import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;

public enum TipoTransicaoFaseTexto {
	LIBERAR_PARA_REVISAO("Liberar para revisão", FaseTexto.EM_ELABORACAO, FaseTexto.EM_REVISAO, TipoSituacaoDocumento.GERADO),

	CONCLUIR_REVISAO("Concluir revisão", new TransicaoFaseTexto[] {
			new TransicaoFaseTexto(FaseTexto.EM_REVISAO, FaseTexto.REVISADO, TipoSituacaoDocumento.SOMENTE_REVISADO),
			new TransicaoFaseTexto(FaseTexto.EM_ELABORACAO, FaseTexto.REVISADO, TipoSituacaoDocumento.SOMENTE_REVISADO), }),

	LIBERAR_PARA_ASSINATURA("Liberar para assinatura", new TransicaoFaseTexto[] {
			new TransicaoFaseTexto(FaseTexto.EM_ELABORACAO, FaseTexto.LIBERADO_ASSINATURA, TipoSituacaoDocumento.REVISADO),
			new TransicaoFaseTexto(FaseTexto.EM_REVISAO, FaseTexto.LIBERADO_ASSINATURA, TipoSituacaoDocumento.REVISADO),
			new TransicaoFaseTexto(FaseTexto.REVISADO, FaseTexto.LIBERADO_ASSINATURA, TipoSituacaoDocumento.REVISADO) }),

	ASSINAR_DIGITALMENTE("Assinar digitalmente", FaseTexto.LIBERADO_ASSINATURA, FaseTexto.ASSINADO,
			TipoSituacaoDocumento.ASSINADO_DIGITALMENTE),

	LIBERAR_PARA_PUBLICACAO("Liberar para publicação", new TransicaoFaseTexto[] {
			new TransicaoFaseTexto(FaseTexto.ASSINADO, FaseTexto.LIBERADO_PUBLICACAO, null),
			new TransicaoFaseTexto(FaseTexto.JUNTADO, FaseTexto.LIBERADO_PUBLICACAO, null)
	}),

	VOLTAR_PARA_ELABORACAO("Voltar para elaboração", new TransicaoFaseTexto[] {
			new TransicaoFaseTexto(FaseTexto.LIBERADO_ASSINATURA, FaseTexto.EM_ELABORACAO,
					TipoSituacaoDocumento.REJEITADO_MINISTRO),
			new TransicaoFaseTexto(FaseTexto.REVISADO, FaseTexto.EM_ELABORACAO, TipoSituacaoDocumento.REJEITADO_PELO_REVISOR),
			new TransicaoFaseTexto(FaseTexto.EM_REVISAO, FaseTexto.EM_ELABORACAO, TipoSituacaoDocumento.REJEITADO_PELO_REVISOR),
			new TransicaoFaseTexto(FaseTexto.LIBERADO_PUBLICACAO, FaseTexto.EM_ELABORACAO, null),
			new TransicaoFaseTexto(FaseTexto.PUBLICADO, FaseTexto.EM_ELABORACAO, null) }),

	VOLTAR_PARA_REVISAO("Voltar para revisão", FaseTexto.REVISADO, FaseTexto.EM_REVISAO, TipoSituacaoDocumento.GERADO),

	SUSPENDER_LIBERACAO("Suspender liberação", FaseTexto.LIBERADO_ASSINATURA, FaseTexto.REVISADO,
			TipoSituacaoDocumento.SOMENTE_REVISADO),

	CANCELAR_ASSINATURA("Cancelar assinatura", FaseTexto.ASSINADO, FaseTexto.EM_ELABORACAO,
			TipoSituacaoDocumento.CANCELADO_PELO_MINISTRO),

	SUSPENDER_PUBLICACAO("Suspender publicação", FaseTexto.LIBERADO_PUBLICACAO, FaseTexto.ASSINADO,
			TipoSituacaoDocumento.ASSINADO_DIGITALMENTE),

	PUBLICAR("Publicar", FaseTexto.LIBERADO_PUBLICACAO, FaseTexto.PUBLICADO, null), 
	
	JUNTAR("Juntar peças", FaseTexto.ASSINADO, FaseTexto.JUNTADO, null),
	
	DESFAZER_JUNTADA("Desfazer juntada", FaseTexto.JUNTADO, FaseTexto.ASSINADO, null);

	private final String descricao;

	private final Map<FaseTexto, TransicaoFaseTexto> transicoes = new HashMap<FaseTexto, TransicaoFaseTexto>();

	private TipoTransicaoFaseTexto(String descricao, TransicaoFaseTexto[] transicoes) {
		this.descricao = descricao;
		for (int i = 0; i < transicoes.length; i++) {
			this.transicoes.put(transicoes[i].origem, transicoes[i]);
		}
	}

	private TipoTransicaoFaseTexto(String descricao, FaseTexto origem, FaseTexto destino,
			TipoSituacaoDocumento tipoSituacaoDocumento) {
		this(descricao, new TransicaoFaseTexto[] { new TransicaoFaseTexto(origem, destino, tipoSituacaoDocumento) });
	}

	public String getDescricao() {
		return descricao;
	}

	public TransicaoFaseTexto getTransicao(FaseTexto origem) {
		return transicoes.get(origem);
	}

	public Map<FaseTexto, TransicaoFaseTexto> getTransicoes() {
		return transicoes;
	}

	public static TipoTransicaoFaseTexto getTransicaoParaFaseDestino(FaseTexto destino) {
		if (!destino.equals(FaseTexto.EM_ELABORACAO)) {
			for (TipoTransicaoFaseTexto tipoTransicao : values()) {
				Map<FaseTexto, TransicaoFaseTexto> transicoes = tipoTransicao.getTransicoes();
				for (TransicaoFaseTexto transicao : transicoes.values()) {
					if (transicao.destino.equals(destino)) {
						return tipoTransicao;
					}
				}
			}
		}
		return null;
	}
	
	public static TipoTransicaoFaseTexto getTransicaoDaOrigem(FaseTexto origem) {
		
		for (TipoTransicaoFaseTexto tipoTransicao : values()) {
			Map<FaseTexto, TransicaoFaseTexto> transicoes = tipoTransicao.getTransicoes();
			for (TransicaoFaseTexto transicao : transicoes.values()) {
				if (transicao.origem.equals(origem)) {
					return tipoTransicao;
				}
			}
		}
		
		return null;
	}

	public String toString() {
		return String.format("[TipoTransicaoFaseTexto %s ]", descricao);
	}
}
