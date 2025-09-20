package br.gov.stf.estf.entidade.documento;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum TipoFaseComunicacao {

	EM_ELABORACAO(1L, "Em elaboração"), 
	PDF_GERADO(2L, "PDF gerado"), 
	CORRECAO(3L, "Correção"), 
	AGUARDANDO_ASSINATURA(4L, "Aguardando assinatura"), 
	ASSINADO(5L, "Assinado"), 
	EXPEDIDO(6L, "Expedido"), 
	AGUARDANDO_ASSINATURA_MINISTRO(7L, "Aguardando assinatura do Ministro"), 
	AGUARDANDO_ENCAMINHAMENTO_ESTFDECISAO(8L,"Aguardando encaminhamento para o eSTF-Decisão"), 
	EXCLUIDO(9L, "Excluído"), 
	ASSINATURA_CANCELADA(10L, "Assinatura Cancelada"), 
	FINALIZADO(11L, "Finalizado"),
	EXCLUIDO_PELO_AUTOR(12L, "Excluído pelo Autor"), 
	EM_REVISAO(13L, "Em Revisão"), 
	REVISADO(14L, "Revisado"),
	AGUARDANDO_ENVIO(15L,"Aguardando envio"),
	ENVIADO(16L,"Enviado"),
	RECEBIDO(17L,"Recebido"),
	COM_PROBLEMA(18L,"Com problema"),
	RESTRITOS(19L,"Restrito");

	private final Long codigoFase;
	private final String descricao;

	private TipoFaseComunicacao(Long codigoFase, String descricao) {
		this.codigoFase = codigoFase;
		this.descricao = descricao;
	}

	public static TipoFaseComunicacao valueOf(Long codigoFase) {
		if (codigoFase != null) {
			for (TipoFaseComunicacao fase : values()) {
				if (codigoFase.equals(fase.getCodigoFase())) {
					return fase;
				}
			}
		} else {
			return null;
		}
		throw new RuntimeException("Nao existe TipoFaseComunicacao com codigo: " + codigoFase);
	}

	public Long getCodigoFase() {
		return codigoFase;
	}

	public String getDescricao() {
		return descricao;
	}

	public static Set<TipoFaseComunicacao> fasesComTextoAssinado = new HashSet<TipoFaseComunicacao>(Arrays.asList(new TipoFaseComunicacao[] { TipoFaseComunicacao.ASSINADO }));

}