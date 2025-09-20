package br.gov.stf.estf.entidade.documento.tipofase;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Indica as fases que um texto pode passar. A ordem de declaração das mesmas
 * influencia o compareTo. Nesse caso, caso seja necessário incluir alguma fase
 * intermediária às que existem, essa inclusão deve obedecer o fluxo do
 * documento.
 * 
 * @author Demetrius.Jube
 * 
 */
public enum FaseTexto {

	NAO_ELABORADO		(null, 	"Não elaborado"), 
	CANCELADO			(null, 	"Cancelado"), 
	EM_ELABORACAO		(1L, 	"Em elaboração"), 
	EM_REVISAO			(2L, 	"Em revisão"), 
	REVISADO			(3L,	"Revisado"), 
	LIBERADO_ASSINATURA	(4L, 	"Liberado para assinatura"), 
	ASSINADO			(5L, 	"Assinado"),
	LIBERADO_PUBLICACAO	(6L, 	"Liberado para publicação"),
	PUBLICADO			(7L, 	"Publicado"), 
	JUNTADO				(8L, 	"Juntado");

	private final Long codigoFase;
	private final String descricao;

	private FaseTexto(Long codigoFase, String descricao) {
		this.codigoFase = codigoFase;
		this.descricao = descricao;
	}

	public static FaseTexto valueOf(Long codigoFase) {
		if (codigoFase != null) {
			for (FaseTexto fase : values()) {
				if (codigoFase.equals(fase.getCodigoFase())) {
					return fase;
				}
			}
		} else {
			return FaseTexto.NAO_ELABORADO;
		}
		throw new RuntimeException("Nao existe FaseTexto com codigo: "
				+ codigoFase);
	}

	public Long getCodigoFase() {
		return codigoFase;
	}

	public String getDescricao() {
		return descricao;
	}

	public boolean hasMarcaDagua() {
		return this.compareTo(LIBERADO_ASSINATURA) < 0;
	}

	public static Set<FaseTexto> fasesComTextoAssinado = new HashSet<FaseTexto>(
			Arrays.asList(new FaseTexto[] { FaseTexto.ASSINADO,
					FaseTexto.LIBERADO_PUBLICACAO, FaseTexto.PUBLICADO,
					FaseTexto.JUNTADO }));

}
