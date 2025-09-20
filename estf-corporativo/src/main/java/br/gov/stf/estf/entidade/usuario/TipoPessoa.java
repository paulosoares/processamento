package br.gov.stf.estf.entidade.usuario;

import java.util.Arrays;
import java.util.List;

/**
 * TODO Glehn mover para o pacote do domínio. <br>
 *  
 * Identifica o tipo de pessoa. Fornece serviços que permitem identificar qual o tipo da pessoa
 * e os detalhes de sua identificação. <br>
 * Obs.: enum migrado do peticionamento v2.
 *  
 * @author Rodrigo Barreiros
 * @since 17.03.2011
 */
public enum TipoPessoa {
	
	PESSOA_PUBLICA("CNPJ", "Pessoa Pública", "PP"), 
	PESSOA_JURIDICA("CNPJ", "Pessoa Jurídica", "PJ"), 
	PESSOA_FISICA("CPF", "Pessoa Física", "PF"), 
	OUTRAS("", "", "");
	
	private String identificador;
	private String descricao;
	private String sigla;
	
	/**
	 * Constroi a instância com os detalhes da identificação
	 * 
	 * @param identificador o identificador
	 * @param descricao a descrição do identificador
	 * @param sigla a sigla da pessoa
	 */
	private TipoPessoa(String identificador, String descricao, String sigla) {
		this.identificador = identificador;
		this.descricao = descricao;
		this.sigla = sigla;
	}
	
	/**
	 * Retorna a lista de pessoas que são "identificaveis", ou seja, que possuem
	 * identificações.
	 * 
	 * @return a lista de "identificáveis"
	 */
	public static List<TipoPessoa> getIdentificaveis() {
		return Arrays.asList(new TipoPessoa[] {TipoPessoa.PESSOA_PUBLICA, TipoPessoa.PESSOA_JURIDICA, TipoPessoa.PESSOA_FISICA});
	}
	
	/**
	 * Determina qual é o tipo de pessoa de um dado jurisdicionado.
	 * 
	 * @param jurisdicionado o jurisdicionado verificado
	 * @return o tipo do jurisdicionado
	 */
	public static TipoPessoa getTipoPessoaPorSigla(String sigla) {
		for (TipoPessoa tipoPessoa : TipoPessoa.values()) {
			if (tipoPessoa.getSigla().equals(sigla)) {
				return tipoPessoa;
			}
		}
		return OUTRAS;
	}
	
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	
	public String getIdentificador() {
		return identificador;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	
	public String getSigla() {
		return sigla;
	}

	public boolean isJuridica() {
		return PESSOA_JURIDICA.equals(this);
	}

	public boolean isFisica() {
		return PESSOA_FISICA.equals(this);
	}

	public boolean isPublica() {
		return PESSOA_PUBLICA.equals(this);
	}
	
	
}

