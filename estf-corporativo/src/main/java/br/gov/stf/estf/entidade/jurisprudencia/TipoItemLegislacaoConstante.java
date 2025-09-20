/**
 * 
 */
package br.gov.stf.estf.entidade.jurisprudencia;

/**
 * @author Paulo.Estevao
 * @since 08.08.2012
 */
public enum TipoItemLegislacaoConstante {

	ARTIGO(1L, "Artigo", "ART", true),
	INCISO(2L, "Inciso", "INC", true),
	LETRA(3L, "Letra", "LET", true),
	PARAGRAFO(4L, "Par�grafo", "PAR", true),
	ITEM(5L, "Item", "ITEM", true),
	TABELA(6L, "Tabela", "TABELA", true),
	ANEXO(7L, "Anexo", "ANEXO", true),
	ALINEA(8L, "Al�nea", "AL�NEA", true),
	CLAUSULA(9L, "Cl�usula", "CL�USULA", true),
	CAPITULO(10L, "Cap�tulo", "CAP�TULO", true),
	NUMERO(11L, "N�mero", "N�MERO", true),
	SECAO(12L, "Se��o", "SE��O", true),
	LIVRO(13L, "Livro", "LIVRO", true),
	TITULO(14L, "T�tulo", "T�TULO", true),
	SUBTITULO(15L, "Subt�tulo", "SUBT�TULO", true),
	PARTEGERAL(16L, "Parte Geral", "PARTE GERAL", true),
	PARTEESPECIAL(17L, "Parte Especial", "PARTE ESPECIAL", true),
	SUBSECAO(18L, "Subse��o", "SUBSE��O", true);
	
	private Long id;
	private String descricao;
	private String sigla;
	private boolean admiteCaput;
	
	private TipoItemLegislacaoConstante(Long id, String descricao, String sigla, boolean admiteCaput) {
		this.id = id;
		this.descricao = descricao;
		this.sigla = sigla;
		this.admiteCaput = admiteCaput;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public String getSigla() {
		return sigla;
	}
	
	public boolean isAdmiteCaput() {
		return admiteCaput;
	}
	
	public static TipoItemLegislacaoConstante valueOfDescricao(String descricao) {
		for (TipoItemLegislacaoConstante tipoItemLegislacao : values()) {
			if (tipoItemLegislacao.getDescricao().equals(descricao)) {
				return tipoItemLegislacao;
			}
		}
		
		return null;
	}
	
	public static TipoItemLegislacaoConstante valueOf(Long id) {
		for (TipoItemLegislacaoConstante tipoItemLegislacao : values()) {
			if (tipoItemLegislacao.getId().equals(id)) {
				return tipoItemLegislacao;
			}
		}
		
		return null;
	}
}
