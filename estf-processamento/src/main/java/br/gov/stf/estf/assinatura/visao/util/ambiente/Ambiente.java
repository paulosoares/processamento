package br.gov.stf.estf.assinatura.visao.util.ambiente;

import br.gov.stf.estf.assinatura.visao.util.commons.StringUtils;

/**
 * Enumeração dos tipos de ambientes existentes.
 * 
 * @author thiago.miranda
 * @since 3.14.0
 */
public enum Ambiente {

	DESCONHECIDO("Desconhecido", "Desconhecido", "desconhecido", new String[] { "desconhecido" }),

	DESENVOLVIMENTO("Desenvolvimento", "stfd", "dstf", new String[] { "localhost:8080" }),

	TESTES("Testes", "stfq", "stf.jus.br", new String[] { "sistemast.stf.jus.br" }),

	HOMOLOGACAO("Homologação", "stfh", "hstf", new String[] { "sistemash.stf.jus.br", "aplh-tramite-gj:8080" }),

	PRODUCAO("Produção", "stfp", "stf.jus.br", new String[] { "sistemas.stf.jus.br", "tramite-gj-01:8080", "tramite-gj-02:8080" });

	private String descricao;
	private String nomeBanco;
	private String portalStf;
	private String[] nomesServidores;

	Ambiente(String descricao, String nomeBanco, String portalStf, String[] nomesServidores) {
		this.descricao = descricao;
		this.nomeBanco = nomeBanco;
		this.portalStf = portalStf;
		this.nomesServidores = nomesServidores;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getNomeBanco() {
		return nomeBanco;
	}

	public String getPortalStf() {
		return portalStf;
	}

	public String[] getNomesServidores() {
		return nomesServidores;
	}

	public String getNomeServidorPadrao() {
		return getNomesServidores()[0];
	}

	public boolean contemNomeServidor(String s) {
		if (StringUtils.isVazia(s)) {
			return false;
		}

		for (String nomeServidor : nomesServidores) {
			if (s.contains(nomeServidor)) {
				return true;
			}
		}

		return false;
	}
}
