package br.jus.stf.estf.decisao.support.util;

/**
 * Enumeração dos tipos de formato de arquivo que podem ser utilizados.
 * 
 * @author thiago.miranda
 * @since 3.12.0
 */
public enum FormatoArquivo {

	PDF("PDF", ".pdf", "application/pdf"), RTF("RTF", ".rtf", "application/rtf"), HTML("HTML", ".html", "text/html"), DOC("DOC", ".doc", "application/msword");

	private String nome;
	private String extensao;
	private String mimeType;

	FormatoArquivo(String nome, String extensao, String mimeType) {
		this.nome = nome;
		this.extensao = extensao;
		this.mimeType = mimeType;
	}
	
	public String getNome() {
		return nome;
	}

	public String getExtensao() {
		return extensao;
	}

	public String getMimeType() {
		return mimeType;
	}
}
